package com.bvhack.comeinstore;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * Created by achin.batra on 4/4/14.
 */
public class RedeemActivity extends Activity {


  @Override
  protected void onCreate(Bundle arg0) {
    super.onCreate(arg0);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setContentView(R.layout.redeem_main);
  }
}
