package com.group_1.usege.userInfo.model;

import android.content.Context;

import com.group_1.usege.R;

public class StoragePlan {
    private String name;
    private float price;
    private boolean canPurchased;
    private StoragePlanAbility[] abilities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getFormatPrice(Context context)
    {
        return price > 0 ? String.format("%f$", price) : context.getString(R.string.label_free);
    }

    public String getDescriptionMultiline(int countLine)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < abilities.length && i < countLine; i++)
            builder.append(abilities[i].getDescription()).append('\n');
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }


    public StoragePlanAbility[] getAbilities() {
        return abilities;
    }

    public void setAbilities(StoragePlanAbility[] abilities) {
        this.abilities = abilities;
    }

    public boolean isCanPurchased() {
        return canPurchased;
    }

    public void setCanPurchased(boolean canPurchased) {
        this.canPurchased = canPurchased;
    }
}

