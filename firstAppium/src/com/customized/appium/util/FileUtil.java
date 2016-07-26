package com.customized.appium.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import com.customized.appium.model.AElementWidget;
import com.customized.appium.model.DebugDevice;
import com.customized.appium.model.RecordEventModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 读取配置文件</br>
 * 输出结构图等调试信息</br>
 * 
 * @author kaliwn
 *
 */
public class FileUtil {

	/**
	 * 初始化本次测试存储路径
	 * @param path System.currentPath()
	 * @param appName
	 * @param udid
	 */
	public static String initFilePath(String appName, String udid, String path){
		path = path + File.separator + appName;
		File file = new File(path);
		if (!file.isFile())
			if (!file.exists())
				file.mkdir();
		file = new File(path + File.separator + udid);
		if (!file.exists())
			file.mkdir();
		path += File.separator + udid;
		return path;
	}

	/**
	 * 截图工具类 png格式
	 * 
	 * @param drivername
	 * @param filename
	 */
	public static void saveScreenshot(TakesScreenshot drivername, String path, String filename) {
		File scrFile = drivername.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(path + File.separator + filename));
			Log.d("save screenshot path is:" + path + File.separator + filename);
		} catch (IOException e) {
			Log.e("Can't save screenshot"+e.getMessage());
		}
	}

	/**
	 * 读取指定应用文件，或者相似名称文件，或该随机文件
	 * 
	 * @param path
	 *            文件位置，null 则读取当前目录 /apps/下的文件
	 * @param name
	 *            文件名，null 则随机读取
	 * @return
	 */
	public static File readAppFile(String path, String name) {
		File currentPath = new File(System.getProperty("user.dir"));
		File appDir = new File(currentPath, "apps");
		File app = null;

		if (path == null) {
			app = new File(appDir, name);
		} else {
			app = new File(path, name);
		}

		if (!app.exists()) {
			String[] s = appDir.list();
			for (String filename : s) {
				System.out.println(filename);
				if (filename.contains(name)) {
					app = new File(appDir, filename);
				} else if (filename.contains(".apk")) {
					app = new File(appDir, filename);
				}

			}
		}
		return app;
	}

	/**
	 * 读取配置文件 默认setting.json setting.xml
	 * 
	 * @param path
	 * @param name
	 * @return
	 */
	public static File readSettingFile(String path, String name) {
		return null;
	}

	/**
	 * 读取记录文件 默认setting.json setting.xml
	 * 
	 * @param path
	 * @param name
	 * @return
	 */
	public static List<RecordEventModel> readRecordFile(String path, String name) {
		File file = new File(path+"/"+name);
		Gson gson = new Gson();
		String str = null;
		List<RecordEventModel> eventList = null;
		try {
			str = FileUtils.readFileToString(file,"UTF-8");
		} catch (IOException e) {
			Log.e(e.getMessage());
		}
		if(str!=null){
			eventList = gson.fromJson(str, new TypeToken<List<RecordEventModel>>(){}.getType());
			Log.v(gson.toJson(eventList));
		}
		return eventList;
	}

	/**
	 * 输出应用截图 控件相关位置 PNG格式
	 * XXX 页面旋转转换
	 * 设备号
	 * @param drivername
	 * @param filename
	 * @param list
	 */
	public static void exportScreenShotWithElement(TakesScreenshot drivername, String filename,
			List<AElementWidget> list, String path) {
		
		File file;
		if(filename!=null){
			file = new File(path + File.separator+filename+".png");
		}else{
			file = new File(path + File.separator+"bitmap.png");
		}
		File img = drivername.getScreenshotAs(OutputType.FILE);
		// try {
		// FileUtils.copyFile(img,file);
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(img);
		} catch (WebDriverException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Graphics g = bi.getGraphics();
		int height = bi.getHeight();
		int width = bi.getWidth();

		System.out.println(height + ":" + width);

		g.setColor(Color.RED);
		Font f = new Font("", Font.BOLD, 50);
		g.setFont(f);

		// 有大部分的控件可能会重叠，这里进行一些特别处理
		List<int[]> location = new ArrayList<int[]>(list.size());
		for (AElementWidget e : list) {
			int[] p = ConvertUtil.convertBounds(e.getBounds());
			int[] pos = new int[2];
			pos[0] = p[0] + (p[2] - p[0]) / 2;
			pos[1] = p[1] + (p[3] - p[1]) / 2;

			location.add(pos);
			// g.drawRect(p[0], p[1], p[2]-p[0], p[3]-p[1]);
			g.draw3DRect(p[0], p[1], p[2] - p[0], p[3] - p[1], true);
			g.drawLine(p[0], p[1], p[2], p[3]);
			g.drawLine(p[2], p[1], p[0], p[3]);
		}

		// 相对位置转换
		int size = list.size();
		int points = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < i; j++) {
				// (x1-x2) (y1-y2)粗略估算两个位置是否重叠
				if ((Math.abs((location.get(i)[0] - location.get(j)[0])) < 10)
						&& (Math.abs((location.get(i)[1] - location.get(j)[1])) < 10)) {
					points++;
				}
			}
			// 只考虑x轴,文字大小
			int x = location.get(i)[0] + 50 * points;
			if (points > 0) {
				g.drawString(" & " + list.get(i).getID(), x, location.get(i)[1]);
			} else {
				g.drawString("" + list.get(i).getID(), x, location.get(i)[1]);
			}
			points = 0;
		}

		g.dispose();
		try {
			ImageIO.write(bi, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出配置文件（操作记录， json文件）
	 */
	public static void exportSettings() {
		
	}

	/**
	 * 导出JSON数据，方便查看，html方式或者其他
	 * 
	 * @param list
	 */
	public static void exportRecordWithJSON(List<RecordEventModel> list, String filename, String path) {
		Gson gson = new Gson();
		String str = gson.toJson(list);
		saveFile(path, filename, str);
	}

	/**
	 * 输出思维导图
	 */
	public static void exportThinkMindMap() {

	}

	/**
	 * 输出日志
	 */
	public static void exportLogFile() {

	}

	/**
	 * 保存文件
	 * @param filePath /xx/xx
	 * @param filename file
	 * @param data
	 */
	public static void saveFile(String filePath, String filename, String data) {
		Log.d("save file path is:" + filePath +File.separator+filename);
		File file = new File(filePath);
		if(!file.exists()) file.mkdirs();
		file = new File(file, filename);
		try {
			FileUtils.writeStringToFile(file, data, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 保存文件
	 * @param file
	 * @param filename
	 */
	public static void saveFile(File file, String filename, String path) {
		Log.d("try save file path is:" + path + File.separator+filename);
		try {
			FileUtils.copyFile(file, new File(path + File.separator + filename));
		} catch (IOException e) {
			Log.e("Can't save file");
		}
	}

	public static <T> void exportRunningSettingWithJSON(List<T> list) {
		Gson gson = new Gson();
		String str = gson.toJson(list);
		saveFile(System.getProperty("user.dir"), "setting", str);
	}

	public static List<DebugDevice> readJsonFile(String path, String name) {
		File file = new File(path + File.separator + name);
		Gson gson = new Gson();
		String str = null;
		List<DebugDevice> list = null;
		try {
			str = FileUtils.readFileToString(file,"UTF-8");
		} catch (IOException e) {
			Log.e(e.getMessage());
		}
		if(str!=null){
			list = gson.fromJson(str, new TypeToken<List<DebugDevice>>(){}.getType());
			Log.v(gson.toJson(list));
		}
		return list;
	}
	/**
	 * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
	 *
	 * @param fileName
	 * @param content
	 */
	public static void method1(String file, String conent) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(conent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 追加文件：使用FileWriter
	 *
	 * @param fileName
	 * @param content
	 */
	public static void method2(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 追加文件：使用RandomAccessFile
	 *
	 * @param fileName
	 *            文件名
	 * @param content
	 *            追加的内容
	 */
	public static void method3(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			String s2 = new String(content.getBytes("GBK"), "iso8859-1");
			randomFile.writeBytes(s2);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static boolean writeToExcelFile(String fileUrl, String sheetName, List timeList)
			throws IOException {
		if (timeList != null) {
			File file = new File(fileUrl);

			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				@SuppressWarnings("resource")
				HSSFWorkbook newWb = new HSSFWorkbook(); // 新建一个工作簿
				FileOutputStream fos = new FileOutputStream(file);
				newWb.write(fos);
				fos.close();
			}

			FileInputStream fis = new FileInputStream(file);

			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(fis); // 新建一个工作簿

			HSSFSheet sheet = wb.getSheet(sheetName); // 获取表格
			// System.out.println("表格：" + sheet);
			if (sheet == null) {
				sheet = wb.createSheet(sheetName); // 新建一个表格
			}

			int numberOfRows = sheet.getLastRowNum() + 1; // 获取已存在数据的行数

			// 将查询出的数据设置到sheet对应的单元格中
			int j=0;
			int i= 0;
//			for (List t : timeList) {
//				HSSFRow row = sheet.createRow(i++ + numberOfRows);// 创建所需的行数
//				j=0;
//				HSSFCell cell = null; // 设置单元格的数据类型
//				cell = row.createCell(j++, HSSFCell.CELL_TYPE_STRING);
//				cell.setCellValue(t.getTag()); // 设置单元格的值
//				cell = row.createCell(j++, HSSFCell.CELL_TYPE_STRING);
//				cell.setCellValue(t.getName()); // 设置单元格的值
//				cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
//				cell.setCellValue(t.getTime()); // 设置单元格的值
//			}

			try {
				FileOutputStream fos = new FileOutputStream(file);
				wb.write(fos);
				fos.close();
			} catch (IOException e) {
				System.out.println("文件“" + file.getPath() + "”创建失败！");
				e.printStackTrace();
			}
		}
		return true;
	}
}
