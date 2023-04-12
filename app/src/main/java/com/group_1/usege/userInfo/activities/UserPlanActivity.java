package com.group_1.usege.userInfo.activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.group_1.usege.R;
import com.group_1.usege.userInfo.fragments.StoragePlanDetailsFragment;
import com.group_1.usege.userInfo.fragments.StoragePlanListFragment;
import com.group_1.usege.userInfo.model.StoragePlan;
import com.group_1.usege.userInfo.model.UserPlan;
import com.group_1.usege.userInfo.repository.UserInfoRepository;
import com.group_1.usege.userInfo.services.MasterServiceGenerator;
import com.group_1.usege.utilities.activities.AuthApiCallerActivity;
import com.group_1.usege.utilities.collection.CollectionUtilities;
import com.group_1.usege.utilities.interfaces.BackSignalReceiver;
import com.group_1.usege.utilities.interfaces.ViewDetailsSignalReceiver;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserPlanActivity extends AuthApiCallerActivity<UserPlan> implements BackSignalReceiver, ViewDetailsSignalReceiver {

    private StoragePlanListFragment fragPlanList;
    private StoragePlanDetailsFragment fragPlanDetails;
    @Inject
    public MasterServiceGenerator masterServiceGenerator;
    @Inject
    public UserInfoRepository userInfoRepository;
    private FragmentManager fm;

    private UserPlan cachePlan;

    public UserPlanActivity()
    {
        super(R.layout.activity_user_plan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getSupportFragmentManager();
        fragPlanList = (StoragePlanListFragment)fm.findFragmentById(R.id.frag_plan_list);
        fragPlanDetails = (StoragePlanDetailsFragment)fm.findFragmentById(R.id.frag_plan_details);
    }

    protected void onResume()
    {
        super.onResume();
        startCallApi(masterServiceGenerator
                .getService(tokenRepository.getToken().getAccessToken())
                .getUserPlan(userInfoRepository.getInfo().getUserId()));
    }

    @Override
    protected void handleCallSuccess(UserPlan body) {
        cachePlan = body;
        fragPlanList.setUserPlan(cachePlan);
    }

    @Override
    public void back() {
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(fragPlanList)
                .hide(fragPlanDetails)
                .commit();
    }

    @Override
    public void view(String id) {
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(fragPlanDetails)
                .hide(fragPlanList)
                .commit();
        StoragePlan plan = CollectionUtilities.find(cachePlan.getPlans(), e -> e.getName().equals(id));
        fragPlanDetails.setPlan(plan);
    }
}