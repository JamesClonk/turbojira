package io.jamesclonk.turbojira;

public class ActivityHolder {
    private static ActivityHolder instance = new ActivityHolder();
    private ListIssuesActivity listIssuesActivity;

    public static ActivityHolder getInstance() {
        return instance;
    }

    public static ListIssuesActivity getListIssuesActivity() {
        return instance.listIssuesActivity;
    }

    public static void setListIssuesActivity(ListIssuesActivity listIssuesActivity) {
        instance.listIssuesActivity = listIssuesActivity;
    }

    private ActivityHolder() {
    }
}
