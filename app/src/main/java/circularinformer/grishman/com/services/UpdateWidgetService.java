package circularinformer.grishman.com.services;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import circularinformer.grishman.com.utils.NetChecker;
import circularinformer.grishman.com.R;

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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
