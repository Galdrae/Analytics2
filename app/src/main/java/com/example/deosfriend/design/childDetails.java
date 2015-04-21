package com.example.deosfriend.design;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import Controller.Message;
import database.DBAdapter;
import database.gradingDB;

/**
 * Created by Deo's Friend on 3/15/2015.
 */
public class childDetails extends ActionBarActivity {

    DBAdapter myDB;
    EditText childName, childGender, childPriDi, childSecDi, childRemarks, childInspector, childVenue, childActivity, childNoAdults, childNoChildren;
    Button start;
    String childName4;

    // Ginger's code - new grading
    TextView RSQ11, RSQ22, RSQ33, RSQ44, RSQ55, name;
    gradingDB gradeDB;
    Button btnSubmit, btnView;
    private RatingBar RSQ1, RSQ2, RSQ3, RSQ4, RSQ5;
    // =================================
    // Ginger's code - view before update
    TextView RSQ1View, RSQ2View, RSQ3View, RSQ4View, RSQ5View, nameView, gradingIDView;
    Button btnDelete, btnEdit;
    // ================================

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

        final TabHost tabhost = (TabHost) findViewById(R.id.tabHost);
        tabhost.setup();

        TabHost.TabSpec tabSpec = tabhost.newTabSpec("details");
        tabSpec.setContent(R.id.childDetailTab);
        tabSpec.setIndicator("Details");
        tabhost.addTab(tabSpec);

        TabHost.TabSpec tabSpec1 = tabhost.newTabSpec("grade");
        tabSpec1.setContent(R.id.tab3);
        tabSpec1.setIndicator("Grade");
        tabhost.addTab(tabSpec1);

        // ===================================================

        childName = (EditText)findViewById(R.id.tbName);
        childGender = (EditText)findViewById(R.id.tbGender);
        childPriDi  = (EditText)findViewById(R.id.tbPriDi);
        childSecDi = (EditText) findViewById(R.id.tbSecDi);
        childRemarks = (EditText) findViewById(R.id.tbRemarks);
        childInspector = (EditText) findViewById(R.id.tbInspector);
        childVenue = (EditText) findViewById(R.id.tbVenue);
        childActivity = (EditText) findViewById(R.id.tbActivity);
        childNoAdults = (EditText) findViewById(R.id.tbNoAdults);
        childNoChildren = (EditText) findViewById(R.id.tbNoChildren);

        start = (Button) findViewById(R.id.btnObserve);

        openDB();
        openGradeDB();
        Intent intent = getIntent();

        final String childID = getIntent().getExtras().getString("childID");
       // Toast.makeText(childDetails.this, "Passed through is: " + childID, Toast.LENGTH_LONG).show();

        Long inID = Long.parseLong(childID);

        Cursor cursor = myDB.getRow(inID);
        childName.setText("Name:     " + cursor.getString(1));
        childGender.setText("Gender:   " + cursor.getString(2));
        childPriDi.setText(cursor.getString(3));
        childSecDi.setText(cursor.getString(4));
        childRemarks.setText(cursor.getString(5));
        childInspector.setText(cursor.getString(6));
        childVenue.setText(cursor.getString(7));
        childActivity.setText(cursor.getString(8));
        childNoAdults.setText("No. of Adults:     " + cursor.getString(9));
        childNoChildren.setText("No. of Children:  " + cursor.getString(10));

        childName4 = cursor.getString(1);

        start.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Intent myIntent = new Intent(childDetails.this, Timer_Test.class);
                        myIntent.putExtra("childID", childID);
                        myIntent.putExtra("childName", childName4);
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
        // start - new grading
        btnSubmit = (Button) findViewById(R.id.button2);
        name = (TextView) findViewById(R.id.tbChildName);
        name.setText(cursor.getString(1));

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
        // end - new grading

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

        // start - view
        RSQ1View = (TextView) findViewById(R.id.textView62);
        RSQ2View = (TextView) findViewById(R.id.textView72);
        RSQ3View = (TextView) findViewById(R.id.textView82);
        RSQ4View = (TextView) findViewById(R.id.textView92);
        RSQ5View = (TextView) findViewById(R.id.textView102);
        nameView = (TextView) findViewById(R.id.tbChildName2);
        gradingIDView = (TextView) findViewById(R.id.tbquestionID);
        nameView.setText(cursor.getString(1));
        // end - view


        if (gradeDB.checkExist(childName4)){

            final TabHost tabhost2 = (TabHost) findViewById(R.id.tabHost);
            tabhost2.setup();
            TabHost.TabSpec tabSpec2 = tabhost2.newTabSpec("gradeEdit");
            tabSpec2.setContent(R.id.gradingTab);
            tabSpec2.setIndicator("Result");
            tabhost2.addTab(tabSpec2);

            String ChildName = nameView.getText().toString();
            Cursor c = gradeDB.getRow1(ChildName);
            gradingIDView.setText(c.getString(0));
            RSQ1View.setText(c.getString(2));
            RSQ2View.setText(c.getString(3));
            RSQ3View.setText(c.getString(4));
            RSQ4View.setText(c.getString(5));
            RSQ5View.setText(c.getString(6));

        }
        else{

            LinearLayout mainLayout=(LinearLayout)this.findViewById(R.id.gradingTab);
            mainLayout.setVisibility(LinearLayout.GONE);
        }

    }

    public void OnClick_Submit(View v) {
        if (gradeDB.checkExist(childName4)) {
            String ChildName = nameView.getText().toString();
            String Qns1_lbl = RSQ11.getText().toString();
            String Qns2_lbl = RSQ22.getText().toString();
            String Qns3_lbl = RSQ33.getText().toString();
            String Qns4_lbl = RSQ44.getText().toString();
            String Qns5_lbl = RSQ55.getText().toString();
            gradeDB.updateRecord(ChildName, Qns1_lbl, Qns2_lbl, Qns3_lbl, Qns4_lbl, Qns5_lbl);
            if (gradeDB.checkExist(childName4)) {

                String ChildName11 = nameView.getText().toString();
                Cursor c = gradeDB.getRow1(ChildName11);
                gradingIDView.setText(c.getString(0));
                RSQ1View.setText(c.getString(2));
                RSQ2View.setText(c.getString(3));
                RSQ3View.setText(c.getString(4));
                RSQ4View.setText(c.getString(5));
                RSQ5View.setText(c.getString(6));

            } else {

            }
        }
        else{
            String Qns1_lbl = RSQ11.getText().toString();
            String Qns2_lbl = RSQ22.getText().toString();
            String Qns3_lbl = RSQ33.getText().toString();
            String Qns4_lbl = RSQ44.getText().toString();
            String Qns5_lbl = RSQ55.getText().toString();
            String Childname2 = name.getText().toString();
            String errorMsg = "";

            if (Qns1_lbl.isEmpty() || Qns2_lbl.isEmpty() || Qns3_lbl.isEmpty() || Qns4_lbl.isEmpty() || Qns5_lbl.isEmpty() ) {
                errorMsg = "Please fill in fields";
                Message.message(this, errorMsg);
            } else {
                if (errorMsg == "") {

                    long newID = gradeDB.insertRow(Childname2, Qns1_lbl, Qns2_lbl, Qns3_lbl, Qns4_lbl, Qns5_lbl);
                    Toast.makeText(childDetails.this, Childname2 + " grade submitted.", Toast.LENGTH_LONG).show();
                    Cursor cursor2 = gradeDB.getRow(newID);
                    displayRecordSet(cursor2);
                }
            }
            if (gradeDB.checkExist(childName4)) {

                final TabHost tabhost2 = (TabHost) findViewById(R.id.tabHost);
                tabhost2.setup();
                TabHost.TabSpec tabSpec2 = tabhost2.newTabSpec("gradeEdit");
                tabSpec2.setContent(R.id.gradingTab);
                tabSpec2.setIndicator("Result");
                tabhost2.addTab(tabSpec2);

                String ChildName = nameView.getText().toString();
                Cursor c = gradeDB.getRow1(ChildName);
                gradingIDView.setText(c.getString(0));
                RSQ1View.setText(c.getString(2));
                RSQ2View.setText(c.getString(3));
                RSQ3View.setText(c.getString(4));
                RSQ4View.setText(c.getString(5));
                RSQ5View.setText(c.getString(6));

            } else {

                //Intent intent = new Intent(this, grading.class);
                //intent.putExtra("username", childName.getText().toString());
                //startActivity(intent);
            }
        }
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
        Message.message(this, message);
    }

    public void OnClick_View(View v) {
       // Toast.makeText(childDetails.this, "View Button", Toast.LENGTH_LONG).show();
        Cursor cursor3 = gradeDB.getAllRows();
        displayRecordSet(cursor3);
    }

    private void openGradeDB() {
        gradeDB = new gradingDB(this);
        gradeDB.open();
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
