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

public class TopicAddActivity extends AppCompatActivity {
    String user,pass;
    public String parent_id;
    SharedPreferences sharedpreferences;
    //private String TAG = MainActivity.class.getSimpleName();

    public void onBackPressed() {

        Intent intent = new Intent(TopicAddActivity.this, TopicActivity.class);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString(Name, user);
        String password = sharedpreferences.getString(Password, pass);
        intent.putExtra("user", username);
        intent.putExtra("pass", password);
        intent.putExtra("selected-item", parent_id);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_add);
        Intent intent = getIntent();

        // fetch value from key-value pair and make it visible on TextView.
        parent_id = intent.getStringExtra("return_forum_id");
        final Button button = findViewById(R.id.button_insertNewTopic);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new SentData().execute();

                Intent intent = new Intent(TopicAddActivity.this, TopicActivity.class);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String username = sharedpreferences.getString(Name, user);
                String password = sharedpreferences.getString(Password, pass);
                intent.putExtra("user", username);
                intent.putExtra("pass", password);
                intent.putExtra("selected-item", parent_id);
                startActivity(intent);
            }
        });

    }

    private class SentData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(TopicAddActivity.this,"Επιτυχή Καταχώρηση",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            EditText topicName = (EditText) findViewById(R.id.topic_add_title);
            String newtopicName = topicName.getText().toString();

            HttpHandler sh = new HttpHandler();

            //από την wpauth ΄ερχεται το extra get the intent from which this activity is called.
                Intent intent = getIntent();

                // fetch value from key-value pair and make it visible on TextView.
                String parent_id = intent.getStringExtra("return_forum_id");

                //ta pernei apo to forumactivity.java

                String user = intent.getStringExtra("user");
                String pass = intent.getStringExtra("pass");

                // Making a request to url and getting response
                String url = "http://forum.odikapoulia.gr/json/allTopicsAdd.php?topicName=" + newtopicName + "&parent_id=" + parent_id + "&username=" + user + "&password=" + pass + "&format=json";

                System.out.println(url);

                sh.makeServiceCall(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }



}


