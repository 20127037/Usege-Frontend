package com.group_1.usege.syncing.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.group_1.usege.R;

public class SyncingActivity extends AppCompatActivity {

    TextView textViewSync;
    ImageView imageViewBackward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncing);

        textViewSync = findViewById(R.id.txtView_sync);
        textViewSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenSetUpSyncingBottomSheetDialog();
            }
        });
    }

    private void clickOpenSetUpSyncingBottomSheetDialog() {
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_set_up_syncing, null);

        final BottomSheetDialog setUpSyncingBottomSheetDialog = new BottomSheetDialog(this);
        setUpSyncingBottomSheetDialog.setContentView(viewDialog);
        setUpSyncingBottomSheetDialog.show();

        imageViewBackward = viewDialog.findViewById(R.id.image_view_backward);
        imageViewBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpSyncingBottomSheetDialog.dismiss();
            }
        });
    }
}
