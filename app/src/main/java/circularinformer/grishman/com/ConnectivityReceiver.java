package circularinformer.grishman.com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Broadcast Receiver to receive updates of the network state
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo info = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
        info.getDetailedState();
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
//        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), CircleInformerWidget.class);
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
//        RemoteViews remoteViews = new RemoteViews(context.getApplicationContext().getPackageName(),
//                R.layout.layout_circle_widget);
//        if (appWidgetIds != null && appWidgetIds.length > 0) {
////            thisWidget.onUpdate(context, appWidgetManager, appWidgetIds);
//            appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);
//        }
    }
}
