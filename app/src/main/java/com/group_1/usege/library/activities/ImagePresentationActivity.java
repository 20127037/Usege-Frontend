package com.group_1.usege.library.activities;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.group_1.usege.R;
import com.group_1.usege.model.Image;

import java.util.ArrayList;
import java.util.List;

public class ImagePresentationActivity extends AppCompatActivity {
    Context context;
    List<Image> selectedImages = new ArrayList<>();
    ImageView currentImageView;
    TextView playingState;
    boolean paused = false;
    int currentImageIndex = 0;
    int waitingTime = 3000;
    String action = null;

    Presentation presentation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_presentation);
        context = getApplicationContext();
        Bundle bundle = getIntent().getExtras();
        selectedImages = bundle.getParcelableArrayList("data");

        currentImageView = findViewById(R.id.current_image);
        playingState = findViewById(R.id.playing_state);

        playingState.setOnClickListener(v -> {
            if (!paused) {
                presentation.cancel(true);
                paused = true;
                playingState.setText("Resume");
                playingState.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_presentation_resume), null, null);

            }
            else {
                presentation = new Presentation();
                presentation.execute();
                paused = false;
                playingState.setText("Pause");
                playingState.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_presentation_pause), null, null);
            }
        });
    }

    private void pausePresentation() {

    }


    private void renderImage(Uri imageUri) {
        Glide.with(context)
                .load(imageUri)
                .apply(new RequestOptions().centerCrop())
                .into(currentImageView);
    }

    private class Presentation extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            while (currentImageIndex < selectedImages.size()) {
                publishProgress(currentImageIndex);

                try {
                    Thread.sleep(waitingTime);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (action == null) currentImageIndex += 1;
                else if (action == "GO_PREVIOUS" || action == "GO_NEXT") {
                    action = null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(context, "The presentation has ended.", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Uri imageUri = selectedImages.get(values[0]).getUri();
            renderImage(imageUri);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presentation = new Presentation();
        presentation.execute();
    }

    public void viewPreviousImage(View v) {
        if (currentImageIndex == 0) {
            Toast.makeText(context, "You are viewing the first image", Toast.LENGTH_SHORT).show();
            return;
        }

        action = "GO_PREVIOUS";
        currentImageIndex -= 1;
        Uri imageUri = selectedImages.get(currentImageIndex).getUri();
        renderImage(imageUri);
    }

    public void viewNextImage(View v) {
        if (currentImageIndex == selectedImages.size() - 1) {
            Toast.makeText(context, "You are viewing the last image", Toast.LENGTH_SHORT).show();
            return;
        }

        action = "GO_NEXT";
        currentImageIndex += 1;
        Uri imageUri = selectedImages.get(currentImageIndex).getUri();
        renderImage(imageUri);
    }

    public void starPresentationOver(View v) {
        currentImageIndex = 0;
        presentation = new Presentation();
        presentation.execute();
        Toast.makeText(context, "The presentation has been started over", Toast.LENGTH_SHORT).show();
    }

    public void adjustPresentationProperties(View v) {
        Toast.makeText(this, "This service is not available now", Toast.LENGTH_SHORT).show();
        return;
    }

    public void exportVideo(View v) {
        Toast.makeText(this, "This service is not available now", Toast.LENGTH_SHORT).show();
        return;
    }

    public void backToPreviousActivity(View v) {
        finish();
    }
}