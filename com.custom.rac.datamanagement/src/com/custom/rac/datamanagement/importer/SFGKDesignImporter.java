package com.custom.rac.datamanagement.importer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.MyClassifyManager;
import com.custom.rac.datamanagement.util.MyDatasetUtil;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentFolderType;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;

/**
 * 图纸导入工具
 * @author Administrator
 *
 */
public class SFGKDesignImporter extends AbstractImporter {

	TCSession session = (TCSession) AIFUtility.getDefaultSession();
	MyClassifyManager cls_manger = new MyClassifyManager(session);
	TCComponentFolder folder = null;
	private File file;
	
	@Override
	public String getName() {
		return "上风高科图纸导入程序";
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) throws Exception {

	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) throws Exception {
		System.out.println("第" +index+ "行异常：" + e.getMessage());
	}

	@Override
	public TCComponentItemType getItemType(int index) throws TCException {
		String type = getValue(index, "图纸类型") + "";
		TCComponentItemType itemType = null;
		switch (type) {
		case "工民建产品图":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_ConstrDesign");
			break;
		case "工业产品图":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_IndustDesign");
			break;
		case "轨道产品图":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_TrackDesign");
			break;
		case "核电产品图":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_NucPowDesign");
			break;
		case "环保产品图":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_EnvDesign");
			break;
		case "电控图":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_EleDesign");
			break;
		case "零件图":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_PartDesign");
			break;			
		default:
			throw new TCException("图纸类型不能为空！");
		}
		return itemType;
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) throws Exception {
		return PropertyContainer.itemRevision;
	}
	
    public  boolean isEnglish(String str) {
        byte[] bytes = str.getBytes();
        int i = bytes.length;// i为字节长度
        int j = str.length();// j为字符长度
        boolean result = i == j ? true : false;
        return result;
    }

	@Override
	public void onSingleStart(int index) throws Exception {
		StringBuilder sb = new StringBuilder();
		String value = getValueFromRealName(index, "item_id");
		if (value == null || value.isEmpty()) {
			sb.append("图纸代号不能为空/");			
		} else if (!isEnglish(value)) {
			sb.append("图纸代号不能出现中文字符/");
		}
		Object obj = getValue(index, "电子档存放地址");
		if (obj == null || obj.toString().trim().isEmpty()) {
			sb.append("电子档存放地址不能为空/");
		} else {
			value = obj.toString().trim();
			file = new File(value);
			if (file == null || !file.exists() || !file.isFile()) {
				sb.append("电子档存放地址路径找不到文件/");				
			}
//			else {
//				if (!value.toString().endsWith("dwg") && !value.toString().endsWith("DWG")) {
//					sb.append("图纸导入工具不支持非dwg类型文件的导入，请检查导入类型是否有误！/");
//				}
//			}
		}
		String msg = sb.toString();
		if (msg != null && !msg.isEmpty()) {
			throw new Exception(msg);
		}
	}

	@Override
	public void onSingleFinish(int index, TCComponent tcc) throws Exception {
		if (folder != null) {
			try {
				folder.add("contents", tcc);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@Override
	public void onSingleError(int index, Exception e) throws Exception {
		System.out.println("第" +index+ "行异常：" + e.getMessage());

	}

	@Override
	public void onStart() throws Exception {
		TCComponentFolderType folderType = (TCComponentFolderType) session.getTypeComponent("Folder");
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String name = "图纸历史数据导入" + format.format(date);
		folder = folderType.create(name, "", "Folder");
		session.getUser().getHomeFolder().add("contents", folder);
	}

	@Override
	public void onFinish() throws Exception {
		MessageBox.post("导入完成","提示", MessageBox.INFORMATION);

	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) throws Exception {
		if (propertyDisplayName.equals("图纸类型") || propertyDisplayName.equals("图纸代号")
			|| propertyDisplayName.equals("版本") || propertyDisplayName.equals("图纸名称")
			|| propertyDisplayName.equals("图文档分类ID")){
			return true;
		}
		return false;
	}
	
	@Override
	public void setValue(TCComponent tcc, int index, String propertyDisplayName) throws Exception {
		if (propertyDisplayName.equals("电子档存放地址")) {
			MyDatasetUtil.createDateset(tcc, file.getName(), file, "TC_Attaches");						
		} else {
			super.setValue(tcc, index, propertyDisplayName);
		}
	}

	@Override
	public boolean ignoreRow(int index) throws Exception {
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) throws Exception {
		return false;
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) throws Exception {
		System.out.println("第：" + index + "行的（" + propertyName + "）属性不存在！");

	}

	@Override
	public void onSingleMessage(int index, String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
