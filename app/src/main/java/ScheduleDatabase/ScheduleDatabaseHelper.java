package ScheduleDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import Models.Teacher;

/**
 * Created by maxmr on 9/17/2015.
 */
public class ScheduleDatabaseHelper extends SQLiteOpenHelper{

    private static final String LOG = "ScheduleDatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ScheduleDatabase.db";

    private Cathedries _cathedries;
    private Faculties _faculties;
    private Groups _groups;
    private Lessons _lessons;
    private Teachers _teachers;

    public ScheduleDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        _cathedries = new Cathedries(this);
        _faculties = new Faculties(this);
        _groups = new Groups(this);
        _teachers = new Teachers(this);
        _lessons = new Lessons();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Cathedries.SQL_CREATE_CATHEDRIES);
        db.execSQL(Faculties.SQL_CREATE_FACULTIES);
        db.execSQL(Groups.SQL_CREATE_GROUPS);
        db.execSQL(Teachers.SQL_CREATE_TEACHERS);
        db.execSQL(Lessons.SQL_CREATE_LESSONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Cathedries.SQL_DELETE_CATHEDRIES);
        db.execSQL(Faculties.SQL_DELETE_FACULTIES);
        db.execSQL(Groups.SQL_DELETE_GROUPS);
        db.execSQL(Teachers.SQL_DELETE_TEACHERS);
        db.execSQL(Lessons.SQL_DELETE_LESSONS);

        onCreate(db);
    }

    public void closeDb(){
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public Cathedries getCathedries(){
        return _cathedries;
    }

    public Faculties getFaculties(){
        return _faculties;
    }



}
