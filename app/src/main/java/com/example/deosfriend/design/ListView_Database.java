package com.example.deosfriend.design;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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


import Controller.Message;
import database.DBAdapter;

/**
 * Created by Deo's Friend on 3/14/2015.
 */
public class ListView_Database extends ActionBarActivity{

    TextView name, age, status;
    DBAdapter myDB;
    Button View;
    ImageButton FAB;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout_db_appbar);


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

        if (getIntent().getExtras() == null ){
            populateListViewFromDB();
        }
        if ( getIntent().getExtras() != null){
            String sortBy = getIntent().getExtras().getString("SortBy");
            if ( sortBy.equals("Incomplete")){
                sortListViewByIncomplete();
            }
            if ( sortBy.equals("Completed")){
                sortListViewByCompleted();
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
                Intent myIntent = new Intent(ListView_Database.this, childDetails.class);
                myIntent.putExtra("childID", id);
                ListView_Database.this.startActivity(myIntent);

            }
        });
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
