package com.example.Demo_Subj;

/*
 * created by Stefan Biereigel - 04.12.2014
 */

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

public class HouseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.house);

        Resources resources = getResources();

        HouseView hv;
        hv = new HouseView(this);
        HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.scrollview0);
        hsv.addView(hv);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}