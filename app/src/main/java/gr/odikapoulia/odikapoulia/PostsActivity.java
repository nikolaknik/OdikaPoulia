package gr.odikapoulia.odikapoulia;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PostsActivity extends AppCompatActivity {


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "userName";
    public static final String Password = "PassWord";

    String user,pass;
    public String topic_id,text_forum_id;
    public MediaPlayer mediaPlayer;
    private Context mContext;
    private Activity mActivity;

    private PopupWindow mPopupWindow;
    SharedPreferences sharedpreferences;

    private ListView lv;
    private  String TAG = PostsActivity.class.getSimpleName();

    ArrayList<HashMap<String, String>> PostsList;

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), TopicActivity.class);
        mediaPlayer.stop();
        intent.putExtra("selected-item", text_forum_id);
        //intent.putExtra("selected-item", topic_id);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString(Name, user);
        String password = sharedpreferences.getString(Password, pass);
        intent.putExtra("user", username);
        intent.putExtra("pass", password);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list);

        Intent intent = getIntent();
        topic_id = intent.getStringExtra("selected-item");
        text_forum_id = intent.getStringExtra("text_forum_id");
        System.out.println("topic_id"+topic_id);


        final Button button = findViewById(R.id.post_add);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PostsActivity.this, PostAddActivity.class);
                intent.putExtra("selected-item", topic_id);
                intent.putExtra("text_forum_id", text_forum_id);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String username = sharedpreferences.getString(Name, user);
                String password = sharedpreferences.getString(Password, pass);
                intent.putExtra("user", username);
                intent.putExtra("pass", password);
                startActivity(intent);

            }
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.nomusic);

        final Button exit = findViewById(R.id.button_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get the application context
                mContext = getApplicationContext();

                // Get the activity
                mActivity = PostsActivity.this;
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.popup_exit,null);

                // Initialize a new instance of popup window
                boolean focusable = true;
                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        focusable
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                mediaPlayer.start();

                mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);

                Button buttonExit = (Button) customView.findViewById(R.id.exit);
                buttonExit.setOnClickListener(new View.OnClickListener() {
                    @Override
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
            }
        });


        PostsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list_post);

        new GetpostData().execute();
    }
    private class GetpostData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(PostsActivity.this,"Φόρτωση Συζητήσεων.",Toast.LENGTH_SHORT).show();

        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            //από την wpauth ΄ερχεται το extra get the intent from which this activity is called.
            Intent intent = getIntent();

            System.out.println("topic_id "+topic_id);

            //ta pernei apo to forumactivity.java

            String user = intent.getStringExtra("user");
            String pass = intent.getStringExtra("pass");



            // Making a request to url and getting response
            String url = "http://forum.odikapoulia.gr/json/allPosts.php?username="+user+"&password="+pass+"&parent_id="+topic_id+"&format=json";
            System.out.println("url allpost "+url);


            String jsonStr = sh.makeServiceCall(url);//Φέρνει το Json όπως είναι

            System.out.println("jsonStr"+jsonStr);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray posts = jsonObj.getJSONArray("posts");

                    int sumPosts = 1;
                    String sumPostsStr;


                    // looping through All posts

                    for (int i = 0; i < posts.length(); i++) {

                        //exei ena posts object
                        JSONObject c = posts.getJSONObject(i);

                        sumPosts = 1 + i;

                        sumPostsStr = ""+sumPosts;


                        // post node is JSON Object kai meta array
                        JSONObject json_post = c.getJSONObject("post");
                        String id = json_post.getString("id");
                        String text = json_post.getString("text");
                        String parent_ids = json_post.getString("parent_id");
                        String date = json_post.getString("date");
                        String author_id = json_post.getString("author_id");
                        String uploads = json_post.getString("uploads");
                        //String text = Html.fromHtml(text1);

                        // tmp hash map for single contact
                        HashMap<String, String> post = new HashMap<>();

                        // adding each child node to HashMap key => value
                        post.put("id", id);
                        post.put("text", text);
                        post.put("parent_ids", parent_ids);
                        post.put("date", date);
                        post.put("sumPostsStr", sumPostsStr);

                        // adding contact to contact list
                        PostsList.add(post);
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
            ListAdapter adapter = new SimpleAdapter(PostsActivity.this, PostsList, R.layout.post_texts,new String[]{"sumPostsStr","text"},new int[]{R.id.post_id,R.id.post_name });

            lv.setAdapter(adapter);

        }



    }



}


