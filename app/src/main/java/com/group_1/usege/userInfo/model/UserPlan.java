package com.group_1.usege.userInfo.model;

public class UserPlan {

    public String getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(String currentPlan) {
        this.currentPlan = currentPlan;
    }

    public StoragePlan[] getPlans() {
        return plans;
    }

    public void setPlans(StoragePlan[] plans) {
        this.plans = plans;
    }

    private String currentPlan;
    private StoragePlan[] plans;
}
