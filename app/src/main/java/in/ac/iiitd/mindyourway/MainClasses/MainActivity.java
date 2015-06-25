package in.ac.iiitd.mindyourway.MainClasses;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import in.ac.iiitd.mindyourway.R;

public class MainActivity extends Activity {

    Button sensorButton;
    Button cameraButton;
    Button serverButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        final Context context = this;
        sensorButton = (Button) findViewById(R.id.button3);
        cameraButton = (Button) findViewById(R.id.button4);
        serverButton = (Button) findViewById(R.id.button5);

        sensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, SensorActivity.class);
                startActivity(intent);
            }

        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, CameraActivity.class);
                startActivity(intent);
            }

        });
        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, ServerActivity.class);
                startActivity(intent);
            }

        });

    }
}
