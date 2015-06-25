package in.ac.iiitd.mindyourway.Server;

/**
 * Created by NikitaGupta on 6/21/2015.
 */

import android.app.Activity;

public class ServerConnection extends Activity {
/*
    private Socket client;
    private PrintWriter printwriter;
    private EditText textField;
    private Button button;
    private String messsage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textField = (EditText) findViewById(R.id.editText1); // reference to the text field
        button = (Button) findViewById(R.id.button1); // reference to the send button

        // Button press event listener
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                messsage = textField.getText().toString(); // get the text message on the text field
                textField.setText(""); // Reset the text field to blank
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.execute();
            }
        });
    }
    private class SendMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {

                client = new Socket("10.0.2.2", 4444); // connect to the server
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
    }*/
}
