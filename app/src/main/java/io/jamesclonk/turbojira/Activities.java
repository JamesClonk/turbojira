package io.jamesclonk.turbojira;

public class Activities {
    private static Activities instance = new Activities();
    private ListIssuesActivity listIssuesActivity;

    public static Activities getInstance() {
        return instance;
    }

    public static ListIssuesActivity getListIssuesActivity() {
        return instance.listIssuesActivity;
    }

    public static void setListIssuesActivity(ListIssuesActivity listIssuesActivity) {
        instance.listIssuesActivity = listIssuesActivity;
    }

    private Activities() {
    }
}
