// ------------------------------------ DBADapter.java ---------------------------------------------

// TODO: Change the package to match your project.
package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class SessionDBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "SessionDBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // TODO: Setup your fields here:
    public static final String KEY_CHILDID = "ChildID";
    public static final String KEY_CENTERID = "Venue";
    public static final String KEY_OBSERVER = "Inspector";
    public static final String KEY_SESSIONCOUNT = "SessionCount";
    public static final String KEY_DATE = "Date";
    public static final String KEY_STARTTIME = "StartTime";
    public static final String KEY_ENDTIME = "EndTime";
    public static final String KEY_NOINTERVALS = "NoOfIntervals";
    public static final String KEY_NOFLAGS = "NoOfFlags";
    public static final String KEY_SESSIONCHILDNAME = "SessionChildName";
    public static final String KEY_SESSIONSTATUS = "SessionStatus";

    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_SESSIONCHILDNAME = 1;
    public static final int COL_SESSIONSTATUS = 2;
    public static final int COL_SESSIONCOUNT = 3;
    public static final int COL_DATETIMETAKEN = 4;




    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_CHILDID, KEY_CENTERID, KEY_OBSERVER, KEY_SESSIONCOUNT,
            KEY_DATE, KEY_STARTTIME, KEY_ENDTIME, KEY_NOINTERVALS, KEY_NOFLAGS, KEY_SESSIONCHILDNAME, KEY_SESSIONSTATUS};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MySessionDb";
    public static final String DATABASE_TABLE = "ChildSessionTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 3;

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
                    + KEY_CHILDID + " text not null, "
                    + KEY_CENTERID + " text not null, "
                    + KEY_OBSERVER + " text not null, "
                    + KEY_SESSIONCOUNT + " text not null, "
                    + KEY_DATE + " text not null, "
                    + KEY_STARTTIME + " text not null, "
                    + KEY_ENDTIME + " text not null, "
                    + KEY_NOINTERVALS + " text not null, "
                    + KEY_NOFLAGS + " text not null, "
                    + KEY_SESSIONCHILDNAME + " text not null, "
                    + KEY_SESSIONSTATUS + " text "


                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public SessionDBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public SessionDBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(String childID, String centerID, String observer, String sessionCount,
                          String date, String startTime, String endTime, String noOfIntervals, String noOfFlags, String sessionChildName, String sessionStatus) {
		/*
		 * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CHILDID, childID);
        initialValues.put(KEY_CENTERID, centerID);
        initialValues.put(KEY_OBSERVER, observer);
        initialValues.put(KEY_SESSIONCOUNT, sessionCount);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_STARTTIME, startTime);
        initialValues.put(KEY_ENDTIME, endTime);
        initialValues.put(KEY_NOINTERVALS, noOfIntervals);
        initialValues.put(KEY_NOFLAGS, noOfFlags);
        initialValues.put(KEY_SESSIONCHILDNAME, sessionChildName);
        initialValues.put(KEY_SESSIONSTATUS, sessionStatus);

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

    // Get a specific row (by rowId)
    public Cursor getChildSession(String childID) {
        String where = KEY_CHILDID + "=" + "'"+childID+"'";
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // get last row
    public Cursor getLastRow() {
        Cursor c = 	db.query(DATABASE_TABLE, ALL_KEYS,null, null, null, null, null);
        if (c != null) {
            c.moveToLast();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateInCompleteSession(long rowId, String endTime, String noOfIntervals, String noOfFlags, String sessionStatus) {
        String where = KEY_ROWID + "=" + rowId;

		/*
		 * CHANGE 4:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_ENDTIME, endTime);
        newValues.put(KEY_NOINTERVALS, noOfIntervals);
        newValues.put(KEY_NOFLAGS, noOfFlags);
        newValues.put(KEY_SESSIONSTATUS, sessionStatus);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }



    public boolean checkExist(String childID) throws SQLException {
        int count = -1;
        Cursor c = null;
        try {
            String query = "SELECT COUNT(*) FROM "
                    + DATABASE_TABLE + " WHERE " + KEY_CHILDID + " = ?";
            c = db.rawQuery(query, new String[] {childID});
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
