package circularinformer.grishman.com.services;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import circularinformer.grishman.com.R;
import circularinformer.grishman.com.utils.NetChecker;

/**
 * Service to update widget
 */
public class UpdateWidgetService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.layout_circle_widget);
            updateUI(remoteViews);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateUI(RemoteViews remoteViews) {
        if (!NetChecker.isOnline(getApplicationContext())) {
            remoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_empty_circle);
            remoteViews.setTextViewText(R.id.connectivity_type_text, getString(R.string.msg_no_internet));
        }
        if (!NetChecker.isOnline(getApplicationContext()) && NetChecker.isAirplaneModeOn(getApplicationContext())) {
            remoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle);
            remoteViews.setTextViewText(R.id.connectivity_type_text, getString(R.string.msg_no_internet));
        }
        if (NetChecker.is3gOr2gConnected(getApplicationContext())) {
            remoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle_yellow);
            remoteViews.setTextViewText(R.id.connectivity_type_text, getString(R.string.msg_mobile));
        }
        if (NetChecker.isWifiAvailable(getApplicationContext())) {
            new DownloadWebpageTask().execute("http://www.google.com");
            //TODO check security type
            if (NetChecker.getSecurityTypeWifi(getApplicationContext()) == NetChecker.SECURITY_EAP) {
                remoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle_green);
                remoteViews.setTextViewText(R.id.connectivity_type_text, getString(R.string.msg_encrypted_non_psk));
            } else {
                remoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle_red);
                remoteViews.setTextViewText(R.id.connectivity_type_text, getString(R.string.msg_open_psk));
            }
        }
    }
    /**
     * Read connection timeout
     */
    private static final int READ_TIMEOUT = 100000;

    /**
     * Common connecton timeout
     */
    private static final int CONNECTION_TIMEOUT = 150000;
    private  class DownloadWebpageTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                boolean isok = false;
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();
                urlConnection.setReadTimeout(READ_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                //urlConnection.setRequestMethod(httpMethod.getName());
                // urlConnection.setDoInput(true);
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    isok=true;
                }
                return isok;
            } catch (IOException e) {
                //return "Unable to retrieve web page. URL may be invalid.";
                return false;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Boolean result) {
            boolean result2=result;
            //textView.setText(result);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
