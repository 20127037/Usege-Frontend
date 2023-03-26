package com.group_1.usege.syncing.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.group_1.usege.R;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity implements MainCallBacks{

    FragmentTransaction ft;
    ImageCardFragment imageCardFragment;
    EmptyFragment emptyFragment;
    ImageView imageViewBackward, imgViewUpload;
    Button btnConfirm;
    List<Uri> uriList = new ArrayList<>();

    private static final int Read_Permission = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        ft = getSupportFragmentManager().beginTransaction();
        emptyFragment = EmptyFragment.newInstance();
        ft.replace(R.id.layout_display_images, emptyFragment).commit();

        imgViewUpload = findViewById(R.id.icon_cloud_upload);
        imgViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenSetUpSyncingBottomSheetDialog();
            }
        });
    }

    public void clickOpenSetUpSyncingBottomSheetDialog() {
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_set_up_syncing, null);

        // Yêu cầu quyền truy cập
        requestPermission();

        final BottomSheetDialog setUpSyncingBottomSheetDialog = new BottomSheetDialog(this);
        setUpSyncingBottomSheetDialog.setContentView(viewDialog);
        setUpSyncingBottomSheetDialog.show();

        btnConfirm = viewDialog.findViewById(R.id.btn_confirm);
        imageViewBackward = viewDialog.findViewById(R.id.image_view_backward);
        imageViewBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpSyncingBottomSheetDialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                //startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                pickImageLauncher.launch(Intent.createChooser(intent, "Select Picture"));

                // Đóng bottommsheet
                setUpSyncingBottomSheetDialog.dismiss();

                // Library fragment thay thế
                ft = getSupportFragmentManager().beginTransaction();
                imageCardFragment = ImageCardFragment.newInstance();
                ft.replace(R.id.layout_display_images, imageCardFragment).commit();
            }
        });
    }

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (result.getResultCode() == RESULT_OK && data != null) {

                    if (data.getClipData() != null) {
                        int countOfImages = data.getClipData().getItemCount();

                        for (int i = 0; i < countOfImages; i++) {
                            uriList.add(data.getClipData().getItemAt(i).getUri());
                        }

                    } else {
                        Uri imageURL = data.getData();
                        uriList.add(imageURL);
                    }
                }
                else {
                    Toast.makeText(this, "You haven't pick any images", Toast.LENGTH_LONG).show();
                }
                sendUriList(uriList, uriList.size());
            });

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }
    }

    @Override
    public void sendUriList(List<Uri> uriList, int size) {
        imageCardFragment.receiveUriList(uriList, size);
    }
}