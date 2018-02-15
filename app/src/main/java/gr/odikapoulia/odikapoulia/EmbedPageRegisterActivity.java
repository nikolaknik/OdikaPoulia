package gr.odikapoulia.odikapoulia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class EmbedPageRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embed_page_register);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://forum.odikapoulia.gr/wp-login.php?action=register");
    }
}
