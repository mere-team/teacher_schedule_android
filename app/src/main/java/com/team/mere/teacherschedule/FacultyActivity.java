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

import Helpers.JsonDownloadTask;
import Helpers.JsonHelper;
import Models.Cathedra;


public class FacultyActivity extends AppCompatActivity
        implements OnItemClickListener,
        JsonDownloadTask.OnJsonDownloadedListener {

    private ListView lvFacultyCathedries;
    private ArrayList<Cathedra> facultyCathedries;
    private ArrayAdapter<Cathedra> adapter;
    private JsonHelper helper;
    private int facultyId;
    String _url;

    private static final int JSON_LOADER_ID = 1;

    private static boolean FileIsExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        setTitle(getIntent().getExtras().getString("FacultyName"));

        lvFacultyCathedries = (ListView) findViewById(R.id.lvFacultyCathedries);
        lvFacultyCathedries.setOnItemClickListener(this);

        facultyId = getIntent().getExtras().getInt("FacultyId");
        _url = "http://ulstuschedule.azurewebsites.net/api/faculties?facultyId=" + facultyId;
        String path = "Cathedries" + facultyId + ".json";

        helper = new JsonHelper(path, this);
        helper.DownloadJson(_url, this);

        //getLoaderManager().initLoader(JSON_LOADER_ID, null, this);
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
        Intent intent = new Intent(this, CathedraOfFacultyActivity.class);
        intent.putExtra("CathedraId", facultyCathedries.get(position).Id);
        intent.putExtra("CathedraName", facultyCathedries.get(position).Name);
        intent.putExtra("FacultyId", facultyId);
        startActivity(intent);
    }

    @Override
    public void onJsonDownloaded(JSONArray data) {
        if(!FileIsExist && data != null) {
            helper.SaveJsonToFile(data);
            FileIsExist = true;
        }

        try {
            facultyCathedries = helper.GetListOfModels(data, new Cathedra());
        } catch (JsonHelper.JsonDownloadException e) {
            e.printStackTrace();
            return;
        }
        adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, facultyCathedries);
        lvFacultyCathedries.setAdapter(adapter);
    }

//    @Override
//    public Loader<JSONArray> onCreateLoader(int i, Bundle bundle) {
//        AsyncJsonLoader loader = new AsyncJsonLoader(this, _url);
//        loader.loadInBackground();
//        return loader;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<JSONArray> loader, JSONArray jsonArray) {
//        if(!FileIsExist) {
//            helper.SaveJsonToFile(jsonArray);
//            FileIsExist = true;
//        }
//
//        facultyCathedries = helper.GetListOfModels(jsonArray, new Cathedra());
//        adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, facultyCathedries);
//        lvFacultyCathedries.setAdapter(adapter);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<JSONArray> loader) {
//        adapter.clear();
//    }
}
