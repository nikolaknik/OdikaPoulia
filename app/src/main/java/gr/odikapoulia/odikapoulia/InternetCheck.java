package gr.odikapoulia.odikapoulia;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by nikolak_nik on 28/12/2017.
 */

public class InternetCheck {

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }

}
