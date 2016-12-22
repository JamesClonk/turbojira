package io.jamesclonk.turbojira.jira;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.IOException;

import io.jamesclonk.turbojira.ItemListActivity;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private ItemListActivity itemListActivity;
    private String endpoint;
    private String username;
    private String password;
    private String project;
    private String epic;
    private String issuetype;
    private String priority;

    public Client(ItemListActivity itemListActivity) {
        this.itemListActivity = itemListActivity;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(itemListActivity);
        String endpoint = prefs.getString("jira_endpoint", "");
        String username = prefs.getString("jira_username", "");
        String password = prefs.getString("jira_password", "");
        String project = prefs.getString("jira_project", "");
        String epic = prefs.getString("jira_epic", "");
        String issuetype = prefs.getString("jira_issuetype", "");
        String priority = prefs.getString("jira_priority", "");

        this.endpoint = endpoint;
        this.username = username;
        this.password = password;
        this.project = project;
        this.epic = epic;
        this.issuetype = issuetype;
        this.priority = priority;
    }

    ItemListActivity getItemListActivity() {
        return itemListActivity;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProject() {
        return project;
    }

    public String getEpic() {
        return epic;
    }

    public String getIssuetype() {
        return issuetype;
    }

    public String getPriority() {
        return priority;
    }

    private API getAPI() {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

        final String credentials = Credentials.basic(getUsername(), getPassword());
        httpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", credentials);
                requestBuilder.header("Accept", "application/json");
                requestBuilder.method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient httpClient = httpBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getEndpoint())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        API api = retrofit.create(API.class);
        return api;
    }

    private void toast(final String msg) {
        getItemListActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getItemListActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void createIssue(Issue issue) {
        try {
            boolean success = new CreateIssue().execute(issue).get();
            if (success) {
                toast("New Issue created:\n" + issue.fields.summary);
            }
        } catch (Exception e) {
            toast(e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateIssue(Issue issue) {
        try {
            boolean success = new UpdateIssue().execute(issue).get();
            if (success) {
                toast("Issue updated:\n" + issue.fields.summary);
            }
        } catch (Exception e) {
            toast(e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateIssues() {
        new UpdateIssues().execute();
    }

    private class CreateIssue extends AsyncTask<Issue, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Issue... params) {
            try {
                API api = getAPI();
                Call<Issue> result = api.createIssue(params[0]);
                retrofit2.Response<Issue> response = result.execute();
                if (response.code() != 201) {
                    String msg = "API call failed, HTTP code [" + response.code() + "]:\n";
                    for (String name : response.headers().names()) {
                        msg += "\n" + name + "=" + response.headers().get(name);
                    }
                    toast(msg);
                    if (response.errorBody() != null) {
                        toast(response.errorBody().string());
                    }
                    return false;
                }
                return true;

            } catch (final Exception e) {
                toast(e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
    }

    private class UpdateIssue extends AsyncTask<Issue, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Issue... params) {
            try {
                API api = getAPI();
                Call<Issue> result = api.updateIssue(params[0]);
                retrofit2.Response<Issue> response = result.execute();
                if (response.code() != 204) {
                    String msg = "API call failed, HTTP code [" + response.code() + "]:\n";
                    for (String name : response.headers().names()) {
                        msg += "\n" + name + "=" + response.headers().get(name);
                    }
                    toast(msg);
                    if (response.errorBody() != null) {
                        toast(response.errorBody().string());
                    }
                    return false;
                }
                return true;

            } catch (final Exception e) {
                toast(e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
    }

    private class UpdateIssues extends AsyncTask<String, Void, Issues> {
        @Override
        protected Issues doInBackground(String... params) {
            try {
                API api = getAPI();
                Call<Issues> result = api.search("assignee=" + getUsername() + "+AND+statusCategory!=done+order+by+priority");
                retrofit2.Response<Issues> response = result.execute();
                if (response.code() != 200) {
                    String msg = "API call failed, HTTP code [" + response.code() + "]:\n";
                    for (String name : response.headers().names()) {
                        msg += "\n" + name + "=" + response.headers().get(name);
                    }
                    toast(msg);
                    if (response.errorBody() != null) {
                        toast(response.errorBody().string());
                    }
                }
                return response.body();

            } catch (final Exception e) {
                toast(e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Issues search) {
            itemListActivity.updateItemList(search);
            super.onPostExecute(search);
        }
    }
}

