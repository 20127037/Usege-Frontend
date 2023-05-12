package com.group_1.usege.manipulation.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.group_1.usege.R;
import com.group_1.usege.model.Image;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class UcropperActivity extends AppCompatActivity {
    String sourceUri, destinationUri;
    Uri uri;
    Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucropper);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            image = bundle.getParcelable("sentImage");
            sourceUri = image.getUri().toString();
            uri = Uri.parse(sourceUri);
        }

        destinationUri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.main));
        options.setActiveControlsWidgetColor(getResources().getColor(R.color.confirmation));
        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationUri)))
                .withOptions(options)
                .withMaxResultSize(2000,2000)
                .start(UcropperActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            image.setUri(resultUri);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("croppedImage", image);
            intent.putExtras(bundle);
            setResult(101, intent);
            finish();
        }

        else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
}