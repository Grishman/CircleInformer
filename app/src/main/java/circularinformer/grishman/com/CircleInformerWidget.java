package circularinformer.grishman.com;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Widget to inform user about network state
 */
public class CircleInformerWidget extends AppWidgetProvider {

    private BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_TIME_TICK.equals(action)) {
            }
        }
    };

    public CircleInformerWidget() {
        super();
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
//        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
//        filters.addAction("android.net.wifi.STATE_CHANGE");
//        context.registerReceiver(mConnectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
//        context.unregisterReceiver(mConnectionReceiver);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
//        context.unregisterReceiver(mConnectionReceiver);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        //for (int id : appWidgetIds) {
            // Build the intent to call the service
            Intent intent = new Intent(context.getApplicationContext(),
                    UpdateWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            // Update the widgets via the service
            context.startService(intent);
            //updateWidget(context, appWidgetManager, id);
        //}
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {
        RemoteViews widgetView = new RemoteViews(context.getPackageName(),
                R.layout.layout_circle_widget);
        //test
        widgetView.setTextViewText(R.id.text_test, "test");

        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }
}
