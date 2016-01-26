package circularinformer.grishman.com.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class for checking network
 */
public class NetChecker {

    public static final int SECURITY_NONE = 0;
    public static final int SECURITY_WEP = 1;
    public static final int SECURITY_PSK = 2;
    public static final int SECURITY_EAP = 3;

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

    public static int getSecurityTypeWifi(Context context) {
        int securityType = SECURITY_NONE;
        WifiManager mWiFiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWiFiManager.getConfiguredNetworks() != null) {
            WifiConfiguration activeConfig = null;
            for (WifiConfiguration conn : mWiFiManager.getConfiguredNetworks()) {
                if (conn.status == WifiConfiguration.Status.CURRENT) {
                    activeConfig = conn;
                    break;
                }
            }
            if (activeConfig != null) {
                securityType = getSecurity(activeConfig);
            }
        }
        return securityType;
    }

    private static int getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) ||
                config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
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
     * @return boolean is Airplane Mode enabled
     **/
    public static boolean isAirplaneModeOn(Context context) {
        return Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    /**
     * Read connection timeout
     */
    private static final int READ_TIMEOUT = 100000;

    /**
     * Common connecton timeout
     */
    private static final int CONNECTION_TIMEOUT = 150000;

    public static void getGoogle() throws IOException {
        new DownloadWebpageTask().execute("http://www.google.com");
    }

    private static class DownloadWebpageTask extends AsyncTask<String, Void, Boolean> {
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

}

