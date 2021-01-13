package com.openclassrooms.realestatemanager.utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    public static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMS_CAMERA = Manifest.permission.CAMERA;
    public static final String PROPERTY_ID = "propertyId";
    public static final String MY_PROPERTY = "MyProperty";
    public static final String NULL_STRING = "null";
    public static final String MAX_SURFACE = "MaxSurface";
    public static final String CURRENCY = "currency";
    public static final String SCOPE = "scope";
    public static final String SHARED_PREFERENCE = "MySharedPref";
    public static final int RC_IMAGE_PERMS = 100;
    public static final int RC_CAMERA_PERMS = 101;
    public static final int RC_CHOOSE_PHOTO = 200;
    public static final int RC_CAMERA_RESULT = 201;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.818);
    }

    public static int convertEuroToDollar(int euros){
        return (int) Math.round(euros * 1.222);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        return dateFormat.format(new Date());
    }

    public static String getFormatDate(int year, int month, int day, Calendar calendar){
        calendar.set(year, month, day);
        return dateFormat.format(calendar.getTime());
    }

    public static Date convertStringToDate(String date)  {
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
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

    public static boolean isConnected() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
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
                .setSmallIcon(R.drawable.ic_stat_name);

        manager.notify(1, builder.build());
    }

    public static String randomUUID(){
        return UUID.randomUUID().toString();
    }

    public static String formatPrice(float price, String currency, String insertCurrency){
        if (currency.equals("USD") && insertCurrency.equals("EUR")){
            price = convertDollarToEuro((int) price);
        }
        if (currency.equals("EUR") && insertCurrency.equals("USD")){
            price = convertEuroToDollar((int) price);
        }
        NumberFormat format = new DecimalFormat("###,###,###");
        String formatPrice = format.format(price);
        if (currency.equals("USD")){
            return formatPrice + " $";
        }else {
            return formatPrice + " €";
        }
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void expand(final View v, View view) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public static void setSelectedNavigationItem(int id, NavigationView navigationView){
        switch (id){
            case 0:
                navigationView.getMenu().getItem(0).setChecked(true);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(false);
                navigationView.getMenu().getItem(3).setChecked(false);
                break;
            case 1:
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(true);
                navigationView.getMenu().getItem(2).setChecked(false);
                navigationView.getMenu().getItem(3).setChecked(false);
                break;
            case 2:
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(true);
                navigationView.getMenu().getItem(3).setChecked(false);
                break;
            case 3:
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(false);
                navigationView.getMenu().getItem(3).setChecked(true);
                break;
        }
    }

    public static Bitmap getBitmap(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static void showSnackBar(View view, String message){
        Snackbar mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View mView = mSnackbar.getView();
        TextView mTextView = (TextView) mView.findViewById(com.google.android.material.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        mSnackbar.show();
    }

    public static void animate(View card, TextView title, Context context){
        if (card.getVisibility() == View.VISIBLE){
            Utils.collapse(card);
            title.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    context.getResources().getDrawable(R.drawable.arrow_down, null), null );
        }else {
            Utils.expand(card);
            title.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    context.getResources().getDrawable(R.drawable.arrow_up, null), null);
        }
    }

}
