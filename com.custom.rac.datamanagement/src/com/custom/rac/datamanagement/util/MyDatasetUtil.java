package com.custom.rac.datamanagement.util;

import java.io.File;

import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentDatasetType;
import com.teamcenter.rac.kernel.TCException;

public class MyDatasetUtil {

	/**
	 * @Title: createDateset
	 * @Description: TODO(创建数据集，并弹出是否打开数据的对话框)
	 * @param @param revision
	 * @param @param file
	 * @param @throws TCException
	 * @param @throws IOException 参数
	 * @return void 返回类型
	 * @throws
	 */

	public static void createDateset(TCComponent tcc, String name, File file, String ref_name)
		throws Exception{
		String fileType = getFileType(file);
		String ref = getrefType(fileType);
		TCComponentDatasetType type = (TCComponentDatasetType) tcc.getSession().getTypeService().getTypeComponent("Dataset");
		TCComponentDataset dataset = type.create(name, "", fileType);
		String[] refs = new String[] { ref };
		String[] files = new String[] { file.getAbsolutePath() };
		dataset.setFiles(files, refs);
		if (ref_name!= null) {
			tcc.add(ref_name, dataset);
		}
		
	}

	/**
	 * @Title: getrefType
	 * @Description: TODO(获取TC文件类型对应的关系类型)
	 * @param @param fileType
	 * @param @return
	 * @param @throws TCException 参数
	 * @return String 返回类型
	 * @throws
	 */

	public static String getrefType(String fileType) throws Exception {
		String refType = null;
		if (fileType.contains("MSExcel")) {
			refType = "excel";
		} else if (fileType.contains("MSWord")) {
			refType = "word";
		} else if (fileType.contains("MSProwerPoint")) {
			refType = "powerpoint";
		} else if (fileType.contains("Zip")) {
			refType = "ZIPFILE";
		} else if (fileType.contains("PDF")) {
			refType = "PDF_Reference";
		} else if (fileType.contains("JPEG")) {
			refType = "JPEG_Reference";
		} else if (fileType.contains("Text")) {
			refType = "Text";
		}

		if (refType == null) {
			throw new Exception("找不到引用类型");
		}
		return refType;
	}

	/**
	 * @Title: getFileType
	 * @Description: TODO(获取文件在TC对应的文件类型)
	 * @param @param file
	 * @param @return
	 * @param @throws TCException 参数
	 * @return String 返回类型
	 * @throws
	 */

	public static String getFileType(File file) throws Exception {
		String datesetType = null;
		if (file == null) {
			throw new TCException("找不到引用类型");
		}
		String fileName = file.getName();
		if (fileName.endsWith("xls")) {
			datesetType = "MSExcel";
		} else if (fileName.endsWith("xlsx")) {
			datesetType = "MSExcelX";
		} else if (fileName.endsWith("doc")) {
			datesetType = "MSWord";
		} else if (fileName.endsWith("docx")) {
			datesetType = "MSWordX";
		} else if (fileName.endsWith("ppt")) {
			datesetType = "MSProwerPoint";
		} else if (fileName.endsWith("pptx")) {
			datesetType = "MSProwerPointX";
		} else if (fileName.endsWith("zip") || fileName.endsWith("rar") || fileName.endsWith("7z")) {
			datesetType = "Zip";
		} else if (fileName.endsWith("pdf")) {
			datesetType = "PDF";
		} else if (fileName.endsWith("jpg") || fileName.endsWith("jpeg")) {
			datesetType = "JPEG";
		} else if (fileName.endsWith("txt")) {
			datesetType = "Text";
		}

		if (datesetType == null) {
			throw new Exception("文件类型未定义");
		}
		return datesetType;
	}

}
