package com.group_1.usege.utilities.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.group_1.usege.R;

/**
 * The fragment for showing circle while asynchronously waiting data from external resource
 */
public class BusyHandlingProgressFragment extends DialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setView(R.layout.fragment_busy_handling_progress)
                .setCancelable(false)
                .create();
    }

}