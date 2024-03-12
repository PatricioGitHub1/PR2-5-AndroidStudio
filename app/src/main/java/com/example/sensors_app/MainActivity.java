package com.example.sensors_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor sensor;
    private long lastUpdate = 0;
    SensorEventListener sensorListener;
    private static final int SHAKE_THRESHOLD = 800;
    TextView textX;
    TextView textZ;
    TextView textY;

    private float last_x,last_y,last_z;
    final Context context = this;
    final GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Toast.makeText(context, "Se ha hecho un DoubleTap ", Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Toast.makeText(context, "onLongPress", Toast.LENGTH_SHORT).show();
        }
    };
    GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textX = findViewById(R.id.textX);
        textZ = findViewById(R.id.textZ);
        textY = findViewById(R.id.textY);

        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                long curTime = System.currentTimeMillis();

                // Valors de l'acceleròmetre en m/s^2
                float xAcc = sensorEvent.values[0];
                float yAcc = sensorEvent.values[1];
                float zAcc = sensorEvent.values[2];

                // Processament o visualització de dades...
                xAcc = (float) (Math.round(xAcc * 100.0) / 100.0);
                yAcc = (float) (Math.round(yAcc * 100.0) / 100.0);
                zAcc = (float) (Math.round(zAcc * 100.0) / 100.0);

                textX.setText(String.valueOf(xAcc));
                textZ.setText(String.valueOf(zAcc));
                textY.setText(String.valueOf(yAcc));

                curTime = System.currentTimeMillis();
                long diffTime = (curTime - lastUpdate);

                if (diffTime > 5.0f) {
                    float speed = Math.abs(xAcc+yAcc+zAcc - last_x - last_y - last_z) / diffTime * 10000;
                    //Log.i("dNFOO ", "diff time: "+diffTime);
                    lastUpdate = curTime;

                    if (speed > SHAKE_THRESHOLD) {
                        Log.i("INFOO", "shake detected w/ speed: " + speed);
                        Toast.makeText(context, "Se ha hecho un DoubleTap ", Toast.LENGTH_SHORT).show();
                    }
                }
                last_x = xAcc;
                last_y = yAcc;
                last_z = zAcc;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Es pot ignorar aquesta CB de moment
            }
        };

        detector = new GestureDetector(context, listener);

        // listeners double tap
        detector.setOnDoubleTapListener(listener);
        detector.setIsLongpressEnabled(true);

        // Select the type of sensor (see official documentation)
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register the listener to capture sensor events
        if (sensor != null) {
            sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event) || super.onTouchEvent(event);
    }
}

