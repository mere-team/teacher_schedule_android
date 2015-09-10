package com.team.mere.teacherschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.prefs.AbstractPreferences;

import Helpers.JsonDownloadTask;
import Helpers.JsonHelper;
import Models.Faculty;


public class FacultiesActivity extends AppCompatActivity
        implements JsonDownloadTask.OnJsonDownloadedListener, AdapterView.OnItemClickListener {

    private ListView lvFaculties;
    private ArrayList<Faculty> faculties;
    private ArrayAdapter<Faculty> adapter;

    private ArrayList<TextView> facultiesViews;
    private ArrayAdapter<TextView> viewsAdapter;


    private String url = "http://ulstuschedule.azurewebsites.net/api/faculties";
    private String path = "faculties.json";
    private JsonHelper helper;

    private static boolean FileIsExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculties);
        lvFaculties = (ListView) findViewById(R.id.lvFaculties);
        lvFaculties.setOnItemClickListener(this);

        //helper = new JsonHelper(path, getApplicationContext());
        //helper.DownloadJson(url, this);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, FacultyActivity.class);
        intent.putExtra("FacultyId", faculties.get(position).Id);
        intent.putExtra("FacultyName", faculties.get(position).Name);
        startActivity(intent);
    }

    @Override
    public void onJsonDownloaded(JSONArray data){
        if(!FileIsExist) {
            helper.SaveJsonToFile(data);
            FileIsExist = true;
        }
        faculties = helper.GetListOfModels(data, new Faculty());
        adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, faculties);

        lvFaculties.setAdapter(adapter);
    }
}