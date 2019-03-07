package com.example.iot_stu.camera;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SensorDemo extends AppCompatActivity implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mOrientation;
    private Sensor mLight;
    private TextView tAccelerometer;
    private TextView tOrientation;
    private TextView tLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_demo);
        tAccelerometer = (TextView)this.findViewById(R.id.tAccelerometer);
        tOrientation = (TextView)this.findViewById(R.id.tOrientation);
        tLight = (TextView)this.findViewById(R.id.tLight);
        //获取传感器管理器
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //获得加速度传感器
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //获得方向度传感器
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //获得光线传感器
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    protected void onResume(){
        super.onResume();
        //对加速度传感器注册传感器监听器
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        //对方向度传感器注册传感器监听器
        mSensorManager.registerListener(this,mOrientation,SensorManager.SENSOR_DELAY_NORMAL);
        //对光线传感器注册传感器监听器
        mSensorManager.registerListener(this,mLight,SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        super.onPause();
        //取消传感器监听器注册
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //在此方法中，编写当某个传感器的精度发生变化时应执行的操作
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //在此方法中，编写当某个传感器的数值发生改变时应执行的操作
        //得到方向的值

    }
}
