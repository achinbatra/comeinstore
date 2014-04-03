package com.bvhack.comeinstore;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class LocationReceiver extends BroadcastReceiver {

  double latitude, longitude;
  private Context APP_CONTEXT;
  private int mId;

  @Override
  public void onReceive(final Context context, final Intent calledIntent)
  {
    Log.d("LOC_RECEIVER", "Location RECEIVED!");
    APP_CONTEXT = context;

    latitude = calledIntent.getDoubleExtra("latitude", -1);
    longitude = calledIntent.getDoubleExtra("longitude", -1);

    updateRemote(latitude, longitude);

  }

  private void updateRemote(final double latitude, final double longitude)
  {
    Geocoder geo = new Geocoder(APP_CONTEXT);

    try {
      List<Address> addressesList = geo.getFromLocation(latitude, longitude, 10);

      for (Address address : addressesList) {
        if (address.getPostalCode() != null) {

          //Send backend the location and get the list of retailers which have a deal
          //If the retailer has already been fully processed - ignore.
          //If the retailer has not already been processed
          //   -  if it is, then display the retailer %centage off - add the retailer to the fully
          //      processed list, remove from partially processed list. (potentially have a time bomb)
          //   -  if it is not, then only display a teaser saying go to this store and see it - add
          //      the retailer to a partially processed list. (potentially have a time bomb)
          //The information stored is the campaign and the state.

          String json = "";
          CampaignBean campaignBean = getCampaignBean(json);

          sendCampaignNotification();
          break;
        }

      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private CampaignBean getCampaignBean(String json)  {
    Gson gson = new Gson();

    return null;
  }

  private void sendCampaignNotification() {

    SharedPreferences settings = APP_CONTEXT.getSharedPreferences(ItemListActivity.PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();


    boolean silent = settings.getBoolean("silentMode", false);

    NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(APP_CONTEXT)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("My notification")
            .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
    Intent resultIntent = new Intent(APP_CONTEXT, ItemListActivity.class);


    TaskStackBuilder stackBuilder = TaskStackBuilder.create(APP_CONTEXT);

    stackBuilder.addParentStack(ItemListActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
    stackBuilder.addNextIntent(resultIntent);
    PendingIntent resultPendingIntent =
        stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        );
    mBuilder.setContentIntent(resultPendingIntent);
    NotificationManager mNotificationManager =
        (NotificationManager) APP_CONTEXT.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
    mNotificationManager.notify(mId, mBuilder.build());



  }
}
