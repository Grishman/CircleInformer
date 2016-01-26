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


    /**
     * Read connection timeout
     */
    private static final int READ_TIMEOUT = 100000;

    /**
     * Common connecton timeout
     */
    private static final int CONNECTION_TIMEOUT = 150000;

    private boolean isWifiEncrypted;
    private boolean isMobileConnected;
    private RemoteViews mRemoteViews;
    private int[] mWidgetIds = null;
    private AppWidgetManager mAppWidgetManager;

    public CircleInformerWidget() {
        super();
    }


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
        mAppWidgetManager = appWidgetManager;
        mWidgetIds = appWidgetIds;
        mRemoteViews = new RemoteViews(context.getPackageName(),
                R.layout.layout_circle_widget);
        if (!NetChecker.isOnline(context)) {
            mRemoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_empty_circle);
            mRemoteViews.setTextViewText(R.id.connectivity_type_text, context.getResources().getString(R.string.msg_no_internet));
        }
        if (!NetChecker.isOnline(context) && NetChecker.isAirplaneModeOn(context)) {
            mRemoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle);
            mRemoteViews.setTextViewText(R.id.connectivity_type_text, context.getString(R.string.msg_no_internet));
        }
        if (NetChecker.is3gOr2gConnected(context)) {
            new DownloadWebpageTask().execute(context.getString(R.string.chech_google_condition));
            isMobileConnected = true;
            mRemoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_empty_circle_yellow);
            mRemoteViews.setTextViewText(R.id.connectivity_type_text, context.getString(R.string.msg_mobile));
        }
        if (NetChecker.isWifiAvailable(context)) {
            new DownloadWebpageTask().execute(context.getString(R.string.chech_google_condition));
            if (NetChecker.getSecurityTypeWifi(context) == NetChecker.SECURITY_EAP) {
                isWifiEncrypted = true;
                mRemoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_empty_circle_green);
                mRemoteViews.setTextViewText(R.id.connectivity_type_text, context.getString(R.string.msg_encrypted_non_psk));
            } else {
                isWifiEncrypted = false;
                mRemoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_empty_circle_red);
                mRemoteViews.setTextViewText(R.id.connectivity_type_text, context.getString(R.string.msg_open_psk));
            }
        }
        appWidgetManager.updateAppWidget(appWidgetIds, mRemoteViews);
    }

    // Dummy async task to check that user can open google.com
    private class DownloadWebpageTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {
            boolean isOk = false;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();
                urlConnection.setReadTimeout(READ_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    isOk = true;
                }
                return isOk;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                if (isMobileConnected) {
                    mRemoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle_yellow);
                }
                if (isWifiEncrypted) {
                    mRemoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle_green);
                } else {
                    mRemoteViews.setImageViewResource(R.id.image_indicator, R.drawable.shape_filled_circle_red);
                }
                mAppWidgetManager.updateAppWidget(mWidgetIds, mRemoteViews);
            }
        }
    }

}
