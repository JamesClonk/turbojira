package io.jamesclonk.turbojira.jira;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface API {
    @GET("/rest/api/2/issue/{key}")
    Call<Issue> getIssue(@Path("key") String key);

    @GET("/rest/api/2/search")
    Call<Issues> search(@Query(value = "jql", encoded = true) String query);
}
