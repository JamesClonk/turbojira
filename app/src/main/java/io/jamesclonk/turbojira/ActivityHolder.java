package io.jamesclonk.turbojira;

public class ActivityHolder {
    private static ActivityHolder instance = new ActivityHolder();
    private ItemListActivity itemListActivity;

    public static ActivityHolder getInstance() {
        return instance;
    }

    public static ItemListActivity getItemListActivity() {
        return instance.itemListActivity;
    }

    public static void setItemListActivity(ItemListActivity itemListActivity) {
        instance.itemListActivity = itemListActivity;
    }

    private ActivityHolder() {
    }
}
