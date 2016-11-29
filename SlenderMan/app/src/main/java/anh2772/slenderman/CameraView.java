package anh2772.slenderman;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by AndyHecht on 11/10/2016.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private android.hardware.Camera mCamera;
    protected Activity mActivity;

    public CameraView(Context context, android.hardware.Camera camera){
        super(context);
        mCamera = camera;
        mCamera.setDisplayOrientation(90);


        mActivity = (Activity)context;
        //get the holder and set this class as the callback, so we can get camera data here
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        System.out.println("created surface");
        try{
            //when the surface is created, we can set the camera to draw images in this surfaceholder
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAH: " + e);
            Log.d("ERROR", "Camera error on surfaceCreated " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int frrmat, int width, int height) {
        //before changing the application orientation, you need to stop the preview, rotate and then start it again
        if(mHolder.getSurface() == null)//check if the surface is ready to receive camera data
            return;

        try{
            mCamera.stopPreview();
        } catch (Exception e){
            //this will happen when you are trying the camera if it's not running
        }
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.getSupportedPreviewSizes();

//        Display display = mActivity.getWindowManager().getDefaultDisplay();
//
//        if(display.getRotation() == Surface.ROTATION_0)
//        {
//            System.out.println("ROTATION 0");
//            parameters.setPreviewSize(height, width);
//            mCamera.setDisplayOrientation(90);
//        }
//
//        if(display.getRotation() == Surface.ROTATION_90)
//        {
//            System.out.println("ROTATION 90");
//            parameters.setPreviewSize(width, height);
//        }
//
//        if(display.getRotation() == Surface.ROTATION_180)
//        {
//            System.out.println("ROTATION 180");
//            parameters.setPreviewSize(height, width);
//        }
//
//        if(display.getRotation() == Surface.ROTATION_270)
//        {
//            System.out.println("ROTATION 270");
//            parameters.setPreviewSize(width, height);
//            mCamera.setDisplayOrientation(180);
//        }

            mCamera.setParameters(parameters);
            mCamera.startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //our app has only one screen, so we'll destroy the camera in the surface
        //if you are unsing with more screens, please move this code your activity
        mCamera.stopPreview();
        mCamera.release();
    }
}