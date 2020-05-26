package com.custom.rac.datamanagement.importer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.MyClassifyManager;
import com.custom.rac.datamanagement.util.MyDatasetUtil;
import com.custom.rac.datamanagement.util.MyStatusUtil;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.custom.rac.datamanagement.util.XMLResult;
import com.sfgk.sie.webservice.SFGKServiceProxy;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentFolderType;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentUser;
import com.teamcenter.rac.kernel.TCComponentUserType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;

public class SFGKDocumentImporter extends AbstractImporter {

	TCSession session = (TCSession) AIFUtility.getDefaultSession();
	MyClassifyManager cls_manger = new MyClassifyManager(session);
	TCComponentFolder folder = null;
	SFGKServiceProxy proxy = new SFGKServiceProxy();
	private File file;
	
	@Override
	public String getName() {
		return "�ĵ��������";
	}
	public String getType(String node_id) {
		String type = "";
		if (node_id.length() <= 4) {
			return type;
		}
		String prefix = node_id.substring(0, 4);
		switch (prefix) {
		case "SF-A":
			type = "SF8_GFBZDocument";
			break;
		case "SF-B":
			type = "SF8_SJZLDocument";
			break;
		case "SF-C":
			type = "SF8_YBZLDocument";
			break;
		case "SF-D":
			type = "SF8_GYZLDocument";
			break;
		case "SF-E":
			type = "SF8_XLHDocument";
			break;
		case "SF-F":
			type = "SF8_HTXMDocument";
			break;
		case "SF-G":
			type = "SF8_YFXMDocument";
			break;
		case "SF-H":
			type = "SF8_TBXMDocument";
			break;
		case "SF-J":
			type = "SF8_SYZXDocument";
			break;
		case "SF-K":
			type = "SF8_JSGLDocument";
			break;
		case "SF-X":
			type = "SF8_XHDocument";
			break;
		default:
			break;
		}
		return type;
	}

	@Override
	public TCComponentItemType getItemType(int index) throws Exception {
		String ics_id = getValue(index, "ͼ�ĵ�����ID") + "";
		if (ics_id == null || ics_id.length() < 4) {
			throw new Exception("����ID����Ϊ�գ�");
		}
		TCComponentItemType type = (TCComponentItemType) session.getTypeComponent(getType(ics_id));
		if (type == null) {
			throw new Exception("����ID�������飡");
		}
		return type;
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) {
		return PropertyContainer.itemRevision;
	}

	@Override
	public void onSingleStart(int index) throws Exception{
		StringBuilder sb = new StringBuilder();
		Object value = getValue(index, "ͼ�ĵ�����ID");
		if (value == null || value.toString().isEmpty()) {
			sb.append("ͼ�ĵ�����ID����Ϊ��/");
		}
		value = getValue(index, "���ӵ���ŵ�ַ");
		if (value == null || value.toString().isEmpty()) {
			sb.append("���ӵ���ŵ�ַ����Ϊ��/");
		} else {
			file = new File(value.toString());
			if (file == null || !file.exists() || !file.isFile()) {
				sb.append("���ӵ���ŵ�ַ·���Ҳ����ļ�/");
			} 
//			else {
//				if (value.toString().endsWith("dwg") || value.toString().endsWith("DWG")) {
//					sb.append("�ĵ����빤�߲�֧��dwg�����ļ��ĵ��룬���鵼�������Ƿ�����/");
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
	public void onSingleError(int index, Exception e) {
		System.out.println("��" +index+ "���쳣��" + e.getMessage());
	}

	@Override
	public void onStart() throws TCException {
		TCComponentFolderType folderType = (TCComponentFolderType) session.getTypeComponent("Folder");
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String name = "�ĵ���ʷ���ݵ���" + format.format(date);
		folder = folderType.create(name, "", "Folder");
		session.getUser().getHomeFolder().add("contents", folder);
	}

	@Override
	public void onFinish() {
		MessageBox.post("�������","��ʾ", MessageBox.INFORMATION);
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		if (propertyDisplayName.equals("�ĵ����") || propertyDisplayName.equals("�汾") 
			||propertyDisplayName.equals("�ĵ�����")){
			return true;
		}
		return false;
	}
	
    public static boolean isEnglish(String str) {
        byte[] bytes = str.getBytes();
        int i = bytes.length;// iΪ�ֽڳ���
        int j = str.length();// jΪ�ַ�����
        boolean result = i == j ? true : false;
        return result;
    }
	
	@Override
	public void setValue(TCComponent tcc, int index, String propertyDisplayName) throws Exception {
		String value = getValue(index, propertyDisplayName)+ "";
		if (propertyDisplayName.equals("�ĵ�״̬")) {
			MyStatusUtil.setStatus(tcc, value);
		} else if (propertyDisplayName.equals("������")) {
			String user_name = getValue(index, propertyDisplayName)+ "";
			if (user_name != null && !user_name.isEmpty()) {
				TCComponentUserType userType = (TCComponentUserType) session.getTypeComponent("User");
				TCComponentUser user = userType.find(user_name);
				if (user != null) {
					tcc.changeOwner(user, user.getLoginGroup());
				}
			}
		} else if (propertyDisplayName.equals("ͼ�ĵ�����ID")) {
			cls_manger.saveItemInNode(tcc, value);		
			
		} else if (propertyDisplayName.equals("���ӵ���ŵ�ַ")) {
			if (value != null && value.length() > 0) {				
				File file = new File(value);
				if (file != null && file.exists() &&file.isFile()) {
					MyDatasetUtil.createDateset(tcc, file.getName(), file, "TC_Attaches");
				} else {
					throw new Exception("�Ҳ������ݼ��ļ�" + value);
				}
			} else {
				throw new Exception("���ӵ���ŵ�ַ����Ϊ��");
			}			
		} else if(!propertyDisplayName.equals("ID")){
			super.setValue(tcc, index, propertyDisplayName);
		}
	}
	
	public String newItemId(int index) throws Exception {
		String value = getValue(index, "ͼ�ĵ�����ID") + "";
		if (value == null || value.length() == 0) {
			throw new Exception("ͼ�ĵ�����ID����Ϊ�գ�");
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String date = format.format(new Date());
		return getID(value +date, 4);
	}
	
	public String getID(String prefix, int serialLength) throws Exception{
		String xml = proxy.getID(prefix, serialLength);
		XMLResult result = XMLResult.read(xml);
		String error = result.error;
		if (error != null && !error.isEmpty()) {
			throw new Exception(error);
		}
        return result.value;
	}
		
	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {		
		System.out.println("�ڣ�" + index + "�еģ�" + propertyName + "�����Բ����ڣ�");
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
		
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		System.out.println("��" +index+ "���쳣��" + e.getMessage());
	}

	@Override
	public boolean ignoreRow(int index) {
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) {
		return true;
	}
	@Override
	public void onSingleMessage(int index, String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
