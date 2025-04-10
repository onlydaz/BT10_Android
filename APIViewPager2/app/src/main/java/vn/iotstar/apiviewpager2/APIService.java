package vn.iotstar.apiviewpager2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface APIService {

    @GET("getvideos.php")
    Call<MessageVideoModel> getVideos();

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    APIService serviceapi = new Retrofit.Builder()
            .baseUrl("http://app.iotstar.vn/appfoods/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);
}

