package com.custom.rac.datamanagement.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMWindowType;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentFormType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentItemRevisionType;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentQuery;
import com.teamcenter.rac.kernel.TCComponentQueryType;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCFormProperty;
import com.teamcenter.rac.kernel.TCPropertyDescriptor;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.TCTypeService;

//TCComponentItem tccomponentitem = tccomponentItemType.create(ITEM ID, REV ID, ITEM TYPE, OBJECT NAME, DESC, null);
//rev.add("IMAN_specification", dataset);
public class MyItemUtil {
	private final String REV_MASTER_PROP = "IMAN_master_form_rev";

	private Logger logger = Logger.getLogger(MyItemUtil.class);

	private String ITEM_TYPE = null;

	private TCComponentItemType itemType = null;

	private String lastErrorMsg = null;
	
	public String getLastErrorMsg() {
		return lastErrorMsg;
	}
	
	public void clearLastErrorMsg() {
		lastErrorMsg = null;
	}
	
	public MyItemUtil(String typeName) {
		this.ITEM_TYPE = typeName;
	}

	public TCComponentItem create(String itemId, String revId, String itemType, String objName, String desc) {
		TCComponentItemType type = getItemType();
		try {
//			return type.create(itemId, revId, itemType, objName, desc, null);
			return type.create(itemId, revId, ITEM_TYPE, objName, desc, null);
		} catch (TCException e) {
			lastErrorMsg = e.getMessage();
			return null;
		}
	}
	
	public TCComponentItem create(String itemId, String revId, String itemType, String objName, String desc, TCComponent uom) {
		TCComponentItemType type = getItemType();
		try {
			return type.create(itemId, revId, itemType, objName, desc, uom);
		} catch (TCException e) {
			lastErrorMsg = e.getMessage();
			return null;
		}
	}
	
	public TCComponentItem create(String itemId, String revId, String objName, String desc) {
		return create(itemId, revId, ITEM_TYPE, objName, desc);
	}
	
	public TCComponentItem create(String itemId, String revId, String objName, String desc, TCComponent uom) {
		return create(itemId, revId, ITEM_TYPE, objName, desc, uom);
	}
	
	public TCComponentItem create(String itemId, String revId) {
		return create(itemId, revId, ITEM_TYPE, "", "");
	}
	
	/**
	 * 谢明宇添加
	 * @param type
	 */
	public void setItemType(String type){
		this.ITEM_TYPE = type;
		getItemType();
	}
	
	public TCComponentItemType getItemType() {
		if (ITEM_TYPE == null) {
			logger.error("获取Item类型对象出错：类型名为null");
			return null;
		}

		if (itemType != null) {
			return itemType;
		}

		TCComponentItemType ret = null;

		try {
			TCSession session = (TCSession) AIFUtility.getDefaultSession();
			TCTypeService service = session.getTypeService();
			ret = (TCComponentItemType)service.getTypeComponent(ITEM_TYPE);
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			logger.error("获取Item类型对象出错：" + e.getMessage());
		}

		itemType = ret;

		return itemType;
	}
	
	public String getNewID() {
		try {
			TCComponentItemType type = getItemType();
			return type.getNewID();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getNewRevID(){
		try {
			TCComponentItemType type = getItemType();
			return type.getNewRev(null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getNewID(TCComponentItem item) {
		try {
			TCComponentItemType type = getItemType();
			return type.getNewID(item);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getNewRevID(TCComponentItem item) {
		try {
			TCComponentItemType type = getItemType();
			return type.getNewRev(item);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@SuppressWarnings("deprecation")
	public TCComponentItem find(String itemId) {
		getItemType();
		if (itemType == null) {
			logger.error("获取Item类型对象出错，无法查询Item对象");
			return null;
		}

		TCComponentItem ret = null;
		try {
			ret = itemType.find(itemId);
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			logger.error("查询Item对象时出错（" + itemId + "）：" + e.getMessage());
		}

		return ret;
	}

	public TCComponent[] findByObjectName(String itemName) {
		try {
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			TCComponentQueryType qType = (TCComponentQueryType)ss.getTypeComponent("ImanQuery");
			TCComponentQuery query = (TCComponentQuery)qType.find("常规...");
			String[] fields = new String[] { "名称" };
			String[] values = new String[] { itemName };
			TCComponent[] ret = query.execute(fields, values);
			
			return ret;
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			e.printStackTrace();
		}
		return null;
	}
	
	public TCComponent[] findByObjectName(String itemType, String itemName) {
		try {
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			TCComponentQueryType qType = (TCComponentQueryType)ss.getTypeComponent("ImanQuery");
			TCComponentQuery query = (TCComponentQuery)qType.find("常规...");
			String[] fields = new String[] { "类型", "名称" };
			String[] values = new String[] { itemType, itemName };
			TCComponent[] ret = query.execute(fields, values);
			
			return ret;
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			e.printStackTrace();
		}
		return null;
	}
	
	public TCComponentForm getItemRevisionMaster(TCComponentItemRevision rev) {
		TCComponentForm ret = null;

		if (rev == null) {
			logger.error("无法获取ItemRevisionMaster对象：ItemRevision为空");
			return null;
		}
		try {
			AIFComponentContext[] afcs = rev.getChildren(REV_MASTER_PROP);
			ret = (TCComponentForm)afcs[0].getComponent();
		}
		catch(Exception e) {
			logger.error("获取ItemRevisionMaster时出错：" + e.getMessage());
			return null;
		}

		return ret;
	}

	public Map<String, TCComponent> getAllUnits() {
		Map<String, TCComponent> ret = new HashMap<String, TCComponent>();

		try {
			TCSession session = (TCSession) AIFUtility.getDefaultSession();
			TCTypeService service = session.getTypeService();

			TCComponentType uomType = service.getTypeComponent("UnitOfMeasure");
			TCComponent[] units = uomType.extent();
			if (units != null) {
				for (TCComponent c : units) {
					ret.put(c.toString().toUpperCase(), c);
				}
			}
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			logger.error("获取计量单位清单出错：" + e.getMessage());
		}

		return ret;
	}

	public TCComponentBOMWindowType getBOMWindowType() {
		TCComponentBOMWindowType ret = null;

		try {
			TCSession session = (TCSession) AIFUtility.getDefaultSession();
			ret = (TCComponentBOMWindowType)session.getTypeComponent("BOMWindow");
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			logger.error("获取BOMWindow类型失败：" + e.getMessage());
		}

		return ret;
	}
	
	/**
	 * 获取对象（或对象版本）主表上的所有属性名
	 * 
	 * 注意：若subItemType为空，默认使用当前对象的类型
	 * 
	 * @param subItemType
	 * @return
	 */
	public ArrayList<String> getMasterFormProperties(String subItemType) {
		if (subItemType == null || "".equals(subItemType.trim())) {
			subItemType = ITEM_TYPE;
		}
		
		try {
			String masterName = null;
			if ("Item".equalsIgnoreCase(subItemType)) {
				masterName = "Item Master";
			}
			else if ("ItemRevision".equalsIgnoreCase(subItemType)) {
				masterName = "ItemRevision Master";
			}
			else if (subItemType.equalsIgnoreCase("Master")) {
				masterName = subItemType;
			}
			else {
				masterName = subItemType + "Master";
			}
			
			TCSession session = (TCSession) AIFUtility.getDefaultSession();
			TCTypeService service = session.getTypeService();
			TCComponentFormType ft = (TCComponentFormType)service.getTypeComponent(masterName);
			
			if (ft == null) return null;
			
			String[] pns = ft.getPropertyNames();
			ArrayList<String> ret = new ArrayList<String>();
			for(String s : pns) {
				ret.add(s);
			}
			
			return ret;
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取对象（或对象版本等）的所有属性名
	 * 
	 * 注意：若typeName为空，默认使用当前对象（Item）的类型
	 * 
	 * @param typeName
	 * @return
	 */
	public ArrayList<String> getTCComponentTypeProperties(String typeName) {
		if (typeName == null || "".equals(typeName.trim())) {
			typeName = ITEM_TYPE;
		}
		
		try {
			TCSession session = (TCSession) AIFUtility.getDefaultSession();
			TCTypeService service = session.getTypeService();
			TCComponentType ft = (TCComponentType)service.getTypeComponent(typeName);
			
			if (ft == null) return null;
			
			String[] pns = ft.getPropertyNames();
			ArrayList<String> ret = new ArrayList<String>();
			for(String s : pns) {
				ret.add(s);
			}
			
			return ret;
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 注意：若subItemType为空，默认使用当前对象的类型
	 * 
	 * @param subItemType
	 * @return
	 */
	public ArrayList<String> getRevMasterFormProperties2(String subItemType) {
		if (subItemType == null || "".equals(subItemType.trim())) {
			subItemType = ITEM_TYPE;
		}
		
		try {
			String itemName = "__" + subItemType + "_InnerTest001";
			TCComponent[] items = findByObjectName(subItemType, itemName);
			TCComponentItem item = null;
			if (items != null && items.length > 0) {
				item = (TCComponentItem)items[0];
			}
			else {
				getItemType();
				String newId = itemType.getNewID();
				String revId = "A01";
				item = itemType.create(newId, revId, subItemType, itemName, "for test", null);
			}
			
			TCComponentItemRevision rev = item.getLatestItemRevision();
			if (rev == null) {
				return null;
			}
			
			TCComponentForm form = getItemRevisionMaster(rev);
			if (form == null) {
				return null;
			}
			
			TCFormProperty[] prs = form.getAllFormProperties();
			ArrayList<String> propList = new ArrayList<String>();
			for (TCFormProperty p : prs) {
				propList.add(p.getPropertyName());
			}
			
			return propList;
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取对象（或对象版本）主表上的所有属性描述对象
	 * 
	 * 注意：若subItemType为空，默认使用当前对象的类型
	 * 
	 * @param subItemType
	 * @return
	 */
	public TCPropertyDescriptor[] getMasterFormPropertyDescriptors(String subItemType) {
		if (subItemType == null || "".equals(subItemType.trim())) {
			subItemType = ITEM_TYPE;
		}
		
		try {
			String masterName = null;
			if ("Item".equalsIgnoreCase(subItemType)) {
				masterName = "Item Master";
			}
			else if ("ItemRevision".equalsIgnoreCase(subItemType)) {
				masterName = "ItemRevision Master";
			}
			else if (subItemType.endsWith("Master")) {
				masterName = subItemType;
			}
			else {
				masterName = subItemType + "RevisionMaster";
			}
			
			TCSession session = (TCSession) AIFUtility.getDefaultSession();
			TCTypeService service = session.getTypeService();
			TCComponentFormType ft = (TCComponentFormType)service.getTypeComponent(masterName);
			
			if (ft == null) return null;
			
			return ft.getPropertyDescriptors();
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取对象（或对象版本）上的所有属性描述对象
	 * 
	 * 注意：若subItemType为空，默认使用当前对象的类型
	 * 
	 * @param revType
	 * @return
	 */
	public TCPropertyDescriptor[] getRevisionPropertyDescriptors(String revType) {
		if (revType == null || "".equals(revType.trim())) {
			revType = ITEM_TYPE;
		}
		
		try {
			String revTypeName = null;
			if ("Item".equalsIgnoreCase(revType)) {
				revTypeName = "ItemRevision";
			}
			else if ("ItemRevision".equalsIgnoreCase(revType)) {
				revTypeName = "ItemRevision Master";
			}
			else if (revType.endsWith("Revision")) {
				revTypeName = revType;
			}
			else {
				revTypeName = revType + "Revision";
			}
			
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			TCTypeService service = ss.getTypeService();
			TCComponentItemRevisionType rt = (TCComponentItemRevisionType)service.getTypeComponent(revTypeName);
			
			if (rt == null) return null;
			
			return rt.getPropertyDescriptors();
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取对象（或对象版本）上的所有属性描述对象
	 * 
	 * 注意：若itemType为空，默认使用当前对象的类型
	 * 
	 * @param itemType
	 * @return
	 */
	public TCPropertyDescriptor[] getItemPropertyDescriptors(String itemType) {
		if (itemType == null || "".equals(itemType.trim())) {
			itemType = ITEM_TYPE;
		}
		
		try {
			if (itemType.endsWith("Revision")) {
				itemType = itemType.substring(0, itemType.length() - "Revision".length());
			}
			
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			TCTypeService service = ss.getTypeService();
			TCComponentItemType rt = (TCComponentItemType)service.getTypeComponent(itemType);
			
			if (rt == null) return null;
			
			return rt.getPropertyDescriptors();
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static TCComponentItemRevision[] getAllRevision(TCComponentItem item)
			throws Exception {
		TCComponentItemRevision revs1[] = item.getWorkingItemRevisions();
		TCComponentItemRevision revs2[] = item.getInProcessItemRevisions();
		TCComponentItemRevision revs3[] = item.getReleasedItemRevisions();
		
		int count = revs1.length + revs2.length + revs3.length;

		TCComponentItemRevision revs[] = new TCComponentItemRevision[count];

		int top = 0;
		for (int i = 0; i < revs1.length; i++) {
			revs[top] = revs1[i];
			top++;
		}
		
		for (int i = 0; i < revs2.length; i++) {
			revs[top] = revs2[i];
			top++;
		}
		
		for (int i = 0; i < revs3.length; i++) {
			revs[top] = revs3[i];
			top++;
		}
		
		return revs;
	}
	
	public TCComponentItemRevision getRevision(TCComponentItem item, String strVer) {
		try {
			TCComponentItemRevision revs[] = item.getWorkingItemRevisions();
			for (int i = 0; i < revs.length; i++) {
				String strTempVer = revs[i].getTCProperty("item_revision_id")
						.getStringValue();
				if (strTempVer.equals(strVer)) {
					return revs[i];
				}
			}
	
			TCComponentItemRevision revs3[] = item.getInProcessItemRevisions();
			for (int i = 0; i < revs3.length; i++) {
				String strTempVer = revs3[i].getTCProperty("item_revision_id")
						.getStringValue();
				if (strTempVer.equals(strVer)) {
					return revs3[i];
				}
			}
	
			TCComponentItemRevision revs2[] = item.getReleasedItemRevisions();
			for (int i = 0; i < revs2.length; i++) {
				String strTempVer = revs2[i].getTCProperty("item_revision_id")
						.getStringValue();
				if (strTempVer.equals(strVer)) {
					return revs2[i];
				}
			}
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
		}
		return null;
	}

	public boolean ifHaveRevision(TCComponentItem item, String strVer) {
		boolean found = false;
		
		try {
			TCComponentItemRevision revs[] = item.getWorkingItemRevisions();
			for (int i = 0; i < revs.length; i++) {
				String strTempVer = revs[i].getTCProperty("item_revision_id")
						.getStringValue();
				// System.out.println("strTempVer:"+strTempVer);
				if (strTempVer.equals(strVer)) {
					return true;
				}
			}
	
			TCComponentItemRevision revs3[] = item.getInProcessItemRevisions();
			for (int i = 0; i < revs3.length; i++) {
				String strTempVer = revs3[i].getTCProperty("item_revision_id")
						.getStringValue();
				if (strTempVer.equals(strVer)) {
					return true;
				}
			}
	
			TCComponentItemRevision revs2[] = item.getReleasedItemRevisions();
			for (int i = 0; i < revs2.length; i++) {
				String strTempVer = revs2[i].getTCProperty("item_revision_id")
						.getStringValue();
				if (strTempVer.equals(strVer)) {
					return true;
				}
			}
		}
		catch(Exception e) {
			lastErrorMsg = e.getMessage();
		}
		
		return found;
	}
	
	public String removeTCComponent(TCComponent comp, String relName) {
		String ret = null;
		
		if (comp == null) return null;
		
		try {
			AIFComponentContext[] ctx = comp.whereReferenced();
			if (ctx != null) {
				for (AIFComponentContext c : ctx) {
					if (c.getComponent() instanceof TCComponent) {
						TCComponent p = (TCComponent)c.getComponent();
						p.remove(relName, comp);
					}
				}
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			comp.delete();
		}
		catch(Exception ed) {
			ret = ed.getMessage();
		}
		
		return ret;
	}
	
	/**
	 * 删除一个ITEM
	 * 
	 * 注意：
	 * 1.如果ITEM被其他对象所使用，将无法删除
	 * 2.如果有附件，则程序也会删除该附件的所有relName关系，再尝试删除此附件！！（小心）
	 * 3.若ITEM删除失败，可能会造成此ITEM的部分子对象已经被删除，无法恢复！！
	 * 
	 * @param item 零组件对角
	 * @return 成功则返回null，否则返回出错信息
	 * 
	 */
	public String deleteItem(TCComponentItem item, boolean deleteDataset) {
		String ret = null;
		
		try {
			if (item == null) return null;
			
			TCComponent[] used = item.whereUsed(TCComponentItem.WHERE_USED_ALL);
			if (used != null && used.length > 0) {
				return "ITEM已被使用，无法删除";
			}
			
			//1.处理数据集
			if (deleteDataset) {
				deleteDataset(item, false);
			}
			
			//2.处理对象版本
			TCComponentItemRevision[] revs = getAllRevision(item);
			if (revs != null) {
				for (TCComponentItemRevision rev : revs) {
					if (deleteDataset) {
						deleteDataset(rev, false);
					}
					removeAllReference(rev, false);
				}
			}
			
			//3.处理item自身
			removeAllReference(item, false);
			
			item.delete();
		}
		catch(Exception e) {
			ret = "删除ITEM时异常：" + e.getMessage();
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public String removeAllReference(TCComponent comp, boolean showError) {
		String ret = null;
		
		if (comp == null) return null;

		try {
			AIFComponentContext[] ctx = comp.whereReferenced();
			if (ctx == null) return null;
			
			for (AIFComponentContext c : ctx) {
				if (c.getComponent() instanceof TCComponentFolder) {
					TCComponentFolder folder = (TCComponentFolder)c.getComponent();
					folder.remove("contents", comp);
				}
				else if (c.getComponent() instanceof TCComponent) {
					String rname = "" + c.getContext();
					if (rname.length() > 0) {
						TCComponent parent = (TCComponent)c.getComponent();
						parent.remove(rname, comp);
					}
				}
			}
		}
		catch(Exception e) {
			ret = "移除关系失败：" + e.getMessage();
			if (showError) e.printStackTrace();
		}
		
		return ret;
	}
	
	public String deleteDataset(TCComponent comp, boolean showError) {
		String ret = null;
		
		if (comp == null) return null;
		
		try {
			AIFComponentContext[] ctx = comp.getChildren();
			if (ctx != null) {
				for (AIFComponentContext c : ctx) {
					InterfaceAIFComponent  ic = c.getComponent();
					if (!(ic instanceof TCComponentDataset)) continue;
					
					TCComponent ds = (TCComponent)ic;
					String rname = "" + c.getContext();
					if (rname.length() > 0) {
						try {
							comp.remove(rname, ds);
						} catch(Exception e) {
							if (showError) e.printStackTrace();
						}
					}
					
					try {
						ds.delete();
					}
					catch(Exception e) {
						if (showError) e.printStackTrace();
					}
				}
			}

		}
		catch(Exception e) {
			ret = "删除数据集失败：" + e.getMessage();
			if (showError) e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * 删除一个ItemRevision
	 * 注意：
	 * 1.如果该ItemRevision被BOM所引用，则删除失败！
	 * 2.如果deleteDataset为true，则程序尝试删除其下的数据集
	 * 
	 * @param rev
	 * @param deleteDataset
	 * @return 删除成功则返回null，否则返回出错信息
	 */
	public String deleteItemRevision(TCComponentItemRevision rev, boolean deleteDataset) {
		String ret = null;
		
		if (rev == null) return null;
		
		try {
			TCComponent[] used = rev.whereUsed(TCComponentItem.WHERE_USED_ALL);
			if (used != null && used.length > 0) {
				return "ItemRevison已被使用，无法删除";
			}
			
			//处理数据集
			if (deleteDataset) {
				deleteDataset(rev, false);
			}
			
			//处理rev自身
			removeAllReference(rev, false);
			
			rev.delete();
		}
		catch(Exception e) {
			ret = "删除ItemRevsion失败：" + e.getMessage();
			e.printStackTrace();
		}
		return ret;
	}

}
