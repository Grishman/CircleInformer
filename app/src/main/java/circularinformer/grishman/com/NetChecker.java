package circularinformer.grishman.com;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Class for checking network
 */
public class NetChecker {

    /**
     * @param context
     * @return is device connect to the internet
     */
    public static boolean isOnline(Context context) {
        final NetworkInfo networkInfo = getNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * @param context
     * @return is wifi available
     */
    public static boolean isWifiAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info == null || info.getType() != ConnectivityManager.TYPE_WIFI) {
            return false;
        }
        return info.isConnectedOrConnecting();
    }

    /**
     * @param context
     * @return is WiMAX available
     */
    public static boolean isWifiOrWimaxConnected(Context context) {
        ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wimaxInfo = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        NetworkInfo wifiInfo = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (wimaxInfo != null && wimaxInfo.isConnected()) || (wifiInfo != null && wifiInfo.isConnected());
    }

    /**
     * @param context
     * @return is network connect
     */
    public static boolean is3gOr2gConnected(Context context) {
        ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && (networkInfo.isConnectedOrConnecting() || networkInfo.getState() == NetworkInfo.State.CONNECTED || networkInfo.getState() == NetworkInfo.State.CONNECTING);
    }

    /**
     * @param context
     * @return information about network
     */
    private static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    /**
     * @param context
     * @return is device connecting to wifi
     */
    public static boolean isWifiHeating(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = null;
        final NetworkInfo[] networks = cm.getAllNetworkInfo();
        for (NetworkInfo network : networks) {
            if (ConnectivityManager.TYPE_WIFI == network.getType()) {
                wifi = network;
                break;
            }
        }
        return wifi != null && wifi.getState() == NetworkInfo.State.CONNECTING;
    }
}
