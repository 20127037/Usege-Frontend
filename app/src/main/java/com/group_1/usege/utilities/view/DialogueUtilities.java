package com.group_1.usege.utilities.view;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.group_1.usege.R;

public class DialogueUtilities {

    public static void showNormalDialogue(Context context, @StringRes int msg, @Nullable final DialogInterface.OnClickListener listener)
    {
        new MaterialAlertDialogBuilder(context)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, listener)
                .show();
    }

    public static void showConfirmDialogue(Context context, @StringRes int msg,
                                           @Nullable final DialogInterface.OnClickListener yesListener,
                                           @Nullable final DialogInterface.OnClickListener noListener)
    {
        new MaterialAlertDialogBuilder(context)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, yesListener)
                .setNegativeButton(R.string.no, noListener)
                .show();
    }
}
