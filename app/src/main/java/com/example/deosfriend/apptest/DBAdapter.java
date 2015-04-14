// ------------------------------------ DBADapter.java ---------------------------------------------

// TODO: Change the package to match your project.
package com.example.deosfriend.apptest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DBAdapter {

	/////////////////////////////////////////////////////////////////////
	//	Constants & Data
	/////////////////////////////////////////////////////////////////////
	// For logging:
	private static final String TAG = "DBAdapter";
	
	// DB Fields
	public static final String KEY_ROWID = "_id";
	public static final int COL_ROWID = 0;
	/*
	 * CHANGE 1:
	 */
	// TODO: Setup your fields here:
	public static final String KEY_CHILDNAME = "ChildName";
	public static final String KEY_PRIDI = "PrimaryDiagnosis";
    public static final String KEY_SECDI = "SecondaryDiagnosis";
    public static final String KEY_REMARKS = "Remarks";
    public static final String KEY_INSPECTOR = "Inspector";
    public static final String KEY_VENUE = "Venue";
    public static final String KEY_ACTIVITY = "Activity";
    public static final String KEY_NOADULTS = "NoOfAdults";
    public static final String KEY_NOCHILDREN = "NoOfChildren";
    public static final String KEY_GENDER = "Gender";
    public static final String KEY_IMAGE = "Icon";
    public static final String KEY_STATUS = "Status";
    public static final String KEY_SESSIONNO = "SessionNumber";
	
	// TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
	public static final int COL_CHILDNAME = 1;
	public static final int COL_AGE = 2;
    public static final int COL_GENDER = 3;
    public static final int COL_IMAGE = 4;
    public static final int COL_STATUS = 5;


	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_CHILDNAME, KEY_GENDER, KEY_PRIDI, KEY_SECDI, KEY_REMARKS,
            KEY_INSPECTOR, KEY_VENUE, KEY_ACTIVITY, KEY_NOADULTS, KEY_NOCHILDREN, KEY_IMAGE, KEY_STATUS, KEY_SESSIONNO};
	
	// DB info: it's name, and the table we are using (just one).
	public static final String DATABASE_NAME = "MyDb";
	public static final String DATABASE_TABLE = "ChildTable";
	// Track DB version if a new version of your app changes the format.
	public static final int DATABASE_VERSION = 11;
	
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
                    + KEY_CHILDNAME + " text not null, "
                    + KEY_GENDER + " text not null, "
                    + KEY_PRIDI + " text not null, "
                    + KEY_SECDI + " text not null, "
                    + KEY_REMARKS + " text not null, "
                    + KEY_INSPECTOR + " text not null, "
                    + KEY_VENUE + " text not null, "
                    + KEY_ACTIVITY + " text not null, "
                    + KEY_NOADULTS + " text not null, "
                    + KEY_NOCHILDREN + " text not null, "
                    + KEY_IMAGE + " integer not null, "
                    + KEY_STATUS + " text not null, "
                    + KEY_SESSIONNO + " integer "

			
			// Rest  of creation:
			+ ");";
	
	// Context of application who uses us.
	private final Context context;
	
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;

	/////////////////////////////////////////////////////////////////////
	//	Public methods:
	/////////////////////////////////////////////////////////////////////
	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}
	
	// Open the database connection.
	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}
	
	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}
	
	// Add a new set of values to the database.
	public long insertRow(String childName , String gender, String priDi, String secDi, String remarks,
                          String inspector, String venue, String activity, String noAdults, String noChildren, int icon, String status, int sessionNo) {
		/*
		 * CHANGE 3:
		 */		
		// TODO: Update data in the row with new fields.
		// TODO: Also change the function's arguments to be what you need!
		// Create row's data:
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CHILDNAME, childName);
        initialValues.put(KEY_GENDER, gender);
        initialValues.put(KEY_PRIDI, priDi);
        initialValues.put(KEY_SECDI, secDi);
        initialValues.put(KEY_REMARKS, remarks);
        initialValues.put(KEY_INSPECTOR, inspector);
        initialValues.put(KEY_VENUE, venue);
        initialValues.put(KEY_ACTIVITY, activity);
        initialValues.put(KEY_NOADULTS, noAdults);
        initialValues.put(KEY_NOCHILDREN, noChildren);
        initialValues.put(KEY_IMAGE, icon);
        initialValues.put(KEY_STATUS, status);
        initialValues.put(KEY_SESSIONNO, sessionNo);

		// Insert it into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
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

    // Retrieve not observed or incomplete status in the database.
    public Cursor getAllRowsIncomplete(String status, String status2, String status3) {
        String where = KEY_STATUS + "=" + "'"+status+"'" + " OR " + KEY_STATUS + "=" + "'"+status2+"'" + " OR " + KEY_STATUS + "=" + "'"+status3+"'";
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Retrieve completed status in the database.
    public Cursor getAllRowsCompleted(String status) {
        String where = KEY_STATUS + "=" + "'"+status+"'";
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
	public boolean updateRow(long rowId, String status) {
        String where = KEY_ROWID + "=" + rowId;

		/*
		 * CHANGE 4:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_STATUS, status);



        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    public boolean updateSessionNo(long rowId, String sessionStatus, int sessionNo) {
        String where = KEY_ROWID + "=" + rowId;

		/*
		 * CHANGE 4:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_STATUS, sessionStatus);
        newValues.put(KEY_SESSIONNO, sessionNo);


        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
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
