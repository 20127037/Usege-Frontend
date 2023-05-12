//package com.group_1.usege.api.apiservice;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.group_1.usege.dto.ImageDto;
//import com.group_1.usege.model.UserFile;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ApiUpdateFile {
//    private Context context;
//    private String userId;
//    private UserFile userFile;
//    private String accessToken;
//
//    private FileServiceGenerator fileServiceGenerator;
//
//    public ApiUpdateFile(Context context, String accessToken, String userId, UserFile userFile) {
//        this.context = context;
//        this.userId = userId;
//        this.userFile = userFile;
//        this.accessToken = accessToken;
//    }
//
//    public void callApiUpdateFile() {
//        fileServiceGenerator = new FileServiceGenerator(context);
//        Gson gson = new Gson();
//        //String userFileJson = gson.toJson(userFile);
//
//        Call<UserFile> call = fileServiceGenerator.getService(accessToken).updateFile(userId, userFile);
//        call.enqueue(new Callback<UserFile>() {
//            @Override
//            public void onResponse(Call<UserFile> call, Response<UserFile> response) {
//                Log.i("Update -----> ", "Update successfully");
//            }
//
//            @Override
//            public void onFailure(Call<UserFile> call, Throwable t) {
//
//            }
//        });
//    }
//}
