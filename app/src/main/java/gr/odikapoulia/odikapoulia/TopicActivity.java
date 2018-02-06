
package gr.odikapoulia.odikapoulia;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;

public class TopicActivity extends AppCompatActivity {

    Intent in;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "userName";
    public static final String Password = "PassWord";
    public static final String MapStatus = "mapStatus";


    String user,pass,mapStatus;
    SharedPreferences sharedpreferences;

    private ListView lv;
    private  String TAG = TopicActivity.class.getSimpleName();

    ArrayList<HashMap<String, String>> TopicsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ekei poy einai h lista
        setContentView(R.layout.topic_list);

        final Button exit = findViewById(R.id.button_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getIntent().setAction("");
                startActivity(intent);
                finish();
                System.exit(0);

            }
        });


        final Button button = findViewById(R.id.topic_add1_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView topicId = (TextView)findViewById(R.id.topic_parent_id);
                String text_topicId = topicId.getText().toString();
                Intent intent = new Intent(TopicActivity.this, TopicAddActivity.class);
                intent.putExtra("selected-item", text_topicId);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String username = sharedpreferences.getString(Name, user);
                String password = sharedpreferences.getString(Password, pass);
                intent.putExtra("user", username);
                intent.putExtra("pass", password);
                startActivity(intent);

            }
        });

        final Button map_button = findViewById(R.id.mapsButton);
        map_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(TopicActivity.this, MapsActivity.class);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String username = sharedpreferences.getString(Name, user);
                String password = sharedpreferences.getString(Password, pass);
                String mapStatusString = sharedpreferences.getString(MapStatus, mapStatus);
                intent.putExtra("user", username);
                intent.putExtra("pass", password);
                intent.putExtra("MapStatus", mapStatusString);
                startActivity(intent);

            }
        });

        TopicsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list_topic);

        new GetData().execute();
    }
    private class GetData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(TopicActivity.this,"!!!",Toast.LENGTH_LONG).show();

        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            //από την wpauth ΄ερχεται το extra get the intent from which this activity is called.
            Intent intent = getIntent();

            // fetch value from key-value pair and make it visible on TextView.
            String text_forum_id = intent.getStringExtra("selected-item");

            //ta pernei apo to forumactivity.java

            String user = intent.getStringExtra("user");
            String pass = intent.getStringExtra("pass");

            System.out.println(text_forum_id);

            // Making a request to url and getting response
            String url = "http://forum.odikapoulia.gr/json/allTopics.php?username="+user+"&password="+pass+"&parent_id="+text_forum_id+"&format=json";

            //String url = "http://forum.odikapoulia.gr/json/allTopics.php?username=gogo&password=123456!a&parent_id=1&format=json";

            System.out.println("url topic "+url);


            String jsonStr = sh.makeServiceCall(url);//Φέρνει το Json όπως είναι

            System.out.println("jsonStr topic"+jsonStr);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray posts = jsonObj.getJSONArray("posts");

                    int sumTopics = 1;
                    String sumTopicsStr;


                    // looping through All posts

                    for (int i = 0; i < posts.length(); i++) {

                        //exei ena posts object
                        JSONObject c = posts.getJSONObject(i);

                        sumTopics = 1 + i;
                        sumTopicsStr = ""+sumTopics;
                        // post node is JSON Object kai meta array
                        JSONObject json_post = c.getJSONObject("post");
                        String id = json_post.getString("id");
                        String views = json_post.getString("views");
                        String parent_id = json_post.getString("parent_id");
                        String name = json_post.getString("name");
                        String status = json_post.getString("status");

                        // tmp hash map for single contact
                        HashMap<String, String> post = new HashMap<>();

                        // adding each child node to HashMap key => value
                        post.put("id", id);
                        post.put("views", views);
                        post.put("parent_id", parent_id);
                        post.put("name", name);
                        post.put("status", status);
                        post.put("sumTopicsStr", sumTopicsStr);

                        // adding contact to contact list
                        TopicsList.add(post);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ListAdapter adapter = new SimpleAdapter(TopicActivity.this, TopicsList, R.layout.topic_texts,new String[]{"id","sumTopicsStr","name","views"},new int[]{R.id.topic_parent_id, R.id.topic_id,R.id.topic_name,R.id.topic_views});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new TopicActivity.GetData.ListClickHandler());


        }

        public class ListClickHandler implements AdapterView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView<?> Adapter, View view, int position, long arg3) {
                // TODO Auto-generated method stub
                TextView topicId = (TextView) view.findViewById(R.id.topic_parent_id);
                String text_topicId = topicId.getText().toString();
                Intent intent = new Intent(TopicActivity.this, PostsActivity.class);
                intent.putExtra("selected-item", text_topicId);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String username = sharedpreferences.getString(Name, user);
                String password = sharedpreferences.getString(Password, pass);
                intent.putExtra("user", username);
                intent.putExtra("pass", password);
                startActivity(intent);

            }

            public void onItemMapClick(AdapterView<?> Adapter, View view, int position, long arg3) {
                // TODO Auto-generated method stub
                TextView topicId = (TextView) view.findViewById(R.id.topic_parent_id);
                String text_topicId = topicId.getText().toString();
                Intent intent = new Intent(TopicActivity.this, PostsActivity.class);
                intent.putExtra("selected-item", text_topicId);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String username = sharedpreferences.getString(Name, user);
                String password = sharedpreferences.getString(Password, pass);
                intent.putExtra("user", username);
                intent.putExtra("pass", password);
                startActivity(intent);

            }

        }

    }



}


