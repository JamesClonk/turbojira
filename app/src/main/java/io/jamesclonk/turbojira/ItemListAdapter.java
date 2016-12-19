package io.jamesclonk.turbojira;

import android.content.Context;
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

        ImageView imageView = (ImageView) rowView.findViewById(R.id.item_list_icon);
        if (issue.fields.issuetype.name.equalsIgnoreCase("task")) {
            imageView.setImageResource(R.drawable.ic_task);
            imageView.setColorFilter(0xff006699);
        } else if (issue.fields.issuetype.name.toLowerCase().contains("epic")) {
            imageView.setImageResource(R.drawable.ic_epic);
            imageView.setColorFilter(0xaa993399);
        } else if (issue.fields.issuetype.name.equalsIgnoreCase("bug")) {
            imageView.setImageResource(R.drawable.ic_bug);
            imageView.setColorFilter(0xffbb0000);
        } else if (issue.fields.issuetype.name.toLowerCase().contains("business")
                || issue.fields.issuetype.name.toLowerCase().contains("requirement")) {
            imageView.setImageResource(R.drawable.ic_requirement);
            imageView.setColorFilter(0xaa33aa33);
        } else if (issue.fields.issuetype.name.toLowerCase().contains("feature")
                || issue.fields.issuetype.name.toLowerCase().contains("request")) {
            imageView.setImageResource(R.drawable.ic_new_feature);
            imageView.setColorFilter(0xaa33aa33);
        } else if (issue.fields.issuetype.name.toLowerCase().contains("story")) {
            imageView.setImageResource(R.drawable.ic_user_story);
            imageView.setColorFilter(0xaa33aa33);
        } else {
            imageView.setImageResource(R.drawable.ic_unknown);
            imageView.setColorFilter(0xaa7f7f7f);
        }

        TextView textView = (TextView) rowView.findViewById(R.id.item_list_key);
        textView.setText(issues.get(position).key);

        textView = (TextView) rowView.findViewById(R.id.item_list_summary);
        textView.setText(issues.get(position).fields.summary);

        return rowView;
    }
}
