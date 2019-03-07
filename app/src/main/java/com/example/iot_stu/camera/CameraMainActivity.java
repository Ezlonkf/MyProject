package com.example.iot_stu.camera;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraMainActivity extends AppCompatActivity implements Callback{
    ImageView imageView;
    File file;
    Camera camera;
    Button button;
    Bitmap bitmap;
    SurfaceView mSurfaceView;
    private boolean mPreviewRunning; //运行相机浏览

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycameralayout);
        button = (Button) this.findViewById(R.id.bTakePhoto);
        mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);
        imageView = (ImageView) findViewById(R.id.image);
        SurfaceHolder mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback((SurfaceHolder.Callback) this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        Parameters params = camera.getParameters();
        //拍照时自动对焦
        params.setFocusMode(Parameters.FOCUS_MODE_AUTO);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            //将SurfaceHolder设置为相机的预览显示
            camera.setPreviewDisplay(holder);
            mPreviewRunning = true;
        } catch (IOException e) {
            camera.release();
            camera = null;
        }
        camera.startPreview();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    PictureCallback pictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                savePicture(data);
            }
        }
    };

    //保存和显示图片
    private void savePicture(byte[] data) {
        try {
            //图片ID
            String imageId = System.currentTimeMillis() + "";
            //相片保存路径
            String pathName = android.os.Environment.getExternalStorageDirectory().getPath() + "/";
            //创建文件
            File file = new File(pathName);
            if (!file.exists()) {
                file.mkdirs();
            }
            pathName = "/" + imageId + ".jpeg";
            file = new File(pathName);
            if (!file.exists()) {
                file.createNewFile();//文件不存在则新建
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();

            //读取照片，并对其进行缩放
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeFile(pathName, options);//此时返回的bitmap为空
            WindowManager manager = getWindowManager();//获取屏幕的宽度
            Display display = manager.getDefaultDisplay();
            int screenWidth = display.getWidth();//将Bitmap的显示宽度设置为手机屏幕宽度

            options.inSampleSize = options.outWidth / screenWidth;
            //将inJustDecodeBounds设置为false，以便可以解码为Bitmap文件
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(pathName, options);//读取照片Bitmap

            //将图片在控件ImageView上显示出来
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            mSurfaceView.setVisibility(View.GONE);
            if (mPreviewRunning) {   //停止相机浏览
                camera.stopPreview();
                mPreviewRunning = false;
            }
            Toast.makeText(this, "已经保存在路径：" + pathName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
