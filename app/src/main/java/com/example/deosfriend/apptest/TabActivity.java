package com.example.deosfriend.apptest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by L335A15 on 3/27/2015.
 */
public class TabActivity extends android.app.TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_test);
        TabHost tabHost = getTabHost();
        Intent intent = new Intent().setClass(this, ActivityStack.class);
        TabHost.TabSpec spec = tabHost.newTabSpec("tabId").setIndicator("Temp", getResources().getDrawable(R.drawable.home));
        spec.setContent(intent);

        tabHost.addTab(spec);

        Intent intent1 = new Intent().setClass(this, ActivityStack.class);
        TabHost.TabSpec spec1 = tabHost.newTabSpec("tabId").setIndicator("Temp", getResources().getDrawable(R.drawable.main_icon));
        spec1.setContent(intent1);
        tabHost.addTab(spec1);

        tabHost.setCurrentTab(0);
    }
}
