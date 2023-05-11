package com.group_1.usege.api.apiservice;

import android.content.Context;

import com.group_1.usege.dto.ImageDto;
import com.group_1.usege.model.UserFile;

public class ApiUpdateFile {
    private Context context;
    private String userId;
    private UserFile userFile;
    private String accessToken;

    private FileServiceGenerator fileServiceGenerator;

    public ApiUpdateFile(Context context, String accessToken, String userId, UserFile userFile) {
        this.context = context;
        this.userId = userId;
        this.userFile = userFile;
        this.accessToken = accessToken;
    }

    public void callApiUpdateFile() {

    }
}
