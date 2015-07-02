package com.team.mere.teacherschedule;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import Helpers.DatabaseHelper;
import Helpers.JsonDownloadTask;
import Models.Faculty;


public class MainActivity extends ActionBarActivity
        implements JsonDownloadTask.OnJsonDownloadedListener{

    private ListView lvSections;
    private Button load_button;
    private SQLiteDatabase sqLiteDatabase;
    private DatabaseHelper databaseHelper;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvSections = (ListView) findViewById(R.id.lvSections);
        String[] sections = new String[] {
                getResources().getString(R.string.list_faculties),
                getResources().getString(R.string.list_cathedries),
                getString(R.string.list_teachers)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, sections);
        lvSections.setAdapter(adapter);

        lvSections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String strText = textView.getText().toString();
                if (strText == getResources().getString(R.string.list_faculties)) {
                    startActivity(new Intent(getApplicationContext(), FacultiesActivity.class));
                } else if (strText == getResources().getString(R.string.list_cathedries)) {
                    startActivity(new Intent(getApplicationContext(), CathedriesActivity.class));
                } else if (strText == getResources().getString(R.string.list_teachers)) {
                    startActivity(new Intent(getApplicationContext(), TeachersActivity.class));
                }
            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());

        load_button = (Button)findViewById(R.id.load_button);
        View.OnClickListener loading = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonDownloadTask("http://ulstuschedule.azurewebsites.net/api/faculties", MainActivity.this).execute();
            }
        };
        load_button.setOnClickListener(loading);
    }

    @Override
    public void onJsonDownloaded(JSONArray data) {

        sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            for (int i = 0; i < data.length(); i++) {
                Faculty faculty = Faculty.getFromJson(data.optJSONObject(i));
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.COLUMN_NAME, faculty.Name);
                cv.put(DatabaseHelper.COLUMN_ID, faculty.Id);

                sqLiteDatabase.insert(DatabaseHelper.TABLE, null, cv);
            }

            toast = Toast.makeText(getApplicationContext(), "Successful" , Toast.LENGTH_SHORT);
            toast.show();
        }
        catch (Exception ex)
        {
            toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
            toast.show();
        }
        finally {
            sqLiteDatabase.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
