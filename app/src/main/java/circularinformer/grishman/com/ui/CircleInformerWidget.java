package circularinformer.grishman.com.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import circularinformer.grishman.com.R;
import circularinformer.grishman.com.utils.NetChecker;

/**
 * Widget to inform user about network state
 */
public class CircleInformerWidget extends AppWidgetProvider {

    public CircleInformerWidget() {
        super();
    }

    RemoteViews remoteViews;
    int[] allWidgetIds = null;
    AppWidgetManager mAppWidgetManager;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), CircleInformerWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // Build the intent to call the service
//        Intent intent = new Intent(context.getApplicationContext(),
//                UpdateWidgetService.class);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//        // Update the widgets via the service
//        context.startService(intent);
        mAppWidgetManager = appWidgetManager;

        allWidgetIds = appWidgetIds;
        remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.layout_circle_widget);
        if (!NetChecker.isOnline(context)) {
            remoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_empty_circle);
            remoteViews.setTextViewText(R.id.connectivity_type_text, context.getResources().getString(R.string.msg_no_internet));
        }
        if (!NetChecker.isOnline(context) && NetChecker.isAirplaneModeOn(context)) {
            remoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle);
            remoteViews.setTextViewText(R.id.connectivity_type_text, context.getResources().getString(R.string.msg_no_internet));
        }
        if (NetChecker.is3gOr2gConnected(context)) {
            remoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle_yellow);
            remoteViews.setTextViewText(R.id.connectivity_type_text, context.getResources().getString(R.string.msg_mobile));
        }
        if (NetChecker.isWifiAvailable(context)) {
            new DownloadWebpageTask().execute("http://www.google.com");
            //TODO check security type
            if (NetChecker.getSecurityTypeWifi(context) == NetChecker.SECURITY_EAP) {
                remoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle_green);
                remoteViews.setTextViewText(R.id.connectivity_type_text, context.getResources().getString(R.string.msg_encrypted_non_psk));
            } else {
                remoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle_red);
                remoteViews.setTextViewText(R.id.connectivity_type_text, context.getResources().getString(R.string.msg_open_psk));
            }
        }
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    /**
     * Read connection timeout
     */
    private static final int READ_TIMEOUT = 100000;

    /**
     * Common connecton timeout
     */
    private static final int CONNECTION_TIMEOUT = 150000;

    private class DownloadWebpageTask extends AsyncTask<String, Void, Boolean> {
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
                    isok = true;
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
            boolean result2 = result;
            remoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_empty_circle);
            mAppWidgetManager.updateAppWidget(allWidgetIds, remoteViews);
            //textView.setText(result);
        }
    }

}
