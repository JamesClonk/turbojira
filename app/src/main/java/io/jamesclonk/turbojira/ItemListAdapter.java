package io.jamesclonk.turbojira;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_list, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.item_list_key);
        textView.setText(issues.get(position).key);

        textView = (TextView) rowView.findViewById(R.id.item_list_summary);
        textView.setText(issues.get(position).fields.summary);

        return rowView;
    }
}
