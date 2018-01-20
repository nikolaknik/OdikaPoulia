package gr.odikapoulia.odikapoulia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button b2, b3;
    String user, pass ,mapStatus;
    Intent in;
    String internetCheck;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "userName";
    public static final String Password = "PassWord";
    public static final String MapStatus = "mapStatus";
    private String TAG = MainActivity.class.getSimpleName();
    int counter = 3;
    String valid;

    SharedPreferences sharedpreferences;


    private class GetValid extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        //@Override
        protected String doInBackground(Void... arg0) {
        //protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();

            username = (EditText) findViewById(R.id.username);
            user = username.getText().toString();
            password = (EditText) findViewById(R.id.password);
            pass = password.getText().toString();

            user = "gogo";
            pass = "123456!a";

            // Making a request to url and getting response
           String url = "https://forum.odikapoulia.gr/json/authValid.php?username="+user+"&password="+pass;

           //String url = "https://forum.odikapoulia.gr/json/authValid.php?username=gogo&password=123456!a";

            System.out.println("url "+url);

            String jsonStr = sh.makeServiceCall(url);//Φέρνει το Json όπως είναι
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject posts = new JSONObject(jsonStr);

                    valid = posts.getString("posts");
                    System.out.println("valid0 "+valid);
                    Log.e(TAG, "Response from url: " + valid);

                } catch( final JSONException e){
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }

            } else

            {
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

            return valid;
           // return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("result "+result);
            if("ok".equals(result)){

                Toast.makeText(getApplicationContext(),
                        "Συνδεθήκατε Επιτυχώς",Toast.LENGTH_SHORT).show();
                mapStatus = "ok";
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(MapStatus, mapStatus);
                editor.putString(Name, user);
                editor.putString(Password, pass);
                editor.commit();
               // Toast.makeText(LoginActivity.this,"...",Toast.LENGTH_LONG).show();

                in = new Intent(LoginActivity.this,ForumActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();

            }else if(user==null||pass==null){

                Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια",Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια",Toast.LENGTH_SHORT).show();
                counter --;

                if (counter == 0) {
                    Toast.makeText(getApplicationContext(), "Προσπαθήστε Αργότερα",Toast.LENGTH_SHORT).show();
                    b2.setEnabled(false);
                }


            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b2 = (Button)findViewById(R.id.button_login);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        b2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            InternetCheck myIncheck = new InternetCheck();
            if(myIncheck.isNetworkAvailable(getApplicationContext())){

                internetCheck = "internetOk";

                System.out.println(internetCheck);

                Toast.makeText(getApplicationContext(), "Γίνεται επεξεργασία... Παρακαλώ περιμένετε",Toast.LENGTH_LONG).show();

                GetValid takeValid  = new GetValid();
                takeValid.execute();

            }else{

                internetCheck = "internetNotOk";

                System.out.println(internetCheck);
                Toast.makeText(getApplicationContext(), "Δεν υπάρχει σύνδεση στο Internet,παρακαλώ δοκιμάστε αργότερα",Toast.LENGTH_SHORT).show();

            }


        }
        });
    }


}
