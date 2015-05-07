package database;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import Controller.Child;
import Controller.Grade_Child;
import Controller.Interval;
import Controller.Session;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class NewGradingDB  extends OrmLiteSqliteOpenHelper{
    private static final String TAG="GradeDatabaseHelper";
    private static final String DATABASE_NAME = "newgrading.db";
    private static final int DATABASE_VERSION = 6;
    private RuntimeExceptionDao<Grade_Child, String> personRuntimeDao=null;
    private RuntimeExceptionDao<Child, String> personRuntimeDaoChild=null;
    private RuntimeExceptionDao<Interval, String> personRuntimeDaoInterval=null;
    private RuntimeExceptionDao<Session, String> personRuntimeDaoSession=null;


    private SQLiteDatabase db;


    public NewGradingDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v(TAG, "DatabaseHelper constructor call");
    }


    public RuntimeExceptionDao<Grade_Child, String> getPersonDataDao() {

        Log.v(TAG, "getTimeDataDao call");

        if (personRuntimeDao == null) {
            personRuntimeDao = getRuntimeExceptionDao(Grade_Child.class);
        }
        return personRuntimeDao;
    }

    public int addPersonData(Grade_Child project)
    {
        Log.v(TAG, "addPersonData call");
        RuntimeExceptionDao<Grade_Child, String> dao = getPersonDataDao();
        int i = dao.create(project);
        return i;
    }

    public List<Grade_Child> GetDataPerson()
    {
        Log.v(TAG, "GetDataPerson call");
        RuntimeExceptionDao<Grade_Child, String> simpleDao = getPersonDataDao();
        List<Grade_Child> list = simpleDao.queryForAll();
        return list;
    }

    public void deleteAllPerson()
    {
        Log.v(TAG, "deleteAllPerson call");
        RuntimeExceptionDao<Grade_Child, String> dao = getPersonDataDao();
        List<Grade_Child> list = dao.queryForAll();
        dao.delete(list);
    }

    public UpdateBuilder<Grade_Child, String> updatePersonData() throws SQLException
    {
        RuntimeExceptionDao<Grade_Child, String> simpleDao = getPersonDataDao();
        UpdateBuilder<Grade_Child, String> updateBuilder = simpleDao.updateBuilder();
        return updateBuilder;
    }

    //*******************************************************************************8
//Child
    public RuntimeExceptionDao<Child, String> getPersonDataDaoChild() {

        Log.v(TAG, "getTimeDataDao call");

        if (personRuntimeDaoChild == null) {
            personRuntimeDaoChild = getRuntimeExceptionDao(Child.class);
        }
        return personRuntimeDaoChild;
    }

    public int addPersonDataChild(Child project)
    {
        Log.v(TAG, "addPersonData call");
        RuntimeExceptionDao<Child, String> dao = getPersonDataDaoChild();
        int i = dao.create(project);
        return i;
    }

    public List<Child> GetDataPersonChild()
    {
        Log.v(TAG, "GetDataPerson call");
        RuntimeExceptionDao<Child, String> simpleDao = getPersonDataDaoChild();
        List<Child> list = simpleDao.queryForAll();
        return list;
    }

    public void deleteAllPersonChild()
    {
        Log.v(TAG, "deleteAllPerson call");
        RuntimeExceptionDao<Child, String> dao = getPersonDataDaoChild();
        List<Child> list = dao.queryForAll();
        dao.delete(list);
    }

    public UpdateBuilder<Child, String> updatePersonDataChild() throws SQLException
    {
        RuntimeExceptionDao<Child, String> simpleDao = getPersonDataDaoChild();
        UpdateBuilder<Child, String> updateBuilder1 = simpleDao.updateBuilder();
        return updateBuilder1;
    }

//Interval
public RuntimeExceptionDao<Interval, String> getPersonDataDaoInterval() {

    Log.v(TAG, "getTimeDataDao call");

    if (personRuntimeDaoInterval == null) {
        personRuntimeDaoInterval = getRuntimeExceptionDao(Interval.class);
    }
    return personRuntimeDaoInterval;
}

    public int addPersonDataInterval(Interval project)
    {
        Log.v(TAG, "addPersonData call");
        RuntimeExceptionDao<Interval, String> dao = getPersonDataDaoInterval();
        int i = dao.create(project);
        return i;
    }

    public List<Interval> GetDataPersonInterval()
    {
        Log.v(TAG, "GetDataPerson call");
        RuntimeExceptionDao<Interval, String> simpleDao = getPersonDataDaoInterval();
        List<Interval> list = simpleDao.queryForAll();
        return list;
    }

    public void deleteAllPersonInterval()
    {
        Log.v(TAG, "deleteAllPerson call");
        RuntimeExceptionDao<Interval, String> dao = getPersonDataDaoInterval();
        List<Interval> list = dao.queryForAll();
        dao.delete(list);
    }

    public UpdateBuilder<Interval, String> updatePersonDataInterval() throws SQLException
    {
        RuntimeExceptionDao<Interval, String> simpleDao = getPersonDataDaoInterval();
        UpdateBuilder<Interval, String> updateBuilder1 = simpleDao.updateBuilder();
        return updateBuilder1;
    }

//Session
public RuntimeExceptionDao<Session, String> getPersonDataDaoSession() {

    Log.v(TAG, "getTimeDataDao call");

    if (personRuntimeDaoSession == null) {
        personRuntimeDaoSession = getRuntimeExceptionDao(Session.class);
    }
    return personRuntimeDaoSession;
}

    public int addPersonDataSession(Session project)
    {
        Log.v(TAG, "addPersonData call");
        RuntimeExceptionDao<Session, String> dao = getPersonDataDaoSession();
        int i = dao.create(project);
        return i;
    }

    public List<Session> GetDataPersonSession()
    {
        Log.v(TAG, "GetDataPerson call");
        RuntimeExceptionDao<Session, String> simpleDao = getPersonDataDaoSession();
        List<Session> list = simpleDao.queryForAll();
        return list;
    }

    public void deleteAllPersonSession()
    {
        Log.v(TAG, "deleteAllPerson call");
        RuntimeExceptionDao<Session, String> dao = getPersonDataDaoSession();
        List<Session> list = dao.queryForAll();
        dao.delete(list);
    }

    public UpdateBuilder<Session, String> updatePersonDataSession() throws SQLException
    {
        RuntimeExceptionDao<Session, String> simpleDao = getPersonDataDaoSession();
        UpdateBuilder<Session, String> updateBuilder1 = simpleDao.updateBuilder();
        return updateBuilder1;
    }
    //*******************************************************************************8
    @Override
    public void close() {
        super.close();
        Log.v(TAG, "close call");
        personRuntimeDao=null;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.v(TAG, "onCreate call");
            TableUtils.createTable(connectionSource, Grade_Child.class);
            TableUtils.createTable(connectionSource, Child.class);
            TableUtils.createTable(connectionSource, Interval.class);
            TableUtils.createTable(connectionSource, Session.class);
        } catch (SQLException e) {
            Log.e(NewGradingDB.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.v(TAG, "onUpgrade call");
            TableUtils.dropTable(connectionSource, Grade_Child.class, true);
            TableUtils.dropTable(connectionSource, Child.class, true);
            TableUtils.dropTable(connectionSource, Interval.class, true);
            TableUtils.dropTable(connectionSource, Session.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(NewGradingDB.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }
}
