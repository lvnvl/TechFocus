package pers.traveler.review.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import pers.traveler.tools.CmdUtil;

public class ImageUtils {

	public static void png2jpg(File pngFile) {
		BufferedImage bufferedImage;
		try {
			// read image file
			bufferedImage = ImageIO.read(pngFile);
			// create a blank, RGB, same width and height, and a white
			// background
			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
			// write to jpeg file
			File jpgFile = new File(
					pngFile.getAbsolutePath().substring(0, pngFile.getAbsolutePath().length() - 3).concat("jpg"));
			// System.out.println("converting:"+pngFile.getAbsolutePath().substring(0,
			// pngFile.getAbsolutePath().length()-3).concat("jpg"));
			if (!jpgFile.exists()) {
				jpgFile.createNewFile();
			}
			// System.out.println("jpg file create done!");

			ImageIO.write(newBufferedImage, "jpg", jpgFile);
			// pngFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取，给定APK的包名，类似：com.meizu.notepaper aapt dump badging
	 * <file_path.apk> package: name='com.rili.android.client' versionCode='28'
	 * versionName='4.0' 命令如上，定位方式：定位到，（='，' ver）之间的字符串
	 * 代码：line.substring(line.indexOf("A=")+2, line.indexOf("U=")-1);
	 */
	public static String getJpgsByPath(String path) {
		System.out.println("path is " + path);
		String command = "dir " + path + " /B /OD |findstr jpg";
		String lines = CmdUtil.run(command);
		System.out.println("command is:" + command);
		System.out.println("lines is:\n" + lines + "\n~~~~~~~");
		String result = "";
		for (String line : lines.split(System.getProperty("line.separator"))) {
			result += line + " ";
			System.out.println(line);
		}
		return result;
	}
}