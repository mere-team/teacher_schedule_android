package com.team.mere.teacherschedule;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Helpers.DatabaseHelper;
import Helpers.JsonDownloadTask;
import Models.Faculty;


public class FacultiesActivity extends ActionBarActivity {

    private ListView lvFaculties;
    private ArrayList<Faculty> faculties;
    private ArrayAdapter<Faculty> adapter;

    private SQLiteDatabase sqLiteDatabase;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculties);

        lvFaculties = (ListView) findViewById(R.id.lvFaculties);

        faculties = new ArrayList<>();

        databaseHelper = new DatabaseHelper(getApplicationContext());
        sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor facultiesCursor = sqLiteDatabase.rawQuery("select * from " + DatabaseHelper.TABLE, null);

        try {
            while (facultiesCursor.moveToNext()) {
                int id = facultiesCursor.getInt(facultiesCursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String name = facultiesCursor.getString(facultiesCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                faculties.add(new Faculty(id, name));
            }
            adapter = new ArrayAdapter<>(this, R.layout.list_item, faculties);
            lvFaculties.setAdapter(adapter);
        }
        catch (Exception ex) {
        }
        finally {
            facultiesCursor.close();
            sqLiteDatabase.close();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_faculties, menu);
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