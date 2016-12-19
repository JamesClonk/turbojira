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
        if (issue.fields.issuetype.name.equalsIgnoreCase("task")) {
            itemType.setImageResource(R.drawable.ic_task);
            itemType.setColorFilter(0xff006699);
        } else if (issue.fields.issuetype.name.toLowerCase().contains("epic")) {
            itemType.setImageResource(R.drawable.ic_epic);
            itemType.setColorFilter(0xaa993399);
        } else if (issue.fields.issuetype.name.equalsIgnoreCase("bug")) {
            itemType.setImageResource(R.drawable.ic_bug);
            itemType.setColorFilter(0xffbb0000);
        } else if (issue.fields.issuetype.name.toLowerCase().contains("business")
                || issue.fields.issuetype.name.toLowerCase().contains("requirement")) {
            itemType.setImageResource(R.drawable.ic_requirement);
            itemType.setColorFilter(0xaa33aa33);
        } else if (issue.fields.issuetype.name.toLowerCase().contains("feature")
                || issue.fields.issuetype.name.toLowerCase().contains("request")) {
            itemType.setImageResource(R.drawable.ic_new_feature);
            itemType.setColorFilter(0xaa33aa33);
        } else if (issue.fields.issuetype.name.toLowerCase().contains("story")) {
            itemType.setImageResource(R.drawable.ic_user_story);
            itemType.setColorFilter(0xaa33aa33);
        } else {
            itemType.setImageResource(R.drawable.ic_unknown);
            itemType.setColorFilter(0xaa7f7f7f);
        }

        ImageView itemPriority = (ImageView) rowView.findViewById(R.id.item_list_priority);
        if (issue.fields.priority.name.equalsIgnoreCase("low")) {
            itemPriority.setImageResource(R.drawable.ic_low);
            itemPriority.setColorFilter(0xaa999999);
        } else if (issue.fields.priority.name.equalsIgnoreCase("high")) {
            itemPriority.setImageResource(R.drawable.ic_high);
            itemPriority.setColorFilter(0xffbb0000);
        } else if (issue.fields.priority.name.equalsIgnoreCase("critical")) {
            itemPriority.setImageResource(R.drawable.ic_critical);
            itemPriority.setColorFilter(0xffcc0000);
        } else { // default to medium
            itemPriority.setImageResource(R.drawable.ic_medium);
            itemPriority.setColorFilter(0xaa33aa33);
        }

        TextView textView = (TextView) rowView.findViewById(R.id.item_list_key);
        textView.setAutoLinkMask(0);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml("<a href=\""+issue.self+"\">"+issue.key+"</a>"));

        textView = (TextView) rowView.findViewById(R.id.item_list_status);
        textView.setText(issue.fields.status.name);

        textView = (TextView) rowView.findViewById(R.id.item_list_summary);
        textView.setText(issue.fields.summary);

        return rowView;
    }
}
