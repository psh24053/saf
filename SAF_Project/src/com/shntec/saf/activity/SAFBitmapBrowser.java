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
import android.os.Bundle; 
import android.util.DisplayMetrics; 
import android.util.FloatMath; 
import android.util.Log; 
import android.view.GestureDetector; 
import android.view.MotionEvent; 
import android.view.View; 
import android.view.GestureDetector.OnGestureListener; 
import android.view.View.OnClickListener; 
import android.view.View.OnTouchListener; 
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button; 
import android.widget.ImageView; 
import android.widget.Toast; 
 
public class SAFBitmapBrowser extends Activity implements OnTouchListener,OnClickListener{ 
    /*
     *
     * Class Descripton goes here.
     *
     * @class testactivity
     * @version  1.0
     * @author  yourname
     * @time  2012-1-6 ����10:58:20
     */ 
    private Button big,small; 
    private  Bitmap newbitmap; 
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
    private long beginTime,endTime; 
     @Override 
     public void onCreate(Bundle savedInstanceState)    { 
      super.onCreate(savedInstanceState); 
      /*display.xml Layout */ 
      
    //设置无标题  
      requestWindowFeature(Window.FEATURE_NO_TITLE);  
      //设置全屏  
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
      setContentView(R.layout.layout_bitmapbrowser); 
     
      big = (Button)this.findViewById(R.id.enlarge); 
      small = (Button)this.findViewById(R.id.narrow); 
     
      big.setOnClickListener(this); 
      small.setOnClickListener(this); 
      
      //获取手机屏幕的宽和高 
      DisplayMetrics dm = new DisplayMetrics();    
      getWindowManager().getDefaultDisplay().getMetrics(dm);   
     
      int width = dm.widthPixels; 
      int height = dm.heightPixels; 
       
       
      // 获取图片本身的宽 和高 
      Bitmap mybitmap=BitmapFactory.decodeResource(getResources(), R.drawable.abc); 
//      System.out.println("old==="+mybitmap.getWidth()); 
       
      int widOrg=mybitmap.getWidth(); 
      int heightOrg=mybitmap.getHeight(); 
       
      // 宽 高 比列 
      float scaleWid = (float)width/widOrg; 
      float scaleHeight = (float)height/heightOrg; 
      float scale; 
       
      bmp = (ImageView)this.findViewById(R.id.bitmap); 
       
      // 如果宽的 比列大于搞的比列 则用高的比列 否则用宽的 
       
       
      if(scaleWid>scaleHeight) 
      { 
          scale = scaleHeight; 
      } 
      else 
          scale = scaleWid; 
       
 //     matrix=new Matrix(); 
      bmp.setImageBitmap(mybitmap); 
       
      matrix.postScale(scale,scale); 
      
      bmp.setImageMatrix(matrix); 
       
      bmp.setOnTouchListener(this); 
      
      bmp.setLongClickable(true); 
     
      savedMatrix.set(matrix); 
     } 
      @Override 
    public boolean onTouch(View v, MotionEvent event) { 
        // TODO Auto-generated method stub 
//        mGestureDetector.onTouchEvent(event); 
//          System.out.println("action==="+event.getAction()); 
          switch(event.getAction()& MotionEvent.ACTION_MASK) 
          { 
            case MotionEvent.ACTION_DOWN: 
                 
                beginTime = System.currentTimeMillis(); 
                 
                mode = DRAG; 
//                System.out.println("down"); 
                first.set(event.getX(), event.getY()); 
                start.set(event.getX(), event.getY()); 
                break; 
            case MotionEvent.ACTION_UP: 
                 
                endTime = System.currentTimeMillis(); 
                 
//                System.out.println("endTime=="+(endTime - beginTime)); 
                float x = event.getX(0) - first.x; 
                float y = event.getY(0) - first.y; 
                // 多长的距离 
                float move = FloatMath.sqrt(x * x + y * y); 
                 
//                System.out.println("move=="+(move)); 
                 
                // 计算时间和移动的距离  来判断你想要的操作，经过测试90%情况能满足 
                if(endTime - beginTime<500&&move>20) 
                { 
                    //这里就是做你上一页下一页的事情了。 
//                    Toast.makeText(this, "----do something-----", 1000).show(); 
                } 
                break; 
            case MotionEvent.ACTION_MOVE: 
                 
//                System.out.println("move"); 
                if(mode == DRAG) 
                { 
                	float[] fs = new float[9];
                	bmp.getImageMatrix().getValues(fs);
                	
                	float left = fs[2];
                	float top = fs[5];
                	float scale = fs[8];
                	
                	float dx = event.getX()-start.x;
                	float dy = event.getY()-start.y;
                	
                	
                	System.out.println(bmp.getImageMatrix());
                	
                	if(left > 0){
                		dx = 0;
                	}else if(left < -scale * bmp.getWidth()){
                		dx = -scale * bmp.getWidth();
                	}
                	
                	if(top > 0){
                		dy = 0;
                	}else if(top < -scale * bmp.getHeight()){
                		dy = -scale * bmp.getHeight();
                	}
                	
                	matrix.postTranslate(dx, dy); 
                	start.set(event.getX(), event.getY());
                	
                	
                	
                	
                     
                } 
                else 
                { 
                    float newDist = spacing(event); 
                    if (newDist > 10f) { 
//                  matrix.set(savedMatrix); 
                    float scale = newDist / oldDist; 
//                    System.out.println("scale=="+scale); 
                    matrix.postScale(scale, scale, mid.x, mid.y); 
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
//                System.out.println("ACTION_POINTER_DOWN"); 
                break; 
            case MotionEvent.ACTION_POINTER_UP: 
//                System.out.println("ACTION_POINTER_UP"); 
                break; 
          } 
          bmp.setImageMatrix(matrix); 
        return false; 
    } 
 
     
 
    @Override 
    public void onClick(View v) { 
        // TODO Auto-generated method stub 
        if(v==small) 
        { 
            matrix.postScale(0.5f,0.5f,0,0); 
//          matrix.setScale(0.5f, 0.5f); 
            bmp.setImageMatrix(matrix); 
        } 
        else 
        { 
            matrix.postScale(2f,2f); 
//          matrix.setScale(2f,2f); 
            bmp.setImageMatrix(matrix); 
        } 
    } 
    /**
     * 计算拖动的距离
     * @param event
     * @return
     */ 
    private float spacing(MotionEvent event) { 
    	if(event.getPointerCount() > 1){
    		float x = event.getX(0) - event.getX(1); 
    		float y = event.getY(0) - event.getY(1); 
    		return FloatMath.sqrt(x * x + y * y); 
    	}
    	return 0;
    } 
    /**
     * 计算两点的之间的中间点
     * @param point
     * @param event
     */ 
     
    private void midPoint(PointF point, MotionEvent event) { 
        float x = event.getX(0) + event.getX(1); 
        float y = event.getY(0) + event.getY(1); 
        point.set(x / 2, y / 2); 
    } 
}