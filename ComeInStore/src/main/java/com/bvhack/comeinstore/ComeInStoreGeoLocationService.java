package com.bvhack.comeinstore;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;


public class ComeInStoreGeoLocationService extends Service implements LocationListener{

  private LocationManager lm;
  private Thread triggerService;

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    addLocationListener();

    return Service.START_NOT_STICKY;
  }

  private void addLocationListener()
  {
    triggerService = new Thread(new Runnable(){
      public void run(){
        try{
          Looper.prepare();//Initialise the current thread as a looper.
          lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

          Criteria c = new Criteria();
          c.setAccuracy(Criteria.ACCURACY_COARSE);

//          final String PROVIDER = lm.getBestProvider(c, true);

//          MyLocationListener myLocationListener = new MyLocationListener();
          lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, ComeInStoreGeoLocationService.this);
          Log.d("LOC_SERVICE", "Service RUNNING!");
          Looper.loop();
        }catch(Exception ex){
          ex.printStackTrace();
        }
      }
    }, "LocationThread");
    triggerService.start();
  }

  @Override
  public void onLocationChanged(Location location)
  {
    updateLocation(location);
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onProviderDisabled(String provider) {

  }

  public static void updateLocation(Location location)
  {
    Context appCtx = MainActivity.getContext();

    double latitude, longitude;

    latitude = location.getLatitude();
    longitude = location.getLongitude();

    Intent filterRes = new Intent();
    filterRes.setAction("xxx.yyy.intent.action.LOCATION");
    filterRes.putExtra("latitude", latitude);
    filterRes.putExtra("longitude", longitude);
    appCtx.sendBroadcast(filterRes);
  }

  @Override
  public IBinder onBind(Intent intent) {
    //TODO for communication return IBinder implementation
    return null;
  }
}
