package com.custom.rac.itemcode.util;

import com.custom.rac.datamanagement.action.ImportAction2;
import com.custom.rac.datamanagement.util.IImporter;
import com.teamcenter.rac.util.MessageBox;

public class ImportThread extends Thread {
	private IImporter importer;
	private String runState = "";// 状态

	public String getRunState() {
		return runState;
	}

	public void setRunState(String runState) {
		this.runState = runState;
	}

	public ImportThread(IImporter importer) {
		super();
		this.importer = importer;
	}

	public void run() {
		synchronized (ImportAction2.o) {
			try {
				importer.execute();
			} catch (Exception e) {
				MessageBox.post(e.toString(), "错误", MessageBox.ERROR);
				e.printStackTrace();
			}
		}
	}
}
