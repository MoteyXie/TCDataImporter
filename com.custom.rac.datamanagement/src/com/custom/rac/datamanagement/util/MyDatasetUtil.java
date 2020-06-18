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
			TCComponent[] coms = tcc.getRelatedComponents(ref_name);
			boolean flag = true;
			for (TCComponent com : coms) {
				if (com instanceof TCComponentDataset) {
					if (name.equals(com.getProperty("object_name"))) {
						flag = false;
						break;
					}
//					TCComponentDataset ds = (TCComponentDataset) com;					
//					TCComponentDatasetDefinition df = (TCComponentDatasetDefinition) ds.getDatasetDefinitionComponent();
//					NamedReferenceContext nameRefContexts[] = df.getNamedReferenceContexts();
//					if (nameRefContexts.length > 0) {
//						NamedReferenceContext nf = nameRefContexts[0];
//						String namedRef = nf.getNamedReference();
//						String[] fileNames = ds.getFileNames(namedRef);
//						if (fileNames.length > 0) {
//							if (fileNames[0].equals(name)) {
//								flag = false;
//								break;
//							}
//						}
//					}
				}
			}
			if (flag) {
				tcc.add(ref_name, dataset);
			}
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
		} else if (fileType.contains("MSPowerPoint")) {
			refType = "powerpoint";
		} else if (fileType.contains("Zip")) {
			refType = "ZIPFILE";
		} else if (fileType.contains("PDF")) {
			refType = "PDF_Reference";
		} else if (fileType.contains("JPEG")) {
			refType = "JPEG_Reference";
		} else if (fileType.contains("Text")) {
			refType = "Text";
		} else if (fileType.contains("SF8_DWG")) {
			refType = "SF8_DWG";
		} else if (fileType.contains("DXF")) {
			refType = "DXF";
		} else if (fileType.contains("SF8_CSV")) {
			refType = "SF8_CSV";
		} else if (fileType.contains("SF8_AP15")) {
			refType = "SF8_AP15";
		} else if (fileType.contains("SF8_MP4")) {
			refType = "SF8_MP4";
		} else if (fileType.contains("SF8_RAR")) {
			refType = "SF8_RAR";
		} else if (fileType.contains("SWDrw")) {
			refType = "DrwFile";
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
			datesetType = "MSPowerPoint";
		} else if (fileName.endsWith("pptx")) {
			datesetType = "MSPowerPointX";
		} else if (fileName.endsWith("zip")) {
			datesetType = "Zip";
		} else if (fileName.endsWith("pdf") || fileName.endsWith("PDF")) {
			datesetType = "PDF";
		} else if (fileName.endsWith("jpg")) {
			datesetType = "JPEG";
		} else if (fileName.endsWith("txt")) {
			datesetType = "Text";
		} else if (fileName.endsWith("dwg") || fileName.endsWith("DWG")) {
			datesetType = "SF8_DWG";
		} else if (fileName.endsWith("dxf")) {
			datesetType = "DXF";
		} else if (fileName.endsWith("rar")) {
			datesetType = "SF8_RAR";
		} else if (fileName.endsWith("mp4")) {
			datesetType = "SF8_MP4";
		} else if (fileName.endsWith("csv")) {
			datesetType = "SF8_CSV";
		} else if (fileName.endsWith("ap15")) {
			datesetType = "SF8_AP15";
		} else if (fileName.endsWith("SLDDRW")) {
			datesetType = "SWDrw";
		}

		if (datesetType == null) {
			throw new Exception("文件类型未定义");
		}
		return datesetType;
	}

}
