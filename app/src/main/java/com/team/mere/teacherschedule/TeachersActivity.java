package com.team.mere.teacherschedule;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;

import Helpers.JsonDownloadException;
import Helpers.JsonDownloadTask.OnJsonDownloadedListener;
import Helpers.JsonHelper;
import Helpers.LoadingIndicator;
import Models.Teacher;


public class TeachersActivity extends AppCompatActivity
        implements OnJsonDownloadedListener, OnItemClickListener {

    private ListView lvTeachers;
    private ArrayList<Teacher> teachers;

    private String url = "http://ulstuschedule.azurewebsites.net/api/teachers";

    private JsonHelper helper;
    private LoadingIndicator _loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers);
        lvTeachers = (ListView) findViewById(R.id.lvTeachers);
        lvTeachers.setOnItemClickListener(this);

        _loadingIndicator = new LoadingIndicator(this, getResources().getString(R.string.teachers_loading));
        _loadingIndicator.show();

        helper = new JsonHelper(this);
        helper.DownloadJson(url, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teachers, menu);
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
    public void onJsonDownloaded(JSONArray data) {
        _loadingIndicator.close();

        try {
            teachers = helper.GetListOfModels(data, new Teacher());
        } catch (JsonDownloadException e) {
            e.printStackTrace();
            return;
        }
        ArrayAdapter<Teacher> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, teachers);
        lvTeachers.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), TeacherActivity.class);
        intent.putExtra("TeacherId", teachers.get(position).Id);
        intent.putExtra("TeacherName", teachers.get(position).Name);
        startActivity(intent);
    }
}
