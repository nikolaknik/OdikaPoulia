package gr.odikapoulia.odikapoulia;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;

import android.content.Intent;

import android.widget.AdapterView.OnItemClickListener;
import static gr.odikapoulia.odikapoulia.LoginActivity.MyPREFERENCES;
import static gr.odikapoulia.odikapoulia.LoginActivity.Name;
import static gr.odikapoulia.odikapoulia.LoginActivity.Password;

public class ForumActivity extends AppCompatActivity  {


    Intent in;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String MapStatus = "mapStatus";
    public static final String Name = "userName";

    public static final String Password = "PassWord";

    String user,pass,mapStatus;
    SharedPreferences sharedpreferences;
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    public MediaPlayer mediaPlayer;
    private Context mContext;
    private Activity mActivity;

    private PopupWindow mPopupWindow;

    ArrayList<HashMap<String, String>> DataList;

    @Override
    public void onBackPressed() {

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = ForumActivity.this;
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

        mediaPlayer = MediaPlayer.create(this, R.raw.nomusic);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_list);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString(Name, user);
        String password = sharedpreferences.getString(Password, pass);


        try{
            if(!"nikolak.nik".equals(username))
            {

            final Button button = findViewById(R.id.forum_add1_button);
            button.setVisibility(View.GONE);

        }else{

        final Button button = findViewById(R.id.forum_add1_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ForumActivity.this, ForumAddActivity.class);
                startActivity(intent);

            }
        });
        }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.nomusic);

        final Button exit = findViewById(R.id.button_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Get the application context
                mContext = getApplicationContext();

                // Get the activity
                mActivity = ForumActivity.this;
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

        DataList = new ArrayList<>();
            lv = (ListView) findViewById(R.id.list_post);

            new GetData().execute();
        }

        private class GetData extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(ForumActivity.this,"Γίνεται Επεξεργασία.",Toast.LENGTH_SHORT).show();

            }

            @Override
            protected Void doInBackground(Void... arg0) {

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String username = sharedpreferences.getString(Name, user);
                String password = sharedpreferences.getString(Password, pass);

                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String url = "http://forum.odikapoulia.gr/json/allForums.php?username="+username+"&password="+password+"&format=json";

                String jsonStr = sh.makeServiceCall(url);//Φέρνει το Json όπως είναι

                System.out.println("jsonStr"+jsonStr);

                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray posts = jsonObj.getJSONArray("posts");

                        // looping through All posts

                        int sumForum = 1;
                        String sumForumStr;

                        for (int i = 0; i < posts.length(); i++) {

                            //exei ena posts object
                            JSONObject c = posts.getJSONObject(i);
                            // post node is JSON Object kai meta array
                            JSONObject json_post = c.getJSONObject("post");
                            sumForum = 1 + i;
                            sumForumStr = ""+sumForum;
                            String id = json_post.getString("id");
                            String name = json_post.getString("name");
                            String parent_id = json_post.getString("parent_id");
                            String description = json_post.getString("description");
                            String icon = json_post.getString("icon");
                            String topic_count = json_post.getString("topic_count");
                            //String slug = json_post.getString("slug");

                            // tmp hash map for single contact
                            HashMap<String, String> post = new HashMap<>();

                            // adding each child node to HashMap key => value

                            post.put("id", id);
                            post.put("name", name);
                            post.put("parent_id", parent_id);
                            post.put("description", description);
                            post.put("topic_count", topic_count);
                            post.put("sumForumStr", sumForumStr);
                           // post.put("slug", slug);

                            // adding contact to contact list
                            DataList.add(post);
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
                ListAdapter adapter = new SimpleAdapter(ForumActivity.this, DataList, R.layout.forum_texts,new String[]{"sumForumStr","name","topic_count"},new int[]{R.id.forum_id,R.id.forum_name,R.id.forum_topics});
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new ListClickHandler());


                    }

            public class ListClickHandler implements OnItemClickListener{

                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                    // TODO Auto-generated method stub
                    TextView forum_id = (TextView) view.findViewById(R.id.forum_id);
                    String text_forum_id = forum_id.getText().toString();
                    Intent intent = new Intent(ForumActivity.this, TopicActivity.class);
                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    String mapStatusString = sharedpreferences.getString(MapStatus, mapStatus);
                    String username = sharedpreferences.getString(Name, user);
                    String password = sharedpreferences.getString(Password, pass);
                    intent.putExtra("MapStatus", mapStatusString);
                    intent.putExtra("user", username);
                    intent.putExtra("pass", password);
                    intent.putExtra("selected-item", text_forum_id);

                    startActivity(intent);

                }

            }

            }



        }


