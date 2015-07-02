package Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "schedule.db";
    private static final int SCHEMA = 1;
    public static final String TABLE = "faculties";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE);
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE);

        onCreate(sqLiteDatabase);
    }



    /*
    private void CreateTableCathedra(SQLiteDatabase sqLiteDatabase){
        String TABLE_NAME = "cathedra";
        String COLUMN_ID = "_id";
        String COLUMN_NAME = "name";

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT);");
    }

    private void CreateTableFaculty(SQLiteDatabase sqLiteDatabase){
        String TABLE_NAME = "faculties";
        String COLUMN_ID = "_id";
        String COLUMN_NAME = "name";

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT);");
    }

    private void CreateTableGroup(SQLiteDatabase sqLiteDatabase){
        String TABLE_NAME = "groups";
        String COLUMN_ID = "_id";
        String COLUMN_NAME = "name";

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT);");
    }

    private void CreateTableLesson(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("CREATE TABLE lessons ( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "number INT, " +
                "name TEXT, " +
                "day_of_week INT, " +
                "number_of_week INT, " +
                "cabinet TEXT, " +
                "group_id INT, " +
                "teacher_id INT, " +
                ");");
    }


    private void CreateTableTeacher(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("CREATE TABLE teachers ( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "faculty_id INT, " +
                "cathedra_id INT, " +
                ");");
    }
    */
}
