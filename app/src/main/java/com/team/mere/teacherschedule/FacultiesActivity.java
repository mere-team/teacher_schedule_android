package com.team.mere.teacherschedule;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Helpers.JsonDownloadTask;


public class FacultiesActivity extends ActionBarActivity
        implements AdapterView.OnItemClickListener, JsonDownloadTask.OnJsonDownloadedListener{

    private ListView lvFaculties;
    private Intent intentToFaculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculties);

        lvFaculties = (ListView) findViewById(R.id.lvFaculties);

        new JsonDownloadTask("http://ulstuschedule.azurewebsites.net/api/faculties", this).execute();

        intentToFaculty = new Intent(this, FacultyActivity.class);
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
        TextView tv = (TextView) view;
        if (tv != null || tv.getText() != "( Отсутствует )") {
            intentToFaculty.putExtra("faculty", tv.getText());
            startActivity(intentToFaculty);
        }
    }

    @Override
    public void onJsonDownloaded(JSONArray data) {
        ArrayList<String> faculties = new ArrayList<>();
        for (int i = 0; i < data.length(); i++){
            try {
                JSONObject faculty = data.optJSONObject(i);
                faculties.add(faculty.getString("Name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, faculties);
        lvFaculties.setAdapter(adapter);
    }
}
