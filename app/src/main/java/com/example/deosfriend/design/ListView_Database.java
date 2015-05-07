package com.example.deosfriend.design;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import Controller.Child;
import Controller.Grade_Child;
import Controller.Interval;
import Controller.Message;
import Controller.Session;
import au.com.bytecode.opencsv.CSVWriter;
import database.DBAdapter;
import database.NewGradingDB;

/**
 * Created by Deo's Friend on 3/14/2015.
 */
public class ListView_Database extends ActionBarActivity{
    private static final String TAG="ListView_Database";
    TextView name, age, status;
    DBAdapter myDB;
    NewGradingDB myNewGradingDB;
    File file=null;
    Button View;
    ImageButton FAB;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout_db_appbar);
        myNewGradingDB = new NewGradingDB(getApplicationContext());
        //  FAB - Floating Action Button
        FAB = (ImageButton) findViewById(R.id.imageButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"Hello World", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ListView_Database.this,MainActivity.class);
                startActivity(i);
            }
        });

        // Action/tool bar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Navigation drawer
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout), toolbar);


        // methods
        openDB();

        if (getIntent().getExtras() == null )
        {
            populateListViewFromDB();
        }
        if ( getIntent().getExtras() != null)
        {
            String sortBy = getIntent().getExtras().getString("SortBy");
            if ( sortBy.equals("Incomplete")){
                sortListViewByIncomplete();
            }
            if ( sortBy.equals("Completed")){
                sortListViewByCompleted();
            }
            if ( sortBy.equals("Export")){
                exportAll();
            }
        }
        registerListCallBack();

    }

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

    public void onClick_View(android.view.View v){
    }

    public void onClick_Create(View v){
        Intent intent = new Intent(ListView_Database.this, MainActivity.class);
        ListView_Database.this.startActivity(intent);
    }

    private void displayRecordSet(Cursor cursor){

        String message = "Hello!, ";

        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String age = cursor.getString(2);
                String gender = cursor.getString(3);

                message += "ID: " +id
                        +", Name: " + name
                        +", Age: " + age
                        +", Gender: " + gender
                        +"\n";
            } while (cursor.moveToNext());
        }

        cursor.close();
        Message.message(this, message);

    }

    // Default sorting
    private void populateListViewFromDB() {

        Cursor cursor = myDB.getAllRows();

        startManagingCursor(cursor);

        String[] fromFieldNames = new String[]
                {DBAdapter.KEY_CHILDNAME, DBAdapter.KEY_SESSIONNO, DBAdapter.KEY_IMAGE, DBAdapter.KEY_STATUS};
        int[] toViewIDs = new int[]
                {R.id.tvName_Db, R.id.tvAge_Db, R.id.lv_Image, R.id.tvStatus};

        SimpleCursorAdapter myCursorAdapter =
                new SimpleCursorAdapter(
                        this,
                        R.layout.listview_layout_db_row,
                        cursor,
                        fromFieldNames, // DB column names
                        toViewIDs       // View IDS to put info in
                );

        // Set adapter for list view
        ListView myList = (ListView) findViewById(R.id.listView_db);
        myList.setAdapter(myCursorAdapter);
    }

    // sort by incomplete child
    private void sortListViewByIncomplete() {

        Cursor cursor = myDB.getAllRowsIncomplete("Not observed", "Incomplete", "Fail");

        startManagingCursor(cursor);

        String[] fromFieldNames = new String[]
                {DBAdapter.KEY_CHILDNAME, DBAdapter.KEY_SESSIONNO, DBAdapter.KEY_IMAGE, DBAdapter.KEY_STATUS};
        int[] toViewIDs = new int[]
                {R.id.tvName_Db, R.id.tvAge_Db, R.id.lv_Image, R.id.tvStatus};

        SimpleCursorAdapter myCursorAdapter =
                new SimpleCursorAdapter(
                        this,
                        R.layout.listview_layout_db_row,
                        cursor,
                        fromFieldNames, // DB column names
                        toViewIDs       // View IDS to put info in
                );

        // Set adapter for list view
        ListView myList = (ListView) findViewById(R.id.listView_db);
        myList.setAdapter(myCursorAdapter);
    }

    // sort by Completed child
    private void sortListViewByCompleted() {

        Cursor cursor = myDB.getAllRowsCompleted("Completed");

        startManagingCursor(cursor);

        String[] fromFieldNames = new String[]
                {DBAdapter.KEY_CHILDNAME, DBAdapter.KEY_SESSIONNO, DBAdapter.KEY_IMAGE, DBAdapter.KEY_STATUS};
        int[] toViewIDs = new int[]
                {R.id.tvName_Db, R.id.tvAge_Db, R.id.lv_Image, R.id.tvStatus};

        SimpleCursorAdapter myCursorAdapter =
                new SimpleCursorAdapter(
                        this,
                        R.layout.listview_layout_db_row,
                        cursor,
                        fromFieldNames, // DB column names
                        toViewIDs       // View IDS to put info in
                );

        // Set adapter for list view
        ListView myList = (ListView) findViewById(R.id.listView_db);
        myList.setAdapter(myCursorAdapter);
    }

    private void registerListCallBack() {
        ListView myList = (ListView) findViewById(R.id.listView_db);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long idInDB) {

                Cursor cursor = myDB.getRow(idInDB);
                String id = String.valueOf(idInDB).toString();

                // Toast.makeText(ListView_Database.this, "Number is: " + id, Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(ListView_Database.this, Tabs.class);
                myIntent.putExtra("childID", id);
                ListView_Database.this.startActivity(myIntent);

            }
        });
    }

    private void exportAll(){
        populateListViewFromDB();
        ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
        task.execute();
    }

    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(ListView_Database.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Exporting database...");
            this.dialog.show();

        }
        protected Boolean doInBackground(final String... args){

            File dbFile=getDatabasePath("newgrading.db");
            Log.v(TAG, "Db path is: " + dbFile);  //get the path of db

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            file = new File(exportDir, "ChildObservation.csv");
            try {

                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                //ormlite core method
                Child person=null;
                List<Child> listdataChild = myNewGradingDB.GetDataPersonChild();
                //this is the Column of the table and same for Header of CSV file
                String child[] ={"Name", "Gender", "SessionNo", "Inspector", "Remarks", "Primary Diagnosis", "Secondary Diagnosis", "Activity", "No of adults", "No of children", "Status", "Venue"};
                csvWrite.writeNext(child);

                if(listdataChild.size() > 1)
                {
                    for(int index1=0; index1 < listdataChild.size(); index1++)
                    {
                        person=listdataChild.get(index1);
                        String arrStr[] ={person.getChildName(), person.getGender(), person.getSessionNo(), person.getInspecter(), person.getRemarks(), person.getPriDi(), person.getSecDi(), person.getActivity(), person.getNoAdults(), person.getNoChildren(), person.getStatus(), person.getVenue()};
                        csvWrite.writeNext(arrStr);
                    }
                }

                csvWrite.writeNext();

                Interval interval =null;
                List<Interval> listdataInterval = myNewGradingDB.GetDataPersonInterval();
                //this is the Column of the table and same for Header of CSV file
                String child_interval[] ={"Name", "Child_id" , "SessionNo", "Flag", "engagement", "Adult", "Interval", "Material", "Peers", "None Other", "Physical"};
                csvWrite.writeNext(child_interval);

                if(listdataInterval.size() > 1)
                {
                    for(int index1=0; index1 < listdataInterval.size(); index1++)
                    {
                        interval=listdataInterval.get(index1);
                        String arrStr[] ={interval.getChildName(), interval.getChild_id(), interval.getSessionNo(), interval.getFlag(), interval.getEngagement(), interval.getAdults(), interval.getInterval(), interval.getMaterials(), interval.getPeers(), interval.getNoneOthers(), interval.getPhyscial()};
                        csvWrite.writeNext(arrStr);
                    }
                }

                csvWrite.writeNext();

                Session session =null;
                List<Session> listdataSession = myNewGradingDB.GetDataPersonSession();
                //this is the Column of the table and same for Header of CSV file
                String child_Session[] ={"Name", "Child_id" , "Center", "Observer", "Session Count", "Session Status", "Date", "Start Time", "End Time", "No of flag", "No of interval "};
                csvWrite.writeNext(child_Session);

                if(listdataSession.size() > 1)
                {
                    for(int index1=0; index1 < listdataSession.size(); index1++)
                    {
                        session=listdataSession.get(index1);
                        String arrStr[] ={session.getSessionChildName(), session.getChild_Id(), session.getCenter_id(), session.getObserver(), session.getSessionCount(), session.getSessionStatus(), session.getSessionStatus(), session.getDate(), session.getStartTime(), session.getEndTime(), session.getNoFlags(), session.getNoInterval()};
                        csvWrite.writeNext(arrStr);
                    }
                }

                csvWrite.writeNext();

                List<Grade_Child> listdata=myNewGradingDB.GetDataPerson();
                Grade_Child Question=null;

                // this is the Column of the table and same for Header of CSV file
                String arrStr1[] ={"Name","Child_id" , "Qns1", "Qns2","Qns3", "Qns4", "Qns5"};
                csvWrite.writeNext(arrStr1);

                if(listdata.size() > 1)
                {
                    for(int index=0; index < listdata.size(); index++)
                    {
                        Question=listdata.get(index);
                        String arrStr[] ={Question.getChildId(), Question.getName(), Question.getQns1(), Question.getQns2(), Question.getQns3(), Question.getQns4(), Question.getQns5()};
                        csvWrite.writeNext(arrStr);
                    }
                }

                csvWrite.writeNext();

                csvWrite.close();
                return true;
            }
            catch (IOException e){
                Log.e("ListView_Database", e.getMessage(), e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success)	{

            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (success){
                Toast.makeText(ListView_Database.this, "Export successful!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ListView_Database.this, "Export failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

 /*   //private SenzorApplication application;
    //private ListView friendListView;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    //private FriendListAdapter friendListAdapter;
    //private ArrayList<User> friendList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);

        return true;
    }*/
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
