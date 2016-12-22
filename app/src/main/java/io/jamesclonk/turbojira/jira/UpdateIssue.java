package io.jamesclonk.turbojira.jira;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateIssue implements Serializable {
    public String self;
    public String key;
    public Fields fields;

    public UpdateIssue(Issue issue) {
        this.self = issue.self;
        this.key = issue.key;

        this.fields = new Fields();
        this.fields.summary = issue.fields.summary;
        this.fields.description = issue.fields.description;

        this.fields.issuetype = new IssueType();
        this.fields.issuetype.name = issue.fields.issuetype.name;

        this.fields.priority = new Priority();
        this.fields.priority.name = issue.fields.priority.name;
    }

    public class Fields implements Serializable {
        public String summary;
        public String description;
        public Priority priority;
        @SerializedName("issuetype")
        public IssueType issuetype;
    }

    public class Priority implements Serializable {
        public String name;
    }

    public class IssueType implements Serializable {
        public String name;
    }
}
