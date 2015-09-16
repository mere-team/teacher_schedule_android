package com.team.mere.teacherschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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


public class CathedraActivity extends AppCompatActivity
        implements OnJsonDownloadedListener, OnItemClickListener{

    private ListView lvCathedraTeachers;
    private ArrayList<Teacher> cathedraTeachers;

    private JsonHelper helper;
    private LoadingIndicator _loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cathedra);
        setTitle(getIntent().getExtras().getString("CathedraName"));
        lvCathedraTeachers = (ListView) findViewById(R.id.lvCathedraTeachers);
        lvCathedraTeachers.setOnItemClickListener(this);

        int cathedraId = getIntent().getExtras().getInt("CathedraId");
        String url = "http://ulstuschedule.azurewebsites.net/api/cathedries/" + cathedraId;

        _loadingIndicator = new LoadingIndicator(this, getResources().getString(R.string.cathedra_loading));
        _loadingIndicator.show();

        helper = new JsonHelper(this);
        helper.DownloadJson(url, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cathedra, menu);
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
            cathedraTeachers = helper.GetListOfModels(data, new Teacher());
        } catch (JsonDownloadException e) {
            e.printStackTrace();
            return;
        }
        ArrayAdapter<Teacher> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, cathedraTeachers);
        lvCathedraTeachers.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), TeacherActivity.class);
        intent.putExtra("TeacherId", cathedraTeachers.get(position).Id);
        intent.putExtra("TeacherName", cathedraTeachers.get(position).Name);
        startActivity(intent);
    }
}
