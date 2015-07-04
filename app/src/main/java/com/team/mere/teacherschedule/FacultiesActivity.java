package com.team.mere.teacherschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Helpers.JsonDownloadTask;
import Models.Faculty;


public class FacultiesActivity extends ActionBarActivity
        implements AdapterView.OnItemClickListener, JsonDownloadTask.OnJsonDownloadedListener{

    private ListView lvFaculties;
    private ArrayList<Faculty> faculties;
    private ArrayAdapter<Faculty> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculties);

        lvFaculties = (ListView) findViewById(R.id.lvFaculties);

        new JsonDownloadTask("http://ulstuschedule.azurewebsites.net/api/faculties", this)
                .execute();

        lvFaculties.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intentToFaculty = new Intent(this, FacultyActivity.class);
        intentToFaculty.putExtra("FacultyId", faculties.get(position).Id);
        startActivity(intentToFaculty);
    }

    @Override
    public void onJsonDownloaded(JSONArray data) {
        faculties = new ArrayList<>(data.length());
        for (int i = 0; i < data.length(); i++){
            Faculty faculty = Faculty.getFromJson(data.optJSONObject(i));
            faculties.add(faculty);
        }
        adapter = new ArrayAdapter<>(this, R.layout.list_item, faculties);
        lvFaculties.setAdapter(adapter);
    }
}