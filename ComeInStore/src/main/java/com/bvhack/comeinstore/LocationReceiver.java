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
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocationReceiver extends BroadcastReceiver {

  double latitude, longitude;
  private static final String TEASER_KEY = "teaser";
  private static final String ACTUAL_KEY = "actual";
  private Context APP_CONTEXT;
  private static int count = 0;

  private static boolean seenTeaser = false;
  private static boolean seenActual = false;

  private static long teaserSentMillis;

  final static Type listType = new TypeToken<ArrayList<CampaignBean>>() {
  }.getType();

  @Override
  public void onReceive(final Context context, final Intent calledIntent) {
    Log.d("LOC_RECEIVER", "Location RECEIVED!");
    APP_CONTEXT = context;

    latitude = calledIntent.getDoubleExtra("latitude", -1);
    longitude = calledIntent.getDoubleExtra("longitude", -1);

    updateRemote(latitude, longitude);

  }

  private void updateRemote(final double latitude, final double longitude) {
    Geocoder geo = new Geocoder(APP_CONTEXT);

    try {
      List<Address> addressesList = geo.getFromLocation(latitude, longitude, 10);

      for (Address address : addressesList) {
        if (address.getPostalCode() != null) {

          MyThread thread = new MyThread(address.getLatitude(), address.getLongitude());
          thread.start();

//          double addressLatitude = address.getLatitude();
//          double addressLongitude = address.getLongitude();
//          HttpClient httpClient = new DefaultHttpClient();
//
//          String url = String.format("http://10.247.3.196:3000/api/nearby/%s/%s", addressLatitude, addressLongitude);
//          HttpGet request = new HttpGet(url);
//
//          HttpResponse response = httpClient.execute(request);
//
//          String retailersAsJson = EntityUtils.toString(response.getEntity());

          //Send backend the location and get the list of retailers which have a deal
          //If the retailer has already been fully processed - ignore.
          //If the retailer has not already been processed
          //   -  if it is, then display the retailer %centage off - add the retailer to the fully
          //      processed list, remove from partially processed list. (potentially have a time bomb)
          //   -  if it is not, then only display a teaser saying go to this store and see it - add
          //      the retailer to a partially processed list. (potentially have a time bomb)
          //The information stored is the campaign and the state.

//          List<CampaignBean> campaignBeans = getCampaignBeans(retailersAsJson);
//
//          persistAndSendNotification(campaignBeans);
          break;
        }

      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<CampaignBean> getCampaignBeans(String json) {
    return new Gson().fromJson(json, listType);
  }

  private void persistAndSendNotification(List<CampaignBean> campaignBeans) {

    for (CampaignBean campaignBean : campaignBeans) {
      if(campaignBean.isTeaser() && !seenTeaser && !seenActual) {
        sendCampaignNotification(campaignBean);
        seenTeaser = true;
        teaserSentMillis = System.currentTimeMillis();
      }
      else if(!campaignBean.isTeaser() && !seenActual && seenTeaser && (teaserSentMillis + 30000 > System.currentTimeMillis())) {
        sendCampaignNotification(campaignBean);
        seenActual = true;
      }

//      Set<String> teaserRetailerSet = getSharedPreferences().getStringSet(TEASER_KEY, null);
//      Set<String> actualRetailerSet = getSharedPreferences().getStringSet(ACTUAL_KEY, null);
//      if(teaserRetailerSet == null) {
//        teaserRetailerSet = new HashSet<String>();
//      }
//      if(actualRetailerSet == null) {
//        actualRetailerSet = new HashSet<String>();
//      }
//      SharedPreferences.Editor editor = getSharedPreferences().edit();
//
//      if (campaignBean.isTeaser()) {
//        if (teaserRetailerSet.contains(campaignBean.get_id())) {
//          Already contains retailer - dont do anything
//          return;
//        } else if (actualRetailerSet.contains(campaignBean.get_id())) {
//          actualRetailerSet.remove(campaignBean.get_id());
//          editor.putStringSet(ACTUAL_KEY, actualRetailerSet);
//          editor.commit();
//        }
//
//        teaserRetailerSet.add(campaignBean.get_id());
//        editor.putStringSet(TEASER_KEY, teaserRetailerSet);
//        editor.commit();
//        sendCampaignNotification(campaignBean);
//      }
//
//      if (!campaignBean.isTeaser()) {
//        if (teaserRetailerSet.contains(campaignBean.get_id())) {
//          teaserRetailerSet.remove(campaignBean.get_id());
//          editor.putStringSet(TEASER_KEY, actualRetailerSet);
//          editor.commit();
//
//          actualRetailerSet.add(campaignBean.get_id());
//          editor.putStringSet(ACTUAL_KEY, actualRetailerSet);
//          editor.commit();
//          sendCampaignNotification(campaignBean);
//        }
//
//        if(teaserRetailerSet.isEmpty() && !actualRetailerSet.contains(campaignBean.get_id())) {
//          actualRetailerSet.add(campaignBean.get_id());
//          editor.putStringSet(ACTUAL_KEY, actualRetailerSet);
//          editor.commit();
//          sendCampaignNotification(campaignBean);
//        }
//      }
    }
  }

  private void sendCampaignNotification(CampaignBean campaignBean) {

    NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(APP_CONTEXT)
            .setSmallIcon(R.drawable.bvicon)
            .setContentTitle(String.format("New Deal At: %s!", campaignBean.getRetailer()))
            .setContentText(String.format("Click to see deal at: %s!", campaignBean.getRetailer()))
            .setAutoCancel(true);


    Intent resultIntent = new Intent(APP_CONTEXT, MainActivity.class);
    resultIntent.putExtra("name", campaignBean.getName());
    resultIntent.putExtra("address", campaignBean.getAddress());
    resultIntent.putExtra("discountPct",campaignBean.getDiscountPct());
    resultIntent.putExtra("latitude",campaignBean.getLatitude());
    resultIntent.putExtra("longitude",campaignBean.getLongitude());
    resultIntent.putExtra("retailerLongName",campaignBean.getRetailer());
    resultIntent.putExtra("isTeaser",campaignBean.isTeaser());
    resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    TaskStackBuilder stackBuilder = TaskStackBuilder.create(APP_CONTEXT);

    stackBuilder.addParentStack(MainActivity.class);

    stackBuilder.addNextIntent(resultIntent);
    PendingIntent resultPendingIntent =
        stackBuilder.getPendingIntent(
            campaignBean.get_id().hashCode(),
            PendingIntent.FLAG_UPDATE_CURRENT
        );
    mBuilder.setContentIntent(resultPendingIntent);
    NotificationManager mNotificationManager =
        (NotificationManager) APP_CONTEXT.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
    mNotificationManager.notify(campaignBean.get_id().hashCode(), mBuilder.build());
  }

  private SharedPreferences getSharedPreferences() {
    return APP_CONTEXT.getSharedPreferences(MainActivity.PREFS_NAME, 0);
  }

  private CampaignBean createDummy() {
    return new CampaignBean("abc", "19 Union Sq West, NY, 10003", true,  "BestBuy", 20, "Best Buy Union Square", 30, 97);
  }

  private class MyThread extends Thread {

    private double mLatitude;
    private double mLongitude;

    public MyThread(double latitude, double longitude) {

      mLatitude = latitude;
      mLongitude = longitude;
    }

    @Override
    public void run() {

      try {
        HttpClient httpClient = new DefaultHttpClient();

        String url = String.format("http://10.247.3.196:3000/api/nearby/%s/%s", mLatitude, mLongitude);
        HttpGet request = new HttpGet(url);

        HttpResponse response = httpClient.execute(request);

        String retailersAsJson = EntityUtils.toString(response.getEntity());

        List<CampaignBean> campaignBeans = getCampaignBeans(retailersAsJson);

        persistAndSendNotification(campaignBeans);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
