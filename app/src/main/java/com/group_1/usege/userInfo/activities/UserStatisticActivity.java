package com.group_1.usege.userInfo.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.group_1.usege.R;
import com.group_1.usege.userInfo.model.UserStatistic;
import com.group_1.usege.userInfo.services.MasterServiceGenerator;
import com.group_1.usege.utilities.activities.NavigatedAuthApiCallerActivity;
import com.group_1.usege.utilities.math.MathUtilities;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class UserStatisticActivity extends NavigatedAuthApiCallerActivity<UserStatistic> {

    private TextView txtUsedSpace;
    private ProgressBar progressUsedSpace;
    private TextView txtCountImg;
    private TextView txtCountAlbums;
    @Inject
    public MasterServiceGenerator masterServiceGenerator;

    public UserStatisticActivity()
    {
        super(R.layout.activity_user_statistic);
    }

    @Override
    public int navigateId()
    {
        return R.id.nav_statistic;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // handle toggle Menu
        txtUsedSpace = findViewById(R.id.txt_used_space);
        progressUsedSpace = findViewById(R.id.progress_used_space);
        txtCountAlbums = findViewById(R.id.txt_total_albums);
        txtCountImg = findViewById(R.id.txt_total_imgs);
    }

    protected void onResume()
    {
        super.onResume();
//        handleCallSuccess(new UserStatistic(10000000, MathUtilities.gbToKb(15), 100, 100));
        try {
            startCallApi(masterServiceGenerator
                    .getService(tokenRepository.getToken().getBearerAccessToken())
                    .getUserStatistic(tokenRepository.getToken().getUserId()));
        }
        catch (Exception e)
        {
            Log.e("UserStatistic", e.getMessage());
        }
    }

    private void setUsedSpace(long usedSpace, long maxSpace)
    {
        double usedSpaceInGb = MathUtilities.kbToGb(usedSpace);
        double maxSpaceInGb = MathUtilities.kbToGb(maxSpace);
        String usedSpaceRep = String.format(Locale.getDefault(),"%.2f/%d GB", usedSpaceInGb, (int)maxSpaceInGb);
        txtUsedSpace.setText(usedSpaceRep);
        progressUsedSpace.setMax((int)maxSpace);
        progressUsedSpace.setProgress((int)usedSpace);
    }

    @Override
    protected void handleCallSuccess(UserStatistic body) {
        setUsedSpace(body.getUsedSpaceInKb(), body.getMaxSpaceInKb());
        txtCountImg.setText(String.valueOf(body.getCountImg()));
        txtCountAlbums.setText(String.valueOf(body.getCountAlbum()));
    }
}