package com.custom.rac.datamanagement.importer;

import java.util.HashMap;
import java.util.Map;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.BOMUtil;
import com.custom.rac.datamanagement.util.MyBOMUtil;
import com.custom.rac.datamanagement.util.MyItemUtil;
import com.custom.rac.datamanagement.util.MyStatusUtil;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMViewRevision;
import com.teamcenter.rac.kernel.TCComponentBOMViewRevisionType;
import com.teamcenter.rac.kernel.TCComponentBOMWindow;
import com.teamcenter.rac.kernel.TCComponentBOMWindowType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentViewType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class SFGKBomImporter extends AbstractImporter {
	
	MyItemUtil myItemUtil = null;
	public final String BOM_VIEW_TYPE = "view";
	private TCComponentViewType viewType = null;
	private TCComponentBOMViewRevisionType viewRevType = null;
	private Boolean ForceUpdateFlag = true;
	
	@Override
	public String getName() {
		
		return "上风高科BOM导入程序";
		
	}
	
	@Override
	public void onSingleMessage(int index, String msg) throws Exception {
		System.out.println("第" +index+ "行:"+msg);		
	}	
	

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) throws Exception {

		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) throws Exception {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public TCComponentItemType getItemType(int index) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onSingleStart(int index) throws Exception {
		System.out.println("第" +index+ "行开始");		
	}

	@Override
	public void onSingleFinish(int index, TCComponent newInstance) throws Exception {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSingleError(int index, Exception e) throws Exception {
		System.out.println("第" +index+ "行异常：" + e.getMessage());
	}

	@Override
	public void onStart() throws Exception {
		System.out.println("BOM导入开始");		
		
	}

	@Override
	public void onFinish() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ignoreRow(int index) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void execute() throws Exception {
		
		boolean hasError = false;
		TCComponentItemRevision parentObj = null;
		TCComponentItemRevision lineObj = null;
		HashMap<Integer, TCComponent> children = new HashMap<Integer, TCComponent>();
		String parentId = null;
		String parentId2 = null;
		String lineId = null;
		String bomOrg = null;
		String status =null;
		onStart();
		driver.onStart();
		myItemUtil = new MyItemUtil("Item");
		viewType = MyBOMUtil.getViewType(BOM_VIEW_TYPE);
		if (viewType == null) {
			return;
		}
		viewRevType = MyBOMUtil.getTCComponentBOMViewRevisionType();
		if (viewRevType == null) {
			return;
		}
		int i = 0;
		while(i<=values.size()-1){
			
			driver.onSingleMessage(i, "导入开始...");			
			int lastRow = i;
			int lineCount = 0;
			parentId = getValue(i, "父项ID")+ "";
			lineId = getValue(i, "子项ID")+"";
			bomOrg = getValue(i, "BOM组织")+"";
			status = getValue(i, "发布状态")+"";
			if (lineId.length()>0) {
				lineCount++;
			}
			for (int j=i+1; j<values.size(); j++) {
				parentId2 = getValue(j,"父项ID" )+ "";
				if (parentId.equals(parentId2)) {
					lineCount++;
					lastRow = j;
				}
				else {
					break;
				}
			}
			if (lineCount == 0) {
				driver.onSingleError(i, new Exception("没有结构BOM，忽略此行"));

			}
			parentObj = queryObj(parentId);
			if (parentObj == null) {
				hasError = true;				
				driver.onSingleError(i, new Exception("父项物料不存在，无法导入当前BOM。"));
				
			}
			boolean linesOK = true;
			children.clear();

			for (int j=i; j<=lastRow; j++) {
				lineId = getValue(j, "子项ID")+"";
				if (lineId.length() == 0) {
					continue;
				}				
				lineObj = queryObj(lineId);
				if (lineObj == null) {
					linesOK = false;
					hasError = true;
					driver.onSingleError(j, new Exception("此子项对象不存在，整个BOM无法导入"));
					continue;
				}				
				if (parentObj!=null&&lineObj!=null) {
					driver.onSingleMessage(j, ">>子BOM行检查ok");
					children.put(j, lineObj);
				}
			}
			if(!linesOK) {
				for (int z = i; z <=lastRow ; z++) {
					driver.onSingleError(z, new Exception(">>导入失败！部分子项数据检查失败，无法导入当前BOM！"));
				}
			}
			if(parentObj==null) {
				for (int z = i; z <=lastRow ; z++) {
					driver.onSingleError(z, new Exception(">>导入失败！父项物料不存在，无法导入当前BOM。"));
				}
			}
			i = lastRow;
			i++;
			if(parentObj==null||!linesOK) {
				continue;
			}

			TCComponentBOMViewRevision oldBvr = null;
			int removeFlag = 0;
			try {
				oldBvr = BOMUtil.findBVR(parentObj, viewType);
				if (oldBvr != null) {
					if (ForceUpdateFlag) {
						driver.onSingleMessage(i-1, ">>结构BOM已经存在，将被删除重建！");
						removeFlag = -1;
					}
					else {
						driver.onSingleMessage(i-1, ">>结构BOM已经存在，不再重复导入此BOM！");
						continue;
					}
				}
			}
			catch(Exception ex) {
				hasError = true;
				driver.onSingleMessage(i-1, "创建BOM失败"+ex.toString());
				continue;
			}

			String ret = null;
			if (removeFlag < 0) {
				ret = createStructureBOM(parentObj, children, oldBvr);
			}else {
				ret = createStructureBOM(parentObj, children, null);
			}
			
			TCComponent bomview = null;
			try {
				bomview = parentObj.getRelatedComponent("structure_revisions");
			} catch (TCException e) {
				e.printStackTrace();
			}			
			if(bomview!=null){
				ret = MyStatusUtil.setStatus(bomview, "量产");												
			}
			
			//导入保存
			if (ret == null) {
				driver.onSingleMessage(i-1, ">>结构BOM导入成功！");
				parentObj.setProperty("sf8_is_bom_send_erp", "true");
				parentObj.setProperty("sf8_bom_org", bomOrg);
			}else {
				hasError = true;
				driver.onSingleError(i-1, new Exception(parentId + ">>结构BOM导入失败！"+ret));
			}
			driver.onSingleFinish(i);
		}		
		onFinish();
	}
	
	private TCComponentItemRevision queryObj(String itemId) {
		TCComponentItemRevision ret = null;
		try {
			TCComponentItem item = myItemUtil.find(itemId);
			if (item != null) {
				ret = item.getLatestItemRevision();
			}
		}
		catch(Exception e) { 
			
		}		
		return ret;
	}
	
	private String createStructureBOM(TCComponentItemRevision parentRev, HashMap<Integer, TCComponent> children, TCComponentBOMViewRevision bvr0) {
		String ret = null;
		
		TCComponentBOMWindow bomWin = null;
		TCComponentBOMViewRevision bvr = null;
		try {
			String itemId = parentRev.getItem().getProperty("item_id");
			String revId = parentRev.getProperty("current_revision_id");
			
			if (bvr0 == null) {
				//itemId, revId, viewType, isPrecise
				bvr = viewRevType.create(itemId, revId, viewType, true);
			}
			else {
				bvr = bvr0;
			}
			
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			TCComponentBOMWindowType type = (TCComponentBOMWindowType)ss.getTypeComponent("BOMWindow");
			bomWin  = type.create(null);

			TCComponentBOMLine topLine = bomWin.setWindowTopLine(null, null, null, bvr);

			topLine.setPrecision(false);
			
			if (bvr0 != null) {
//				ui.addMsg("开始删除BOM结构！");
//				topLine.setPrecision(true);
				AIFComponentContext[] ctx = topLine.getChildren();
				if (ctx != null) {
					for (AIFComponentContext c : ctx) {
						if (!(c.getComponent() instanceof TCComponentBOMLine)) continue;
						TCComponentBOMLine line = (TCComponentBOMLine)c.getComponent();
						line.cut();
					}
				}			
				topLine.save();
				bomWin.save();
			}
			String subinventory = null;
			String v = null;
			TCComponentBOMLine subLine = null;
			
			for (Integer i : children.keySet()) {
				TCComponent comp = children.get(i);
				subLine = topLine.addBOMLine(topLine, comp, null);				
//				设置版本属性上的
				v = getValue(i, "用量")+"";
				subinventory = (String) getValue(i, "子库存");
				subLine.setProperty("bl_occ_sf8_subinventory", subinventory);
				subLine.setProperty("bl_quantity", v);									
				subLine.save();
			}
			topLine.save();
			bomWin.save();
			bomWin.close();
			
		}
		catch(Exception e) {
			e.printStackTrace();
			ret = "创建结构BOM时出错：" + e.getMessage();
			BOMUtil.removeBOM(bomWin, bvr);
		}		
		return ret;
	}
}
