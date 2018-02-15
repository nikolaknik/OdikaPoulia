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

public class PostAddActivity extends AppCompatActivity {
    String user,pass;
    public String parent_id2,text_forum_id;
    SharedPreferences sharedpreferences;

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(PostAddActivity.this, PostsActivity.class);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString(Name, user);
        String password = sharedpreferences.getString(Password, pass);
        intent.putExtra("user", username);
        intent.putExtra("pass", password);
        Intent intent2 = getIntent();
        parent_id2 = intent2.getStringExtra("selected-item");
        System.out.println("parent_id button"+parent_id2);
        intent.putExtra("text_forum_id", text_forum_id);
        intent.putExtra("selected-item", parent_id2);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_add);
        Intent intent = getIntent();
        text_forum_id = intent.getStringExtra("text_forum_id");
        System.out.println("text_forum_id add post "+text_forum_id);

        final Button button = findViewById(R.id.button_insertNewPost);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new SentData().execute();

                Intent intent = new Intent(PostAddActivity.this, PostsActivity.class);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String username = sharedpreferences.getString(Name, user);
                String password = sharedpreferences.getString(Password, pass);
                intent.putExtra("user", username);
                intent.putExtra("pass", password);
                Intent intent2 = getIntent();
                parent_id2 = intent2.getStringExtra("selected-item");
                System.out.println("parent_id button"+parent_id2);

                intent.putExtra("text_forum_id", text_forum_id);

                intent.putExtra("selected-item", parent_id2);
                startActivity(intent);
            }
        });

    }

    private class SentData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(PostAddActivity.this,"Επιτυχή Καταχώρηση",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            EditText postName = (EditText) findViewById(R.id.post_add_comment);
            String newpostName = postName.getText().toString();

            HttpHandler sh = new HttpHandler();

            Intent intent = getIntent();

            parent_id2 = intent.getStringExtra("selected-item");
            System.out.println("parent_id mesa"+parent_id2);


            //ta pernei apo to forumactivity.java

            String user = intent.getStringExtra("user");
            String pass = intent.getStringExtra("pass");

            // Making a request to url and getting response
            //String url = "" ;

           String url = "http://forum.odikapoulia.gr/json/allPostsAdd.php?postText=" + newpostName + "&parent_id=" + parent_id2 + "&username=" + user + "&password=" + pass + "&format=json";

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


