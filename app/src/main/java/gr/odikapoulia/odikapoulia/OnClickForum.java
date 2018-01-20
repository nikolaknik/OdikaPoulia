package gr.odikapoulia.odikapoulia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;


/**
 * Created by nikolak_nik on 28/11/2017.
 */

public class OnClickForum extends Activity {



        Intent in;
        String forumName;
        SharedPreferences sharedforumpreferences;

        public static final String MyForumPREFERENCES = "MyForumPrefs";
        public static final String ForumName = "name";

    public void onClickForum (View v) {
        TextView nameId = (TextView) findViewById(R.id.name);
        forumName = nameId.getText().toString();
        SharedPreferences.Editor editor = sharedforumpreferences.edit();
        editor.putString(ForumName, forumName);
        editor.commit();

        in = new Intent(OnClickForum.this, PostsActivity.class);
        startActivity(in);

    }

}


