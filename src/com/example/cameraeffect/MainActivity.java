package com.example.cameraeffect;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity implements PreviewCallback,
                                                      SurfaceHolder.Callback{

	private Camera mCamera;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	
	private int imageWidth = -1;
	private int imageHeight = -1;
	
	private Bitmap sourceBitmap;
	private Bitmap destBitmap;
	private int RGBData[];
	
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView) findViewById(R.id.camera_view);
        mCamera = Camera.open();
        mCamera.setPreviewCallback(this);
        Camera.Parameters p = mCamera.getParameters();
        p.setPreviewSize(300, 400);
        p.setPreviewFpsRange(10, 30);
        
        mCamera.startPreview();
        
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        
        paint.setColor(0x000000);
    }

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		if(imageWidth == -1){
			imageWidth = camera.getParameters().getPreviewSize().width;
			imageHeight = camera.getParameters().getPreviewSize().height;
		}
		Log.i("DDD", "preview");
		RGBData = new int[imageWidth * imageHeight];
		ImageEffect.decodeYUV420SP(RGBData,  data, imageWidth, imageHeight);
		sourceBitmap = Bitmap.createBitmap(RGBData, imageWidth, imageHeight, Bitmap.Config.RGB_565);
		//destBitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.RGB_565);
		
		//ImageEffect.reverseBitmap(sourceBitmap, destBitmap);
		/*ImageEffect.convolutionFilter(sourceBitmap, destBitmap, new float[]{-1, -1, -1,
				                                                            -1,  8, -1,
				                                                            -1, -1, -1}, 1, 0);*/
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mCamera.release();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		new Thread(new MyRunnable()).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	private class MyRunnable implements Runnable{
		
		@Override
		public void run(){
			while(true){
				if(sourceBitmap != null){
					Canvas canvas = surfaceHolder.lockCanvas();
					canvas.drawBitmap(sourceBitmap, new Matrix(), null);
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
