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

import java.util.List;

import io.jamesclonk.turbojira.jira.Issue;

public class ItemListAdapter extends ArrayAdapter<Issue> {
    private final Context context;
    private final List<Issue> issues;

    public ItemListAdapter(Context context, List<Issue> issues) {
        super(context, -1, issues);
        this.context = context;
        this.issues = issues;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Issue issue = issues.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_list, parent, false);

        ImageView itemType = (ImageView) rowView.findViewById(R.id.item_list_type);
        issue.UpdateTypeImageView(itemType);

        ImageView itemPriority = (ImageView) rowView.findViewById(R.id.item_list_priority);
        issue.UpdatePriorityImageView(itemPriority);

        TextView textView = (TextView) rowView.findViewById(R.id.item_list_key);
        textView.setAutoLinkMask(0);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml("<a href=\"https://issue.swisscom.ch/browse/"+issue.key+"\">"+issue.key+"</a>"));

        textView = (TextView) rowView.findViewById(R.id.item_list_status);
        textView.setText(issue.fields.status.name);

        textView = (TextView) rowView.findViewById(R.id.item_list_summary);
        textView.setText(issue.fields.summary);

        return rowView;
    }
}
