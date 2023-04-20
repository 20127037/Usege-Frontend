package com.group_1.usege.userInfo.model;

public class StoragePlanAbility {
    private String description;
    private boolean isCovered;

    public StoragePlanAbility(String description, boolean isCovered) {
        this.description = description;
        this.isCovered = isCovered;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCovered() {
        return isCovered;
    }

    public void setCovered(boolean covered) {
        this.isCovered = covered;
    }
}
