package io.jamesclonk.turbojira.jira;

import com.google.gson.annotations.SerializedName;

public class Issue {
    public String self;
    public String key;
    public Fields fields;

    public class Fields {
        public String summary;
        public String description;
        public String created;
        public String updated;
        public String duedate;
        @SerializedName("customfield_13520")
        public String epic;
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
