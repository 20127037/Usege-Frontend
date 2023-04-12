package com.group_1.usege.layout.adapter;

import android.content.Context;

import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.model.Image;

import java.util.List;

public class TrashCardAdapter extends CardAdapter{
    public TrashCardAdapter(List<Image> lstImage, Context context, IClickItemImageListener listener) {
        super(lstImage, context, listener);
    }
}
