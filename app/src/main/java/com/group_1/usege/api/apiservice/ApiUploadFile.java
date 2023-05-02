package com.group_1.usege.api.apiservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.group_1.usege.api.googlemaps.GeocodeResponse;
import com.group_1.usege.dto.ImageDto;
import com.group_1.usege.library.activities.LibraryActivity;

import java.io.File;
import java.lang.reflect.Field;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class ApiUploadFile {

    private Context context;
    private String userId;
    private ImageDto imageDto;
    private String pathFile;
    public ApiUploadFile(Context context, String userId, ImageDto imageDto, String pathFile) {
        this.context = context;
        this.userId = userId;
        this.imageDto = imageDto;
        this.pathFile = pathFile;
    }

    public void callApiUploadFile() {

        //Khởi tạo retrofit
        //http://localhost:8083/api/v1/file/4fc96649-4e07-4f80-8ce4-326056fddc7f/upload
        Gson gson = new Gson();

        ApiService apiService = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8083/api/v1/file/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        File file = new File(pathFile);
        String imageDtoJson = gson.toJson(imageDto);
        // Tạo request body cho object Image Dto
        Log.e("User Id", userId);
        Log.e("ImageDto", imageDtoJson);
        Log.e("File", "" + file);
        Log.e("ReadPath", pathFile);

        RequestBody requestBodyObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), imageDtoJson);
        RequestBody requestBodyFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), requestBodyFile);
        Log.i("File", "" + file.getName());

        Call<ResponseBody> call = apiService.uploadFile(userId, requestBodyObject, multipartBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context, "Call API Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Call API Fail", Toast.LENGTH_LONG).show();
            }
        });

    }
}
