package com.team.mere.teacherschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;

import Helpers.JsonDownloadTask.OnJsonDownloadedListener;
import Helpers.JsonHelper;
import Models.Teacher;


public class CathedraOfFacultyActivity extends ActionBarActivity
        implements OnItemClickListener, OnJsonDownloadedListener{

    private ListView lvCathedraOfFacultyTeachers;
    private ArrayList<Teacher> facultyCathedraTeachers;
    private ArrayAdapter<Teacher> adapter;
    private JsonHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cathedra_of_faculty);
        setTitle(getIntent().getExtras().getString("CathedraName"));
        lvCathedraOfFacultyTeachers = (ListView) findViewById(R.id.lvCathedraOfFacultyTeachers);
        lvCathedraOfFacultyTeachers.setOnItemClickListener(this);

        int facultyId = getIntent().getExtras().getInt("FacultyId");
        int cathedraId = getIntent().getExtras().getInt("CathedraId");
        String url = "http://ulstuschedule.azurewebsites.net/api/faculties?facultyId=" + facultyId
                + "&cathedraId=" + cathedraId;
        String path = "CathedraOfFaculty" + facultyId + ".json";

        helper = new JsonHelper(path, getApplicationContext());
        helper.DownloadJson(url, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        Intent intent = new Intent(this, TeacherActivity.class);
        intent.putExtra("TeacherId", facultyCathedraTeachers.get(position).Id);
        intent.putExtra("TeacherName", facultyCathedraTeachers.get(position).Name);
        startActivity(intent);
    }

    @Override
    public void onJsonDownloaded(JSONArray data) {
        facultyCathedraTeachers = helper.GetListOfModels(data, new Teacher());
        adapter = new ArrayAdapter<>(this, R.layout.list_item, facultyCathedraTeachers);
        lvCathedraOfFacultyTeachers.setAdapter(adapter);
    }
}