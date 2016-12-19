package io.jamesclonk.turbojira.jira;

import java.util.List;

public class Issues {
    private String total;
    private List<Issue> issues;

    public String getTotal() {
        return total;
    }
    public List<Issue> getIssues() {
        return issues;
    }
}
