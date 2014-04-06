package com.bvhack.comeinstore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends Activity {

  private static Context context;
  public static final String PREFS_NAME = "RetailerList";

  public static Context getContext() {
    return context;
  }


  @Override
  protected void onCreate(Bundle arg0) {
    super.onCreate(arg0);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    String name = checkNotNullAndReturn("name")?(String) getIntent().getExtras().get("name"):null;
    String address = checkNotNullAndReturn("address")?(String) getIntent().getExtras().get("address"):null;
    int discountPct = checkNotNullAndReturn("discountPct")?(Integer)getIntent().getExtras().get("discountPct"):0;
    final double latitude = checkNotNullAndReturn("latitude")?(Double)getIntent().getExtras().get("latitude"):0.0;
    final double longitude = checkNotNullAndReturn("longitude")?(Double)getIntent().getExtras().get("longitude"):0.0;
    String retailerLongName = checkNotNullAndReturn("retailerLongName")?(String) getIntent().getExtras().get("retailerLongName"):null;
    boolean isTeaser = checkNotNullAndReturn("isTeaser")?(Boolean) getIntent().getExtras().get("isTeaser"):true;

    context = getApplicationContext();
    // use this to start and trigger a service
    Intent i = new Intent(getApplicationContext(), ComeInStoreGeoLocationService.class);
    // potentially add data to the intent
    i.putExtra("KEY1", "Value to be used by the service");
    getApplicationContext().startService(i);

    //Ideally get information from the bundle, but for now, get it from campaign bean.
    CampaignBean campaignBean = new CampaignBean("abc1234", "19 union sq west, nyc", true, "Macy's", 50, "Macy's Time's Square", 30.0, 97.0);

    setContentView(R.layout.activity_main);


    ImageView imgView = (ImageView) findViewById(R.id.myimg);
    ImageView macysbath = (ImageView) findViewById(R.id.macysbath);
    TextView txtView = (TextView) findViewById(R.id.discount_text);

    Typeface myTypeface = Typeface.createFromAsset(
        this.getAssets(),
        "RobotoLight.ttf");

    txtView.setTypeface(myTypeface);
    Button button = (Button) findViewById(R.id.goToMap);
    Button redeemButton = (Button) findViewById(R.id.redeem);

    if(isTeaser) {
      redeemButton.setVisibility(View.GONE);
      txtView.setText("Secret Coupon! Come to store below!");
    } else {
      redeemButton.setVisibility(View.VISIBLE);
      txtView.setText("Welcome to " + name + "! You unlock " + discountPct + "% discount!");
    }
    if(retailerLongName == null) {
      retailerLongName = "Macy's - North Austin";
    }
    button.setText(retailerLongName);

    if(name == null) {
      button.setVisibility(View.GONE);
      txtView.setVisibility(View.GONE);
      imgView.setVisibility(View.GONE);
      redeemButton.setVisibility(View.GONE);
      macysbath.setVisibility(View.GONE);
    }

    redeemButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, RedeemActivity.class);
        startActivity(intent);
      }
    });

    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
      }
    });
  }
  public boolean checkNotNullAndReturn(String name){

    if(getIntent().getExtras() == null) {
      return false;
    }

    return getIntent().getExtras().get(name) != null;
  }
}