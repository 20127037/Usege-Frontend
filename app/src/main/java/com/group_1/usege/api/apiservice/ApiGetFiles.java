package com.group_1.usege.api.apiservice;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.group_1.usege.dto.ImageDto;
import com.group_1.usege.dto.LoadFileRequestDto;
import com.group_1.usege.layout.adapter.ListAdapter;
import com.group_1.usege.model.Image;
import com.group_1.usege.model.UserFile;
import com.group_1.usege.userInfo.services.MasterServiceGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiGetFiles {
    private MasterFileServiceGenerator masterFileServiceGenerator;
    private Context context;
    private String userId;
    private String accessToken;
    private LoadFileRequestDto loadFileRequestDto;
    private int limit;
    private String[] attributes;
    private Map<String, String> lastKey;
    private List<Image> lstImage;

    public ApiGetFiles (Context context, String userId, String accessToken, LoadFileRequestDto loadFileRequestDto, List<Image> lstImage) {
        this.context = context;
        this.userId = userId;
        this.accessToken = accessToken;
        this.loadFileRequestDto = loadFileRequestDto;
        this.lstImage = lstImage;
    }

    public void callApiGetFiles() {
        masterFileServiceGenerator = new MasterFileServiceGenerator(context);

        limit = loadFileRequestDto.getLimit();
        attributes = loadFileRequestDto.getAttributes();
        lastKey = loadFileRequestDto.getLastKey();

        //LoadFileRequestDto loadFileRequestDto = new LoadFileRequestDto(6, null, null);

        Call<MasterFileService.QueryResponse<UserFile>> call = masterFileServiceGenerator
                                                                        .getService(accessToken)
                                                                        .getFiles(userId, false, limit, attributes, lastKey);

        call.enqueue(new Callback<MasterFileService.QueryResponse<UserFile>>() {
            @Override
            public void onResponse(Call<MasterFileService.QueryResponse<UserFile>> call, Response<MasterFileService.QueryResponse<UserFile>> response) {
                if (response.isSuccessful()) {
                   MasterFileService.QueryResponse<UserFile> userFileQueryResponse = response.body();
                     //Xử lý dữ liệu trả về ở đây
                    List<UserFile> userFiles = new ArrayList<>();
                    userFiles.addAll(userFileQueryResponse.getResponse());

                    for (UserFile userFile : userFiles) {
                        Image image = new Image(userFile.getFileName(),
                                                userFile.getTags(),
                                                userFile.getDescription(),
                                                userFile.getDate(),
                                                userFile.getSizeInKb(),
                                                userFile.getLocation(),
                                Uri.parse("content://com.android.providers.media.documents/document/image%3A36"));

                        lstImage.add(image);
                    }

                    Map<String, String> nextEvaluatedKey = new HashMap<>(userFileQueryResponse.getNextEvaluatedKey());
                    loadFileRequestDto.setLastKey(nextEvaluatedKey);
                } else {
                    // Xử lý lỗi ở đây
                }
                Toast.makeText(context, "Call API Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<MasterFileService.QueryResponse<UserFile>> call, Throwable t) {

            }
        });
    }
}
