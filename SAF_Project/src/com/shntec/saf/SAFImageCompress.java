package com.shntec.saf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.util.Log;

/**
 * 图片下载压缩工具
 * @author shihao
 *
 */
public class SAFImageCompress {

	/**
	 * 根据url获取一个bitmap，根据传入的大小来进行缩放
	 * @param url
	 * @param width
	 * @param height
	 * @return
	 * @throws SAFException 
	 */
	public Bitmap HttpFixedCompress(String url, int width, int height) throws SAFException{
		BitmapFactory.Options options = new Options();
		// 设置这个Bitmap不获取像素矩阵，只获取大小参数
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inJustDecodeBounds = true;

		SAFHTTP http = new SAFHTTP();
//		byte[] bytes = null;
//		
//		try {
//			bytes = SAFUtils.readInputStream(entity.getContent());
//		} catch (IllegalStateException e) {
//			throw new SAFException(0, e.getMessage(), e);
//		} catch (IOException e) {
//			throw new SAFException(0, e.getMessage(), e);
//		}
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(http.GET(url).getEntity().getContent(), null, options);
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int bwidth = options.outWidth;
		int bheight = options.outHeight;
		
		Log.i("SAF", "w "+bwidth+" ,h "+bheight);
		
		int be = 1;//be=1表示不缩放  
	    if (bwidth > bheight && bwidth > width) {//如果宽度大的话根据宽度固定大小缩放  
	        be = (int) (bwidth / width);  
	    } else if (bwidth < bheight && bheight > height) {//如果高度高的话根据宽度固定大小缩放  
	        be = (int) (bheight / height);  
	    }
	    // 缩放比例不能小于1
	    if (be <= 0) {
	    	be = 1;  
	    } 
	    // 计算好缩放比例之后重新生成一个bitmap，然后使用thumbnailutils工具生成缩略图，并且释放之前图片所占资源
		options.inSampleSize = be;
		options.inJustDecodeBounds = false;
		try {
			bitmap = BitmapFactory.decodeStream(http.GET(url).getEntity().getContent(), null, options);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap newbitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);
		bitmap.recycle();
		
		return newbitmap;
	}
	/**
	 * 根据url获取一个bitmap,自动适应图片压缩，将图片压缩至100k以下
	 * @param url
	 * @return
	 * @throws SAFException
	 */
	public Bitmap HttpAutoCompress(String url) throws SAFException{
		BitmapFactory.Options options = new Options();
		// 设置这个Bitmap不获取像素矩阵，只获取大小参数
		options.inJustDecodeBounds = true;
		
		SAFHTTP http = new SAFHTTP();
		Bitmap bitmap = null;
		HttpEntity entity = http.GET(url).getEntity();
		long totalSize = entity.getContentLength();
		InputStream in = null;
		
		try {
			in = entity.getContent();
//			bitmap = BitmapFactory.decodeStream(in, null, options);
		} catch (IllegalStateException e) {
			throw new SAFException(0, e.getMessage(), e);
		} catch (IOException e) {
			throw new SAFException(0, e.getMessage(), e);
		}
		// 将bitmap设置为正常加载模式
		options.inJustDecodeBounds = false;
		// 设置bitmap的色彩模式为RGB565，每个像素占两个字节
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		
		int imgWidth = options.outWidth;
		int imgHeight = options.outHeight;
		
		// 如果图片的大小大于100k则执行压缩
		if(totalSize / 1024 > 100){
			int m = (int) (totalSize / 1024 / 100);
			if(m > 2){
				m /= 2;
			}
			if(m <= 0){
				m = 1;
			}
			options.inSampleSize = m;
		}
		
		bitmap = BitmapFactory.decodeStream(in, null, options);
		
		try {
			in.close();
		} catch (IOException e) {
			throw new SAFException(0, e.getMessage(), e);
		}
		return bitmap;
		
	}
	
}
