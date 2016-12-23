package io.jamesclonk.turbojira;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import io.jamesclonk.turbojira.jira.Client;
import io.jamesclonk.turbojira.jira.Issue;

public class IssuesAdapter extends ArrayAdapter<Issue> {
    private final Context context;
    private final List<Issue> issues;

    public IssuesAdapter(Context context, List<Issue> issues) {
        super(context, -1, issues);
        this.context = context;
        this.issues = issues;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Client client = new Client();

        Issue issue = issues.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.issues, parent, false);

        ImageView typeIcon = (ImageView) rowView.findViewById(R.id.issue_type_icon);
        issue.UpdateTypeImageView(typeIcon);

        ImageView priorityIcon = (ImageView) rowView.findViewById(R.id.issue_priority_icon);
        issue.UpdatePriorityImageView(priorityIcon);

        TextView textView = (TextView) rowView.findViewById(R.id.issue_key);
        textView.setAutoLinkMask(0);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        URL url = client.getURL();
        if (url != null) {
            textView.setText(Html.fromHtml(
                    "<a href=\"" + url.getProtocol() + "://" + url.getAuthority() +
                            "/browse/" + issue.key + "\">" + issue.key + "</a>"
            ));
        } else {
            textView.setText(issue.key);
        }

        textView = (TextView) rowView.findViewById(R.id.issue_status);
        textView.setText(issue.fields.status.name);

        textView = (TextView) rowView.findViewById(R.id.issue_summary);
        textView.setText(issue.fields.summary);

        return rowView;
    }
}
