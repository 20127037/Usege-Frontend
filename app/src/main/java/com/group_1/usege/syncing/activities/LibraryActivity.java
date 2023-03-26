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
import com.group_1.usege.layout.fragment.EmptyFragment;
import com.group_1.usege.layout.fragment.ImageCardFragment;
import com.group_1.usege.layout.fragment.ImageListFragment;
import com.group_1.usege.modle.Image;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    FragmentTransaction ft;
    ImageCardFragment imageCardFragment;
    ImageListFragment imageListFragment;
    EmptyFragment emptyFragment = new EmptyFragment();
    ImageView imageViewBackward, imgViewUpload, imgViewCard, imgViewList;
    Button btnConfirm;
    List<Image> imgList = new ArrayList<>();
    private boolean firstAccess = true;

    private int displayView = 1;
    private static final int Read_Permission = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        ft = getSupportFragmentManager().beginTransaction();
        emptyFragment = EmptyFragment.newInstance();
        ft.replace(R.id.layout_display_images, emptyFragment).commit();

        imgViewCard = findViewById(R.id.icon_card);
        imgViewList = findViewById(R.id.icon_list);
        imgViewUpload = findViewById(R.id.icon_cloud_upload);

        imgViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenSetUpSyncingBottomSheetDialog();
            }
        });

        imgViewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewList.setAlpha(0.5F);
                imgViewCard.setAlpha(1F);

                firstAccess = true;
                displayView = 1;
                updateViewDisplay();
            }
        });

        imgViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewList.setAlpha(1F);
                imgViewCard.setAlpha(0.5F);

                firstAccess = true;
                displayView = 2;
                updateViewDisplay();
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


            }
        });
    }

    public void updateViewDisplay() {
        // Image Card fragment thay thế
        // 1 - CARD VIEW
        // 2 - LIST VIEW
        if (displayView == 1) {
            if (firstAccess == true) {
                ft = getSupportFragmentManager().beginTransaction();
                imageCardFragment = ImageCardFragment.newInstance(imgList);
                ft.replace(R.id.layout_display_images, imageCardFragment).commit();

                firstAccess = false;
            }
            else {
                imageCardFragment.recycleAdapter.notifyDataSetChanged();
            }
        }
        else if (displayView == 2) {
            if (firstAccess == true) {
                ft = getSupportFragmentManager().beginTransaction();
                imageListFragment = ImageListFragment.newInstance(imgList);
                ft.replace(R.id.layout_display_images, imageListFragment).commit();

                firstAccess = false;
            }
            else {
                imageListFragment.recycleAdapter.notifyDataSetChanged();
            }
        }
    }

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (result.getResultCode() == RESULT_OK && data != null) {

                    if (data.getClipData() != null) {
                        int countOfImages = data.getClipData().getItemCount();

                        for (int i = 0; i < countOfImages; i++) {
                            Image image = new Image("", 0F, "Favorite", "", data.getClipData().getItemAt(i).getUri());
                            imgList.add(image);
                        }

                    } else {
                        Uri imageURL = data.getData();

                        Image image = new Image("", 0F, "Favorite", "", imageURL);
                        imgList.add(image);
                    }
                }
                else {
                    Toast.makeText(this, "You haven't pick any images", Toast.LENGTH_LONG).show();
                }
                //sendUriList(uriList, uriList.size());
                // Update Fragment View
                updateViewDisplay();
            });

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }
    }
}