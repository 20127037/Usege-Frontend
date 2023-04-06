package com.group_1.usege.utilities.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Usege_Dialogue_FullScreenDialog);
    }

//    @Override
//    public int getTheme() {
//        return R.style.Usege_Dialogue_FullScreenDialog;
//    }



//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return new AlertDialog.Builder(requireContext())
//                .setView(R.layout.fragment_busy_handling_progress)
//                .setCancelable(false)
//                .create();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busy_handling_progress, container, false);
        return view;
    }
}