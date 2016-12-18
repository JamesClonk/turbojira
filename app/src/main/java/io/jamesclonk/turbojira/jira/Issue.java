package io.jamesclonk.turbojira.jira;

public class Issue {
    public final String summary;
    public final String self;
    public final String key;

    public Issue(String summary, String self, String key) {
        this.summary = summary;
        this.self = self;
        this.key = key;
    }
}