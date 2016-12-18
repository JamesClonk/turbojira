package io.jamesclonk.turbojira;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import io.jamesclonk.turbojira.jira.Issue;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ItemList extends AppCompatActivity {

    private ArrayList<String> itemList = new ArrayList<>();
    private ArrayAdapter<String> itemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addTaskButton = (FloatingActionButton) findViewById(R.id.add_task);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Create new Jira Task", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                addItems(view);
            }
        });

        for( int i = 1; i <= 15; i++) {
            itemList.add("Item: "+i);
        }
        ListView listView = (ListView) findViewById(R.id.item_list);
        itemListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                itemList);
        listView.setAdapter(itemListAdapter);
    }

    public void updateItemList() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String endpoint = prefs.getString(getResources().getString(R.string.preference_jira_endpoint_key),"");
        String username = prefs.getString(getResources().getString(R.string.preference_jira_username_key),"");
        String password = prefs.getString(getResources().getString(R.string.preference_jira_password_key),"");
        String project = prefs.getString(getResources().getString(R.string.preference_jira_project_key),"");

        if(!endpoint.isEmpty()
                && !username.isEmpty()
                && !password.isEmpty()) {

//            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
//
//            final String credentials = Credentials.basic(username, password);
//            httpBuilder.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Interceptor.Chain chain) throws IOException {
//                    Request original = chain.request();
//
//                    Request.Builder requestBuilder = original.newBuilder()
//                            .header("Authorization", credentials);
//                    requestBuilder.header("Accept", "application/json");
//                    requestBuilder.method(original.method(),original.body());
//
//                    Request request = requestBuilder.build();
//                    return chain.proceed(request);
//                }
//            });
//
//            OkHttpClient httpClient = httpBuilder.build();
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(endpoint)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(httpClient)
//                    .build();
//
//            JiraRestApi jira = retrofit.create(JiraRestApi.class);
//            Call<Issue> result = jira.getIssue("CLOUDAC-633");
////            retrofit2.Response result = service.search("assignee=" + username);
//            Issue issue = null;
//            try {
//                issue = result.execute().body();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            JiraCaller caller = new JiraCaller();
            AsyncTask<String, Void, Issue> result = caller.execute(endpoint, username, password);
            try {
                Issue issue = result.get();
                if (issue != null) {
                    System.out.println(issue.toString());
                    System.out.println(issue.self);
                    System.out.println(issue.key);
                    //itemList.add(issue.summary);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            itemListAdapter.notifyDataSetChanged();
        }
    }

    public interface JiraRestApi {
        @GET("/rest/api/2/issue/{key}")
        Call<Issue> getIssue(@Path("key") String key);

        @GET("/rest/api/2/search")
        retrofit2.Response search(@Query("jql") String query);
    }

    public void addItems(View view) {
        updateItemList();
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String item = prefs.getString(getResources().getString(R.string.preference_jira_username_key),"");
//        itemList.add(item);
//        itemListAdapter.notifyDataSetChanged();
    }

    public void openSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
