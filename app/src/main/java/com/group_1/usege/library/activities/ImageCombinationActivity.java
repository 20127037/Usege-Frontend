package com.group_1.usege.library.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.group_1.usege.R;
import com.group_1.usege.layout.fragment.ImageResourceQueueCardFragment;
import com.group_1.usege.model.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageCombinationActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    Context context;
    public List<Image> selectedImages = new ArrayList<>();
    LinearLayout imageContainerLinearLayout;
    LinearLayout resourceQueueLayout;
    FragmentTransaction ft;
    ImageResourceQueueCardFragment imageResourceQueueCardFragment;
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_combination);
        context = getApplicationContext();
        Bundle bundle = getIntent().getExtras();
        selectedImages = bundle.getParcelableArrayList("data");

        imageContainerLinearLayout = findViewById(R.id.image_container_linear_layout);
        resourceQueueLayout = findViewById(R.id.resource_queue_linear_layout);

        ft = getSupportFragmentManager().beginTransaction();
        imageResourceQueueCardFragment = ImageResourceQueueCardFragment.newInstance(selectedImages);
        ft.replace(R.id.resource_queue_linear_layout, imageResourceQueueCardFragment).commit();
    }

    public void createNewImageView(ImageView imageView, Uri dragData) {
        Glide.with(context)
                .load(dragData)
                .apply(new RequestOptions().centerCrop())
                .into(imageView);

        imageView.setOnLongClickListener(containerImageView -> {
            imageView.setImageResource(0);
            return true;
        });
    }
    View.OnDragListener myDragListener = (v, e) -> {
        ImageView imageView = (ImageView) v;

        switch(e.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    v.invalidate();
                    return true;
                }
                return false;
            case DragEvent.ACTION_DROP:
                ClipData.Item resourceItem = e.getClipData().getItemAt(0);
                Uri dragData = resourceItem.getUri();
                v.invalidate();

                createNewImageView(imageView, dragData);

                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                v.invalidate();
                return true;
            default:
                Log.e("In image combination activity","Unknown action type received by View.OnDragListener.");
                break;
        }
        return false;
    };

    public void backToPreviousActivity(View v) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("action", "back");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private Boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        return false;
    }

    Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void reSketchContainerLayout(int numberOfItems) {
        int containerWidth = imageContainerLinearLayout.getWidth();
        int containerHeight = imageContainerLinearLayout.getHeight();

        switch (numberOfItems) {
            case 2:
                ImageView newContainerImageView1 = new ImageView(context);
                ViewGroup.MarginLayoutParams layoutParams1 = new ViewGroup.MarginLayoutParams(containerWidth / 2 - 40, (int) (containerHeight * 0.8));
                layoutParams1.setMargins(20, (int) (containerHeight * 0.1), 10, 0);
                newContainerImageView1.setLayoutParams(layoutParams1);
                newContainerImageView1.setBackgroundColor(Color.parseColor("#3c4045"));
                imageContainerLinearLayout.addView(newContainerImageView1);
                newContainerImageView1.setOnDragListener(myDragListener);

                ImageView newContainerImageView2 = new ImageView(context);
                ViewGroup.MarginLayoutParams layoutParams2 = new ViewGroup.MarginLayoutParams(containerWidth / 2 - 40, (int) (containerHeight * 0.8));
                layoutParams2.setMargins(10, (int) (containerHeight * 0.1), 20, 0);
                newContainerImageView2.setLayoutParams(layoutParams2);
                newContainerImageView2.setBackgroundColor(Color.parseColor("#3c4045"));
                imageContainerLinearLayout.addView(newContainerImageView2);
                newContainerImageView2.setOnDragListener(myDragListener);

                break;

            case 3:
                ImageView newContainerImageView3 = new ImageView(context);
                ViewGroup.MarginLayoutParams layoutParams3 = new ViewGroup.MarginLayoutParams(containerWidth / 2 - 40, (int) (containerHeight * 0.8));
                layoutParams3.setMargins(20, (int) (containerHeight * 0.1), 10, 0);
                newContainerImageView3.setLayoutParams(layoutParams3);
                newContainerImageView3.setBackgroundColor(Color.parseColor("#3c4045"));
                imageContainerLinearLayout.addView(newContainerImageView3);
                newContainerImageView3.setOnDragListener(myDragListener);

                LinearLayout rightLinearLayout = new LinearLayout(this);
                rightLinearLayout.setOrientation(LinearLayout.VERTICAL);
                ViewGroup.MarginLayoutParams layoutParams4 = new ViewGroup.MarginLayoutParams(containerWidth / 2 - 40, (int) (containerHeight * 0.8));
                layoutParams4.setMargins(20, (int) (containerHeight * 0.1), 10, 0);
                rightLinearLayout.setLayoutParams(layoutParams4);

                ImageView newContainerImageView4 = new ImageView(context);
                ViewGroup.MarginLayoutParams layoutParams5 = new ViewGroup.MarginLayoutParams(containerWidth / 2 - 40, (int) (containerHeight * 0.4));
                layoutParams5.setMargins(0, 0, 0, 20);
                newContainerImageView4.setLayoutParams(layoutParams5);
                newContainerImageView4.setBackgroundColor(Color.parseColor("#3c4045"));
                rightLinearLayout.addView(newContainerImageView4);
                newContainerImageView4.setOnDragListener(myDragListener);

                ImageView newContainerImageView5 = new ImageView(context);
                ViewGroup.MarginLayoutParams layoutParams6 = new ViewGroup.MarginLayoutParams(containerWidth / 2 - 40, (int) (containerHeight * 0.4));
                layoutParams6.setMargins(0, 20, 0, 0);
                newContainerImageView5.setLayoutParams(layoutParams5);
                newContainerImageView5.setBackgroundColor(Color.parseColor("#3c4045"));
                rightLinearLayout.addView(newContainerImageView5);
                newContainerImageView5.setOnDragListener(myDragListener);

                imageContainerLinearLayout.addView(rightLinearLayout);

                break;
        }
    }
    public void changeContainerLayout(View v) {
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_change_combination_container_layout, null);

        final BottomSheetDialog containerLayoutChangingBottomSheetDialog = new BottomSheetDialog(this);
        containerLayoutChangingBottomSheetDialog.setContentView(viewDialog);
        containerLayoutChangingBottomSheetDialog.show();

        TextView backwardButton;
        backwardButton = viewDialog.findViewById(R.id.backward_from_combination_container_layout_bottom_sheet);
        backwardButton.setOnClickListener(e -> containerLayoutChangingBottomSheetDialog.dismiss());

        ImageView layout2Items, layout3Items, layout4Items, layout5Items;
        layout2Items = viewDialog.findViewById(R.id.combination_container_layout_2_items);
        layout3Items = viewDialog.findViewById(R.id.combination_container_layout_3_items);
        layout4Items = viewDialog.findViewById(R.id.combination_container_layout_4_items);
        layout5Items = viewDialog.findViewById(R.id.combination_container_layout_5_items);

        View.OnClickListener onClickListener = v1 -> {
            clearContainer(null);

            int selectedLayoutID = v1.getId();

            if (selectedLayoutID == layout2Items.getId()) {
                reSketchContainerLayout(2);
            }
            else if (selectedLayoutID == layout3Items.getId()) {
                reSketchContainerLayout(3);
            }
            else if (selectedLayoutID == layout4Items.getId()) {
                reSketchContainerLayout(4);
            }
            else if (selectedLayoutID == layout5Items.getId()) {
                reSketchContainerLayout(5);
            }
        };

        layout2Items.setOnClickListener(onClickListener);
        layout3Items.setOnClickListener(onClickListener);
        layout4Items.setOnClickListener(onClickListener);
        layout5Items.setOnClickListener(onClickListener);
    }

    public void combineImagesFromContainer(View v) {
        if (imageContainerLinearLayout.getChildCount() < 2) {
            Toast.makeText(context, "Oops, your container seems to be so spacious", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestPermission()) {
            Bitmap bitmap = getBitmapFromView(imageContainerLinearLayout);
            String savedImageURL = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "myScreenshot", "Image of myScreenshot");

            Intent returnIntent = new Intent();
            returnIntent.putExtra("action", "combine ok");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    public void clearContainer(View v) {
        imageContainerLinearLayout.removeAllViews();
    }

    public void addMoreImagesToResourceQueue(View v) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("action", "add more");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void includeAll(View v) {
//        selectedImages.forEach(image -> {
//            createNewImageView(image.getUri());
//        });
    }
}