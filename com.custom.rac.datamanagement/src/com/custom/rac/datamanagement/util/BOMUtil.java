package com.custom.rac.datamanagement.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.kernel.RevisionRuleEntry;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMView;
import com.teamcenter.rac.kernel.TCComponentBOMViewRevision;
import com.teamcenter.rac.kernel.TCComponentBOMWindow;
import com.teamcenter.rac.kernel.TCComponentBOMWindowType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentRevisionRule;
import com.teamcenter.rac.kernel.TCComponentRevisionRuleType;
import com.teamcenter.rac.kernel.TCComponentViewType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.tcservices.TcBOMService;
import com.teamcenter.rac.pse.operations.ExpandBelowOperation;
import com.teamcenter.rac.util.Registry;

public class BOMUtil {

	public static TCComponentBOMView findBV(TCComponentItem item,
			TCComponentViewType vt) throws TCException {
		for (TCComponent bv : item.getRelatedComponents("bom_view_tags")) {
			if (bv instanceof TCComponentBOMView) {
				TCComponent bvt = bv.getReferenceProperty("view_type");
				if (bvt == null)
					throw new TCException("No ViewType for BV " + bv);
				if (vt == bvt)
					return (TCComponentBOMView) bv;
			}
		}
		return null;
	}

	public static TCComponentBOMViewRevision findBVR(
			TCComponentItemRevision rev, TCComponentViewType vt)
			throws TCException {
		for (TCComponent bvr : rev.getRelatedComponents("structure_revisions")) {
			if (bvr instanceof TCComponentBOMViewRevision) {
				TCComponent bv = bvr.getReferenceProperty("bom_view");
				if (bv == null)
					throw new TCException("No BV for BVR " + bvr);
				TCComponent bvt = bv.getReferenceProperty("view_type");
				if (bvt == null)
					throw new TCException("No ViewType for BV " + bv);
				if (vt == bvt)
					return (TCComponentBOMViewRevision) bvr;
			}
		}
		return null;
	}

	public static TCComponentRevisionRule createLatestReleasedRevRule(
			TCSession session) throws TCException {
		TCComponentRevisionRuleType ruleType = (TCComponentRevisionRuleType) session
				.getTypeComponent("RevisionRule");
		TCComponentRevisionRule revRule = ruleType.create(
				"LatestReleasedRevRule", "Revision Rule for LatestReleased");
		revRule.addEntry(ruleType.createEntry(RevisionRuleEntry.STATUSENTRY));
		return revRule;
	}

	public static TCComponentBOMWindow createLatestReleasedBOMWin(
			TCComponentBOMView bv) throws TCException {
		TCComponentBOMWindowType winType = (TCComponentBOMWindowType) bv
				.getSession().getTypeComponent("BOMWindow");
		TCComponentBOMWindow win = winType
				.create(createLatestReleasedRevRule(bv.getSession()));
		try {
			win.setWindowTopLine(null, null, bv, null);
		} catch (Exception e) {
			if (win != null)
				win.close();
			if (e instanceof TCException)
				throw (TCException) e;
			e.printStackTrace();
			return null;
		}
		return win;
	}

	public static TCComponentBOMWindow createLatestReleasedBOMWin(
			TCComponentBOMViewRevision bvr) throws TCException {
		TCComponentBOMWindowType winType = (TCComponentBOMWindowType) bvr
				.getSession().getTypeComponent("BOMWindow");
		TCComponentBOMWindow win = winType
				.create(createLatestReleasedRevRule(bvr.getSession()));
		try {
			win.setWindowTopLine(null, null, null, bvr);
		} catch (Exception e) {
			if (win != null)
				win.close();
			if (e instanceof TCException)
				throw (TCException) e;
			e.printStackTrace();
			return null;
		}
		return win;
	}

	public static TCComponentRevisionRule createLatestWorkingRevRule(
			TCSession session) throws TCException {
		TCComponentRevisionRuleType ruleType = (TCComponentRevisionRuleType) session
				.getTypeComponent("RevisionRule");
		TCComponentRevisionRule revRule = ruleType.create(
				"LatestWorkingRevRule", "Revision Rule for LatestWorking");
		revRule.addEntry(ruleType.createEntry(RevisionRuleEntry.WORKINGENTRY));
		return revRule;
	}

	public static TCComponentBOMWindow createLatestWorkingBOMWin(
			TCComponentBOMView bv) throws TCException {
		TCComponentBOMWindowType winType = (TCComponentBOMWindowType) bv
				.getSession().getTypeComponent("BOMWindow");
		TCComponentBOMWindow win = winType.create(createLatestWorkingRevRule(bv
				.getSession()));
		try {
			win.setWindowTopLine(null, null, bv, null);
		} catch (Exception e) {
			if (win != null)
				win.close();
			if (e instanceof TCException)
				throw (TCException) e;
			e.printStackTrace();
			return null;
		}
		return win;
	}

	public static TCComponentBOMWindow createLatestWorkingBOMWin(
			TCComponentBOMViewRevision bvr) throws TCException {
		TCComponentBOMWindowType winType = (TCComponentBOMWindowType) bvr
				.getSession().getTypeComponent("BOMWindow");
		TCComponentBOMWindow win = winType
				.create(createLatestWorkingRevRule(bvr.getSession()));
		try {
			win.setWindowTopLine(null, null, null, bvr);
		} catch (Exception e) {
			if (win != null)
				win.close();
			if (e instanceof TCException)
				throw (TCException) e;
			e.printStackTrace();
			return null;
		}
		return win;
	}

	public static TCComponentRevisionRule createLatestRevRule(TCSession session)
			throws TCException {
		TCComponentRevisionRuleType ruleType = (TCComponentRevisionRuleType) session
				.getTypeComponent("RevisionRule");
		TCComponentRevisionRule revRule = ruleType.create("LatestRevRule",
				"Revision Rule for Latest");
		RevisionRuleEntry entry = ruleType
				.createEntry(RevisionRuleEntry.LATESTENTRY);
		entry.getTCComponent().setIntProperty("config_type",
				RevisionRuleEntry.ALPHANUMERIC);
		revRule.addEntry(entry);
		return revRule;
	}

	public static TCComponentBOMWindow createLatestBOMWin(TCComponentBOMView bv)
			throws TCException {
		TCComponentBOMWindowType winType = (TCComponentBOMWindowType) bv
				.getSession().getTypeComponent("BOMWindow");
		TCComponentBOMWindow win = winType.create(createLatestRevRule(bv
				.getSession()));
		try {
			win.setWindowTopLine(null, null, bv, null);
		} catch (Exception e) {
			if (win != null)
				win.close();
			if (e instanceof TCException)
				throw (TCException) e;
			e.printStackTrace();
			return null;
		}
		return win;
	}

	public static TCComponentBOMWindow createLatestBOMWin(
			TCComponentBOMViewRevision bvr) throws TCException {
		TCComponentBOMWindowType winType = (TCComponentBOMWindowType) bvr
				.getSession().getTypeComponent("BOMWindow");
		TCComponentBOMWindow win = winType.create(createLatestRevRule(bvr
				.getSession()));
		try {
			win.setWindowTopLine(null, null, null, bvr);
		} catch (Exception e) {
			if (win != null)
				win.close();
			if (e instanceof TCException)
				throw (TCException) e;
			e.printStackTrace();
			return null;
		}
		return win;
	}

	protected static void doExpandFully(TCComponentBOMLine... lines)
			throws Exception {
		if (lines.length < 1)
			return;

		TcBOMService.expandOneLevel(lines[0].getSession(), lines);

		ArrayList<TCComponentBOMLine> l2e = new ArrayList<TCComponentBOMLine>(
				lines.length * 25);
		for (TCComponentBOMLine line : lines) {
			// System.err.println(" %%% Children of " + line);
			for (AIFComponentContext ctx : line.getChildren()) {
				TCComponentBOMLine cLine = (TCComponentBOMLine) ctx
						.getComponent();
				l2e.add(cLine);
				// System.err.println(" %%%%% " + cLine);
			}
		}
		int lc = l2e.size();
		if (lc < 1)
			return;
		TCComponentBOMLine[] lines2exp = new TCComponentBOMLine[lc];
		l2e.toArray(lines2exp);
		doExpandFully(lines2exp);
	}

	protected static HashSet<String> m_tcpropsToCache;

	public static void expandFully(TCComponentBOMLine... lines)
			throws Exception {
		if (lines.length < 1)
			return;

		if (m_tcpropsToCache == null) {
			m_tcpropsToCache = new HashSet<String>();

			// Same set as used in BOMLineNode...
			Collections.addAll(m_tcpropsToCache,
					TCComponentBOMLine.propsToCache);

			String str = Registry.getRegistry(ExpandBelowOperation.class)
					.getString("tcpropertesToCache");

			// needed for the child query...
			Collections.addAll(m_tcpropsToCache, str.split(","));
		}

		try {
			TCComponent.setTCPropertyFilter(TCComponentBOMLine.class,
					m_tcpropsToCache);
			doExpandFully(lines);
		} finally {
			TCComponent.setTCPropertyFilter(null, null);
		}
	}
	
	public static void removeBOM(TCComponentBOMWindow bomWnd, TCComponentBOMViewRevision bvr) {
		if (bomWnd == null && bvr == null) return;
		
		if (bomWnd != null) {
			try {
				TCComponentBOMLine topLine = bomWnd.getTopBOMLine();
				if (topLine == null) {
					bomWnd.delete();
					return;
				}
				TCComponentBOMLine[] lines = topLine.getPackedLines();
				if (lines != null) {
					for (TCComponentBOMLine line : lines) {
						line.delete();
					}
				}
				topLine.delete();
				bomWnd.delete();
				
				try {
					bomWnd.close();
				}
				catch(Exception ex) { }
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if (bvr != null) {
			try {
				bvr.delete();
			}
			catch(Exception ex) { }
		}
	}

}
