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

import java.util.List;

import Helpers.JsonDownloadException;
import Helpers.JsonDownloadTask.OnJsonDownloadedListener;
import Helpers.JsonHelper;
import Helpers.LoadingIndicator;
import Models.Cathedra;
import ScheduleDatabase.ScheduleDatabaseHelper;


public class CathedriesActivity extends AppCompatActivity
        implements OnItemClickListener, OnJsonDownloadedListener{

    private ListView lvCathedries;
    private List<Cathedra> cathedries;

    private static final String _url = "http://ulstuschedule.azurewebsites.net/api/cathedries";
    private JsonHelper helper;
    private LoadingIndicator _loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cathedries);
        lvCathedries = (ListView) findViewById(R.id.lvCathdries);
        lvCathedries.setOnItemClickListener(this);

        _loadingIndicator = new LoadingIndicator(this, getResources().getString(R.string.cathedries_loading));
        _loadingIndicator.show();

        ScheduleDatabaseHelper db = new ScheduleDatabaseHelper(this);
        cathedries = db.getCathedries().getAll();
        if (cathedries.size() == 0) {
            helper = new JsonHelper(this);
            helper.DownloadJson(_url, this);
        }
        else{
            ArrayAdapter<Cathedra> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, cathedries);
            lvCathedries.setAdapter(adapter);
            _loadingIndicator.close();
        }
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cathedries, menu);
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
        Intent intent = new Intent(getApplicationContext(), CathedraActivity.class);
        intent.putExtra("CathedraId", cathedries.get(position).Id);
        intent.putExtra("CathedraName", cathedries.get(position).Name);
        startActivity(intent);
    }

    @Override
    public void onJsonDownloaded(JSONArray data) {
        _loadingIndicator.close();

        try {
            cathedries = helper.GetListOfModels(data, new Cathedra());
        } catch (JsonDownloadException e) {
            e.printStackTrace();
            return;
        }

        ScheduleDatabaseHelper db = new ScheduleDatabaseHelper(this);
        db.getCathedries().insert(cathedries);
        db.close();

        ArrayAdapter<Cathedra> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, cathedries);
        lvCathedries.setAdapter(adapter);
    }
}
