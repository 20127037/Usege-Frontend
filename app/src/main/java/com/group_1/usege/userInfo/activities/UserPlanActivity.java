package com.group_1.usege.userInfo.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.group_1.usege.R;
import com.group_1.usege.userInfo.fragments.StoragePlanDetailsFragment;
import com.group_1.usege.userInfo.fragments.StoragePlanListFragment;
import com.group_1.usege.userInfo.model.StoragePlan;
import com.group_1.usege.userInfo.model.UserPlan;
import com.group_1.usege.userInfo.repository.UserInfoRepository;
import com.group_1.usege.userInfo.services.MasterUserServiceGenerator;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.activities.NavigatedAuthApiCallerActivity;
import com.group_1.usege.utilities.collection.CollectionUtilities;
import com.group_1.usege.utilities.interfaces.BackSignalReceiver;
import com.group_1.usege.utilities.interfaces.SubmitSignalReceiver;
import com.group_1.usege.utilities.interfaces.ViewDetailsSignalByIdReceiver;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserPlanActivity extends NavigatedAuthApiCallerActivity<StoragePlan[]> implements BackSignalReceiver, ViewDetailsSignalByIdReceiver, SubmitSignalReceiver {
    private StoragePlanListFragment fragPlanList;
    private StoragePlanDetailsFragment fragPlanDetails;
    @Inject
    public MasterUserServiceGenerator masterServiceGenerator;
    @Inject
    public UserInfoRepository userInfoRepository;
    private FragmentManager fm;

    private StoragePlan[] cachePlan;

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
        fm.beginTransaction()
                .show(fragPlanList)
                .hide(fragPlanDetails)
                .commit();
    }
    @Override
    public int navigateId()
    {
        return R.id.nav_plan;
    }

    protected void onResume()
    {
        super.onResume();
//        handleCallSuccess(new StoragePlan[]{
//                new StoragePlan("Basic", 0, false, 0, new StoragePlanAbility[]{
//                        new StoragePlanAbility("A", true),
//                        new StoragePlanAbility("B", true),
//                        new StoragePlanAbility("C", false)
//                }),
//                new StoragePlan("Standard", 103, false, 1, new StoragePlanAbility[]{
//                        new StoragePlanAbility("A", true),
//                        new StoragePlanAbility("B", true),
//                        new StoragePlanAbility("C", true)
//                }),
//                new StoragePlan("Premium", 206, true, 2, new StoragePlanAbility[]{
//                        new StoragePlanAbility("A", true),
//                        new StoragePlanAbility("B", true),
//                        new StoragePlanAbility("C", true)
//                })
//        });
        try {
            startCallApi(masterServiceGenerator
                    .getService(tokenRepository.getToken().getBearerAccessToken())
                    .getUserPlan(tokenRepository.getToken().getUserId()));
        }
        catch (Exception exception)
        {
            Log.e("UserPlanActivity", exception.getMessage());
        }
    }

    @Override
    protected void handleCallSuccess(StoragePlan[] body) {
        cachePlan = body;
        fragPlanList.setUserPlan(new UserPlan(userInfoRepository.getInfo().getPlan(), body));
//        fragPlanList.setUserPlan(new UserPlan("Standard", body));
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
        StoragePlan plan = CollectionUtilities.find(cachePlan, e -> e.getName().equals(id));
        fragPlanDetails.setPlan(plan);
    }

    @Override
    public void submit(String id) {
        StoragePlan plan = CollectionUtilities.find(cachePlan, e -> e.getName().equals(id));
        Bundle bundle = new Bundle();
        bundle.putString(PaymentActivity.PLAN_NAME_KEY, plan.getName());
        bundle.putFloat(PaymentActivity.PRICE_KEY, plan.getPrice());
        ActivityUtilities.TransitActivity(this, PaymentActivity.class, bundle);
    }
}