package com.example.deosfriend.apptest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Deo's Friend on 3/15/2015.
 */
public class childDetails extends ActionBarActivity {

    DBAdapter myDB;
    EditText childName, childAge, childGender;
    Button start;

    // Ginger's code
    TextView RSQ11, RSQ22, RSQ33, RSQ44, RSQ55, name;
    gradingDB gradeDB;
    Button btnSubmit, btnView;
    private RatingBar RSQ1, RSQ2, RSQ3, RSQ4, RSQ5;
    // =================================

    private Toolbar toolbar;
    private ViewPager mPager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_details);

        // ================ Action bar codes ================
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(childDetails.this, ListView_Database.class);
                        childDetails.this.startActivity(intent);
                    }
                }
        );
        // ===================================================

        // ================ Tabs ================
/*        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);*/

         final TabHost tabhost = (TabHost) findViewById(R.id.tabHost);

/*        TabHost.TabSpec spec = tabhost.newTabSpec("Tab 1");
        spec.setContent(new Intent(this, Timer_Test.class));
        spec.setIndicator("Tab1 secondActivity");
        tabhost.addTab(spec);

        TabHost.TabSpec spec2 = tabhost.newTabSpec("Tab2");
        spec2.setContent(new Intent(this, ListView_Database.class));
        spec2.setIndicator("Tab2 thirdActivity");
        tabhost.addTab(spec2);*/

        tabhost.setup();
        TabHost.TabSpec tabSpec = tabhost.newTabSpec("details");
        tabSpec.setContent(R.id.childDetailTab);
        tabSpec.setIndicator("Details");
        tabhost.addTab(tabSpec);

/*        tabSpec =  tabhost.newTabSpec("timer");
        tabSpec.setContent(new Intent(this, Timer_Test.class));
        tabSpec.setIndicator("Timer");
        //tabSpec.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tabhost.addTab(tabSpec);*/

        tabSpec =  tabhost.newTabSpec("test");
        tabSpec.setContent(R.id.timerTab);
        tabSpec.setIndicator("Test");
        tabhost.addTab(tabSpec);

        tabSpec =  tabhost.newTabSpec("test");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Test");
        tabhost.addTab(tabSpec);
        // ===================================================

        childName = (EditText)findViewById(R.id.tbName);
        childAge = (EditText)findViewById(R.id.tbAge);
        childGender = (EditText)findViewById(R.id.tbGender);
        start = (Button) findViewById(R.id.btnObserve);

        openDB();
        openGradeDB();
        Intent intent = getIntent();

        final String childID = getIntent().getExtras().getString("childID");
        Toast.makeText(childDetails.this, "Passed through is: " + childID, Toast.LENGTH_LONG).show();

        Long inID = Long.parseLong(childID);

        Cursor cursor = myDB.getRow(inID);
        childName.setText(cursor.getString(1));
        childAge.setText(cursor.getString(2));
        childGender.setText(cursor.getString(3));

        final String childName = cursor.getString(1);

        start.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Intent myIntent = new Intent(childDetails.this, Timer_Test.class);
                        myIntent.putExtra("childID", childID);
                        myIntent.putExtra("childName", childName);
                        childDetails.this.startActivity(myIntent);

/*                        Intent myIntent = new Intent(childDetails.this, ActivityStack.class);
                        myIntent.putExtra("childID", childID);
                        myIntent.putExtra("childName", childName);
                        TabHost.TabSpec spec = tabhost.newTabSpec("tabId");
                        spec.setContent(myIntent);*/
                    }
                }
        );

        // ================================== Ginger's code ==========================================

        btnSubmit = (Button) findViewById(R.id.button2);


//        Intent intent = getIntent();
//        String fName = intent.getStringExtra("username");
        name = (TextView) findViewById(R.id.tbChildName);
        name.setText(cursor.getString(1));
//        name.setText(fName);


        //display the current rating value in textview
        RSQ1 = (RatingBar) findViewById(R.id.Qns1_RS);
        RSQ2 = (RatingBar) findViewById(R.id.Qns2_RS);
        RSQ3 = (RatingBar) findViewById(R.id.Qns3_RS);
        RSQ4 = (RatingBar) findViewById(R.id.Qns4_RS);
        RSQ5 = (RatingBar) findViewById(R.id.Qns5_RS);
        btnSubmit = (Button) findViewById(R.id.button2);
        RSQ11 = (TextView) findViewById(R.id.textView6);
        RSQ22 = (TextView) findViewById(R.id.textView7);
        RSQ33 = (TextView) findViewById(R.id.textView8);
        RSQ44 = (TextView) findViewById(R.id.textView9);
        RSQ55 = (TextView) findViewById(R.id.textView10);

        RSQ1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RSQ11.setText(String.valueOf(rating));
            }
        });

        RSQ2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RSQ22.setText(String.valueOf(rating));
            }
        });
        RSQ3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RSQ33.setText(String.valueOf(rating));
            }
        });
        RSQ4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RSQ44.setText(String.valueOf(rating));
            }
        });
        RSQ5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RSQ55.setText(String.valueOf(rating));
            }
        });
    }

    public void OnClick_Submit(View v) {

        String Qns1_lbl = RSQ11.getText().toString();
        String Qns2_lbl = RSQ22.getText().toString();
        String Qns3_lbl = RSQ33.getText().toString();
        String Qns4_lbl = RSQ44.getText().toString();
        String Qns5_lbl = RSQ55.getText().toString();
        String Childname2 = name.getText().toString();
        String errorMsg = "";

        if (Qns1_lbl == "New Text" || Qns2_lbl == "New Text" || Qns3_lbl == "New Text" || Qns4_lbl == "New Text" || Qns5_lbl == "New Text") {
            errorMsg = "Please fill in fields";
            // Message.message(this, errorMsg);
        } else {
            if (errorMsg == "") {

                long newID = gradeDB.insertRow(Childname2, Qns1_lbl, Qns2_lbl, Qns3_lbl, Qns4_lbl, Qns5_lbl);
                Toast.makeText(childDetails.this, "Test: " + Childname2 + ", " + Qns1_lbl , Toast.LENGTH_LONG).show();
                Cursor cursor2 = gradeDB.getRow(newID);
                displayRecordSet(cursor2);
            }
        }
    }

    private void openGradeDB() {
        gradeDB = new gradingDB(this);
        gradeDB.open();
    }


    // Retreive Database
    private void displayRecordSet(Cursor cursor2) {

        String message = "";

        if (cursor2.moveToFirst()) {
            do {
                int id = cursor2.getInt(0);
                String name = cursor2.getString(1);
                String qns1 = cursor2.getString(2);
                String qns2 = cursor2.getString(3);
                String qns3 = cursor2.getString(4);
                String qns4 = cursor2.getString(5);
                String qns5 = cursor2.getString(6);


                message += "ID: " + id
                        + ", Name: " + name
                        + ", Qns1: " + qns1
                        + ", Qns2: " + qns2
                        + ", Qns3: " + qns3
                        + ", Qns4: " + qns4
                        + ", Qns5: " + qns5
                        + "\n";
            } while (cursor2.moveToNext());
        }

        cursor2.close();
    }

    public void OnClick_View(View v) {
        Toast.makeText(childDetails.this, "View Button", Toast.LENGTH_LONG).show();
        Cursor cursor3 = gradeDB.getAllRows();
        displayRecordSet(cursor3);
    }

        // ============ End of ginger code =================



    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB(){
        myDB = new DBAdapter(this);
        myDB.open();
    }

    public void closeDB() {
        myDB.close();
    }

    public void onClick_Back(View view){
        Intent myIntent = new Intent(childDetails.this, ListView_Database.class);
        childDetails.this.startActivity(myIntent);
    }

   /* class MyPagerAdapter extends FragmentPagerAdapter{

        String[] tabs;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment = MyFragment.getInstance(position);
            return myFragment;
        }

        public CharSequence getPageTitle(int position){
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class MyFragment extends Fragment{

        private TextView tabsTv;

        public static MyFragment getInstance(int position){
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return  myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_tabs, container, false);
            tabsTv = (TextView) layout.findViewById(R.id.tvTabs);
            Bundle bundle = getArguments();
            if(bundle != null)
            {
                tabsTv.setText("Position is: " + bundle.getInt("position"));
            }
            return layout;
        }
    }*/
}
