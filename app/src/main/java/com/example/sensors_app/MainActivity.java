package com.example.sensors_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor sensor;
    SensorEventListener sensorListener;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // Valors de l'acceleròmetre en m/s^2
                float xAcc = sensorEvent.values[0];
                float yAcc = sensorEvent.values[1];
                float zAcc = sensorEvent.values[2];

                // Processament o visualització de dades...
                // Log.i("info", "Xacc" + String.valueOf(xAcc));
                // Log.i("info", "Yacc" + String.valueOf(yAcc));
                // Log.i("info", "Zacc" + String.valueOf(zAcc));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Es pot ignorar aquesta CB de moment
            }
        };

        // Seleccionem el tipus de sensor (veure doc oficial)
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // registrem el Listener per capturar els events del sensor
        if( sensor!=null ) {
            sensorManager.registerListener(sensorListener,sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}

