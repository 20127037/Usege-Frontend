package com.group_1.usege.library.activities;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.group_1.usege.R;
import com.group_1.usege.layout.fragment.ImageResourceQueueCardFragment;
import com.group_1.usege.model.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageCombinationActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    Context context;
    public List<Image> selectedImages = new ArrayList<>();
    GridLayout imageContainerGridLayout;
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

        imageContainerGridLayout = findViewById(R.id.image_container_grid_layout);
        resourceQueueLayout = findViewById(R.id.resource_queue_linear_layout);

        ft = getSupportFragmentManager().beginTransaction();
        imageResourceQueueCardFragment = ImageResourceQueueCardFragment.newInstance(selectedImages);
        ft.replace(R.id.resource_queue_linear_layout, imageResourceQueueCardFragment).commit();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30,30,30,30);

        imageContainerGridLayout.setOnDragListener( (v, e) -> {
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
                            .apply(new RequestOptions() .override(400, 300).centerCrop())
                            .into(newImageView);

                    imageContainerGridLayout.addView(newImageView);

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

    Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(
                view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
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
    public void combineImagesFromContainer(View v) throws InterruptedException {
        if (requestPermission()) {
            Bitmap bitmap = getBitmapFromView(imageContainerGridLayout);
            String savedImageURL = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "myScreenshot", "Image of myScreenshot");
            Toast.makeText(context, "Successfully combining images", Toast.LENGTH_SHORT).show();
            Thread.sleep(1000);
            this.finish();
        }

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