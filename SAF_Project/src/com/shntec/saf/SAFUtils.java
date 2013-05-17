package com.shntec.saf;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;


import android.content.Context;
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
	/**
	 * 将一个inputstream，以字符串的形式返回
	 * @param in
	 * @return
	 */
	public static String readInputStreamToString(InputStream in){
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
		
		return result;
	}
	
	/**
	 * 将输入的dp转换为对应的px值，并返回px值
	 * @param dp
	 * @param c
	 * @return
	 */
	public static int dp2px(int dp, Context c) {

		return (int) (dp * c.getResources().getDisplayMetrics().density + 0.5f);
	}
	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
	/**
	 * 去除特殊字符或将所有中文标号替换为英文标号
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]")
				.replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	/**
	 * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
	 * @param tms
	 * @return
	 */
	public static String convertTimeToFormat(long tms){
		
		
		long curTime = System.currentTimeMillis();
		
		long time = (curTime - tms) / (long)1000;
		
		
		if(time < 60 && time >= 0){
			return "刚刚";
		}else if(time >= 60 && time < 3600){
			return time / 60 +"分钟前";
		}else if(time >= 3600 && time < 3600 * 24){
			return time / 3600 + "小时前";
		}else if(time >= 3600 * 24 && time < 3600 * 24 * 30 ){
			return time / 3600 / 24 + "天前";
		}else if(time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12){
			return time / 3600 / 24 / 30 + "个月前";
		}else if(time >= 3600 * 24 * 30 * 12){
			return time / 3600 / 24 / 30 / 12 + "年前";
		}else{
			return "刚刚";
		}
		
	}
	
	/**
	 * MD5加密
	 * @param str
	 */
	public static String getMD5Str(String str) {
		
		String md5 = DigestUtils.md5Hex(str);
		
		return md5;
	}
	/**
	 * 根据传入的byte数组生成MD5
	 * @param bytes
	 * @return
	 */
	public static String getMD5ByteArray(byte[] bytes){
		String md5 = DigestUtils.md5Hex(bytes);
		
		return md5;
	}
	/**
	 * 判断传入的字符串是否是一个邮箱地址
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail){
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(strEmail);
        return m.find();
	}
	/**
	 * 判断传入的字符串是否是一个手机号码
	 * @param strPhone
	 * @return
	 */
	public static boolean isPhoneNumber(String strPhone){
		Pattern p = Pattern.compile("^[+]{0,1}[0-9]{0,3}[-]{0,1}[0-9]{11}$");
        Matcher m = p.matcher(strPhone);
        return m.find();
	}
	/**
	 * 将一个时间戳转化成时间字符串，如2010-12-12 23:24:33
	 * @param time
	 * @return
	 */
	public static String convertTime(long time){
		if(time == 0){
			return "";
		}
		
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
		
	}
	/**
	 * 将一个时间戳转化成时间字符串，自定义格式 
	 * @param time
	 * @param format  如 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String convertTime(long time, String format){
		if(time == 0){
			return "";
		}
		
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	
}
