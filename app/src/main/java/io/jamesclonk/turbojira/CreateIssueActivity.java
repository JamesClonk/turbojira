package io.jamesclonk.turbojira;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

import io.jamesclonk.turbojira.jira.Client;
import io.jamesclonk.turbojira.jira.Issue;

public class CreateIssueActivity extends AppCompatActivity {

    private ArrayAdapter<CharSequence> priorities;
    private ArrayAdapter<CharSequence> types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        priorities = ArrayAdapter.createFromResource(this,
                R.array.jira_issue_priorities, android.R.layout.simple_spinner_item);
        types = ArrayAdapter.createFromResource(this,
                R.array.jira_issue_types, android.R.layout.simple_spinner_item);

        setupView();
    }

    private void setupView() {
        setContentView(R.layout.activity_create_issue);

        final HashMap<String, Boolean> states = new HashMap<>();
        final Client client = new Client();

        final Button cancelButton = (Button) findViewById(R.id.create_issue_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        final Button createButton = (Button) findViewById(R.id.create_issue_create);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createIssue();
            }
        });
        createButton.setEnabled(false);

        TextView textView = (TextView) findViewById(R.id.create_issue_summary);
        textView.addTextChangedListener(new InputValidator(textView) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty() || text.length() < 5) {
                    states.put("SUMMARY", false);
                    textView.setError("Please set a summary!");
                    createButton.setEnabled(false);
                } else {
                    states.put("SUMMARY", true);
                    if (states.get("PROJECT")) {
                        createButton.setEnabled(true);
                    }
                }
            }
        });
        states.put("SUMMARY", false);
        textView.setError("Please set a summary!");
        textView.requestFocus();

        textView = (TextView) findViewById(R.id.create_issue_project);
        textView.setText(client.getProject());
        textView.addTextChangedListener(new InputValidator(textView) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty() || text.length() < 5) {
                    states.put("PROJECT", false);
                    textView.setError("Please set a project!");
                    createButton.setEnabled(false);
                } else {
                    states.put("PROJECT", true);
                    if (states.get("SUMMARY")) {
                        createButton.setEnabled(true);
                    }
                }
            }
        });
        states.put("PROJECT", true);
        if (client.getProject().isEmpty() || client.getProject().length() < 5) {
            states.put("PROJECT", false);
            textView.setError("Please set a project!");
        }

        textView = (TextView) findViewById(R.id.create_issue_epic);
        textView.setText(client.getEpic());

        Spinner spinner = (Spinner) findViewById(R.id.create_issue_priority);
        priorities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(priorities);
        spinner.setSelection(priorities.getPosition(client.getPriority()));

        spinner = (Spinner) findViewById(R.id.create_issue_type);
        types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(types);
        spinner.setSelection(types.getPosition(client.getIssuetype()));
    }

    private void createIssue() {
        final Client client = new Client();

        TextView textView = (TextView) findViewById(R.id.create_issue_summary);
        String summary = textView.getText().toString();
        textView = (TextView) findViewById(R.id.create_issue_project);
        String project = textView.getText().toString();
        textView = (TextView) findViewById(R.id.create_issue_epic);
        String epic = textView.getText().toString();
        Spinner spinner = (Spinner) findViewById(R.id.create_issue_type);
        String issuetype = types.getItem(spinner.getSelectedItemPosition()).toString();
        spinner = (Spinner) findViewById(R.id.create_issue_priority);
        String priority = priorities.getItem(spinner.getSelectedItemPosition()).toString();

        if (epic != null && epic.isEmpty()) {
            epic = null;
        }

        final Issue issue = new Issue(
                client.getUsername()
                , summary
                , project
                , epic
                , issuetype
                , priority
        );
        client.createIssue(issue);

        finish();
        Activities.getListIssuesActivity().updateIssues();
    }
}
