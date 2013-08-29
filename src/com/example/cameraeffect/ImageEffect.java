package com.example.cameraeffect;

import android.graphics.Bitmap;

public class ImageEffect {
	
	static{
		System.loadLibrary("cameraeffect");
	}
	
	public static native void reverseBitmap(Bitmap sourceBitmap, Bitmap reverseBitmap);
	public static native void convertToGray(Bitmap sourceBitmap, Bitmap grayBitmap);
	public static native void reverseAndGray(Bitmap sourceBitmap, Bitmap destBitmap);
	public static native void colorMatrix(Bitmap sourceBitmap, Bitmap destBitmap, float[] colorMatrix);
	public static native void convolutionFilter(Bitmap sourceBitmap, Bitmap destBitmap, float[] matrix, float divisor, float bias);
	public static native void decodeYUV420SP(int[] RGBData, byte[] data, int width, int height);

}
