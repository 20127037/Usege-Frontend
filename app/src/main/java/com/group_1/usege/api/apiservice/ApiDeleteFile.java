package com.group_1.usege.api.apiservice;

import android.content.Context;

import com.group_1.usege.dto.ImageDto;
import com.group_1.usege.model.UserFile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiDeleteFile {
    private Context context;
    private final String userId;
    private String[] fileNames;

    private String accessToken;
    private final FileServiceGenerator fileServiceGenerator;

    public ApiDeleteFile(FileServiceGenerator fileServiceGenerator, String userId, String[] fileNames) {
        this.userId = userId;
        this.fileNames = fileNames;
        this.fileServiceGenerator = fileServiceGenerator;
    }

    public void callApiDeleteFile() {
        Call<List<UserFile>> call = fileServiceGenerator.getService().deleteFile(userId, fileNames);
        call.enqueue(new Callback<List<UserFile>>() {
            @Override
            public void onResponse(Call<List<UserFile>> call, Response<List<UserFile>> response) {

            }

            @Override
            public void onFailure(Call<List<UserFile>> call, Throwable t) {

            }
        });
    }
}
