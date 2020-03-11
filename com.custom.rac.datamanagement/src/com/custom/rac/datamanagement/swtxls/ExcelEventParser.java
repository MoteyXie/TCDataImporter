package com.custom.rac.datamanagement.swtxls;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @autor jasmine
 */
public class ExcelEventParser {
    private String fileName;
    private SimpleSheetContentsHandler handler;
    
	private void setHandler(SimpleSheetContentsHandler handler) {
		this.handler = handler;
	}

	// 放置读取数据
//    protected List<List<String>> table = new ArrayList<>();
	protected MyTable myTable = new MyTable();

    public ExcelEventParser(String filename){
        this.fileName = filename;
    }

    public MyTable parse() {
        OPCPackage opcPackage = null;
        InputStream inputStream = null;

        try {
            FileInputStream fileStream = new FileInputStream(fileName);
            opcPackage = OPCPackage.open(fileStream);
            XSSFReader xssfReader = new XSSFReader(opcPackage);

            StylesTable styles = xssfReader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcPackage);
            
            SheetIterator sheets = (SheetIterator)xssfReader.getSheetsData();
            while(sheets.hasNext()) {
            	inputStream = sheets.next();
                processSheet(styles, strings, inputStream, sheets.getSheetName());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (opcPackage != null) {
                try {
                    opcPackage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return myTable;
    }

	// 确定XMLReader解析器，使用SAX模式解析xml文件
    private void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream, String sheetName) throws SAXException, ParserConfigurationException, IOException {
        XMLReader sheetParser = SAXHelper.newXMLReader();

        if (handler == null) {
            setHandler(new SimpleSheetContentsHandler());
        }
        
        handler.setSheetName(sheetName);
        sheetParser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, handler, false));
        
        try {
            sheetParser.parse(new InputSource(sheetInputStream));
        } catch (RuntimeException e) {
            System.out.println("---> 遇到空行读取文件结束！");
        }
    }

	// 实现SheetContentsHandler
    public class SimpleSheetContentsHandler implements SheetContentsHandler{
    	
    	protected String sheetName;
        protected MyRow myRow;
     
        
        public void setSheetName(String sheetName) {
        	this.sheetName = sheetName;
        }
        
        @Override
        public void startRow(int rowNum) {
        	myRow = new MyRow(rowNum);
        }

        @Override
        public void endRow() {

        	MySheet mySheet = myTable.getSheet(sheetName);
        	if(mySheet == null) {
        		mySheet = new MySheet(sheetName);
        		try {
					myTable.addSheet(sheetName, mySheet);
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        	mySheet.addRow(myRow);
        }

        /**
         * 所有单元格数据转换为string类型，需要自己做数据类型处理
         * @param cellReference 单元格索引
         * @param formattedValue 单元格内容（全部被POI格式化为字符串）
         */
        @Override
        public void cell(String cellReference, String formattedValue) {
//        	System.out.println(cellReference);
//            row.add(formattedValue);
            myRow.cells.add(new MyCell(formattedValue, cellReference));
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {
        	System.out.println(text + ", " + isHeader + "," + tagName);
        }

    }
}

