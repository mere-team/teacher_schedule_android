package com.team.mere.teacherschedule;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.List;

import Helpers.AsyncTaskJsonLoader;
import Helpers.JsonDownloadException;
import Helpers.JsonDownloadTask;
import Helpers.JsonHelper;
import Helpers.LoadingIndicator;
import Models.Faculty;
import ScheduleDatabase.ScheduleDatabaseHelper;


public class FacultiesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<JSONArray>,
        AdapterView.OnItemClickListener,
        JsonDownloadTask.OnJsonDownloadedListener {

    private final String LOG_TAG = "FacultiesActivity";
    private final int JSON_LOADER_ID = 1;

    private ListView lvFaculties;
    private List<Faculty> faculties;
    private ArrayAdapter<Faculty> adapter;

    private static final String _url = "http://ulstuschedule.azurewebsites.net/api/faculties";
    private JsonHelper helper;
    private LoadingIndicator _loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculties);
        lvFaculties = (ListView) findViewById(R.id.lvFaculties);
        lvFaculties.setOnItemClickListener(this);

        _loadingIndicator = new LoadingIndicator(this, getResources().getString(R.string.faculties_loading));
        _loadingIndicator.show();

        ScheduleDatabaseHelper db = new ScheduleDatabaseHelper(this);
        faculties = db.getFaculties().getAll();
        if (faculties.size() == 0) {
            helper = new JsonHelper(this);
            helper.DownloadJson(_url, this);
        }
        else{
            ArrayAdapter<Faculty> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, faculties);
            lvFaculties.setAdapter(adapter);
            _loadingIndicator.close();
        }

        db.close();
        getLoaderManager().initLoader(JSON_LOADER_ID, null, this);
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
    public Loader<JSONArray> onCreateLoader(int id, Bundle bundle) {
        AsyncTaskJsonLoader loader = null;
        if (id == JSON_LOADER_ID) {
            loader = new AsyncTaskJsonLoader(this, _url);
            Log.d(LOG_TAG, "onCreateLoader: " + loader.hashCode());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray data) {
        Log.d(LOG_TAG, "onLoadFinished for loader " + loader.hashCode()
                + ", result = " + data.toString());

        try {
            faculties = helper.GetListOfModels(data, new Faculty());
        } catch (JsonDownloadException e) {
            e.printStackTrace();
            return;
        }
        adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, faculties);

        lvFaculties.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {
        Log.d(LOG_TAG, "onLoaderReset for loader " + loader.hashCode());
    }

    @Override
    public void onJsonDownloaded(JSONArray data){
        _loadingIndicator.close();

        try {
            faculties = helper.GetListOfModels(data, new Faculty());
        } catch (JsonDownloadException e) {
            e.printStackTrace();
            return;
        }

        ScheduleDatabaseHelper db = new ScheduleDatabaseHelper(this);
        db.getFaculties().insert(faculties);
        db.close();

        adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, faculties);
        lvFaculties.setAdapter(adapter);
    }
}