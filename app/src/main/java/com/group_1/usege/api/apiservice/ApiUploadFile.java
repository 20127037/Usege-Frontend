package com.group_1.usege.api.apiservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.group_1.usege.dto.ImageDto;
import com.group_1.usege.model.UserFile;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUploadFile {

    private Context context;
    private final String userId;
    private final ImageDto imageDto;
    private final String pathFile;

    private String accessToken;

    private final FileServiceGenerator fileServiceGenerator;
    public ApiUploadFile(FileServiceGenerator fileServiceGenerator, String userId, ImageDto imageDto, String pathFile) {
        this.context = context;
        this.userId = userId;
        this.imageDto = imageDto;
        this.pathFile = pathFile;
        this.accessToken = accessToken;
        this.fileServiceGenerator = fileServiceGenerator;
    }

    public void callApiUploadFile() {

////        tokenHttpClient.addInterceptor(chain -> {
////            Request original = chain.request();
////            Request.Builder builder1 = original.newBuilder()
////                    .header("Authorization", "Bearer " + token);
////            Request request = builder1.build();
////            return chain.proceed(request);
////        });
//        //Khởi tạo retrofit
//        //http://localhost:8083/api/v1/file/4fc96649-4e07-4f80-8ce4-326056fddc7f/upload
//        Gson gson = new Gson();
//
//        ApiService apiService = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8083/api/v1/file/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(ApiService.class);

        Gson gson = new Gson();
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

        Call<UserFile> call = fileServiceGenerator.getService().uploadFile(userId, requestBodyObject, multipartBody);
        //Call<ResponseBody> call = apiService.uploadFile(userId, requestBodyObject, multipartBody);
        call.enqueue(new Callback<UserFile>() {
            @Override
            public void onResponse(Call<UserFile> call, Response<UserFile> response) {
//                Toast.makeText(context, "Call API Successfully", Toast.LENGTH_LONG).show();
                System.out.println("Call API Successfully");
            }

            @Override
            public void onFailure(Call<UserFile> call, Throwable t) {
                //Toast.makeText(context, "Call API Fail", Toast.LENGTH_LONG).show();
                System.out.println("Call API Failure");
            }
        });

    }
}
