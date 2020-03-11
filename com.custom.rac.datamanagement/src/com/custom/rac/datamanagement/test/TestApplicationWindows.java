package com.custom.rac.datamanagement.test;

import java.util.Date;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TestApplicationWindows {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TestApplicationWindows window = new TestApplicationWindows();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(600, 600);
		shell.setText("SWT Application");
		
//		String[] propertyNames = new String[] { "id", "name", "birthDate" };
//		new ListDataProvider(people, new ReflectiveColumnPropertyAccessor(propertyNames));
//		
//		DefaultCornerDataProvider cornerDataProvider = 
//				new DefaultCornerDataProvider(colHeaderDataProvider, rowHeaderDataProvider);
//			CornerLayer cornerLayer = 
//				new CornerLayer(
//					new DataLayer(cornerDataProvider), rowHeaderLayer, columnHeaderLayer);
//			
//		GridLayer gridLayer = new GridLayer(bodyLayer, columnHeaderLayer, rowHeaderLayer, cornerLayer);
//		
//		NatTable natTable = new NatTable(shell, gridLayer);

	}
	
//	public class RowHeaderLayerStack extends AbstractLayerTransform {
//
//		public RowHeaderLayerStack(IDataProvider dataProvider) {
//			DataLayer dataLayer = new DataLayer(dataProvider, 50, 20);
//			RowHeaderLayer rowHeaderLayer = 
//				new RowHeaderLayer(
//					dataLayer, bodyLayer, bodyLayer.getSelectionLayer());
//			setUnderlyingLayer(rowHeaderLayer);
//		}
//	}
//	
//	public class ColumnHeaderLayerStack extends AbstractLayerTransform {
//
//		public ColumnHeaderLayerStack(IDataProvider dataProvider) {
//			DataLayer dataLayer = new DataLayer(dataProvider);
//			ColumnHeaderLayer colHeaderLayer = 
//				new ColumnHeaderLayer(
//					dataLayer, bodyLayer, bodyLayer.getSelectionLayer());
//			setUnderlyingLayer(colHeaderLayer);
//		}
//	}
	
	public class BodyLayerStack extends AbstractLayerTransform {

		private SelectionLayer selectionLayer;

		public BodyLayerStack(IDataProvider dataProvider) {
			DataLayer bodyDataLayer = new DataLayer(dataProvider);
			ColumnReorderLayer columnReorderLayer = 
				new ColumnReorderLayer(bodyDataLayer);
			ColumnHideShowLayer columnHideShowLayer = 
				new ColumnHideShowLayer(columnReorderLayer);
			selectionLayer = new SelectionLayer(columnHideShowLayer);
			ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
			setUnderlyingLayer(viewportLayer);
		}

		public SelectionLayer getSelectionLayer() {
			return selectionLayer;
		}
	}
	
	public class Person {
		private int id;
		private String name;
		private Date birthDate;

		public Person(int id, String name, Date birthDate) {
			this.id = id; 
			this.name = name;    
			this.birthDate = birthDate;
		}

		public int getId() {return id;}
		public String getName() {return name;}
		public Date getBirthDate() {return birthDate;}
	}

}
