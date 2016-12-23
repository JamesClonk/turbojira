package io.jamesclonk.turbojira;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import io.jamesclonk.turbojira.jira.Client;
import io.jamesclonk.turbojira.jira.Issue;
import io.jamesclonk.turbojira.jira.Issues;
import me.leolin.shortcutbadger.ShortcutBadger;

public class ListIssuesActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefresh;
    private ArrayList<Issue> issues = new ArrayList<>();
    private ArrayAdapter<Issue> issuesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activities.getInstance().setListIssuesActivity(this);

        setupView();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activities.getListIssuesActivity());
        Boolean startupCreate = prefs.getBoolean("jira_create_flag", false);
        if (startupCreate) {
            createIssue(getCurrentFocus());
        }
        updateIssues();
    }

    private void setupView() {
        setContentView(R.layout.activity_list_issues);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addTaskButton = (FloatingActionButton) findViewById(R.id.create_issue);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createIssue(view);
            }
        });

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateIssues();
            }
        });
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        ListView listView = (ListView) findViewById(R.id.issues);
        issuesAdapter = new IssuesAdapter(this, issues);
        listView.setAdapter(issuesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                editIssue(issuesAdapter.getItem(position));
            }
        });
        listView.setClickable(true);
    }

    private void setToolbarSubtitle(String text) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle(text);
    }

    public void updateIssues() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
                new Client().updateIssues();
            }
        });
    }

    public void updateIssues(Issues issues) {
        if (issues != null) {
            this.issues.clear();
            for (Issue issue : issues.getIssues()) {
                this.issues.add(issue);
            }
            issuesAdapter.notifyDataSetChanged();
            setToolbarSubtitle("Open Issues: " + this.issues.size());
            try {
                ShortcutBadger.applyCount(ListIssuesActivity.this, this.issues.size());
            } catch (Exception e) {
                // nothing
            }
        }
        swipeRefresh.setRefreshing(false);
    }

    public void createIssue(View view) {
        Intent intent = new Intent(this, CreateIssueActivity.class);
        startActivity(intent);
    }

    public void editIssue(Issue issue) {
        Intent intent = new Intent(this, EditIssueActivity.class);
        intent.putExtra("JIRA_ISSUE", issue);
        startActivity(intent);
    }

    public void openSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
