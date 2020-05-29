package com.custom.rac.datamanagement.views;

import java.io.InputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.custom.rac.datamanagement.bean.ToolBarItemBean;
import com.custom.rac.datamanagement.swtxls.MyTable;
import com.custom.rac.datamanagement.swtxls.SWTWorkbook;
import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.util.ImporterReader;
import com.custom.rac.datamanagement.util.ProgressCircle;
import com.teamcenter.rac.util.MessageBox;

import swing2swt.layout.BorderLayout;
import swing2swt.layout.FlowLayout;

public class ExcelTableViewPart extends ViewPart implements ISaveablePart2, EventHandler {

	public static final String ID = ExcelTableViewPart.class.getName();
	private Composite container;
	private SashForm centerPanel;
	private Label lb;
	public Combo importerSelecter;
	public ImporterReader importerReader;
	private MyTable myTable;
	private ProgressCircle circle;
	private boolean executing;
	
	private ToolBarItemBean[] centerToolBarItems = new ToolBarItemBean[] {
			new ToolBarItemBean("加载", "More_Normal.png", "OpenFileAction"),
			new ToolBarItemBean("重载", "Replay_Normal.png", "ReloadAction"),
			new ToolBarItemBean("开始", "Next_B_Pressed.png", "ImportAction"),
			new ToolBarItemBean("暂停", "Pause_Normal.png", ""),
			new ToolBarItemBean("停止", "Less_Normal.png", ""),
//			new ToolBarItemBean("测试", "test.png", "TestAction"),
	};
	
	private ToolBarItemBean[] rightToolBarItems = new ToolBarItemBean[] {
			
			new ToolBarItemBean("导出", "Down_Normal.png", "ExportTableAction"),
//			new ToolBarItemBean("保存", "Load_Normal.png", "SaveResultAction"),
			new ToolBarItemBean("关于", "Info_Normal.png", ""),
			new ToolBarItemBean("设置", "Setting_Normal.png", ""),
	};
	private SWTWorkbook swtWorkbook;
	private ProgressBar progressBar;
	
	public boolean isExecuting() {
		return executing;
	}
	
	public void setExecuting(boolean flag) {
		this.executing = flag;
	}
	
	public SWTWorkbook getSWTWorkbook() {
		return swtWorkbook;
	}
	
	public Composite getContainer() {
		return container;
	}

	public ExcelTableViewPart() {
		
	}
	
	public void initProgressBar() {
		progressBar.setMinimum(0);
		progressBar.setMaximum(swtWorkbook.getSelectedSheet().getTable().getItemCount() - 1);
	}
	
	public void setProgressValue(int value) {
		progressBar.setSelection(value);
	}
	
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
//		Composite container = new Composite(parent, SWT.NONE);
		this.container = parent;

		createActions();
		initializeToolBar();
		initializeMenu();
		
		parent.setLayout(new BorderLayout());
//		createTable(parent);
		createHeader(parent);
//		initImporterPanel(parent);
		initBody(parent);
	}
	
	public void initBody(Composite parent) {
		lb = new Label(parent, SWT.NONE);
	}
	
	public void load(MyTable myTable) {
		this.myTable = myTable;
		if(swtWorkbook != null && !swtWorkbook.isDisposed()) {
			swtWorkbook.dispose();
		}
		lb.dispose();
		swtWorkbook = new SWTWorkbook(container, SWT.NONE);
		swtWorkbook.setLayoutData(BorderLayout.CENTER);
		swtWorkbook.load(myTable);
		
		container.layout(true);
		
	}
	
	public MyTable getTable() {
		return myTable;
	}
	
	private void createToolItem(ToolBar toolBar, ToolBarItemBean[] toolItems) {
		for (ToolBarItemBean toolBarItemBean : toolItems) {
			final ToolItem ti = new ToolItem(toolBar, SWT.PUSH);
			ti.setText(toolBarItemBean.getName());
			ti.setImage(getImage(toolBar.getDisplay(), toolBarItemBean.getIconName()));
			try {
				final AbstractTableAction action = toolBarItemBean.getAction(this);
				if(action != null)ti.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(org.eclipse.swt.widgets.Event arg0) {
						try {
							action.run(ti);
						} catch (Exception e) {
							e.printStackTrace();
							MessageBox.post(e);
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void createHeader(Composite parent) {
		
		Composite toolBarPanel = new Composite(parent, SWT.NONE);
		
		toolBarPanel.setLayout(new BorderLayout(10, 10));
		toolBarPanel.setLayoutData(BorderLayout.NORTH);
		
		ToolBar toolBar = new ToolBar(toolBarPanel, SWT.FLAT);
		toolBar.setLayoutData(BorderLayout.CENTER);
		
		createToolItem(toolBar, centerToolBarItems);
		
		ToolBar rightToolBar = new ToolBar(toolBarPanel, SWT.FLAT);
		rightToolBar.setLayoutData(BorderLayout.EAST);
		
		createToolItem(rightToolBar, rightToolBarItems);
		
		Composite msgPanel = new Composite(toolBarPanel, SWT.NONE);
		msgPanel.setLayoutData(BorderLayout.SOUTH);
		msgPanel.setLayout(new BorderLayout(5,5));
		
		Composite importerPanel = new Composite(msgPanel, SWT.NONE);
		importerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		importerPanel.setLayoutData(BorderLayout.NORTH);
		
		new Label(importerPanel, SWT.NONE).setText("请选择导入程序：");
		importerSelecter = new Combo(importerPanel, SWT.DROP_DOWN | SWT.READ_ONLY);
		
		importerReader = new ImporterReader();
		System.out.println(importerReader.getAllImporterName());
		importerSelecter.setItems(importerReader.getAllImporterName());
		
		progressBar = new ProgressBar(msgPanel,SWT.HORIZONTAL|SWT.SMOOTH);
		progressBar.setLayoutData(BorderLayout.SOUTH);
		progressBar.setMinimum(0);
		progressBar.setMaximum(30);
//		progressBar.setSelection(14);
	}
	
	public Image getImage(Display display, String imgName) {
		InputStream is = this.getClass().getResourceAsStream("/resources/"+imgName);
		return is == null ? null : new Image(display,  is);
	}
	
	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleEvent(Event arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int promptToSaveOnClose() {
		// TODO Auto-generated method stub
		return 0;
	}


}
