package in.ac.iiitd.mindyourway.MainClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import in.ac.iiitd.mindyourway.Others.Constants;
import in.ac.iiitd.mindyourway.Others.PreferencesData;
import in.ac.iiitd.mindyourway.Others.RSFileWriter;
import in.ac.iiitd.mindyourway.R;
import in.ac.iiitd.mindyourway.Sensors.AccelerometerResult;
import in.ac.iiitd.mindyourway.Sensors.AccelerometerSensor;
import in.ac.iiitd.mindyourway.Sensors.GPSResult;
import in.ac.iiitd.mindyourway.Sensors.GPSSensor;
import in.ac.iiitd.mindyourway.Sensors.GyroscopeResult;
import in.ac.iiitd.mindyourway.Sensors.GyroscopeSensor;
import in.ac.iiitd.mindyourway.Sensors.OrientationResult;
import in.ac.iiitd.mindyourway.Sensors.OrientationSensor;

/**
 * Created by NikitaGupta on 6/24/2015.
 */
//reference: CA project, http://cyrixmorten.net/CA-Project.zip

public class SensorActivity extends Activity{

    AccelerometerResult accelerometerResultReceiver;
    OrientationResult orientationResultReceiver;
    GyroscopeResult gyroscopeResultReceiver;
    GPSResult gpsResultReceiver;

    private static String FILE_PREFIX;
    private static final String FILE_SUFFIX = ".csv";
    RSFileWriter accelerometerWriter;
    RSFileWriter stateWriter;
    RSFileWriter orientationWriter;
    RSFileWriter gyroscopeWriter;
    RSFileWriter gpsWriter;


    private final String SENSOR_NOT_SUPPORTED = "Not supported";
    Button back;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_main);

        back = (Button)findViewById(R.id.button6);

        FILE_PREFIX = PreferencesData.getString(this,
                PreferencesData.PREFERENCE_FILE_PREFIX, "test_");
        setupSensors();
        setupStateToggles();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<ToggleButton> toggleButtons;
    private String currentState = "";

    private void setupStateToggles() {
        toggleButtons = new ArrayList<ToggleButton>();
        LinearLayout toggle_layout = (LinearLayout) findViewById(R.id.toggle_state_layout);
        for (int toggle_index = 0; toggle_index < toggle_layout.getChildCount(); toggle_index++) {
            ToggleButton toggle = (ToggleButton) toggle_layout.getChildAt(toggle_index);
            toggle.setOnCheckedChangeListener(new ToggleButtonChange());
            toggleButtons.add(toggle);
        }
    }

    private class ToggleButtonChange implements CompoundButton.OnCheckedChangeListener {

        private final String TAG = ToggleButtonChange.class.getSimpleName();

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (isChecked) {

                String state = (String) buttonView.getTag();

                for (ToggleButton toggle : toggleButtons) {
                    // uncheck others
                    if (!toggle.getTag().equals(state)) {
                        toggle.setChecked(false);
                    }
                }

                if (!state.equals(currentState)) {
                    currentState = state;
                    Toast.makeText(SensorActivity.this, "State set to " + currentState, Toast.LENGTH_SHORT).show();
                    stateWriter.write(currentState);
                }

            }
        }

    }
    private void setupSensors() {

        Handler handler = new Handler();

        gpsResultReceiver = new GPSResult(handler);
        gpsResultReceiver.setReceiver(new GPSReceiver());

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sm.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < sensorList.size(); i++) {
            int type = sensorList.get(i).getType();
            if (type == Sensor.TYPE_ACCELEROMETER) {
                accelerometerResultReceiver = new AccelerometerResult(
                        handler);
                accelerometerResultReceiver
                        .setReceiver(new AccelerometerReceiver());
            }
            if (accelerometerResultReceiver == null) {
                new UpdateUI(R.id.tv_accel_result, SENSOR_NOT_SUPPORTED);
            }

            if (type == Sensor.TYPE_ORIENTATION) {
                orientationResultReceiver = new OrientationResult(
                        handler);
                orientationResultReceiver
                        .setReceiver(new OrientationReceiver());
            }
            if (orientationResultReceiver == null) {
                new UpdateUI(R.id.tv_orientation_result, SENSOR_NOT_SUPPORTED);
            }
            if (type == Sensor.TYPE_GYROSCOPE) {
                gyroscopeResultReceiver = new GyroscopeResult(handler);
                gyroscopeResultReceiver.setReceiver(new GyroscopeReceiver());
            }
            if (gyroscopeResultReceiver == null) {
                new UpdateUI(R.id.tv_gyro_result, SENSOR_NOT_SUPPORTED);
            }
        }
    }

    private void createFileWriters() {
        accelerometerWriter = new RSFileWriter(
                getQualifiedFilename("accelerometer"), "x", "y", "z");

        orientationWriter = new RSFileWriter(
                getQualifiedFilename("orientation"), "x", "y", "z");

        gyroscopeWriter = new RSFileWriter(getQualifiedFilename("gyroscope"),
                "x", "y", "z");

        gpsWriter = new RSFileWriter(getQualifiedFilename("gps"), "firstfix",
                "lat", "lng", "speed", "altitude", "bearing", "accuracy",
                "time");

        stateWriter = new RSFileWriter(getQualifiedFilename("state"), "state");
    }

    private String getQualifiedFilename(String filename) {
        return FILE_PREFIX + filename + FILE_SUFFIX;
    }

    @Override
    protected void onDestroy() {
        closeWriters();
        super.onDestroy();
    }

    private void closeWriters() {

        accelerometerWriter.close();
        orientationWriter.close();
        gyroscopeWriter.close();
        gpsWriter.close();
        stateWriter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_file_prefix) {
            LayoutInflater factory = LayoutInflater.from(SensorActivity.this);
            final View textEntryView = factory.inflate(
                    R.layout.dialog_stringinput, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(SensorActivity.this);
            alert.setTitle("Set file prefix");
            alert.setView(textEntryView);

            final EditText mUserText = (EditText) textEntryView
                    .findViewById(R.id.value);
            mUserText.setText(FILE_PREFIX);

            alert.setPositiveButton(getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            String prefixString = mUserText.getText()
                                    .toString();

                            FILE_PREFIX = prefixString;

                            PreferencesData.saveString(SensorActivity.this,
                                    PreferencesData.PREFERENCE_FILE_PREFIX,
                                    prefixString);

                            createFileWriters();

                            return;
                        }
                    });

            alert.setNegativeButton(getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            return;
                        }
                    });
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    // button click
    public void start(View v) {

        createFileWriters();
        // start accelerator service
        if (accelerometerResultReceiver != null) {
            Intent accIntent = new Intent(SensorActivity.this, AccelerometerSensor.class);
            accIntent.putExtra(Constants.EXTRA_RECEIVER,
                    accelerometerResultReceiver);
            // idempotent call
            startService(accIntent);
        }

        // start orientation service
        if (orientationResultReceiver != null) {
            Intent orientationIntent = new Intent(SensorActivity.this, OrientationSensor.class);
            orientationIntent.putExtra(Constants.EXTRA_RECEIVER,orientationResultReceiver);
            // idempotent call
            startService(orientationIntent);
        }

        // start gyroscope service
        if (gyroscopeResultReceiver != null) {
            Intent gyroscopeIntent = new Intent(SensorActivity.this, GyroscopeSensor.class);
            gyroscopeIntent.putExtra(Constants.EXTRA_RECEIVER,
                    gyroscopeResultReceiver);
            // idempotent call
            startService(gyroscopeIntent);
        }
    }

    public void stop(View v) {
        if (accelerometerResultReceiver != null)
            stopService(new Intent(this, AccelerometerSensor.class));
        if (orientationResultReceiver != null)
            stopService(new Intent(this, OrientationSensor.class));
        if (gyroscopeResultReceiver != null)
            stopService(new Intent(this, GyroscopeSensor.class));

        stopService(new Intent(this, GPSSensor.class));
    }
    private class UpdateUI implements Runnable {

        private String mText;
        private TextView mTv;

        public UpdateUI(int textviewid, String text) {
            this.mTv = (TextView) findViewById(textviewid);
            this.mText = text;
        }

        @Override
        public void run() {
            mTv.setText(mText);
        }

    }

    private class AccelerometerReceiver implements
            AccelerometerResult.Receiver {

        private int resultTextViewID;
        private float x, y, z;

        public AccelerometerReceiver() {
            resultTextViewID = R.id.tv_accel_result;
        }

        private void sendLocationToUI() {
            double roundx = Math.round(x);
            double roundy = Math.round(y);
            double roundz = Math.round(z);
            runOnUiThread(new UpdateUI(resultTextViewID, "x: " + roundx
                    + ", y: " + roundy + ", z: " + roundz));
        }

        @Override
        public void newEvent(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            sendLocationToUI();
            accelerometerWriter.write(x, y, z);
        }

        @Override
        public void error(String error) {
            runOnUiThread(new UpdateUI(resultTextViewID, error));
        }

    }

    private class OrientationReceiver implements OrientationResult.Receiver {

        private int resultTextViewID;
        private float x, y, z;

        public OrientationReceiver() {
            resultTextViewID = R.id.tv_orientation_result;
        }

        private void sendLocationToUI() {
            double roundx = Math.round(x);
            double roundy = Math.round(y);
            double roundz = Math.round(z);
            runOnUiThread(new UpdateUI(resultTextViewID, "x: " + roundx
                    + ", y: " + roundy + ", z: " + roundz));
        }

        @Override
        public void newEvent(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            sendLocationToUI();
            orientationWriter.write(x, y, z);
        }

        @Override
        public void error(String error) {
            runOnUiThread(new UpdateUI(resultTextViewID, error));
        }

    }

    private class GyroscopeReceiver implements GyroscopeResult.Receiver {

        private int resultTextViewID;
        private float x, y, z;

        public GyroscopeReceiver() {

            resultTextViewID = R.id.tv_gyro_result;
        }

        private void sendLocationToUI() {
            double roundx = Math.round(x);
            double roundy = Math.round(y);
            double roundz = Math.round(z);
            runOnUiThread(new UpdateUI(resultTextViewID, "x: " + roundx
                    + ", y: " + roundy + ", z: " + roundz));
        }

        @Override
        public void newEvent(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            sendLocationToUI();
            gyroscopeWriter.write(x, y, z);
        }

        @Override
        public void error(String error) {
            runOnUiThread(new UpdateUI(resultTextViewID, error));
        }

    }

    private class GPSReceiver implements GPSResult.Receiver {

        private int resultTextViewID;

        boolean hasFirstFix;
        float accuracy;
        long time;
        float speed;

        double lat;
        double lng;

        double altitude;
        float bearing;

        public GPSReceiver() {
            resultTextViewID = R.id.tv_gps_result;
        }

        @Override
        public void firstFixChange(boolean hasFirstFix) {
            this.hasFirstFix = hasFirstFix;
            if (hasFirstFix) {
                sendLocationToUI();


            }
        }

        @Override
        public void newLocation(Location location) {
            this.accuracy = location.getAccuracy();
            this.time = location.getTime();
            this.speed = location.getSpeed();

            this.lat = location.getLatitude();
            this.lng = location.getLongitude();

            this.altitude = location.getAltitude();
            this.bearing = location.getBearing();

            sendLocationToUI();
            writeLocationToFile();

        }
        private void writeLocationToFile() {
            // gpsWriter.open("timestamp", "firstfix", "lat", "lng", "speed",
            // "altitude", "bearing", "accuracy", "time");
            gpsWriter.write(getStringValues());
        }

        private String[] getStringValues() {
            String[] values = new String[] { String.valueOf(hasFirstFix),
                    String.valueOf(lat), String.valueOf(lng),
                    String.valueOf(speed), String.valueOf(altitude),
                    String.valueOf(bearing), String.valueOf(accuracy),
                    String.valueOf(time) };

            return values;
        }

        private void sendLocationToUI() {
            runOnUiThread(new UpdateUI(resultTextViewID, "hasFirstFix: "
                    + hasFirstFix + "\n" + "time: " + time + "\n" + "speed: "
                    + speed + "\n" + "accuracy: " + accuracy + "\n"
                    + "latitude: " + lat + "\n" + "longitude: " + lng));
        }

        @Override
        public void error(String error) {
            runOnUiThread(new UpdateUI(resultTextViewID, error));
        }

    }
}
