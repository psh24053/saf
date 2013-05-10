package com.shntec.saf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import android.util.Log;

public class SAFUtils {

	/**
	 * 将一个inputstream读入，以Byte数组的形式返回
	 * @param in
	 * @return
	 */
	public static byte[] readInputStream(InputStream in){
		
		String result = "";
		byte[] readByte = new byte[1024];
		int readCount = -1;
		
		try {
			while((readCount = in.read(readByte, 0, 1024)) != -1){
				result += new String(readByte).trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result.getBytes();
	}
	
	
}
