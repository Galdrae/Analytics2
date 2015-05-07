// ------------------------------------ DBADapter.java ---------------------------------------------

// TODO: Change the package to match your project.
package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class IntervalDBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "IntervalDBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // TODO: Setup your fields here:
    public static final String KEY_INTERVAL = "Interval";
    public static final String KEY_ENGAGEMENT = "Engagement";
    public static final String KEY_PHYSICAL = "PhysicalPrompt";
    public static final String KEY_ADULTS = "Adults";
    public static final String KEY_PEERS = "Peers";
    public static final String KEY_MATERIALS = "Materials";
    public static final String KEY_NONEOTHER = "NoneOther";
    public static final String KEY_CHILDID = "ChildID";
    public static final String KEY_CHILDNAME = "ChildName";
    public static final String KEY_SESSIONNO = "SessionNumber";
    public static final String KEY_FLAG = "Flag";

    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_CHILDNAME = 1;
    public static final int COL_AGE = 2;
    public static final int COL_GENDER = 3;
    public static final int COL_IMAGE = 4;
    public static final int COL_STATUS = 5;



    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_INTERVAL, KEY_ENGAGEMENT, KEY_PHYSICAL, KEY_ADULTS, KEY_PEERS, KEY_MATERIALS, KEY_NONEOTHER,
            KEY_CHILDID, KEY_CHILDNAME, KEY_SESSIONNO, KEY_FLAG};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MyIntervalDb";
    public static final String DATABASE_TABLE = "IntervalTable";
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
                    + KEY_INTERVAL + " text not null, "
                    + KEY_ENGAGEMENT + " text not null, "
                    + KEY_PHYSICAL + " text not null, "
                    + KEY_ADULTS + " text not null, "
                    + KEY_PEERS + " text not null, "
                    + KEY_MATERIALS + " text not null, "
                    + KEY_NONEOTHER + " text not null, "
                    + KEY_CHILDID + " integer not null, "
                    + KEY_CHILDNAME + " text not null, "
                    + KEY_SESSIONNO + " integer not null, "
                    + KEY_FLAG + " text "

                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public IntervalDBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public IntervalDBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(String interval, String engagement, String physicalPrompt, String adults, String peers, String materials,
                          String noneOther, long childId, String childName, long sessionNo, String flag) {
		/*
		 * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_INTERVAL, interval);
        initialValues.put(KEY_ENGAGEMENT, engagement);
        initialValues.put(KEY_PHYSICAL, physicalPrompt);
        initialValues.put(KEY_ADULTS, adults);
        initialValues.put(KEY_PEERS, peers);
        initialValues.put(KEY_MATERIALS, materials);
        initialValues.put(KEY_NONEOTHER, noneOther);
        initialValues.put(KEY_CHILDID, childId);
        initialValues.put(KEY_CHILDNAME, childName);
        initialValues.put(KEY_SESSIONNO, sessionNo);
        initialValues.put(KEY_FLAG, flag);


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
