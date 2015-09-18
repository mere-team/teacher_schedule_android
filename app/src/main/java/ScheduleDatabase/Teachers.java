package ScheduleDatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import Models.Cathedra;
import Models.Faculty;
import Models.Teacher;

/**
 * Created by maxmr on 9/18/2015.
 */
public class Teachers implements BaseColumns {
    private ScheduleDatabaseHelper _dbHelper;

    static final String TABLE_NAME = "teacher";
    static final String COLUMN_NAME_NAME = "teacher_name";
    static final String COLUMN_NAME_FACULTY_ID = "teacher_faculty_id";
    static final String COLUMN_NAME_CATHEDRA_ID = "teacher_cathedra_id";

    static final String SQL_CREATE_TEACHERS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME_NAME + " TEXT " +
                    COLUMN_NAME_FACULTY_ID + " INTEGER " +
                    COLUMN_NAME_CATHEDRA_ID + " INTEGER " +
                    ")";

    static final String SQL_DELETE_TEACHERS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public Teachers(ScheduleDatabaseHelper db){
        _dbHelper = db;
    }

    public Teacher get(long id){
        SQLiteDatabase db = _dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + ", " + Faculties.TABLE_NAME + ", " + Cathedries.TABLE_NAME +
                " WHERE " + _ID + " = " + id +
                " WHERE " + TABLE_NAME + "." + COLUMN_NAME_FACULTY_ID + " = " + Faculties.TABLE_NAME + "." + Faculties._ID +
                " WHERE " + TABLE_NAME + "." + COLUMN_NAME_CATHEDRA_ID + " = " + Cathedries.TABLE_NAME + "." + Cathedries._ID;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c == null) return null;
        c.moveToFirst();

        Teacher teacher = getTeacherFromDb(c);

        return teacher;
    }

    public List<Teacher> getAll(){
        List<Teacher> teachers = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c == null) return teachers;

        if (c.moveToFirst()){
            do {
                Teacher teacher = getTeacherFromDb(c);
                teachers.add(teacher);
            } while (c.moveToNext());
        }

        return teachers;
    }

    public long insert(Teacher teacher){
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, teacher.Name);
        values.put(COLUMN_NAME_CATHEDRA_ID, teacher.CathedraId);
        values.put(COLUMN_NAME_FACULTY_ID, teacher.FacultyId);

        long id = db.insert(TABLE_NAME, null, values);

        return id;
    }

    public void insert(List<Teacher> teachers){
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        for (Teacher teacher : teachers) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_NAME, teacher.Name);
            values.put(COLUMN_NAME_CATHEDRA_ID, teacher.CathedraId);
            values.put(COLUMN_NAME_FACULTY_ID, teacher.FacultyId);

            db.insert(TABLE_NAME, null, values);
        }
    }

    public void delete(Teacher teacher){
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_NAME +
                " WHERE " + _ID + " = " + teacher.Id;

        db.execSQL(deleteQuery);
    }

    public void deleteAll(){
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_NAME;

        db.execSQL(deleteQuery);
    }

    static Teacher getTeacherFromDb(Cursor c){
        Teacher teacher = new Teacher();
        teacher.Id = c.getInt(c.getColumnIndex(_ID));
        teacher.Name = c.getString(c.getColumnIndex(COLUMN_NAME_NAME));
        teacher.FacultyId = c.getInt(c.getColumnIndex(COLUMN_NAME_FACULTY_ID));
        teacher.Faculty = new Faculty();
        teacher.Faculty.Id = teacher.FacultyId;
        teacher.Faculty.Name = c.getString(c.getColumnIndex(Faculties.COLUMN_NAME_NAME));
        teacher.CathedraId = c.getInt(c.getColumnIndex(COLUMN_NAME_CATHEDRA_ID));
        teacher.Cathedra = new Cathedra();
        teacher.Cathedra.Id = teacher.CathedraId;
        teacher.Cathedra.Name = c.getString(c.getColumnIndex(Cathedries.COLUMN_NAME_NAME));

        return teacher;
    }
}
