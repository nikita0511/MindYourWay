package in.ac.iiitd.mindyourway.Sensors;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by NikitaGupta on 6/22/2015.
 */
public class AccelerometerResult extends ResultReceiver{

    public static final int RESULTCODE_ERROR = -1;
    public static final String EXTRA_ERRORMSG = "AccelerometerResultReceiver.ERRORMSG";

    public static final int RESULTCODE_UPDATE = 1;
    public static final String EXTRA_X = "AccelerometerResult.X";
    public static final String EXTRA_Y = "AccelerometerResult.Y";
    public static final String EXTRA_Z = "AccelerometerResult.Z";

    private Receiver mReceiver;

    public AccelerometerResult(Handler handler) {
        super(handler);
    }

    public interface Receiver {
        public void newEvent(float x, float y, float z);
        public void error(String error);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            if (resultCode == RESULTCODE_ERROR) {
                mReceiver.error(resultData.getString(EXTRA_ERRORMSG));
            } else {
                float x = resultData.getFloat(EXTRA_X);
                float y = resultData.getFloat(EXTRA_Y);
                float z = resultData.getFloat(EXTRA_Z);
                mReceiver.newEvent(x, y, z);
            }
        }
    }

}