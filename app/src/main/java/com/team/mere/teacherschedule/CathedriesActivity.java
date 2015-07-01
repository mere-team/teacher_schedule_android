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
import Models.Cathedra;


public class CathedriesActivity extends ActionBarActivity
        implements OnItemClickListener, OnJsonDownloadedListener{

    private ListView lvCathedries;
    private ArrayList<Cathedra> cathedries;
    private ArrayAdapter<Cathedra> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cathedries);
        lvCathedries = (ListView) findViewById(R.id.lvCathdries);

        new JsonDownloadTask("http://ulstuschedule.azurewebsites.net/api/cathedries", this)
                .execute();
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
        Intent intent = new Intent(this, CathedraActivity.class);
        intent.putExtra("CathedraId", cathedries.get(position).Id);
        startActivity(intent);
    }

    @Override
    public void onJsonDownloaded(JSONArray data) {
        cathedries = new ArrayList<>(data.length());
        for (int i = 0; i < data.length(); i++){
            Cathedra cathedra = Cathedra.getFromJson(data.optJSONObject(i));
            cathedries.add(cathedra);
        }
        adapter = new ArrayAdapter<Cathedra>(this, R.layout.list_item, cathedries);
        lvCathedries.setAdapter(adapter);
    }
}
