package com.team.mere.teacherschedule;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private ListView lvSections;
    private Intent faculties_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvSections = (ListView) findViewById(R.id.lvSections);
        String[] sections = new String[] {
                getResources().getString(R.string.list_faculties),
                getResources().getString(R.string.list_cathedries),
                getString(R.string.list_teachers)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, sections);
        lvSections.setAdapter(adapter);

        faculties_intent = new Intent(this, FacultiesActivity.class);
        lvSections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String strText = textView.getText().toString();
                if (strText == getResources().getString(R.string.list_faculties)){
                    startActivity(faculties_intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
