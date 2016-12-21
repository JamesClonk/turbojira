package io.jamesclonk.turbojira;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.jamesclonk.turbojira.jira.Issue;

public class EditIssueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_issue);

        Issue issue = (Issue) getIntent().getSerializableExtra("JIRA_ISSUE");

        TextView textView = (TextView) this.findViewById(R.id.item_key);
        textView.setText(issue.key);
        textView = (TextView) this.findViewById(R.id.item_status);
        textView.setText(issue.fields.status.name);
    }
}
