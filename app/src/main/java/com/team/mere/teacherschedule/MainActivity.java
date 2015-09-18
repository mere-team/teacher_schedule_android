package com.team.mere.teacherschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import ArrayAdapters.SectionListArrayAdapter;
import Models.Section;

public class MainActivity extends AppCompatActivity {

    private ListView lvSections;
    private EditText etMainSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvSections = (ListView) findViewById(R.id.lvSections);
        etMainSearch = (EditText) findViewById(R.id.etMainSearch);

        ArrayList<Section> sections = new ArrayList<>();
        sections.add(new Section(new ImageView(this), getResources().getString(R.string.list_faculties)));
        sections.add(new Section(new ImageView(this), getResources().getString(R.string.list_cathedries)));
        sections.add(new Section(new ImageView(this), getString(R.string.list_teachers)));

        SectionListArrayAdapter adapter = new SectionListArrayAdapter(this, R.layout.section_list_item, R.id.sectionListLabel, sections);
        lvSections.setAdapter(adapter);

        lvSections.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Section current = (Section)parent.getItemAtPosition(position);
                String strText = current.Name;
                if (strText == getResources().getString(R.string.list_faculties)) {
                    startActivity(new Intent(getApplicationContext(), FacultiesActivity.class));
                } else if (strText == getResources().getString(R.string.list_cathedries)) {
                    startActivity(new Intent(getApplicationContext(), CathedriesActivity.class));
                } else if (strText == getResources().getString(R.string.list_teachers)) {
                    startActivity(new Intent(getApplicationContext(), TeachersActivity.class));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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

    public void onFabMainSearchClick(View view) {
        InputMethodManager imm =  (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(etMainSearch, InputMethodManager.SHOW_IMPLICIT);
    }
}
