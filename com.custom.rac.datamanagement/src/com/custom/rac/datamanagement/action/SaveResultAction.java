package com.custom.rac.datamanagement.action;

import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.util.WriteDataToExcel;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.teamcenter.rac.util.MessageBox;

public class SaveResultAction extends AbstractTableAction {

	public SaveResultAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		// ���жϳ����Ƿ��ڽ�����
		tableViewPart.setExecuting(false);
		boolean isExecuting = tableViewPart.isExecuting();
		if (isExecuting) {
			throw new Exception("����ִ���У��޷��������ݣ�");
		}
		// �ļ���·��
		String lastSelectedFilePath = OpenFileAction.lastSelectFile;
		if (lastSelectedFilePath == null) {
			throw new Exception("û�����ݱ��棡");
		}
		WriteDataToExcel.WriteData(tableViewPart, lastSelectedFilePath, lastSelectedFilePath);
		MessageBox.post("����ɹ�������·����" + lastSelectedFilePath, "��ʾ", MessageBox.INFORMATION);

	}

}
