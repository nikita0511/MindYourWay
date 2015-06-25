package in.ac.iiitd.mindyourway.Sensors;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by NikitaGupta on 6/22/2015.
 */
public class GPSResult extends ResultReceiver{

    public static final int RESULTCODE_ERROR = -1;
    public static final String EXTRA_ERRORMSG = "GPSResultReceiver.ERRORMSG";

    public static final int RESULTCODE_LOCATION_UPDATE = 1;
    public static final String EXTRA_LOCATION = "GPSResultReceiver.LOCATION";

    public static final int RESULTCODE_FIRST_FIX_CHANGE = 2;
    public static final String EXTRA_HASFIRSTFIX = "GPSResultReceiver.HASFIRSTFIX";

    private Receiver mReceiver;

    public GPSResult(Handler handler) {
        super(handler);
    }

    public interface Receiver {
        public void firstFixChange(boolean hasFirstFix);
        public void newLocation(Location location);
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
            } else if (resultCode == RESULTCODE_FIRST_FIX_CHANGE) {
                mReceiver.firstFixChange(resultData.getBoolean(EXTRA_HASFIRSTFIX));
            } else {
                mReceiver.newLocation((Location)resultData.getParcelable(EXTRA_LOCATION));
            }
        }
    }
}
