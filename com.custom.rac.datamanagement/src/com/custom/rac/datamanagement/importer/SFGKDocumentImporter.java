package com.custom.rac.datamanagement.importer;

import java.io.File;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.MyClassifyManager;
import com.custom.rac.datamanagement.util.MyDatasetUtil;
import com.custom.rac.datamanagement.util.MyStatusUtil;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentUser;
import com.teamcenter.rac.kernel.TCComponentUserType;
import com.teamcenter.rac.kernel.TCSession;

public class SFGKDocumentImporter extends AbstractImporter {

	String shared_directory_path = "\\\\192.168.25.11";
	TCSession session = (TCSession) AIFUtility.getDefaultSession();
	MyClassifyManager cls_manger = new MyClassifyManager(session);
	
	
	@Override
	public String getName() {
		return "上风高科文档导入程序";
	}

	@Override
	public TCComponentItemType getItemType(int index) throws Exception{
		TCSession session = (TCSession) AIFUtility.getDefaultSession();
		TCComponentItemType type = (TCComponentItemType) session.getTypeComponent("SF8_Document");
		return type;
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) {
		return PropertyContainer.itemRevision;
	}

	@Override
	public void onSingleStart(int index) throws Exception{
		
	}

	@Override
	public void onSingleFinish(int index, TCComponent tcc) throws Exception {
		
	}	

	@Override
	public void onSingleError(int index, Exception e) {
		e.printStackTrace();
		
	}

	@Override
	public void onStart() {
		
	}

	@Override
	public void onFinish() {
		System.out.println("导入完成！");
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		if (propertyDisplayName.equals("文档编号") || propertyDisplayName.equals("版本") 
			||propertyDisplayName.equals("文档名称") || propertyDisplayName.equals("文档分类ID")
			|| propertyDisplayName.equals("电子档存放地址") || propertyDisplayName.equals("所有者")
			|| propertyDisplayName.equals("文档状态")){
			return true;
		}
		return false;
	}
	
	@Override
	public void setValue(TCComponent tcc, int index, String propertyDisplayName) throws Exception {
		String value = getValue(index, propertyDisplayName)+ "";
		if (propertyDisplayName.equals("文档状态")) {
			MyStatusUtil.setStatus(tcc, value);
		} else if (propertyDisplayName.equals("所有者")) {
			String user_name = getValue(index, propertyDisplayName)+ "";
			TCComponentUserType userType = (TCComponentUserType) session.getTypeComponent("User");
			TCComponentUser user = userType.find(user_name);
			if (user != null) {
				tcc.changeOwner(user, user.getLoginGroup());
			}
		} else if (propertyDisplayName.equals("文档分类ID")) {
			cls_manger.saveItemInNode(tcc, value);			
		} else if (propertyDisplayName.equals("电子档存放地址")) {
			if (value != null && value.length() > 0) {
				if (!value.startsWith("\\") && !value.startsWith("/")) {
					value = "\\" + value;
				}
				value = shared_directory_path + value;
				File file = new File(value);
				if (file != null && file.exists() &&file.isFile()) {
					MyDatasetUtil.createDateset(tcc, file.getName(), file, "IMAN_specification");
				} else {
					throw new Exception("找不到数据集文件" + value);
				}
			} else {
				throw new Exception("电子档存放地址不能为空");
			}			
		} else {
			super.setValue(tcc, index, propertyDisplayName);
		}
	}
		
	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		
		
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
		System.out.println("第" + index + "行");
		
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		e.printStackTrace();
		
	}

	@Override
	public boolean ignoreRow(int index) {
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) {
		return true;
	}

}
