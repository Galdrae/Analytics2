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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Controller.Child;
import Controller.Grade_Child;
import Controller.Interval;
import Controller.Message;
import Controller.Session;
import au.com.bytecode.opencsv.CSVWriter;
import database.DBAdapter;
import database.IntervalDBAdapter;
import database.NewGradingDB;
import database.SessionDBAdapter;

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
    IntervalDBAdapter myIntervalDb;
    SessionDBAdapter mySessionDb;

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
        openIntervalDB();
        openSessionDB();

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
                exportAllPG();            }
        }
        registerListCallBack();

    }

    private void exportAllPG() {
        Intent intent = new Intent(ListView_Database.this, Export_List.class);
        ListView_Database.this.startActivity(intent);
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

    private void openIntervalDB(){
        myIntervalDb = new IntervalDBAdapter(this);
        myIntervalDb.open();
    }

    public void closeIntervalDB() {
        myIntervalDb.close();
    }

    private void openSessionDB(){
        mySessionDb = new SessionDBAdapter(this);
        mySessionDb .open();
    }

    public void closeSessionDB() {
        mySessionDb .close();
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
                String childName4 = cursor.getString(1);


                // Toast.makeText(ListView_Database.this, "Number is: " + id, Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(ListView_Database.this, Tabs.class);
                myIntent.putExtra("childID", id);
                myIntent.putExtra("childNAME", childName4);
                ListView_Database.this.startActivity(myIntent);

            }
        });
    }

    private void exportAll(){
        populateListViewFromDB();
        ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
        task.execute();
        exportDB();


        myDB.deleteAll();
        myNewGradingDB.deleteAllPerson();
        myNewGradingDB.deleteAllPersonChild();
        myNewGradingDB.deleteAllPersonInterval();
        myNewGradingDB.deleteAllPersonSession();
        myIntervalDb.deleteAll();
        mySessionDb.deleteAll();

        Intent myIntent = new Intent(ListView_Database.this, ListView_Database.class);
        ListView_Database.this.startActivity(myIntent);
    }

    private void exportDB() {

        // date and time code
        long dateInMillis = System.currentTimeMillis();
        String formatDate = "dd-MM-yyyy";
        String formatTime = "HH:mm:ss";
        // convert date to format
        final SimpleDateFormat dateString = new SimpleDateFormat(formatDate);
        // convert time
        final SimpleDateFormat timeString = new SimpleDateFormat(formatTime);
        final String date = dateString.format(new Date(dateInMillis));
        final String time = timeString.format(new Date(dateInMillis));


        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.example.deosfriend.apptest"
                        + "//databases//" + "newgrading.db";
                String backupDBPath = "backup " + date +" .db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getApplicationContext(), "Backup Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Backup Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
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
                SQLiteDatabase db = myNewGradingDB.getReadableDatabase();

//                //ormlite core method
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

//                List<Grade_Child> listdata=myNewGradingDB.GetDataPerson();
//                Grade_Child Question=null;
//
//                // this is the Column of the table and same for Header of CSV file
//                String arrStr1[] ={"Name","Child_id" , "Qns1", "Qns2","Qns3", "Qns4", "Qns5"};
//                csvWrite.writeNext(arrStr1);
//
//                if(listdata.size() > 1)
//                {
//                    for(int index=0; index < listdata.size(); index++)
//                    {
//                        Question=listdata.get(index);
//                        String arrStr[] ={Question.getChildId(), Question.getName(), Question.getQns1(), Question.getQns2(), Question.getQns3(), Question.getQns4(), Question.getQns5()};
//                        csvWrite.writeNext(arrStr);
//                    }
//                }

//                Cursor curCSV3 = db.rawQuery("SELECT * FROM child", null);
//                csvWrite.writeNext("Name", "Gender", "SessionNo", "Inspector", "Remarks", "Primary Diagnosis", "Secondary Diagnosis", "Activity", "No of adults", "No of children", "Status", "Venue");
//                while (curCSV3.moveToNext()) {
//                    String arrStr[] = {curCSV3.getString(3), curCSV3.getString(4),
//                            curCSV3.getString(9), curCSV3.getString(5), curCSV3.getString(8), curCSV3.getString(0), curCSV3.getString(1), curCSV3.getString(2),
//                            curCSV3.getString(6), curCSV3.getString(7), curCSV3.getString(10), curCSV3.getString(11)};
//                    csvWrite.writeNext(arrStr);
//                }
//
//                csvWrite.writeNext();
//
//                Cursor curCSV4 = db.rawQuery("SELECT * FROM interval", null);
//                csvWrite.writeNext("Name", "Child_id", "SessionNo", "Flag", "engagement", "Adult", "Interval", "Material", "Peers", "None Other", "Physical");
//                while (curCSV4.moveToNext()) {
//                    String arrStr[] = {curCSV4.getString(1), curCSV4.getString(2),
//                            curCSV4.getString(10), curCSV4.getString(4), curCSV4.getString(3), curCSV4.getString(0), curCSV4.getString(5), curCSV4.getString(6),
//                            curCSV4.getString(8), curCSV4.getString(7), curCSV4.getString(9)};
//                    csvWrite.writeNext(arrStr);
//                }
//
//                csvWrite.writeNext();
//
//                Cursor curCSV5 = db.rawQuery("SELECT * FROM session ", null);
//                csvWrite.writeNext("Name", "Child_id", "Center", "Observer", "Session Count", "Session Status", "Date", "Start Time", "End Time", "No of flag", "No of interval ");
//                while (curCSV5.moveToNext()) {
//                    String arrStr[] = {curCSV5.getString(7), curCSV5.getString(1), curCSV5.getString(0), curCSV5.getString(6),
//                            curCSV5.getString(8), curCSV5.getString(9), curCSV5.getString(2), curCSV5.getString(10),
//                            curCSV5.getString(3), curCSV5.getString(4), curCSV5.getString(5)};
//                    csvWrite.writeNext(arrStr);
//                }
//
//                csvWrite.writeNext();
//
                Cursor curCSV = db.rawQuery("SELECT * FROM grade_child", null);
                csvWrite.writeNext("Name", "Child_id", "Qns1", "Qns2", "Qns3", "Qns4", "Qns5");
                while (curCSV.moveToNext()) {
                    String arrStr[] = {curCSV.getString(1), curCSV.getString(0),
                            curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7)};
                    csvWrite.writeNext(arrStr);
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

