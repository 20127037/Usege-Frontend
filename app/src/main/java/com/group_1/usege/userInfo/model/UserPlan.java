package com.group_1.usege.userInfo.model;

public class UserPlan {
    private String currentPlan;
    private StoragePlan[] plans;

    public UserPlan(String currentPlan, StoragePlan[] plans) {
        this.currentPlan = currentPlan;
        this.plans = plans;
    }

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
}
