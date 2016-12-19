package io.jamesclonk.turbojira.jira;

import com.google.gson.annotations.SerializedName;

public class Issue {
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

    public class Fields {
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

    public class Assignee {
        public String key;
        public String name;
    }

    public class Reporter {
        public String key;
        public String name;
    }

    public class Priority {
        public String name;
    }

    public class Status {
        public String name;
    }

    public class Project {
        public String self;
        public String key;
        public String name;
    }

    public class IssueType {
        public String name;
    }
}
