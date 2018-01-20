package gr.odikapoulia.odikapoulia;

        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;

        import android.widget.EditText;
        import android.widget.Toast;
        import android.content.Context;

        import android.content.Intent;
        import static gr.odikapoulia.odikapoulia.LoginActivity.MyPREFERENCES;
        import static gr.odikapoulia.odikapoulia.LoginActivity.Name;
        import static gr.odikapoulia.odikapoulia.LoginActivity.Password;

    public class ForumAddActivity extends AppCompatActivity {
    String user,pass;
    SharedPreferences sharedpreferences;
    private String TAG = MainActivity.class.getSimpleName();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_forum_add);

            final Button button = findViewById(R.id.button_insertNewTopic);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    new SentData().execute();

                    Intent intent = new Intent(ForumAddActivity.this, ForumActivity.class);
                    startActivity(intent);
                }
            });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        String username = sharedpreferences.getString(Name, user);



            String password = sharedpreferences.getString(Password, pass);

            System.out.println("username,pass"+username+password);

    }

    private class SentData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ForumAddActivity.this,"Επιτυχή Καταχώρηση",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            EditText newforumName = (EditText) findViewById(R.id.topic_add_title);
            String forumName = newforumName.getText().toString();
            //System.out.println("forumName"+forumName);
            EditText newforumDescr = (EditText) findViewById(R.id.topic_add_description);
            String forumDescr = newforumDescr.getText().toString();


            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://forum.odikapoulia.gr/json/allForumsAdd.php?forumName="+forumName+"&description="+forumDescr+"&username=gogo&password=123456!a&format=json";

           sh.makeServiceCall(url);//Φέρνει το Json όπως είναι

            //System.out.println("jsonStr"+jsonStr);

           // Log.e(TAG, "Response from url: " + jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }



}


