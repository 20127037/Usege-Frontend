package com.group_1.usege.manipulation.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.group_1.usege.R;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;

public class ImagePhotoshopActivity extends AppCompatActivity {
    Context context = this;
    String action = "ADJUST";
    LinearLayout layoutBottom;
    ImageView ivImage, ivBack;
    ActivityResultLauncher<String> cropImage;
    Button btnReset, btnConfirm;
    TextView tvCrop, tvAdjust;
    Drawable icon_crop, icon_adjust;

    View layoutTags, layoutButton;

    private int position;
    private Image image;
    private Album album = null;

    private SeekBar brightnessSeekBar;
    private float brightness = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_photoshop);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) { return; }

        image = (Image) bundle.getParcelable("object_image");

        //lstAlbum = (List<Album>) bundle.getSerializable("object_album");
        //Log.d("Date", "D: " + image.getDate());
        // Ánh xạ các widgets
        init();
        tvAdjust.performClick();
    }

    private final ActivityResultLauncher<Intent> imageCroppingLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Intent intent = result.getData();

        if (result.getResultCode() == 101 && intent != null) {
            String croppedImageUri = intent.getStringExtra("croppedImage");
            ivImage.setImageURI(Uri.parse(croppedImageUri));
        } else {
            Toast.makeText(this, "Something wrong", Toast.LENGTH_LONG).show();
        }
    });

    private void startCroppingImage() {
        Intent intent = new Intent(this, UcropperActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("sentImage", image);
        intent.putExtras(bundle);
        imageCroppingLauncher.launch(intent);
    }

    private void init() {
        layoutBottom = findViewById(R.id.layout_bottom);
        ivImage = findViewById(R.id.image_view_image);
        ivBack = findViewById(R.id.image_view_backward);
        btnReset = findViewById(R.id.btn_reset);
        btnConfirm = findViewById(R.id.btn_confirm);
        layoutButton = findViewById(R.id.layout_reset_confirm);
        brightnessSeekBar = findViewById(R.id.seek_bar_brightness);
        tvCrop = findViewById(R.id.text_view_crop_image);
        tvAdjust = findViewById(R.id.text_view_adjust_image);

        icon_crop = getResources().getDrawable(R.drawable.icon_crop);
        icon_adjust = getResources().getDrawable(R.drawable.icon_adjust);


        ivBack.setOnClickListener(v -> onBackPressed());

        btnReset.setOnClickListener(v -> {
            renderOriginalImage();
        });

        btnConfirm.setOnClickListener(v -> {

//                try {
//                    Bitmap editedBitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
//                    File file = new File(Environment.getExternalStorageDirectory(), "edited_image_1.jpg");
//                    FileOutputStream outputStream = new FileOutputStream(file);
//                    editedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                    outputStream.close();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            applyBrightnessFilterAndSaveImage();
            Toast.makeText(getApplication(), "Save image succesfully!", Toast.LENGTH_SHORT).show();
        });

        icon_crop.setAlpha(255);
        tvCrop.setTypeface(null, Typeface.BOLD);
        icon_adjust.setAlpha(120);

        tvCrop.setOnClickListener(v -> {
            action = "CROP";
            brightnessSeekBar.setVisibility(View.GONE);
            startCroppingImage();

            icon_crop.setAlpha(255);
            tvCrop.setTypeface(null, Typeface.BOLD);
            tvAdjust.setTypeface(null, Typeface.NORMAL);
            icon_adjust.setAlpha(120);
        });

        tvAdjust.setOnClickListener(v -> {
            action = "ADJUST";
            brightnessSeekBar.setVisibility(View.VISIBLE);
            setValueToLayout();

            icon_crop.setAlpha(120);
            tvAdjust.setTypeface(null, Typeface.BOLD);
            tvCrop.setTypeface(null, Typeface.NORMAL);
            icon_adjust.setAlpha(255);
        });
    }

    private void renderOriginalImage() {
        Glide.with(context)
                .load(image.getUri())
                .apply(new RequestOptions().centerCrop())
                .into(ivImage);
    }

    @SuppressLint("CheckResult")
    private void applyBrightnessFilterAndSaveImage() {
        Glide.with(this)
                .load(image.getUri())
                .apply(RequestOptions.bitmapTransform(new BrightnessFilterTransformation(brightness)))
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, Transition<? super File> transition) {
                        try {
                            OutputStream outputStream = getContentResolver().openOutputStream(image.getUri());
                            FileInputStream inputStream = new FileInputStream(resource);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }
                            inputStream.close();
                            outputStream.close();
                            Toast.makeText(ImagePhotoshopActivity.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(ImagePhotoshopActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        returnDataImageToLibraryActivity();
    }

    public void returnDataImageToLibraryActivity() {
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("return_image", image);

        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void setValueToLayout() {
        // Hiển thị hình ảnh
        //Log.d("URI", "I: " + image.getUri());
        Glide.with(this)
                .load(image.getUri())
//                .transform(new BrightnessFilterTransformation(brightness))
                .apply(RequestOptions.bitmapTransform(new BrightnessFilterTransformation(brightness)))
                .into(ivImage);

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = (float) progress / 100;
                System.out.println(brightness);
                Glide.with(ImagePhotoshopActivity.this)
                        .load(image.getUri())
//                        .transform(new BrightnessFilterTransformation(brightness))
                        .apply(RequestOptions.bitmapTransform(new BrightnessFilterTransformation(brightness)))
                        .into(ivImage);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void setAlphaForDrawableInTextView(TextView textView, int alpha, int direction) {
        Drawable[] drawables = textView.getCompoundDrawables();
        Drawable drawable = drawables[direction]; // index 1 tương ứng với drawableTop

        // Đặt độ mờ (alpha) cho drawableTop
        drawable.setAlpha(alpha); // giá trị alpha từ 0-255, 128 tương đương với độ mờ 50%

        // Thiết lập lại drawables cho TextView
        textView.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3]);
    }
}
