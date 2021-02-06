package io.github.syske.commont.utils.ireport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *   <p>
 * Description: IReport工具类
 * </p>
 * @author CaoLei
 *
 *
 */
public class IReportUtil {
	private static Logger log = Logger.getLogger(IReportUtil.class);
	// 报表临时文件存放路径
	private final static String tmpDirName = "tmp/";
	// ireport报表存放路径
	private final static String IREPORT_SOURCES_DIR = "/ireport/";

	static {
		File tmpDir = new File(tmpDirName);
		if(!tmpDir.exists()) {
			tmpDir.mkdir();
		}
	}

	private IReportUtil() {}

	/**
	 * IReport报表生成pdf
	 *
	 * @param request
	 * @param reprotfileName 报表文件名称
	 * @param exportFileName pdf保存完整路径，包含文件名及后缀
	 * @param parameters 报表入参参数集
	 * @param connection 数据库连接
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	/**
	 * @param reprotfileName
	 * @param exportFileName
	 * @param parameters
	 * @param connection
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	public static String exportPdfToServer(String reprotfileName, String exportFileName,
	                                       Map<String, Object> parameters, Connection connection)
			throws IOException, JRException {

		String sourceFileName = IREPORT_SOURCES_DIR	+ reprotfileName;
		// 根据文件创建文件的输出流
		JasperRunManager.runReportToPdfFile(sourceFileName, exportFileName,
				parameters, connection);
		return exportFileName;

	}

	/**
	 * IReport报表生成pdf，返回文件名
	 * @param reprotfileName 报表文件名称
	 * @param exportFileOutputStream 报表输出数据流
	 * @param parameters 报表入参参数集
	 * @param connection 数据库连接
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	public static String exportPdfToServer(String reprotfileName,
	                                       Map<String, Object> parameters, Connection connection) throws Exception {
		InputStream sourceFileInputStream = IReportUtil.class.getClass()
				.getResourceAsStream(IREPORT_SOURCES_DIR + reprotfileName);
		String exportFileName = tmpDirName + UUID.randomUUID().toString() + ".pdf";
		try {
			log.info("入参：sourceFileInputStream-->:" + sourceFileInputStream);
			log.info("入参：exportFileName-->:" + exportFileName);
			log.info("入参：parameters-->:" + parameters);
			log.info("入参：connection-->:" + connection);
			FileOutputStream exportFileOutputStream = new FileOutputStream(
					exportFileName);
			// 根据文件创建文件的输出流
			JasperRunManager.runReportToPdfStream(sourceFileInputStream,
					exportFileOutputStream, parameters, connection);
		} catch (FileNotFoundException e) {
			log.error("报表生成错误", e);
			throw new Exception("报表生成错误");
		} catch (JRException e) {
			log.error("报表生成错误", e);
			throw new Exception("报表生成错误");
		} finally {
			if(sourceFileInputStream != null)
				try {
					sourceFileInputStream.close();
				} catch (IOException e) {
					log.error("报表生成错误", e);
					throw new Exception("报表生成错误");
				}
			if(sourceFileInputStream != null)
				try {
					sourceFileInputStream.close();
				} catch (IOException e) {
					log.error("报表生成错误", e);
					throw new Exception("报表生成错误");
				}
		}

		return exportFileName;

	}

}