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
 * ͼֽ���빤��
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
		return "ͼֽ�������";
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) throws Exception {

	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) throws Exception {
		System.out.println("��" +index+ "���쳣��" + e.getMessage());
	}

	@Override
	public TCComponentItemType getItemType(int index) throws TCException {
		String type = getValue(index, "ͼֽ����") + "";
		TCComponentItemType itemType = null;
		switch (type) {
		case "���񽨲�Ʒͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_ConstrDesign");
			break;
		case "��ҵ��Ʒͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_IndustDesign");
			break;
		case "�����Ʒͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_TrackDesign");
			break;
		case "�˵��Ʒͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_NucPowDesign");
			break;
		case "������Ʒͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_EnvDesign");
			break;
		case "���ͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_EleDesign");
			break;
		case "���ͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_PartDesign");
			break;			
		default:
			throw new TCException("ͼֽ���Ͳ���Ϊ�գ�");
		}
		return itemType;
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) throws Exception {
		return PropertyContainer.itemRevision;
	}
	
    public  boolean isEnglish(String str) {
        byte[] bytes = str.getBytes();
        int i = bytes.length;// iΪ�ֽڳ���
        int j = str.length();// jΪ�ַ�����
        boolean result = i == j ? true : false;
        return result;
    }

	@Override
	public void onSingleStart(int index) throws Exception {
		StringBuilder sb = new StringBuilder();
		String value = getValueFromRealName(index, "item_id");
		if (value == null || value.isEmpty()) {
			sb.append("ͼֽ���Ų���Ϊ��/");			
		} else if (!isEnglish(value)) {
			sb.append("ͼֽ���Ų��ܳ��������ַ�/");
		}
		Object obj = getValue(index, "���ӵ���ŵ�ַ");
		if (obj == null || obj.toString().trim().isEmpty()) {
			sb.append("���ӵ���ŵ�ַ����Ϊ��/");
		} else {
			value = obj.toString().trim();
			file = new File(value);
			if (file == null || !file.exists() || !file.isFile()) {
				sb.append("���ӵ���ŵ�ַ·���Ҳ����ļ�/");				
			}
//			else {
//				if (!value.toString().endsWith("dwg") && !value.toString().endsWith("DWG")) {
//					sb.append("ͼֽ���빤�߲�֧�ַ�dwg�����ļ��ĵ��룬���鵼�������Ƿ�����/");
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
		System.out.println("��" +index+ "���쳣��" + e.getMessage());

	}

	@Override
	public void onStart() throws Exception {
		TCComponentFolderType folderType = (TCComponentFolderType) session.getTypeComponent("Folder");
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String name = "ͼֽ��ʷ���ݵ���" + format.format(date);
		folder = folderType.create(name, "", "Folder");
		session.getUser().getHomeFolder().add("contents", folder);
	}

	@Override
	public void onFinish() throws Exception {
		MessageBox.post("�������","��ʾ", MessageBox.INFORMATION);

	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) throws Exception {
		if (propertyDisplayName.equals("ͼֽ����") || propertyDisplayName.equals("ͼֽ����")
			|| propertyDisplayName.equals("�汾") || propertyDisplayName.equals("ͼֽ����")
			|| propertyDisplayName.equals("ͼ�ĵ�����ID")){
			return true;
		}
		return false;
	}
	
	@Override
	public void setValue(TCComponent tcc, int index, String propertyDisplayName) throws Exception {
		if (propertyDisplayName.equals("���ӵ���ŵ�ַ")) {
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
		System.out.println("�ڣ�" + index + "�еģ�" + propertyName + "�����Բ����ڣ�");

	}

	@Override
	public void onSingleMessage(int index, String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
