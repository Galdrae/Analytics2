package com.example.deosfriend.apptest;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import Controller.Message;
import database.SessionDBAdapter;


public class MainActivity extends ActionBarActivity {

    Button create, view;
    TextView tbName, tbAge;
    RadioButton male, female;

    DBAdapter myDB;
    SessionDBAdapter mySessionDB;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ListView_Database.class);
                        MainActivity.this.startActivity(intent);
                    }
                }
        );

         create = (Button)findViewById(R.id.btnCreate);
         view = (Button)findViewById(R.id.btnView);

        openDB();
        openSessionDB();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB(){
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void openSessionDB(){
        mySessionDB = new SessionDBAdapter(this);
        mySessionDB.open();
    }

    public void closeDB() {
        myDB.close();
    }

    // Create record
    // =================================================
    public void onClick_Create(View v){

        tbName = (TextView)findViewById(R.id.tbName);
        tbAge = (TextView)findViewById(R.id.tbAge);
        female = (RadioButton)findViewById(R.id.rbFemale);
        male = (RadioButton)findViewById(R.id.rbMale);

        String name = tbName.getText().toString();
        String age = tbAge.getText().toString();
        String gender = "";
        String errorMsg = "";

        if ( male.isChecked())
                   gender = male.getText().toString();
        if ( female.isChecked())
                   gender = female.getText().toString();

        if ( name=="" || age==""  ){
            errorMsg = "Please fill in fields";
            Message.message(this, errorMsg);
        }
        if ( female.isChecked() == false && male.isChecked() == false ){
            errorMsg = "Fields not filled";
            Message.message(this, errorMsg);
        }
        if (errorMsg == "" && female.isChecked())
        {
            long newID = myDB.insertRow(name, age, gender, R.drawable.icon_female, "Not observed");

            Cursor cursor = myDB.getRow(newID);
            Message.message(this, "Female created");
            displayRecordSet(cursor);
            Intent intent = new Intent(MainActivity.this, ListView_Database.class);
            MainActivity.this.startActivity(intent);
        }
        if (errorMsg == "" && male.isChecked())
        {
            long newID = myDB.insertRow(name, age, gender, R.drawable.icon_male, "Not observed");

            Cursor cursor = myDB.getRow(newID);
            Message.message(this, "Male created");
            displayRecordSet(cursor);
            Intent intent = new Intent(MainActivity.this, ListView_Database.class);
            MainActivity.this.startActivity(intent);
        }

        tbName.setText("");
        tbAge.setText("");
        female.setChecked(false);
        male.setChecked(false);

        mySessionDB.insertRow(name, "Not observed", "1", "");
    }

    // View Database
    // =================================================
    public void onClick_View(View v){
    //    Message.message(this, "View clicked");
        Cursor cursor = myDB.getAllRows();
        displayRecordSet(cursor);
    }

    // Wipe Database
    // =================================================
    public void onClick_Delete(View v){
    //    Message.message(this, "Delete clicked");
        myDB.deleteAll();

    }

    // Go to View List
    // =================================================
    public void onClick_List(View v){
        Intent intent = new Intent(MainActivity.this, ListView_Database.class);
        MainActivity.this.startActivity(intent);
    }

    // Radio Buttons
    // =================================================
    public void onClick_rbMale(View v){
        female = (RadioButton)findViewById(R.id.rbFemale);
        female.setChecked(false);
    }
    public void onClick_rbFemale(View v){
        male = (RadioButton)findViewById(R.id.rbMale);
        male.setChecked(false);
    }

    // Retreive Database
    private void displayRecordSet(Cursor cursor){

        String message = "";

        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String age = cursor.getString(2);
                String gender = cursor.getString(3);
                String image = cursor.getString(4);

                message += "ID: " +id
                        +", Name: " + name
                        +", Age: " + age
                        +", Gender: " + gender
                        +", Image is: " + image
                        +"\n";
            } while (cursor.moveToNext());
         }

        cursor.close();
        Message.message(this, message);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
    }

}
