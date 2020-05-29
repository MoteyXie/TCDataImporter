package com.custom.rac.datamanagement.perspectives;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.ui.views.TCComponentView;
import com.teamcenter.rac.ui.views.ViewerViewPart;
            

public class DataManagementViewPerspective implements IPerspectiveFactory{

	public static String VIEWER_VIEW_PART_ID = "com.teamcenter.rac.ui.views.ViewerView";
	
	public static final int MODEL_GENERAL = 1;
	public static final int MODEL_GROUP_STAT = 2; 
	public static final int MODEL_PERSON_STAT = 3; 
	
	public static int current_model = MODEL_GROUP_STAT;
	
	public static IPageLayout m_layout;
	public static IFolderLayout tableLayout;
	public static IFolderLayout componetViewLayout;
	public static IFolderLayout jtViewLayout;
	public static IFolderLayout viewerViewLayout;
	public static IFolderLayout statFolder; 
	public static ViewPart currentViewPart;     
	
	private static Map<Class, Integer> viewPartCounts = new HashMap<>();
	private static Map<Class, Object> viewParts = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	public static void traverseViews() {
		viewParts.clear();
		IViewPart[] _viewParts = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViews();
        if (_viewParts != null && _viewParts.length > 0) {
            for (IViewPart viewPart : _viewParts) {
				viewParts.put(viewPart.getClass(), viewPart);
			}
        }
	}
	
	public static <T> T getViewPart(Class cls){
		
//		traverseViews();
//		if(viewParts.containsKey(cls))return (T)viewParts.get(cls);
		IViewPart[] _viewParts = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViews();
        if (_viewParts != null && _viewParts.length > 0) {
            for (IViewPart viewPart : _viewParts) {
//				viewParts.put(viewPart.getClass(), viewPart);
            	if(viewPart.getClass().equals(cls)) {
            		return (T) viewPart;
            	}
			}
        }
		
		return null;
	}
	
	public static <T> T getViewPart(String viewId) {
		return (T)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewId);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void showSelection(final Display display, final TCComponent selection) {
		
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					
					List list = new ArrayList<TCComponent>();
					list.add(selection);
					Method method = null;
					
					//在查看器中显示选中的对象信息
					ViewerViewPart viewerViewPart = getViewerViewPart();
					
					if(viewerViewPart != null) {
						method = viewerViewPart.getClass().getDeclaredMethod("processSetInput", IWorkbenchPart.class, List.class);
						method.setAccessible(true);
						method.invoke(viewerViewPart, null, list);
					}
					
					//在组件视图中显示选中的对象
					TCComponentView componentView = getTCComponentView();
					
					if(componentView != null) {
						method = componentView.getClass().getDeclaredMethod("processSetInput", IWorkbenchPart.class, List.class);
						method.setAccessible(true);
						method.invoke(componentView, null, list);
						
						//隐藏组件视图底部显示版本的窗口
						Field field = componentView.getClass().getDeclaredField("m_bottomForm");
						field.setAccessible(true);
						ViewForm bottomForm = (ViewForm) field.get(componentView);
						bottomForm.setVisible(false);
						bottomForm.getParent().layout();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
	}
	
	public static TCComponentView getTCComponentView() {
        return getViewPart(TCComponentView.class);
	}
	
	public static ViewerViewPart getViewerViewPart() {
		return getViewPart(ViewerViewPart.class);
	}
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		
		layout.setEditorAreaVisible(false);
		m_layout = layout;
		String editorArea = layout.getEditorArea();
		tableLayout = layout.createFolder("table_view_layout", IPageLayout.BOTTOM, -2f, editorArea);
		tableLayout.addView(ExcelTableViewPart.ID);
		
//		componetViewLayout = layout.createFolder("component_view_layout", IPageLayout.BOTTOM, 0.6f, "table_view_layout");
//		componetViewLayout.addView("com.teamcenter.rac.ui.views.TCComponentView");
//		
//		jtViewLayout = layout.createFolder("jt_view_layout", IPageLayout.RIGHT, 0.34f, "component_view_layout");
//		jtViewLayout.addView("com.teamcenter.rac.tdv.views.ThumbnailView");
//		
//		viewerViewLayout = layout.createFolder("viewer_view_layout", IPageLayout.RIGHT, 0.5f, "jt_view_layout");
//		viewerViewLayout.addView("com.teamcenter.rac.ui.views.ViewerView");
	}
	
	
	/**
	 * 添加一个视图,异步线程
	 * @param viewPartClass
	 */
	public static void createViewPartOnAsyncExec(final Class viewPartClass) {
		
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				
				createViewPart(viewPartClass);
            }
		});
	}
	
	/**
	 * 添加一个视图
	 * @param <T>
	 * @param viewPartClass
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T createViewPart(final Class viewPartClass) {
		
		if (tableLayout == null) {
            AIFUtility.getCurrentPerspectiveDef().openPerspective();
            getLastViewPart().getSite().getPage().resetPerspective();
        }
		
		IViewPart viewPart = getViewPart(viewPartClass);
		
		if(viewPart != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().bringToTop(viewPart);
		}else {
			tableLayout.addView(viewPartClass.getName() + ":" + nextViewCounter(viewPartClass));
			viewPart = getViewPart(viewPartClass);
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().bringToTop(viewPart);
		}
		
		return (T)viewPart;
	}
	
	public static int nextViewCounter(Class cls) {
		if(viewPartCounts.containsKey(cls)) {
			int count = viewPartCounts.get(cls);
			viewPartCounts.put(cls, count+1);
			return count;
		}else {
			int count = 1;
			viewPartCounts.put(cls, count);
			return count;
		}
	}
	
	
	public static void setCurrentViewPart(ViewPart viewPart) {
		currentViewPart = viewPart;
	}
	
	@SuppressWarnings("deprecation")
	public static ViewPart getLastViewPart() {
        
		IViewPart[] viewParts = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViews();
        if (viewParts != null && viewParts.length > 0) {
            return (ViewPart)viewParts[viewParts.length - 1];
        }
        return null;
    }

}
