package com.group_1.usege.library.activities;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.group_1.usege.R;
import com.group_1.usege.layout.fragment.ImageResourceQueueCardFragment;
import com.group_1.usege.model.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageCombinationActivity extends AppCompatActivity {
    Context context;
    public List<Image> selectedImages = new ArrayList<>();
    LinearLayout imageContainerLinearLayout;
    LinearLayout resourceQueueLayout;
    FragmentTransaction ft;
    ImageResourceQueueCardFragment imageResourceQueueCardFragment;

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

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30,30,30,30);

        imageContainerLinearLayout.setOnDragListener( (v, e) -> {
            switch(e.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        v.invalidate();
                        return true;
                    }
                    return false;

                case DragEvent.ACTION_DROP:
                    ClipData.Item item = e.getClipData().getItemAt(0);
                    Uri dragData = item.getUri();
                    v.invalidate();

                    ImageView newImageView = new ImageView(context);
                    newImageView.setLayoutParams(layoutParams);
                    Glide.with(this)
                            .load(dragData)
                            .override(600,300)
                            .into(newImageView);
                    imageContainerLinearLayout.addView(newImageView);

                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    v.invalidate();
                    return true;
                default:
                    Log.e("In image combination activity","Unknown action type received by View.OnDragListener.");
                    break;
            }
            return false;

        });

    }

    public void backToPreviousActivity(View v) {
        this.finish();
    }

    public void combineImagesFromContainer(View v) {

    }

    public void removeImageFromResourceQueue(View v) {

    }

    public void clearResourceQueue(View v) {

    }

    public void autoAlignCurrentImagesInContainer(View v) {

    }

    public void addMoreImagesToResourceQueue(View v) {

    }
}