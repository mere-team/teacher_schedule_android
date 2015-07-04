package com.team.mere.teacherschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import Helpers.JsonDownloadTask;
import Helpers.JsonHelper;
import Models.Faculty;


public class FacultiesActivity extends ActionBarActivity
        implements JsonDownloadTask.OnJsonDownloadedListener, AdapterView.OnItemClickListener {

    private ListView lvFaculties;
    private ArrayList<Faculty> faculties;
    private ArrayAdapter<Faculty> adapter;

    private String url = "http://ulstuschedule.azurewebsites.net/api/faculties";
    private String path = "faculties.json";
    private JsonHelper helper;

    private static boolean FileIsExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculties);
        lvFaculties = (ListView) findViewById(R.id.lvFaculties);

        helper = new JsonHelper(path, getApplicationContext());

        try {
            if (FileIsExist) {
                onJsonDownloaded(helper.GetDataFromFile());

            } else if(helper.IsNetworkConnected()) {
                Toast toast = Toast.makeText(getApplicationContext(), "loading...", Toast.LENGTH_LONG);
                toast.show();
                new JsonDownloadTask(url, this)
                        .execute();
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "Not connected to net", Toast.LENGTH_LONG);
                toast.show();
            }

        }
        catch (Exception ex)
        {
            Toast toast = Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG);
            toast.show();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, TeacherActivity.class);
        intent.putExtra("FacultyId", faculties.get(position).Id);
        startActivity(intent);
    }

    @Override
    public void onJsonDownloaded(JSONArray data){
        if(!FileIsExist) {
            helper.SaveJsonToFile(data);
            FileIsExist = true;
        }
        faculties = helper.GetListOfModels(data, new Faculty());
        adapter = new ArrayAdapter<>(this, R.layout.list_item, faculties);
        lvFaculties.setAdapter(adapter);
    }
}