package com.group_1.usege.library.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group_1.usege.R;
import com.group_1.usege.api.apiservice.ApiService;
import com.group_1.usege.api.apiservice.FileServiceGenerator;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.library.service.MasterFileServiceGenerator;
import com.group_1.usege.model.Image;
import com.group_1.usege.model.UserFile;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.activities.AuthApiCallerActivity;
import com.group_1.usege.utilities.dto.ErrorResponse;
import com.group_1.usege.utilities.modules.ActivityModule;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnlineImageActivity extends AuthApiCallerActivity<UserFile> {

    private static String IMG_PREFIX_NAME = "PEXELS";
    public static  final String IMG_KEY = "IMG_KEY";
    private MaterialToolbar barTop;
    private FloatingActionButton btnDownload;
    private ImageView viewImg;
    private Image receivedImg;
    @Inject
    @ActivityModule.BigPlaceHolder
    public RequestManager glide;
    @Inject
    public MasterFileServiceGenerator masterFileServiceGenerator;
    @Inject
    public FileServiceGenerator fileServiceGenerator;
    @Inject
    public TokenRepository tokenRepository;

    private static String getPexelsName(String id){
        return String.format("%s-%s", IMG_PREFIX_NAME, id);
    }

    public OnlineImageActivity()
    {
        super(R.layout.activity_online_image);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        barTop = findViewById(R.id.bar_top);
        btnDownload = findViewById(R.id.btn_download);
        viewImg = findViewById(R.id.view_img);
        barTop.setNavigationOnClickListener(v -> {
            finish();
        });
        Bundle transBundle = getIntent().getBundleExtra(ActivityUtilities.TRAN_ACT_BUNDLE);
        if (transBundle != null)
        {
            Image receivedImg = transBundle.getParcelable(IMG_KEY);
            if (receivedImg != null)
                showImg(receivedImg);
        }
    }

    public void showImg(Image image)
    {
        this.receivedImg = image;
        showImgInternal();
    }
    private void showImgInternal()
    {
        barTop.setTitle(receivedImg.getDescription());
        glide.load(receivedImg.getUri()).into(viewImg);
        startCallApi(masterFileServiceGenerator
                .getService()
                .getFile(tokenRepository.getToken().getUserId(), getPexelsName(receivedImg.getId()), true));
        btnDownload.setOnClickListener(l ->
                startCallApi(fileServiceGenerator
                        .getService()
                        .uploadRefFile(tokenRepository.getToken().getUserId(),
                                ApiService.UserFileRefUploadDto.builder()
                                        .fileName(getPexelsName(receivedImg.getId()))
                                        .description(receivedImg.getDescription())
                                        .tinyUri(receivedImg.getSmallUri().toString())
                                        .uri(receivedImg.getUri().toString())
                                        .build())));
    }

    @Override
    protected void handleCallFail(ErrorResponse errorResponse)
    {
        if (errorResponse.getStatus() == 404)
        {
            btnDownload.setImageResource(R.drawable.ic_file_download);
            btnDownload.setClickable(true);
        }
        else
            super.handleCallFail(errorResponse);
    }

    @Override
    protected void handleCallSuccess(UserFile body) {
        btnDownload.setImageResource(R.drawable.ic_file_downloaded);
        btnDownload.setClickable(false);
    }
}