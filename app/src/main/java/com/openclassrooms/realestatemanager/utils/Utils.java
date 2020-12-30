package com.openclassrooms.realestatemanager.utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.openclassrooms.realestatemanager.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    public static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMS_CAMERA = Manifest.permission.CAMERA;
    public static final int RC_IMAGE_PERMS = 100;
    public static final int RC_CAMERA_PERMS = 101;
    public static final int RC_CHOOSE_PHOTO = 200;
    public static final int RC_CAMERA_RESULT = 201;

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){
        WifiManager wifi = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    /**
     * Display Notification
    **/
    public static void displayNotification(String title, String desc, Context context) {
        NotificationManager manager =
                (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("realEstateManager", "realEstateManager", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "realEstateManager")
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(desc))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.notif_icon);

        manager.notify(1, builder.build());
    }

    public static String randomUUID(){
        return UUID.randomUUID().toString();
    }
}
