package in.ac.iiitd.mindyourway.Sensors;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

/**
 * Created by NikitaGupta on 6/21/2015.
 */

public class AccelerometerSensor extends Service implements SensorEventListener{

    private ResultReceiver mReceiver;
    private SensorManager sensorManager;
    private final String TAG = AccelerometerSensor.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mReceiver = intent.getParcelableExtra("in.ac.iiitd.RECEIVER");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        // Setup and start collecting
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // Stop collecting and tear down
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //no need for this now
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if (mReceiver != null) {
                Bundle result = new Bundle();
                result.putFloat(AccelerometerResult.EXTRA_X, x);
                result.putFloat(AccelerometerResult.EXTRA_Y, y);
                result.putFloat(AccelerometerResult.EXTRA_Z, z);
                mReceiver.send(AccelerometerResult.RESULTCODE_UPDATE,
                        result);
            }
        }
    }

}
