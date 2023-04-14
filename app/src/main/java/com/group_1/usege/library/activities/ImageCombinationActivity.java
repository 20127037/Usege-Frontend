package com.group_1.usege.library.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.group_1.usege.R;
import com.group_1.usege.layout.fragment.ImageResourceQueueCardFragment;
import com.group_1.usege.model.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageCombinationActivity extends AppCompatActivity {
    public List<Image> selectedImages = new ArrayList<>();
    View imageContainerView;
    LinearLayout resourceQueueLayout;
    FragmentTransaction ft;
    ImageResourceQueueCardFragment imageResourceQueueCardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_combination);

        Bundle bundle = getIntent().getExtras();
        selectedImages = bundle.getParcelableArrayList("data");

        imageContainerView = findViewById(R.id.image_container_view);
        resourceQueueLayout = findViewById(R.id.resource_queue_linear_layout);

        ft = getSupportFragmentManager().beginTransaction();
        imageResourceQueueCardFragment = ImageResourceQueueCardFragment.newInstance(selectedImages);
        ft.replace(R.id.resource_queue_linear_layout, imageResourceQueueCardFragment).commit();

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