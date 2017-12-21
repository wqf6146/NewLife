package com.vise.xsnow.net.api;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @Description: 提供的系列接口
 */
public interface ApiService {
    @POST()
    @FormUrlEncoded
    Observable<ResponseBody> post(@Url() String url, @FieldMap Map<String, String> maps);

//    @POST()
//    @FormUrlEncoded
//    Observable<ResponseBody> post(@Url() String url, @Header("token")String token, @FieldMap Map<String, String> maps);

    @POST()
    @FormUrlEncoded
    Observable<ResponseBody> post(@Url() String url, @Header("token")String token, @FieldMap Map<String, Object> maps);


    @POST()
    Observable<ResponseBody> post(@Url() String url, @Header("token")String token, @QueryMap Map<String, String> maps, @Body RequestBody jsonBody);


    @POST()
    @FormUrlEncoded
    Observable<ResponseBody> post(@Url() String url, @Header("token")String token, @FieldMap Map<String, String> maps, @Query("productIds[]") String... linked);



    @POST()
    Observable<ResponseBody> postJson(@Url() String url,  @FieldMap Map<String, String> maps,@Body RequestBody jsonBody);

    @POST()
    Observable<ResponseBody> postBody(@Url() String url, @Body Object object);

    @GET()
    Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, String> maps);

    @DELETE()

    Observable<ResponseBody> delete(@Url() String url, @QueryMap Map<String, String> maps);

    @PUT()
    Observable<ResponseBody> put(@Url() String url, @QueryMap Map<String, String> maps);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadImage(@Url() String url,
                                         @Part("image\"; filename=\"image" + ".jpg") RequestBody
                                                 requestBody);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadImage(@Url() String url,@Header("token")String token,
                                         @Part("headImg\"; filename=\"image" + ".jpg") RequestBody requestBody,
                                         @Part("name") RequestBody name,
                                         @Part("sex") RequestBody sex);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadImage(@Url() String url,@Header("token")String token,
                                         @Part("img\"; filename=\"image" + ".jpg") RequestBody requestBody,
                                         @Part("id") RequestBody id);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFile(@Url String fileUrl,@Header("token")String token,
                                        @Part("description") RequestBody description, @Part() MultipartBody.Part file);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFile(@Url String fileUrl,@Header("token")String token,
                                        @Part("name") RequestBody description, @Part() MultipartBody.Part file,  @PartMap Map<String, Object> maps);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFile(@Url String fileUrl,
                                        @Part("description") RequestBody description, @Part("files") MultipartBody.Part file);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFiles(@Url() String url, @PartMap() Map<String, RequestBody>
            maps);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFiles(@Url() String path, @Part() List<MultipartBody.Part> parts);


    /*********
     *
     */
    @POST()
    @FormUrlEncoded
    Observable<ResponseBody> postImp(@Url() String url, @Header("token")String token, @FieldMap Map<String, Object> maps, @Query("impressions[]") String... linked);

    @POST()
    Observable<ResponseBody> uploadEva(@Url() String url, @Header("token")String token,@Query("impressions[]") String[] linked, @QueryMap Map<String, Object> maps,
                                       @Body MultipartBody multipartBody );

    @POST()
    Observable<ResponseBody> uploadImg(@Url() String url, @Header("token")String token, @QueryMap Map<String, Object> maps,
                                       @Body MultipartBody multipartBody );

    @POST()
    Observable<ResponseBody> postWithNoForm(@Url() String url,  @Header("token")String token, @QueryMap Map<String, Object> maps);

    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> postForm(@Url() String url, @FieldMap Map<String, Object> maps);

}
