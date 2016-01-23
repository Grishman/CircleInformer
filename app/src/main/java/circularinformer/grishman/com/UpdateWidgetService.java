package circularinformer.grishman.com;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

/**
 * Service to update widget
 */
public class UpdateWidgetService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        ComponentName thisWidget = new ComponentName(getApplicationContext(),
                CircleInformerWidget.class);

        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.layout_circle_widget);

//            EnableDisableConnectivity edConn = new EnableDisableConnectivity(this.getApplicationContext());
//            edConn.enableDisableDataPacketConnection(!checkConnectivityState(this.getApplicationContext()));

            // Register an onClickListener
            Intent clickIntent = new Intent(this.getApplicationContext(),
                    CircleInformerWidget.class);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    allWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setTextViewText(R.id.text_test,"lolkkaksk");
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
