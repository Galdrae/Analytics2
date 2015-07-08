package com.example.deosfriend.design;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Controller.Child;
import Controller.Grade_Child;
import Controller.Interval;
import Controller.Message;
import Controller.Session;
import au.com.bytecode.opencsv.CSVWriter;
import database.DBAdapter;
import database.NewGradingDB;
import database.SessionDBAdapter;
import database.gradingDB;


public class Result extends Activity {

    private static final String TAG = "Grade";
    File file = null;
    DBAdapter myDB;
    NewGradingDB myNewGradingDB;
    SessionDBAdapter mySessionDB;


    String array[] = new String[]{"qns1", "qns2", "qns3", "qns4", "qns5"};
    float qns1, qns2, qns3, qns4, qns5;
    ArrayList<Grade_Child> qns1Array = new ArrayList<Grade_Child>();
    ArrayList<Grade_Child> qns2Array = new ArrayList<Grade_Child>();
    ArrayList<Grade_Child> qns3Array = new ArrayList<Grade_Child>();
    ArrayList<Grade_Child> qns4Array = new ArrayList<Grade_Child>();
    ArrayList<Grade_Child> qns5Array = new ArrayList<Grade_Child>();
    private View mChart;
    TextView title;
    String childName,childID;

    Button start;
    String childrenName, childrenID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        myNewGradingDB = new NewGradingDB(getApplicationContext());

        title = (TextView) findViewById(R.id.tv_title);
        myNewGradingDB = new NewGradingDB(getApplicationContext());
        childName = getIntent().getExtras().getString("childNAME");
        childID =  getIntent().getExtras().getString("childID");

        Cursor c = getAllKeyedQuestion(childName, array);
        String qns = "";
        int i = 0;
        do{
            qns1 = c.getFloat(0);
            qns = qns + " " + Float.toString(qns1);
            Grade_Child gc = new Grade_Child();
            gc.setQns1(qns1);
            qns1Array.add(gc);
            i++;
        }
        while(c.moveToNext());

        Cursor c2 = getAllKeyedQuestion(childName, array);
        String qns1 = "";
        int i2 = 0;
        do{
            qns2 = c2.getFloat(1);
            qns1 = qns1 + " " + Float.toString(qns2);
            Grade_Child gc = new Grade_Child();
            gc.setQns2(qns2);
            qns2Array.add(gc);
            i2++;
        }
        while(c2.moveToNext());

        Cursor c3 = getAllKeyedQuestion(childName, array);
        int i3 = 0;
        do{
            qns3 = c3.getFloat(2);
            Grade_Child gc = new Grade_Child();
            gc.setQns3(qns3);
            qns3Array.add(gc);
            i3++;
        }
        while(c3.moveToNext());

        Cursor c4 = getAllKeyedQuestion(childName, array);
        int i4 = 0;
        do{
            qns4 = c4.getFloat(3);
            Grade_Child gc = new Grade_Child();
            gc.setQns4(qns4);
            qns4Array.add(gc);
            i4++;
        }
        while(c4.moveToNext());

        Cursor c5 = getAllKeyedQuestion(childName, array);
        int i5 = 0;
        do{
            qns5 = c5.getFloat(4);
            Grade_Child gc = new Grade_Child();
            gc.setQns5(qns5);
            qns5Array.add(gc);
            i5++;
        }
        while(c5.moveToNext());

        title.setText(childName);

        openChart();
    }

    private void openChart(){

        if(CheckIfExist(childName)){

            XYSeries Qns1Series = new XYSeries("Qns1");
            XYSeries Qns2Series = new XYSeries("Qns2");
            XYSeries Qns3Series = new XYSeries("Qns3");
            XYSeries Qns4Series = new XYSeries("Qns4");
            XYSeries Qns5Series = new XYSeries("Qns5");


            for(int i=0 ; i<qns1Array.size() ; i++){
                Qns1Series.add(i + 1 ,qns1Array.get(i).getQns1());
            }

            for(int i=0 ; i<qns2Array.size() ; i++){
                Qns2Series.add(i + 1 , qns2Array.get(i).getQns2());
            }

            for(int i=0 ; i<qns3Array.size() ; i++){
                Qns3Series.add(i + 1 , qns3Array.get(i).getQns3());
            }

            for(int i=0 ; i<qns4Array.size() ; i++){
                Qns4Series.add(i + 1 , qns4Array.get(i).getQns4());
            }

            for(int i=0 ; i<qns5Array.size() ; i++){
                Qns5Series.add(i + 1 , qns5Array.get(i).getQns5());
            }

            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

            dataset.addSeries(Qns1Series);
            dataset.addSeries(Qns2Series);
            dataset.addSeries(Qns3Series);
            dataset.addSeries(Qns4Series);
            dataset.addSeries(Qns5Series);

            XYSeriesRenderer Qns1Renderer = new XYSeriesRenderer();
            Qns1Renderer.setColor(Color.BLUE);
            Qns1Renderer.setPointStyle(PointStyle.CIRCLE);
            Qns1Renderer.setFillPoints(true);
            Qns1Renderer.setLineWidth(2);
            Qns1Renderer.setDisplayChartValues(true);

            XYSeriesRenderer Qns2Renderer = new XYSeriesRenderer();
            Qns2Renderer.setColor(Color.BLACK);
            Qns2Renderer.setPointStyle(PointStyle.CIRCLE);
            Qns2Renderer.setFillPoints(true);
            Qns2Renderer.setLineWidth(2);
            Qns2Renderer.setDisplayChartValues(true);

            XYSeriesRenderer Qns3Renderer = new XYSeriesRenderer();
            Qns3Renderer.setColor(Color.RED);
            Qns3Renderer.setPointStyle(PointStyle.DIAMOND);
            Qns3Renderer.setFillPoints(true);
            Qns3Renderer.setLineWidth(2);
            Qns3Renderer.setDisplayChartValues(true);

            XYSeriesRenderer Qns4Renderer = new XYSeriesRenderer();
            Qns4Renderer.setColor(Color.LTGRAY);
            Qns4Renderer.setPointStyle(PointStyle.DIAMOND);
            Qns4Renderer.setFillPoints(true);
            Qns4Renderer.setLineWidth(2);
            Qns4Renderer.setDisplayChartValues(true);


            // Creating XYSeriesRenderer to customize expenseSeries
            XYSeriesRenderer Qns5Renderer = new XYSeriesRenderer();
            Qns5Renderer.setColor(Color.GREEN);
            Qns5Renderer.setPointStyle(PointStyle.CIRCLE);
            Qns5Renderer.setFillPoints(true);
            Qns5Renderer.setLineWidth(2);
            Qns5Renderer.setDisplayChartValues(true);

            // Creating a XYMultipleSeriesRenderer to customize the whole chart
            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

            multiRenderer.setApplyBackgroundColor(true);
            multiRenderer.setBackgroundColor(Color.WHITE);
            multiRenderer.setMarginsColor(Color.WHITE);

            multiRenderer.setYAxisAlign(Paint.Align.LEFT, 0);
            multiRenderer.setYLabelsAlign(Paint.Align.LEFT, 0);
            multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
            multiRenderer.setYLabelsColor(0, Color.BLACK);
            multiRenderer.setXLabelsColor(Color.BLACK);
            multiRenderer.setAxesColor(Color.BLACK);
            multiRenderer.setYLabelsPadding(30);
            multiRenderer.setLabelsColor(Color.BLACK);

            multiRenderer.setXLabels(0);
            multiRenderer.setChartTitle("Progress");
            multiRenderer.setXTitle("Process");
            multiRenderer.setYTitle("Grade");
            multiRenderer.setAxisTitleTextSize(15);
            multiRenderer.setZoomButtonsVisible(true);
//__
            multiRenderer.addSeriesRenderer(Qns1Renderer);
            multiRenderer.addSeriesRenderer(Qns2Renderer);
            multiRenderer.addSeriesRenderer(Qns3Renderer);
            multiRenderer.addSeriesRenderer(Qns4Renderer);
            multiRenderer.addSeriesRenderer(Qns5Renderer);
//__
            LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
            mChart = ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);
            chartContainer.addView(mChart);
        }
    }

    // method to check if child's id exist in DB
    public boolean CheckIfExist(String name){
        SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
        int count = -1;
        Cursor c = null;
        try {
            String query = "SELECT COUNT(*) FROM grade_child WHERE name = ?";
            c = db.rawQuery(query, new String[] {name});
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            return count > 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
        }
    }

    // retrieve all keys from child's id in database
    public Cursor getAllKeyedQuestion(String name, String ALL_KEYS[]) {
        SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
        String where = "name = '" + name + "'";
        Cursor c = 	db.query(true, "grade_child", ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public void OnClick_Export(View v) {
        ExportDatabaseCSVTask task = new ExportDatabaseCSVTask();
        task.execute();
    }

    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(Result.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Exporting database...");
            this.dialog.show();

        }

        protected Boolean doInBackground(final String... args) {

            File dbFile = getDatabasePath("newgrading.db");
            Log.v(TAG, "Db path is: " + dbFile);  //get the path of db

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            file = new File(exportDir, "" + childrenName + " Details.csv");
            try {

                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));


                // sqlite core query
                SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
                //Cursor curCSV=mydb.rawQuery("select * from " + TableName_ans,null);

                Cursor curCSV3 = db.rawQuery("SELECT * FROM child WHERE childName = ?", new String[]{childrenName});
                csvWrite.writeNext("Name", "Gender", "SessionNo", "Inspector", "Remarks", "Primary Diagnosis", "Secondary Diagnosis", "Activity", "No of adults", "No of children", "Status", "Venue");
                while (curCSV3.moveToNext()) {
                    String arrStr[] = {curCSV3.getString(3), curCSV3.getString(4),
                            curCSV3.getString(9), curCSV3.getString(5), curCSV3.getString(8), curCSV3.getString(0), curCSV3.getString(1), curCSV3.getString(2),
                            curCSV3.getString(6), curCSV3.getString(7), curCSV3.getString(10), curCSV3.getString(11)};
                    csvWrite.writeNext(arrStr);
                }

                csvWrite.writeNext();

                Cursor curCSV4 = db.rawQuery("SELECT * FROM interval WHERE childName = ?", new String[]{childrenName});
                csvWrite.writeNext("Name", "Child_id", "SessionNo", "Flag", "engagement", "Adult", "Interval", "Material", "Peers", "None Other", "Physical");
                while (curCSV4.moveToNext()) {
                    String arrStr[] = {curCSV4.getString(1), curCSV4.getString(2),
                            curCSV4.getString(10), curCSV4.getString(4), curCSV4.getString(3), curCSV4.getString(0), curCSV4.getString(5), curCSV4.getString(6),
                            curCSV4.getString(8), curCSV4.getString(7), curCSV4.getString(9)};
                    csvWrite.writeNext(arrStr);
                }

                csvWrite.writeNext();

                Cursor curCSV5 = db.rawQuery("SELECT * FROM session WHERE sessionChildName = ?", new String[]{childrenName});
                csvWrite.writeNext("Name", "Child_id", "Center", "Observer", "Session Count", "Session Status", "Date", "Start Time", "End Time", "No of flag", "No of interval ");
                while (curCSV5.moveToNext()) {
                    String arrStr[] = {curCSV5.getString(7), curCSV5.getString(1), curCSV5.getString(0), curCSV5.getString(6),
                            curCSV5.getString(8), curCSV5.getString(9), curCSV5.getString(2), curCSV5.getString(10),
                            curCSV5.getString(3), curCSV5.getString(4), curCSV5.getString(5)};
                    csvWrite.writeNext(arrStr);
                }

                csvWrite.writeNext();

                Cursor curCSV = db.rawQuery("SELECT * FROM grade_child WHERE name = ?", new String[]{childrenName});
                csvWrite.writeNext("Name", "Child_id", "Qns1", "Qns2", "Qns3", "Qns4", "Qns5");
                while (curCSV.moveToNext()) {
                    String arrStr[] = {curCSV.getString(1), curCSV.getString(0),
                            curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6)};
                    csvWrite.writeNext(arrStr);
                }

                csvWrite.writeNext();

                csvWrite.close();
                return true;
            } catch (IOException e) {
                Log.e("Grade", e.getMessage(), e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
                Toast.makeText(Result.this, "Export successful!", Toast.LENGTH_SHORT).show();
            } else {
                    Toast.makeText(Result.this, "Export failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
}