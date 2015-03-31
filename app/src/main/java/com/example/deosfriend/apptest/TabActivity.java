package com.example.deosfriend.apptest;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by L335A15 on 3/27/2015.
 */
public class TabActivity extends ActivityGroup {

    private TabHost tabhost;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_test);

        tabhost = (TabHost) this.findViewById(R.id.tabHost);
        tabhost.setup();

        tabhost.setup(this.getLocalActivityManager());

    }

}
