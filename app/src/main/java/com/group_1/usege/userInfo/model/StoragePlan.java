package com.group_1.usege.userInfo.model;

import android.content.Context;

import com.group_1.usege.R;

public class StoragePlan {
    private String name;
    private float price;
    private boolean canPurchased;
    private int order;
    private StoragePlanAbility[] abilities;

    public StoragePlan(String name, float price, boolean canPurchased, int order, StoragePlanAbility[] abilities) {
        this.name = name;
        this.price = price;
        this.canPurchased = canPurchased;
        this.order = order;
        this.abilities = abilities;
    }

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
        if (price <= 0)
            return context.getString(R.string.label_free);
        String format = price % 1.0 != 0 ?  "%s$" : "%.0f$";
        return String.format(format, price);
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String toString()
    {
        return name;
    }
}

