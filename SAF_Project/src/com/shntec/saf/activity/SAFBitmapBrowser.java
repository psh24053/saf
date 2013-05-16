package com.shntec.saf.activity;

/*
 * @project testbmplarge
 * @package com.bmp.large
 * @file testactivity.java
 * @version  1.0
 * @author  yourname
 * @time  2012-1-6 ����10:58:20
 * CopyRight:������������Ϣ�������޹�˾ 2012-1-6
 */

import com.shntec.saf.R;
import com.shntec.saf.SAFException;
import com.shntec.saf.SAFImageCompress;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SAFBitmapBrowser extends Activity implements OnTouchListener,
		OnClickListener,OnGestureListener,OnDoubleTapListener {
	/*
	 * 
	 * Class Descripton goes here.
	 * 
	 * @class testactivity
	 * 
	 * @version 1.0
	 * 
	 * @author yourname
	 * 
	 * @time 2012-1-6 ����10:58:20
	 */
	private Button big, small;
	private Bitmap newbitmap;
	private GestureDetector mGestureDetector;
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	ImageView bmp;
	PointF first = new PointF();
	PointF start = new PointF();
	PointF mid = new PointF();;
	private float oldDist;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	private long beginTime, endTime;
	private float defaultScale;
	private int displayWidth;
	private int displayHeight;
	private int mainWidth;
	private int mainHeight;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* display.xml Layout */

		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_bitmapbrowser);

		// 获取手机屏幕的宽和高
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int width = dm.widthPixels;
		int height = dm.heightPixels;

		mainWidth = width;
		mainHeight = height;

		// 获取图片本身的宽 和高
		Bitmap mybitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.abc);
		// System.out.println("old==="+mybitmap.getWidth());

		int widOrg = mybitmap.getWidth();
		int heightOrg = mybitmap.getHeight();

		displayWidth = widOrg;
		displayHeight = heightOrg;

		// 宽 高 比列
		float scaleWid = (float) width / widOrg;
		float scaleHeight = (float) height / heightOrg;
		float scale;

		bmp = (ImageView) this.findViewById(R.id.bitmap);

		// 如果宽的 比列大于搞的比列 则用高的比列 否则用宽的

		if (scaleWid > scaleHeight) {
			scale = scaleHeight;
		} else
			scale = scaleWid;

		defaultScale = scale;
		// matrix=new Matrix();
		bmp.setImageBitmap(mybitmap);

		matrix.postScale(scale, scale);
		matrix.postTranslate(width / 2 - scale * widOrg / 2, height / 2 - scale
				* heightOrg / 2);
		bmp.setImageMatrix(matrix);

		bmp.setOnTouchListener(this);

		bmp.setLongClickable(true);
		
		mGestureDetector = new GestureDetector(this, this);
		
		savedMatrix.set(matrix);
	}
	/**
	 * 将大小设置为最大
	 */
	public void setMaxScale(){
		matrix.setScale(1, 1);
		bmp.setImageMatrix(matrix);
	}
	
	/**
	 * 还原图片大小位置
	 */
	public void resetPoint() {

		matrix.setScale(defaultScale, defaultScale);
		bmp.setImageMatrix(matrix);
		matrix.postTranslate(mainWidth / 2 - defaultScale * displayWidth / 2,
				mainHeight / 2 - defaultScale * displayHeight / 2);
		bmp.setImageMatrix(matrix);
	}

	/**
	 * 还原图片位置
	 */
	public void resetLocal() {
		float[] values = new float[9];
		bmp.getImageMatrix().getValues(values);
		float widthScale = values[0];
		float heightScale = values[4];
		matrix.postTranslate(mainWidth / 2 - defaultScale * displayWidth / 2, 0);
		bmp.setImageMatrix(matrix);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		 mGestureDetector.onTouchEvent(event);
		// System.out.println("action==="+event.getAction());
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:

			beginTime = System.currentTimeMillis();

			mode = DRAG;
			// System.out.println("down");
			first.set(event.getX(), event.getY());
			start.set(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_UP:

			endTime = System.currentTimeMillis();

			// System.out.println("endTime=="+(endTime - beginTime));
			float x = event.getX(0) - first.x;
			float y = event.getY(0) - first.y;
			// 多长的距离
			float move = FloatMath.sqrt(x * x + y * y);

			// System.out.println("move=="+(move));

			// 计算时间和移动的距离 来判断你想要的操作，经过测试90%情况能满足
			if (endTime - beginTime < 500 && move > 20) {
				// 这里就是做你上一页下一页的事情了。
				// Toast.makeText(this, "----do something-----", 1000).show();
				// 如果缩放比例小于默认比例则还原图片的位置
			}
			if (isScale()) {
				resetPoint();
			}

			break;
		case MotionEvent.ACTION_MOVE:

			// System.out.println("move");
			if (mode == DRAG) {
				float[] values = new float[9];
				bmp.getImageMatrix().getValues(values);
				// for(int i = 0 ; i < values.length ; i ++){
				// System.out.println(values[i]);
				// }

				// matrix矩阵中的宽度和高度缩放值
				float widthScale = values[0];
				float heightScale = values[4];

				// matrix矩阵中的x,y偏移量
				float matrixX = values[2];
				float matrixY = values[5];

				float dx = event.getX() - start.x;
				float dy = event.getY() - start.y;

				// 如果当前大小比例大于默认比例
				if (widthScale > defaultScale) {
					System.out.println("widthScale -> " + widthScale
							+ " ,heightScale -> " + heightScale);
					System.out.println("matrixX -> " + matrixX
							+ " ,matrixY -> " + matrixY);

					// 获取边界比例值
					float cScale = widthScale - defaultScale;

					System.out.println(heightScale * displayHeight);

					// 左边界判断，左边界的x值为0
					if (matrixX + dx >= 0) {
						dx = 0;
					}

					// 右边界判断，右边界的x值为cScale*displayWidth
					if (matrixX + dx + -mainWidth <= -(widthScale * displayWidth)) {
						dx = 0;
					}

					// 上边界判断，上边界的y值为0
					if (matrixY + dy >= 0) {
						dy = 0;
					}

					// 下边界判断，下边界的y值为cScale*displayHeight
					if (matrixY + dy + -mainHeight <= -(heightScale * displayHeight)) {
						dy = 0;
					}
					//

				} else if (widthScale <= defaultScale) {
					// 如果当前大小比例小于默认比例
					dx = 0;
					dy = 0;
				}

				matrix.postTranslate(dx, dy);

				start.set(event.getX(), event.getY());

			} else {
				float newDist = spacing(event);
				if (newDist > 10f) {
					// matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					// System.out.println("scale=="+scale);
					float[] values = new float[9];
					bmp.getImageMatrix().getValues(values);

					// matrix矩阵中的宽度和高度缩放值
					float widthScale = values[0];
					float heightScale = values[4];

					// matrix矩阵中的x,y偏移量
					float matrixX = values[2];
					float matrixY = values[5];

					// 如果scale大于1则代表是在放大
					System.out.println(mid.x+" ,"+mid.y);
					if (scale > 1) {
						if (widthScale < 1) {
							matrix.postScale(scale, scale, mainWidth / 2, mainHeight / 2);
						}
					} else {
						matrix.postScale(scale, scale, mainWidth / 2, mainHeight / 2);
					}

				}
				oldDist = newDist;
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				midPoint(mid, event);
				mode = ZOOM;
			}

			// System.out.println("ACTION_POINTER_DOWN");
			break;
		case MotionEvent.ACTION_POINTER_UP:
			// System.out.println("ACTION_POINTER_UP");

			// 如果缩放比例小于默认比例则还原图片的位置
			if (isScale()) {
				resetPoint();
			} else {
				float dx = event.getX() - start.x;
				float dy = event.getY() - start.y;
				float[] values = new float[9];
				bmp.getImageMatrix().getValues(values);

				// matrix矩阵中的宽度和高度缩放值
				float widthScale = values[0];
				float heightScale = values[4];

				// matrix矩阵中的x,y偏移量
				float matrixX = values[2];
				float matrixY = values[5];
				float cScale = widthScale - defaultScale;

				if (matrixX + dx > 0 || matrixX + dx < -(cScale * displayWidth)) {
					
					matrix.postTranslate(-matrixX, 0);
				}

			}

			break;
		}
		bmp.setImageMatrix(matrix);
		return false;
	}

	/**
	 * 判断缩放比例是否比默认比例大了
	 * 
	 * @return
	 */
	public boolean isScale() {
		float[] values = new float[9];
		bmp.getImageMatrix().getValues(values);

		// matrix矩阵中的宽度和高度缩放值
		float widthScale = values[0];
		float heightScale = values[4];

		// matrix矩阵中的x,y偏移量
		float matrixX = values[2];
		float matrixY = values[5];
		return widthScale <= defaultScale;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == small) {
			matrix.postScale(0.5f, 0.5f, 0, 0);
			// matrix.setScale(0.5f, 0.5f);
			bmp.setImageMatrix(matrix);
		} else {
			matrix.postScale(2f, 2f);
			// matrix.setScale(2f,2f);
			bmp.setImageMatrix(matrix);
		}
	}

	/**
	 * 计算拖动的距离
	 * 
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event) {
		if (event.getPointerCount() > 1) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}
		return 0;
	}

	/**
	 * 计算两点的之间的中间点
	 * 
	 * @param point
	 * @param event
	 */

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
	
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		float[] values = new float[9];
		bmp.getImageMatrix().getValues(values);

		// matrix矩阵中的宽度和高度缩放值
		float widthScale = values[0];
		float heightScale = values[4];

		if(widthScale >= 1){
			
//			resetPoint();
			
new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					boolean stop = false;
					
					while(!stop){
						float[] values = new float[9];
						bmp.getImageMatrix().getValues(values);

						// matrix矩阵中的宽度和高度缩放值
						float widthScale = values[0];
						float heightScale = values[4];

						// matrix矩阵中的x,y偏移量
						float matrixX = values[2];
						float matrixY = values[5];
						if(widthScale >= defaultScale){
							matrix.postScale(0.9f, 0.9f, mainWidth / 2, mainHeight / 2);
							
							runOnUiThread(new Runnable() {
								public void run() {
									bmp.setImageMatrix(matrix);
								}
							});
						}else{
							stop = true;
							runOnUiThread(new Runnable() {
								public void run() {
									resetPoint();
								}
							});
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
					
				}
			}).start();
			
		}else{
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					boolean stop = false;
					
					while(!stop){
						float[] values = new float[9];
						bmp.getImageMatrix().getValues(values);

						// matrix矩阵中的宽度和高度缩放值
						float widthScale = values[0];
						float heightScale = values[4];

						// matrix矩阵中的x,y偏移量
						float matrixX = values[2];
						float matrixY = values[5];
						if(widthScale <= 1){
							matrix.postScale(1.1f, 1.1f, mainWidth / 2, mainHeight / 2);
							
							runOnUiThread(new Runnable() {
								public void run() {
									bmp.setImageMatrix(matrix);
								}
							});
						}else{
							stop = true;
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
					
				}
			}).start();
			
			
		}
		
		
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}