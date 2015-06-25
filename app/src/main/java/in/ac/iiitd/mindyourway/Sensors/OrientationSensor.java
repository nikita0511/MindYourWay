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

import in.ac.iiitd.mindyourway.Others.Constants;

/**
 * Created by NikitaGupta on 6/22/2015.
 */
public class OrientationSensor extends Service implements SensorEventListener{

    private ResultReceiver mReceiver;
    private SensorManager sensorManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mReceiver = intent.getParcelableExtra(Constants.EXTRA_RECEIVER);

        return START_STICKY;

    }

    @Override
    public void onCreate() {
        // Setup and start collecting
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);
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

    private final String TAG = OrientationSensor.class.getSimpleName();

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            // send something?
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float[] values = event.values;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if (mReceiver != null) {
                Bundle result = new Bundle();
                result.putFloat(OrientationResult.EXTRA_X, x);
                result.putFloat(OrientationResult.EXTRA_Y, y);
                result.putFloat(OrientationResult.EXTRA_Z, z);
                mReceiver.send(OrientationResult.RESULTCODE_UPDATE,
                        result);
            }
        }
    }
}
