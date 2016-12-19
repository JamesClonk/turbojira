package io.jamesclonk.turbojira;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import io.jamesclonk.turbojira.jira.Client;
import io.jamesclonk.turbojira.jira.Issue;
import io.jamesclonk.turbojira.jira.Issues;

public class ItemList extends AppCompatActivity {

    private ArrayList<Issue> itemList = new ArrayList<>();
    private ArrayAdapter<Issue> itemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addTaskButton = (FloatingActionButton) findViewById(R.id.add_task);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItems(view);
            }
        });

        ListView listView = (ListView) findViewById(R.id.item_list);
//        itemListAdapter = new ArrayAdapter<>(this,
//                //android.R.layout.simple_list_item_1,
//                R.layout.item_list,
//                R.id.item_list_key,
//                itemList);
        itemListAdapter = new ItemListAdapter(this, itemList);
        listView.setAdapter(itemListAdapter);
        updateItemList();
    }

    public void updateItemList() {
        Client client = new Client(this);
        Issues issues = client.getIssues();
        if (issues != null) {
            itemList.clear();
            for (Issue issue : issues.getIssues()) {
                itemList.add(issue);
            }
            itemListAdapter.notifyDataSetChanged();
        }
    }

    public void addItems(View view) {
        updateItemList();
    }

    public void openSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
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
