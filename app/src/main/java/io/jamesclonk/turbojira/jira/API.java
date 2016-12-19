package io.jamesclonk.turbojira.jira;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface API {
    @POST("/rest/api/2/issue")
    Call<Issue> createIssue(@Body Issue issue);

    @GET("/rest/api/2/issue/{key}")
    Call<Issue> getIssue(@Path("key") String key);

    @GET("/rest/api/2/search")
    Call<Issues> search(@Query(value = "jql", encoded = true) String query);
}
