package io.jamesclonk.turbojira;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import io.jamesclonk.turbojira.jira.Client;
import io.jamesclonk.turbojira.jira.Issue;
import io.jamesclonk.turbojira.jira.Issues;

public class ItemList extends AppCompatActivity {

    private AlertDialog dialog;
    private SwipeRefreshLayout swipeRefresh;
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
                createItem(view);
            }
        });

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        updateItemList();
                    }
                });
            }
        });
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        ListView listView = (ListView) findViewById(R.id.item_list);
        itemListAdapter = new ItemListAdapter(this, itemList);
        listView.setAdapter(itemListAdapter);
        updateItemList();
    }

    public void updateItemList() {
        swipeRefresh.setRefreshing(true);
        new Client(this).updateIssues();
    }

    public void updateItemList(Issues issues) {
        if (issues != null) {
            itemList.clear();
            for (Issue issue : issues.getIssues()) {
                itemList.add(issue);
            }
            itemListAdapter.notifyDataSetChanged();
        }
        swipeRefresh.setRefreshing(false);
    }

    public void createItem(View view) {
        final HashMap<String, Boolean> states = new HashMap<>();
        final ItemList activity = this;
        final Client client = new Client(activity);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_create_item, null);
        builder.setView(dialogView);

        TextView textView = (TextView) dialogView.findViewById(R.id.create_issue_summary);
        textView.addTextChangedListener(new InputValidator(textView) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty() || text.length() < 5) {
                    states.put("SUMMARY", false);
                    textView.setError("Please set a summary!");
                    activity.dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    states.put("SUMMARY", true);
                    if(states.get("PROJECT")) {
                        activity.dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                }
            }
        });
        states.put("SUMMARY", false);
        textView.setError("Please set a summary!");

        textView = (TextView) dialogView.findViewById(R.id.create_issue_project);
        textView.setText(client.getProject());
        textView.addTextChangedListener(new InputValidator(textView) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty() || text.length() < 5) {
                    states.put("PROJECT", false);
                    textView.setError("Please set a project!");
                    activity.dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    states.put("PROJECT", true);
                    if(states.get("SUMMARY")) {
                        activity.dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                }
            }
        });
        states.put("PROJECT", true);
        if (client.getProject().isEmpty() || client.getProject().length() < 5) {
            states.put("PROJECT", false);
            textView.setError("Please set a project!");
        }

        textView = (TextView) dialogView.findViewById(R.id.create_issue_epic);
        textView.setText(client.getEpic());

        Spinner spinner = (Spinner) dialogView.findViewById(R.id.create_issue_priority);
        final ArrayAdapter<CharSequence> priorities = ArrayAdapter.createFromResource(this,
                R.array.jira_item_priorities, android.R.layout.simple_spinner_item);
        priorities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(priorities);
        spinner.setSelection(priorities.getPosition(client.getPriority()));

        spinner = (Spinner) dialogView.findViewById(R.id.create_issue_type);
        final ArrayAdapter<CharSequence> types = ArrayAdapter.createFromResource(this,
                R.array.jira_item_types, android.R.layout.simple_spinner_item);
        types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(types);
        spinner.setSelection(types.getPosition(client.getIssuetype()));

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TextView textView = (TextView) dialogView.findViewById(R.id.create_issue_summary);
                String summary = textView.getText().toString();
                textView = (TextView) dialogView.findViewById(R.id.create_issue_project);
                String project = textView.getText().toString();
                textView = (TextView) dialogView.findViewById(R.id.create_issue_epic);
                String epic = textView.getText().toString();
                Spinner spinner = (Spinner) dialogView.findViewById(R.id.create_issue_type);
                String issuetype = types.getItem(spinner.getSelectedItemPosition()).toString();
                spinner = (Spinner) dialogView.findViewById(R.id.create_issue_priority);
                String priority = priorities.getItem(spinner.getSelectedItemPosition()).toString();

                if (epic != null && epic.isEmpty()) {
                    epic = null;
                }

                Issue issue = new Issue(
                        client.username
                        , summary
                        , project
                        , epic
                        , issuetype
                        , priority
                );
                client.createIssue(issue);

                swipeRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        updateItemList();
                    }
                });
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setTitle("Create new Jira issue");

        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    public void openSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
