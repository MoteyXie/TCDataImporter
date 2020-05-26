package com.custom.rac.datamanagement.util;

import com.teamcenter.rac.classification.common.G4MUserAppContext;
import com.teamcenter.rac.classification.common.tree.G4MTree;
import com.teamcenter.rac.classification.common.tree.G4MTreeNode;
import com.teamcenter.rac.classification.icm.ClassificationService;
import com.teamcenter.rac.kernel.TCClassificationService;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentICO;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.ics.ICSApplicationObject;
import com.teamcenter.rac.kernel.ics.ICSHierarchyNodeDescriptor;

public class MyClassifyManager {
	public TCSession session;
	private ClassificationService classificationService = null;
	private G4MUserAppContext g4mUserAppContext = null;
	private String classifyRootId = "ICM";
	private G4MTree tree;
	private TCClassificationService icsService;
	public MyClassifyManager(TCSession session){
		this.session = session;
		initData();
	}
	
	private void initData(){
		try {
			classificationService = new ClassificationService();
			g4mUserAppContext = new G4MUserAppContext(classificationService, classifyRootId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public G4MTree getClassificationTree(){
		if (tree == null) {
			tree = new G4MTree(g4mUserAppContext);
		}
		G4MTreeNode root = getNode(classifyRootId);
		tree.setRootNode(root, true);
		
		return tree;
	}
	
	public G4MTreeNode getNode(String ics_id) {
		if (tree == null) {
			tree = new G4MTree(g4mUserAppContext);
		}
		if (icsService == null) {
			icsService = g4mUserAppContext.getClassificationService();
		}
		ICSHierarchyNodeDescriptor icsNodeDescriptor = icsService.describeNode(ics_id, 0);
		if(icsNodeDescriptor == null){
			System.out.println("��ȡ����ڵ�ʧ��!");
			return null;
		}
		G4MTreeNode root = new G4MTreeNode(tree, icsNodeDescriptor);
		return root;
	}
	
	/**
	 * 
	 * @param item ��Ҫ��������Item
	 * @param icsId ����id
	 * @throws TCException
	 */
	public void saveItemInNode(TCComponent tcc, String ics_id) throws TCException{
		G4MTreeNode node = getNode(ics_id);
		if (node != null) {
			tcc.setProperty("sf8_type", node.getNodeLabel());
		}
		
		// ��ȡ�����Ӧ��
		ICSApplicationObject icsApp = g4mUserAppContext.getICSApplicationObject();
		// �ж϶����Ƿ��Ѿ�����,����Ѿ�����,��ȡ�÷�����ͼ,���򴴽�һ���µ���ͼ��
		// ��������id
		TCComponentICO[] icos = tcc.getClassificationObjects();
		if (icos != null && icos.length > 0) {
			for (TCComponentICO ico : icos) {
				ico.delete();
			}
		}	
		icsApp.create(ics_id, tcc.getUid());
		icsApp.setView(ics_id);
		icsApp.save();
	}
}
