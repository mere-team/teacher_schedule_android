package com.team.mere.teacherschedule;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;

import Helpers.JsonDownloadTask;
import Helpers.JsonDownloadTask.OnJsonDownloadedListener;
import Models.Teacher;


public class FacultyActivity extends ActionBarActivity implements OnJsonDownloadedListener, OnItemClickListener{

    private ListView lvFacultyTeachers;
    private ArrayList<Teacher> facultyTeachers;
    private ArrayAdapter<Teacher> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        lvFacultyTeachers = (ListView) findViewById(R.id.lvFacultyTeachers);

        int facultyId = getIntent().getExtras().getInt("FacultyId");
        new JsonDownloadTask("http://ulstuschedule.azurewebsites.net/api/faculties/" + facultyId, this)
                .execute();

        lvFacultyTeachers.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_faculty, menu);
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
        intent.putExtra("TeacherId", facultyTeachers.get(position).Id);
        startActivity(intent);
    }

    @Override
    public void onJsonDownloaded(JSONArray data) {
        facultyTeachers = new ArrayList<>(data.length());
        for (int i = 0; i < data.length(); i++){
            Teacher teacher = Teacher.getFromJson(data.optJSONObject(i));
            facultyTeachers.add(teacher);
        }
        adapter = new ArrayAdapter<>(this, R.layout.list_item, facultyTeachers);
        lvFacultyTeachers.setAdapter(adapter);
    }
}
