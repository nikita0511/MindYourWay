package in.ac.iiitd.mindyourway.MainClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import in.ac.iiitd.mindyourway.R;

/**
 * Created by NikitaGupta on 6/24/2015.
 */
//reference: http://lakjeewa.blogspot.in/2014/05/simple-android-client-server-application.html

public class ServerActivity extends Activity{

    private Socket client;
    private PrintWriter printwriter;
    private EditText textField;
    private Button button;
    private Button backbutton;
    private String messsage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_main);
        final Context context = this;

        textField = (EditText) findViewById(R.id.editText1); // reference to the text field
        button = (Button) findViewById(R.id.button1); // reference to the send button
        backbutton = (Button) findViewById(R.id.button10);
        // Button press event listener
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                messsage = textField.getText().toString(); // get the text message on the text field
                textField.setText(""); // Reset the text field to blank
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.execute();
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private class SendMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {

                client = new Socket("192.168.1.101", 2000); // connect to the server
                printwriter = new PrintWriter(client.getOutputStream(), true);
                printwriter.write(messsage); // write the message to output stream

                printwriter.flush();
                printwriter.close();
                client.close(); // closing the connection

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
