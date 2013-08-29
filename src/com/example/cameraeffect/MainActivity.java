package com.example.cameraeffect;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity implements PreviewCallback{

	private Camera mCamera;
	private ImageView imageView;
	
	private int imageWidth = -1;
	private int imageHeight = -1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.camera_view);
        mCamera = Camera.open();
        mCamera.setPreviewCallback(this);
        Camera.Parameters p = mCamera.getParameters();
        
        mCamera.startPreview();
    }

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		if(imageWidth == -1){
			imageWidth = camera.getParameters().getPreviewSize().width;
			imageHeight = camera.getParameters().getPreviewSize().height;
		}
		Log.i("DDD", "preview");
		int RGBData[] = new int[imageWidth * imageHeight];
		ImageEffect.decodeYUV420SP(RGBData,  data, imageWidth, imageHeight);
		Bitmap sourceBitmap = Bitmap.createBitmap(RGBData, imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
		Bitmap destBitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
		
		ImageEffect.reverseBitmap(sourceBitmap, destBitmap);
		/*
		ImageEffect.convolutionFilter(sourceBitmap, destBitmap, new float[]{-1, -1, -1,
				                                                            -1,  8, -1,
				                                                            -1, -1, -1}, 1, 0);
		 */
		imageView.setImageBitmap(destBitmap);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mCamera.release();
	}
}
