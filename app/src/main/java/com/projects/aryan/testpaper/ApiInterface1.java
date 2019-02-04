package  com.projects.aryan.testpaper;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface1 {
//    @GET("/api/app/shikshaSchool_url")
//    fun getDynamicApi():Call<ResponseModel>

    @GET("/school/api/apps/practiceTest_url")
    Call<ResponseModel1> getDynamicApi();

    @GET("/school/api/apps/btestpaper_leftmenu")
    Call<DrawerItemModel> getItemForDrawer();
}
