package io.jamesclonk.turbojira.jira;

import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.jamesclonk.turbojira.R;

public class Issue implements Serializable {
    public String self;
    public String key;
    public Fields fields;

    public Issue(String username, String summary, String project, String epic, String issuetype, String priority ) {
        this.fields = new Fields();
        this.fields.summary = summary;
        this.fields.epic = epic;

        this.fields.assignee = new Assignee();
        this.fields.assignee.key = username;
        this.fields.assignee.name = username;

        this.fields.reporter = new Reporter();
        this.fields.reporter.key = username;
        this.fields.reporter.name = username;

        this.fields.project = new Project();
        this.fields.project.key = project;

        this.fields.issuetype = new IssueType();
        this.fields.issuetype.name = issuetype;

        this.fields.priority = new Priority();
        this.fields.priority.name = priority;
    }

    public class Fields implements Serializable {
        public String summary;
        public String description;
        public String created;
        public String updated;
        public String duedate;
        @SerializedName("customfield_13520")
        public String epic;
        public Assignee assignee;
        public Reporter reporter;
        public Priority priority;
        public Status status;
        public Project project;
        @SerializedName("issuetype")
        public IssueType issuetype;
    }

    public class Assignee implements Serializable {
        public String key;
        public String name;
    }

    public class Reporter implements Serializable {
        public String key;
        public String name;
    }

    public class Priority implements Serializable {
        public String name;
    }

    public class Status implements Serializable {
        public String name;
    }

    public class Project implements Serializable {
        public String self;
        public String key;
        public String name;
    }

    public class IssueType implements Serializable {
        public String name;
    }

    public void UpdateTypeImageView(ImageView issueType) {
        if (this.fields.issuetype.name.equalsIgnoreCase("task")) {
            issueType.setImageResource(R.drawable.ic_task);
            issueType.setColorFilter(0xff006699);
        } else if (this.fields.issuetype.name.toLowerCase().contains("epic")) {
            issueType.setImageResource(R.drawable.ic_epic);
            issueType.setColorFilter(0xaa993399);
        } else if (this.fields.issuetype.name.equalsIgnoreCase("bug")) {
            issueType.setImageResource(R.drawable.ic_bug);
            issueType.setColorFilter(0xffbb0000);
        } else if (this.fields.issuetype.name.toLowerCase().contains("business")
                || this.fields.issuetype.name.toLowerCase().contains("requirement")) {
            issueType.setImageResource(R.drawable.ic_requirement);
            issueType.setColorFilter(0xaa33aa33);
        } else if (this.fields.issuetype.name.toLowerCase().contains("feature")
                || this.fields.issuetype.name.toLowerCase().contains("request")) {
            issueType.setImageResource(R.drawable.ic_new_feature);
            issueType.setColorFilter(0xaa33aa33);
        } else if (this.fields.issuetype.name.toLowerCase().contains("story")) {
            issueType.setImageResource(R.drawable.ic_user_story);
            issueType.setColorFilter(0xaa33aa33);
        } else {
            issueType.setImageResource(R.drawable.ic_unknown);
            issueType.setColorFilter(0xaa7f7f7f);
        }
    }

    public void UpdatePriorityImageView(ImageView issuePriority) {
        if (this.fields.priority.name.equalsIgnoreCase("low")) {
            issuePriority.setImageResource(R.drawable.ic_low);
            issuePriority.setColorFilter(0xaa999999);
        } else if (this.fields.priority.name.equalsIgnoreCase("high")) {
            issuePriority.setImageResource(R.drawable.ic_high);
            issuePriority.setColorFilter(0xffbb0000);
        } else if (this.fields.priority.name.equalsIgnoreCase("critical")) {
            issuePriority.setImageResource(R.drawable.ic_critical);
            issuePriority.setColorFilter(0xffcc0000);
        } else { // default to medium
            issuePriority.setImageResource(R.drawable.ic_medium);
            issuePriority.setColorFilter(0xaa33aa33);
        }
    }
}
