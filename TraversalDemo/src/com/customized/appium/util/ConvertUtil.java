package com.customized.appium.util;

public class ConvertUtil {

	/**
	 * Bounds坐标转换
	 * @param bounds
	 * @return
	 */
	public static int[] convertBounds(String bounds){
		if(bounds == null||bounds.length()<3){
			return null;
		}
		byte s1='[';
		byte s2=']';
		byte[] a = bounds.getBytes();
		int lengthA = a.length;
		byte[] b = new byte[lengthA-3];//delete'['']' and add ','
		int flag = 0;
		for(int i = 0,j=0; i<a.length;i++){
			if(a[i]==s1||a[i]==s2){
				if(flag==1){
					b[j] = ',';
					j++;
				}
				flag++;
				continue;
			}
			b[j] = a[i];
			j++;
		}

		String[] position = new String(b).split(",");
		int[] positions = new int[4];
		positions[0] = Integer.parseInt(position[0]);
		positions[1] = Integer.parseInt(position[1]);
		positions[2] = Integer.parseInt(position[2]);
		positions[3] = Integer.parseInt(position[3]);
		return positions;
	}
}
