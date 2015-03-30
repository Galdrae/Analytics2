package com.example.deosfriend.apptest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Aw on 24/3/2015.
 */
public class gradingDB{
    private static final String TAG = "gradingDB";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_Qns1 = "qns1";
    public static final String KEY_Qns2 = "qns2";
    public static final String KEY_Qns3 = "qns3";
    public static final String KEY_Qns4 = "qns4";
    public static final String KEY_Qns5 = "qns5";

    public static final int COL_NAME = 1;
    public static final int COL_Qns1 = 2;
    public static final int COL_Qns2 = 3;
    public static final int COL_Qns3 = 4;
    public static final int COL_Qns4 = 5;
    public static final int COL_Qns5 = 6;

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_NAME, KEY_Qns1, KEY_Qns2, KEY_Qns3, KEY_Qns4, KEY_Qns5};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "gradingDb";
    public static final String DATABASE_TABLE = "grading";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 5;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "

			/*
			 * CHANGE 2:
			 */
                    // TODO: Place your fields here!
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
                    + KEY_NAME + " text not null,"
                    + KEY_Qns1 + " text not null, "
                    + KEY_Qns2 + " text not null, "
                    + KEY_Qns3 + " text not null, "
                    + KEY_Qns4 + " text not null, "
                    + KEY_Qns5 + " text not null"


                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public gradingDB(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public gradingDB open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(String childName , String qns1,  String qns2, String qns3,  String qns4, String qns5) {
		/*
		 * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, childName);
        initialValues.put(KEY_Qns1, qns1);
        initialValues.put(KEY_Qns2, qns2);
        initialValues.put(KEY_Qns3, qns3);
        initialValues.put(KEY_Qns4, qns4);
        initialValues.put(KEY_Qns5, qns5);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public boolean deleteRecord(Long childGradingId){
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + childGradingId, null) > 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, String childName, String qns1,  String qns2, String qns3,  String qns4, String qns5) {
        String where = KEY_ROWID + "=" + rowId;

		/*
		 * CHANGE 4:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME, childName);
        newValues.put(KEY_Qns1, qns1);
        newValues.put(KEY_Qns2, qns2);
        newValues.put(KEY_Qns3, qns3);
        newValues.put(KEY_Qns4, qns4);
        newValues.put(KEY_Qns5, qns5);


        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    public void updateRecord(long rowID, String qns1, String qns2, String qns3, String qns4, String qns5)
    {
        ContentValues newValues = new ContentValues();
        //newValues.put(KEY_NAME, childName);
        newValues.put(KEY_Qns1, qns1);
        newValues.put(KEY_Qns2, qns2);
        newValues.put(KEY_Qns3, qns3);
        newValues.put(KEY_Qns4, qns4);
        newValues.put(KEY_Qns5, qns5);
        db.update(DATABASE_TABLE, newValues, KEY_ROWID + "=" + rowID, null);

    }



    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
