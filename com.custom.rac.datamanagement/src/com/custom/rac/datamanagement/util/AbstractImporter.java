package com.custom.rac.datamanagement.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.custom.rac.datamanagement.driver.IImportDriver;
import com.teamcenter.rac.kernel.ListOfValuesInfo;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevisionType;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentListOfValues;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCPropertyDescriptor;

public abstract class AbstractImporter implements IImporter {

	protected MyItemUtil itemUtil = new MyItemUtil("Item");
	
	/*
	 * 属性值列表
	 * List => 所有属性行
	 * Map => key:表格中的属性名， value:表格中的属性值
	 */
	protected List<Map<String, String>> values = new ArrayList<>();
	
	/**
	 * 属性的真实值映射表
	 * Map1 => key:TC对象的类型名，value:Map2
	 * Map2 => key:表格中的属性名，value:属性在TC中的真实值
	 */
	protected Map<String, Map<String, String>> propertyRealNameMap = new HashMap<>();
	
	IImportDriver driver;

	@Override
	public void loadDriver(IImportDriver driver) {
		this.driver = driver;
	}
	
	
	public List<Map<String, String>> getValues() {
		return values;
	}

	public void setValues(List<Map<String, String>> values) {
		this.values = values;
	}
	
	public abstract TCComponentItemType getItemType(int index);
	
	public TCComponentItemRevisionType getItemRevisionType(int index) {
		return getItemType(index).getItemRevisionType();
	}
	
	public TCComponentType getPropertyContainerType(int index) throws Exception {
		
		PropertyContainer pc = getPropertyContainer(index);
		if(pc == PropertyContainer.item) {
			return getItemType(index);
		}else if(pc == PropertyContainer.itemRevision) {
			return getItemRevisionType(index);
		}else if(pc == PropertyContainer.itemRevisionMasterForm) {
			//这种情况少，先不做处理了
			return null;
		}
		
		return null;
	}
	
	public abstract PropertyContainer getPropertyContainer(int index) throws Exception;
	
	public abstract void onSingleStart(int index) throws Exception;

	public abstract void onSingleFinish(int index) throws Exception;
	
	public abstract void onSingleError(int index, Exception e) throws Exception;
	
	public abstract void onStart() throws Exception;
	
	public abstract void onFinish() throws Exception;
	
	public abstract boolean ignoreProperty(int index, String propertyDisplayName) throws Exception;
	
	public abstract boolean ignoreRow(int index) throws Exception;
	
	public abstract boolean deleteOldItemWhenItemIdExist(int index) throws Exception;
	
	/**
	 * 
	 * @param index
	 * @param propertyName
	 * @return 是否继续，默认为继续
	 */
	public abstract void onPropertyRealNameNotFound(int index, String propertyName) throws Exception;
	
	/**
	 * -获取指定行的属性
	 * @param index
	 * @return
	 */
	public Map<String, String> getProperties(int index){
		return getValues().get(index);
	}
	
	/**
	 * -获取指定行的指定显示属性名对应的真实属性名
	 * @param index
	 * @param propertyDisplayName
	 * @return
	 * @throws Exception
	 */
	public String getPropertyRealName(int index, String propertyDisplayName) throws Exception {
		TCComponentType type = getPropertyContainerType(index);
		String typeName = type.getTypeName();
		if(propertyRealNameMap.containsKey(typeName)) {
			return propertyRealNameMap.get(typeName).get(propertyDisplayName);
		}else {
			TCPropertyDescriptor[] pds = type.getPropertyDescriptors();
			Map<String, String> map = new HashMap<>();
			Class<?> cls = TCPropertyDescriptor.class;
			Field field =cls.getDeclaredField("m_displayName");
			field.setAccessible(true);
			for (TCPropertyDescriptor pd : pds) {
				//getDisplayName的方法受界面影响，如果界面设置了显示真实值，这里获取到的也是真实值
				//因此改成反射强行获取真实值
				map.put((String)field.get(pd), pd.getName());
			}
			propertyRealNameMap.put(typeName, map);
			return map.get(propertyDisplayName);
		}
	}
	
	/**
	 * -根据显示名获取指定行的属性
	 * @param index
	 * @param propertyDisplayName
	 * @return
	 */
	public Object getValue(int index, String propertyDisplayName) {
		return getProperties(index).get(propertyDisplayName);
	}
	
	/**
	 * -往对象中设置属性
	 * @param tcComponent
	 * @param index
	 * @param propertyDisplayName
	 * @throws Exception
	 */
	public void setValue(TCComponent tcComponent, int index, String propertyDisplayName) throws Exception {
		
		String propertyRealName = getPropertyRealName(index, propertyDisplayName);
		if(propertyRealName == null) {
			 onPropertyRealNameNotFound(index, propertyDisplayName);
			throw new Exception("属性【" + propertyDisplayName + " 未找到！");
		}
		
		Object value = getValue(index, propertyDisplayName);
		TCProperty tcp = tcComponent.getTCProperty(propertyRealName);
		TCPropertyDescriptor tcpDesc = tcp.getDescriptor();
		
		if(tcpDesc.hasLOVAttached()) {
			TCComponentListOfValues lov = tcp.getLOV();
			ListOfValuesInfo lovInfo = lov.getListOfValues();
			String realValue = (String)lovInfo.getRealValue((String)value);
			tcComponent.setProperty(propertyRealName, realValue);
			
		}else {
			
			int propertyType = tcp.getPropertyType();
			switch(propertyType) {
				case TCProperty.PROP_logical: 
					String sv = ((String)value).toLowerCase();
					tcp.setLogicalValue(sv.equals("y") || sv.equals("yes") || sv.equals("true"));
					break;
				case TCProperty.PROP_date: 
					if(value instanceof Date) {
						tcp.setDateValue((Date)value);
					}else {
						Date date = new Date((String)value);
						tcp.setDateValue(date);
					}
					tcComponent.setTCProperty(tcp);
					break;
				default:tcComponent.setProperty(propertyRealName, (String)value);break;
			}
		}
	}
	
	/**
	 * -创建新实例
	 * @param index
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public TCComponent newTCComponent(int index) throws Exception {
		
		TCComponentItemType itemType = getItemType(index);
		
		String itemId = getItemId(index);
		if(itemId != null && itemId.length() > 0) {
//			itemUtil.find(itemId);
			TCComponentItem oldItem = itemType.find(itemId);
			if(oldItem != null && deleteOldItemWhenItemIdExist(index)) {
				itemUtil.deleteItem(oldItem, true);
			}
		}else {
			itemId = newItemId(index);
		}
		TCComponentItem newItem = itemType.create(
				getItemId(index), 
				getItemRevId(index), 
				itemType.getTypeName(), 
				getItemObjectName(index), null, null, null, null);
		
		return newItem.getLatestItemRevision();
	}
	
	/**
	 * -获取item_id,如果属性未填写则创建新的ID
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public String getItemId(int index) throws Exception {
		
		String value = getValueFromRealName(index, "item_id");
		return value;
	}
	
	public String getItemRevId(int index) throws Exception {
		
		String value = getValueFromRealName(index, "item_revision_id");
		return value == null ? newItemRevId(index) : value;
	}
	
	public String getItemObjectName(int index) throws Exception {
		
		String value = getValueFromRealName(index, "object_name");
		return value == null ? "" : value;
	}
	
	/**
	 * -根据属性的真实名称获取属性值
	 * @param index
	 * @param realName
	 * @return
	 * @throws Exception
	 */
	public String getValueFromRealName(int index, String realName) throws Exception {
		
		for (Entry<String, String> entry : values.get(index).entrySet()) {
			String theRealName = getPropertyRealName(index, entry.getKey());
			if(realName.equals(theRealName)) {
				return (String)entry.getValue();
			}
		}
		
		return null;
	}
	
	
	public String newItemId(int index) throws Exception {
		TCComponentItemType itemType = getItemType(index);
		return itemType.getNewID();
	}
	
	public String newItemRevId(int index) throws Exception {
		TCComponentItemType itemType = getItemType(index);
		return itemType.getNewRev(null);
	}
	
	@Override
	public void loadData(Object obj) {
		this.values = (List<Map<String, String>>) obj;
	}

	@Override
	public void execute() throws Exception {
		
		onStart();
		driver.onStart();
		for(int i = 0; i < values.size(); i++) {
			if(ignoreRow(i))continue;
			onSingleStart(i);
			driver.onSingleStart(i);
			Map<String, String> map = values.get(i);
			try {
				TCComponent newInstance = newTCComponent(i);
				for (String propertyDisplayName : map.keySet()) {
					try {
						if(ignoreProperty(i, propertyDisplayName))continue;
						setValue(newInstance, i, propertyDisplayName);
						onSetPropertyFinish(i, propertyDisplayName);
						driver.onSetPropertyFinish(i, propertyDisplayName);
					}catch(Exception e) {
//						System.out.println(e.toString());
						onSetPropertyError(i, propertyDisplayName, e);
						driver.onSetPropertyError(i, propertyDisplayName, e);
					}
				}
				onSingleFinish(i);
				driver.onSingleFinish(i);
			} catch (Exception e) {
				//TODO 异常处理
//				e.printStackTrace();
				onSingleError(i, e);
				driver.onSingleError(i, e);
			}
		}
		
		onFinish();
		
	}

}
