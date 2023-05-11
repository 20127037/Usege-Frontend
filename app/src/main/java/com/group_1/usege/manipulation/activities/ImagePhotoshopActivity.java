package com.group_1.usege.manipulation.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;

public class ImagePhotoshopActivity extends AppCompatActivity {

    LinearLayout layoutBottom;
    TextView tvDatetime, tvSize, tvDescribe, tvDelete, tvCut;
    ImageView ivImage, ivBack;
    EditText etDescription;
    Button btnReset, btnConfirm;
    View layoutTags, layoutButton;
    Context context = this;

    private int position;
    private Image image;
    private Album album = null;

    private static final int UPDATE_IMAGE = 1;
    private static final int DELETE_IMAGE = 2;

    private SeekBar brightnessSeekBar;
    private float brightness = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_photoshop);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        image = (Image) bundle.getParcelable("object_image");

        //lstAlbum = (List<Album>) bundle.getSerializable("object_album");
        //Log.d("Date", "D: " + image.getDate());
        // Ánh xạ các widgets
        init();
        setValueToLayout();
    }

    public void init() {
        layoutBottom = findViewById(R.id.layout_bottom);
        ivImage = findViewById(R.id.image_view_image);
        ivBack = findViewById(R.id.image_view_backward);
        btnReset = findViewById(R.id.btn_reset);
        btnConfirm = findViewById(R.id.btn_confirm);
        layoutButton = findViewById(R.id.layout_reset_confirm);
        brightnessSeekBar = findViewById(R.id.seekBarBrightness);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            }
        });
    }

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
        returnDataImageToLibraryActivity(UPDATE_IMAGE);
    }

    public void returnDataImageToLibraryActivity(int task) {
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
