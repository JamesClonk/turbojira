package io.jamesclonk.turbojira;

import android.os.AsyncTask;
import java.io.IOException;
import io.jamesclonk.turbojira.jira.Issue;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class JiraCaller extends AsyncTask<String, Void, Issue> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Issue doInBackground(String... params) {
        try {
            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

            final String credentials = Credentials.basic(params[1], params[2]);
            httpBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", credentials);
                    requestBuilder.header("Accept", "application/json");
                    requestBuilder.method(original.method(),original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            OkHttpClient httpClient = httpBuilder.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(params[0])
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();

            ItemList.JiraRestApi jira = retrofit.create(ItemList.JiraRestApi.class);
            Call<Issue> result = jira.getIssue("CLOUDAC-633");
//            retrofit2.Response result = service.search("assignee=" + username);
            retrofit2.Response<Issue> response = result.execute();
            Issue issue = response.body();
            return issue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Issue issue) {
    }
}