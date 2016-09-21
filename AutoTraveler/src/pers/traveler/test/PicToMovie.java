package pers.traveler.test;

import java.io.File;
import java.io.FilenameFilter;
import pers.traveler.review.utils.MovieUtils;
import java.util.ArrayList;
import java.util.List;

public class PicToMovie {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int fps = 2;
		String suffix = "png";
		System.out.println(File.separator);
		String pngDir = "E:\\\\log_for_auto\\\\png\\\\20160829172042\\\\";
	    String aviFileName = pngDir + "review.avi";
		System.out.println("开始创建视频......");
//        try {
//			PicToAvi.convertPicToAvi(pngDir, suffix, aviFileName, fps, mWidth, mHeight);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		final File[] pics = new File(pngDir).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("." + suffix);
            }
        });
//		String mjpeg = "E:\\plugins\\mjpeg\\bin\\Release\\mjpeg.exe";
//		for(File file:pics){
////			System.out.println(file.getAbsolutePath().replaceAll(".png", ".jpg"));
////			jpgs.append(file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-3).concat("jpg") + " ");
//			ImageUtils.png2jpg(file);
//		}
//		//调用mjpeg去生成视频,mjpeg [-f fps] [-o output.avi] [-s input.mp3] input1.jpg [input2.jpg ...]
//		String command = mjpeg + 
//				" -f " + fps + " -o " + new File(aviFileName).getPath() + " -s " + 
//				ImageUtils.getJpgsByPath(new File(pngDir).getPath());
//		System.out.println("---------\n"+command+"\n-------");
//		String lines = CmdUtil.run(command);
//		for(String line:lines.split(System.getProperty("line.separator"))){
////			result += line+" ";
//			System.out.println("++"+line+"++");
//		}
		
		
		List<String> a = new ArrayList<String>();
		a.add(aviFileName);
		for(File file:pics){
//			System.out.println(file.getAbsolutePath().replaceAll(".png", ".jpg"));
//			jpgs.append(file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-3).concat("jpg") + " ");
//			ImageUtils.png2jpg(file);
			a.add(file.getAbsolutePath());
		}
		try {
			MovieUtils.platavi(pngDir,fps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("创建视频完毕!");
	}
}