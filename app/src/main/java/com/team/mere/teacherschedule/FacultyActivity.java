package com.team.mere.teacherschedule;

import android.content.Intent;
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
import Helpers.JsonDownloadTask;
import Helpers.JsonHelper;
import Helpers.LoadingIndicator;
import Models.Cathedra;


public class FacultyActivity extends AppCompatActivity
        implements OnItemClickListener,
        JsonDownloadTask.OnJsonDownloadedListener {

    private ListView lvFacultyCathedries;
    private ArrayList<Cathedra> facultyCathedries;
    private JsonHelper helper;
    private int facultyId;

    private LoadingIndicator _loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        setTitle(getIntent().getExtras().getString("FacultyName"));

        lvFacultyCathedries = (ListView) findViewById(R.id.lvFacultyCathedries);
        lvFacultyCathedries.setOnItemClickListener(this);

        facultyId = getIntent().getExtras().getInt("FacultyId");
        String url = "http://ulstuschedule.azurewebsites.net/api/faculties?facultyId=" + facultyId;

        _loadingIndicator = new LoadingIndicator(this, getResources().getString(R.string.faculty_loading));
        _loadingIndicator.show();

        helper = new JsonHelper(this);
        helper.DownloadJson(url, this);
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
        Intent intent = new Intent(this, CathedriesOfFacultyActivity.class);
        intent.putExtra("CathedraId", facultyCathedries.get(position).Id);
        intent.putExtra("CathedraName", facultyCathedries.get(position).Name);
        intent.putExtra("FacultyId", facultyId);
        startActivity(intent);
    }

    @Override
    public void onJsonDownloaded(JSONArray data) {
        _loadingIndicator.close();

        try {
            facultyCathedries = helper.GetListOfModels(data, new Cathedra());
        } catch (JsonDownloadException e) {
            e.printStackTrace();
            return;
        }
        ArrayAdapter<Cathedra> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, facultyCathedries);
        lvFacultyCathedries.setAdapter(adapter);
    }
}
