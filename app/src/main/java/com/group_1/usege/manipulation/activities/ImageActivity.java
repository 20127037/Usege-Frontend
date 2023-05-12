package com.group_1.usege.manipulation.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.group_1.usege.R;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.Image;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageActivity extends AppCompatActivity {

    LinearLayout layoutBottom;
    TextView tvDatetime, tvSize, tvPhotoshop, tvFavorite, tvDescribe, tvDelete, tvCut;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        position = bundle.getInt("position");
        image = (Image) bundle.getParcelable("object_image");
        album = (Album) bundle.getParcelable("object_album");

        //lstAlbum = (List<Album>) bundle.getSerializable("object_album");
        //Log.d("Date", "D: " + image.getDate());
        // Ánh xạ các widgets
        init();
        setValueToLayout();

    }

    public void init() {
        layoutBottom = findViewById(R.id.layout_bottom);
        tvDatetime = findViewById(R.id.text_view_datetime);
        tvSize = findViewById(R.id.text_view_size);
        ivImage = findViewById(R.id.image_view_image);
        ivBack = findViewById(R.id.image_view_backward);
        tvDescribe = findViewById(R.id.text_view_describe);
        tvPhotoshop = findViewById(R.id.text_view_photoshop);
        tvFavorite = findViewById(R.id.text_view_favorite);
        tvDelete = findViewById(R.id.text_view_delete_in_file);
        tvCut = findViewById(R.id.text_view_delete_in_album);
        etDescription = findViewById(R.id.edit_text_description);
        btnReset = findViewById(R.id.btn_reset);
        btnConfirm = findViewById(R.id.btn_confirm);
        layoutTags = findViewById(R.id.layout_tags);
        layoutButton = findViewById(R.id.layout_reset_confirm);

        if (album != null) {
            tvCut.setVisibility(View.VISIBLE);
            tvDelete.setVisibility(View.GONE);
        } else {
            tvCut.setVisibility(View.GONE);
            tvDelete.setVisibility(View.VISIBLE);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvDescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutTags.setVisibility(View.GONE);
                layoutButton.setVisibility(View.VISIBLE);

                // Thiết lập edit text description
                etDescription.setEnabled(true);
                etDescription.requestFocus();

                // Thiết lập text view describe
                setAlphaForDrawableInTextView(tvDescribe, 255, 1);

            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnDataImageToLibraryActivity(DELETE_IMAGE);
            }
        });
        tvCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnDataImageToLibraryActivity(DELETE_IMAGE);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDescription.setEnabled(false);
                layoutTags.setVisibility(View.VISIBLE);
                layoutButton.setVisibility(View.GONE);
                etDescription.setText(image.getDescription());

                // Thiết lập text view describe
                setAlphaForDrawableInTextView(tvDescribe, 153, 1);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDescription.setEnabled(false);
                layoutTags.setVisibility(View.VISIBLE);
                layoutButton.setVisibility(View.GONE);

                // Lưu vào image
                image.setDescription(etDescription.getText().toString());
                if (album != null) {
                    album.getAlbumImages()
                            .get(position)
                            .setDescription(etDescription.getText().toString());
                }
                etDescription.setText(image.getDescription());
                Log.d("Text", image.getDescription());
                // Thiết lập text view describe
                setAlphaForDrawableInTextView(tvDescribe, 153, 1);
            }
        });

        tvPhotoshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAndReceiveImage(image);
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
        bundle.putInt("position", position);
        bundle.putParcelable("return_image", image);
        bundle.putInt("task", task);
        if (album != null) {
            bundle.putParcelable("return_album", album);
        }

        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void setValueToLayout() {
        // Hiển thị dung lượng
        tvSize.setText(image.getSize() + " KB");

        // Format Date
        String date = convertToFormatMDY(image.getDate());
        tvDatetime.setText(date);

        // Hiển thị hình ảnh
        //Log.d("URI", "I: " + image.getUri());
        Glide.with(this)
                .load(image.getUri())
                .into(ivImage);

        // Hiển thị mô tả
        etDescription.setText(image.getDescription());
    }

    public String convertToFormatMDY(String datetime) {
        String formattedDate = "";

        if (datetime != null && datetime.length() > 0) {
            Date date = new Date();
            // Chuyển String thành Date
            SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            try {
                date = format.parse(datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Chuyển thành dạng Month Day, Year
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            formattedDate = sdf.format(date);
        }

        return formattedDate;
    }

    public void setAlphaForDrawableInTextView(TextView textView, int alpha, int direction) {
        Drawable[] drawables = textView.getCompoundDrawables();
        Drawable drawable = drawables[direction]; // index 1 tương ứng với drawableTop

        // Đặt độ mờ (alpha) cho drawableTop
        drawable.setAlpha(alpha); // giá trị alpha từ 0-255, 128 tương đương với độ mờ 50%

        // Thiết lập lại drawables cho TextView
        textView.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3]);
    }

    public void sendAndReceiveImage(Image image) {
        Intent intent = new Intent(context, ImagePhotoshopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("object_image", image);
        //bundle.putSerializable("object_album", (Serializable) albumList);
        intent.putExtras(bundle);
        launcherSendAndReceiveImage.launch(intent);
    }

    private final ActivityResultLauncher<Intent> launcherSendAndReceiveImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }

            Image returnedImage = bundle.getParcelable("returnedImage");
            image = returnedImage;
            Glide.with(this)
                    .load(image.getUri())
                    .into(ivImage);

        } else {
            Toast.makeText(this, "Something wrong happened.", Toast.LENGTH_LONG).show();
        }
    });

}
