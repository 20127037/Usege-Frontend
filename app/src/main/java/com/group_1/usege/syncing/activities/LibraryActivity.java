package com.group_1.usege.syncing.activities;

import static android.os.FileUtils.*;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.group_1.usege.R;
import com.group_1.usege.api.googlemaps.ApiService;
import com.group_1.usege.api.googlemaps.CallApi;
import com.group_1.usege.syncing.fragment.EmptyFragment;
import com.group_1.usege.layout.fragment.ImageCardFragment;
import com.group_1.usege.layout.fragment.ImageListFragment;
import com.group_1.usege.modle.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LibraryActivity extends AppCompatActivity {

    FragmentTransaction ft;
    ImageCardFragment imageCardFragment;
    ImageListFragment imageListFragment;
    EmptyFragment emptyFragment = new EmptyFragment();
    ImageView imageViewBackward, imgViewUpload, imgViewCard, imgViewList;
    Button btnConfirm;
    List<Image> imgList = new ArrayList<>();

    Context context = this;
    private String displayView = "card";
    private Boolean firstAccess = true;
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

        imgViewCard.setEnabled(false);
        imgViewList.setEnabled(false);

        imgViewUpload.setOnClickListener(v -> clickOpenSetUpSyncingBottomSheetDialog());

        imgViewCard.setOnClickListener(v -> {
            displayView = "card";
            firstAccess = true;
            updateViewDisplay();
        });

        imgViewList.setOnClickListener(v -> {
            displayView = "list";
            firstAccess = true;
            updateViewDisplay();
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

    @SuppressLint("NotifyDataSetChanged")
    public void updateViewDisplay() {

        if (displayView.equals("card")) {
            if (firstAccess == true) {
                imgViewList.setAlpha(0.5F);
                imgViewCard.setAlpha(1F);
                firstAccess = false;

                ft = getSupportFragmentManager().beginTransaction();
                imageCardFragment = ImageCardFragment.newInstance(imgList);
                ft.replace(R.id.layout_display_images, imageCardFragment).commit();
            }
            else {
                imageCardFragment.recycleAdapter.notifyDataSetChanged();
            }
        }
        else if (displayView.equals("list")) {
            if (firstAccess == true) {
                imgViewList.setAlpha(1F);
                imgViewCard.setAlpha(0.5F);
                firstAccess = false;

                ft = getSupportFragmentManager().beginTransaction();
                imageListFragment = ImageListFragment.newInstance(imgList);
                ft.replace(R.id.layout_display_images, imageListFragment).commit();
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

                    // Lấy nhiều ảnh
                    if (data.getClipData() != null) {
                        int countOfImages = data.getClipData().getItemCount();

                        for (int i = 0; i < countOfImages; i++) {
                            // Thêm dữ liệu
                            Uri imageURI = data.getClipData().getItemAt(i).getUri();
                            //Image image = getInformationOfImage(imageURI);
                            Image image = new Image();
                            image.setUri(imageURI);
                            image.setLocation("");
                            GetInformationThread getInformationThread = new GetInformationThread(image, imageURI);
                            getInformationThread.start();
                            // Đây là dữ liệu mẫu
                            //Image image = new Image("", 0F, "A favorite image", "", imageURI);

                            imgList.add(image);
                        }

                    // Lấy 1 ảnh
                    } else {
                        Uri imageURI = data.getData();
                        // Thêm dữ liệu
                        //Image image = getInformationOfImage(imageURI);
                        // Đây là dữ liệu mẫu
                        //Image image = new Image("", 0F, "The beautiful place", "", imageURI);
                        Image image = new Image();
                        image.setUri(imageURI);
                        image.setLocation("");
                        GetInformationThread getInformationThread = new GetInformationThread(image, imageURI);
                        getInformationThread.start();

                        imgList.add(image);
                        Log.e("NOTE", "LOCATION " + imgList.get(0).getLocation());
                        Log.e("NOTE", "LOCATION " + imgList.get(1).getLocation());
                        Log.e("NOTE", "LOCATION " + imgList.get(2).getLocation());
                    }

                    setStatusOfWidgets();
                }
                else {
                    Toast.makeText(this, "You haven't pick any images", Toast.LENGTH_LONG).show();
                }
                // Thread

                // Update Fragment View
                updateViewDisplay();
            });

    // Kiểm tra xem ứng dụng có quyền truy cập chưa, nếu chưa sẽ yêu cầu
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }
    }

    public void setStatusOfWidgets() {
        if (imgList.size() > 0) {
            //imgViewCard.setAlpha(1F);
            //imgViewList.setAlpha(1F);
            imgViewCard.setEnabled(true);
            imgViewList.setEnabled(true);
        }
        else {
            imgViewCard.setAlpha(0.5F);
            imgViewList.setAlpha(0.5F);
            imgViewCard.setEnabled(false);
            imgViewList.setEnabled(false);
        }
    }

    public String getImagePath(Uri imageURI) throws IOException {
        String imagePath = "";

        // lấy InputStream từ Uri của ảnh
        InputStream inputStream = getContentResolver().openInputStream(imageURI);

        // tạo tập tin tạm thời để lưu ảnh
        File tempFile = null;
        tempFile = File.createTempFile("temp", null, getCacheDir());
        tempFile.deleteOnExit();

        // copy dữ liệu từ InputStream vào tập tin tạm thời
        copyInputStreamToFile(inputStream, tempFile);

        // lấy đường dẫn tới tập tin tạm thời
        imagePath = tempFile.getAbsolutePath();

        return imagePath;
    }

    public long getSizeOfImage(String imagePath) {
        File file = new File(imagePath);
        long length = file.length() / 1024; // Đổi sang đơn vị KB
        Log.d("ImageInfo", "Size: " + length + " KB");

        return length;
    }

    public String getDateTimeOfImage(ExifInterface exif) {
        String dateTime = "";
        dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME);
        Log.d("ImageInfo", "DateTime: " + dateTime);

        return dateTime;
    }

    public float[] getLocationOfImage(ExifInterface exif) {
        float[] latLong = new float[2];
        if (exif != null) {
            String latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String latitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String longitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

            if (latitude != null && latitudeRef != null && longitude != null && longitudeRef != null) {
                try {
                    latLong[0] = convertToDegree(latitude, latitudeRef);
                    latLong[1] = convertToDegree(longitude, longitudeRef);

                    Log.d("ImageInfo", "Latitude: " + latLong[0]);
                    Log.d("ImageInfo", "Longitude: " + latLong[1]);

                    return latLong;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        return  null;
    }

    public String convertToString(float[] latLong) {
        String location = "";
        if (latLong != null) {
            location = String.valueOf(latLong[0]) + "," + String.valueOf(latLong[1]);
        }

        return location;
    }


    public Image getInformationOfImage(Uri imageURI) {
        String imagePath = null;
        try {
            imagePath = getImagePath(imageURI);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Tạo đối tượng ExifInterface để lấy thông tin ảnh
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Lấy kích thước của ảnh
        long sizeOfImage = getSizeOfImage(imagePath);

        // Lấy thời gian chụp của ảnh
        String dateTime = getDateTimeOfImage(exif);

        // Lấy vị trí
        float latLong[] = getLocationOfImage(exif);
        String location = convertToString(latLong);
        String address = "";
        if (location.length() > 0) {
            //CallApi callApi = new CallApi();
            //address = callApi.callApiGetAddress(location, im);

        }
        Log.d("Location", "Address: " + address);

                // Lưu thông tin vào Image
        Image image = new Image(dateTime, sizeOfImage, "A favarite image", address, imageURI);

        return image;
    }

    private static float convertToDegree(String stringDMS, String ref) {
        float result = 0.0f, numbers;
        String[] DMS = stringDMS.split(",", 3);

        for (int i = 0; i < DMS.length; i++) {
            String[] stringNumber = DMS[i].split("/");
            if (stringNumber.length == 1) {
                numbers = Float.parseFloat(DMS[i]);
            } else if (stringNumber.length == 2) {
                numbers = Float.parseFloat(stringNumber[0]) / Float.parseFloat(stringNumber[1]);
            } else {
                numbers = 0.0f;
            }
            switch (i) {
                case 0:
                    result += numbers / 1.0f;
                    break;
                case 1:
                    result += numbers / 60.0f;
                    break;
                case 2:
                    result += numbers / 3600.0f;
                    break;
                default:
                    break;
            }
        }
        if (ref.equals("S") || ref.equals("W")) {
            return -result;
        }
        return result;
    }

    public class GetInformationThread extends Thread {
        private Image image;
        private Uri uri;

        private GetInformationThread(Image image, Uri uri) {
            this.image = image;
            this.uri = uri;
        }
        @Override
        public void run(){
            String imagePath = null;
            try {
                imagePath = getImagePath(uri);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Tạo đối tượng ExifInterface để lấy thông tin ảnh
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imagePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Lấy kích thước của ảnh
            long sizeOfImage = getSizeOfImage(imagePath);

            // Lấy thời gian chụp của ảnh
            String dateTime = getDateTimeOfImage(exif);

            // Lấy vị trí
            float latLong[] = getLocationOfImage(exif);
            String location = convertToString(latLong);
            String address = "";
            if (location.length() > 0) {
                CallApi callApi = new CallApi(location, image, context);
                address = callApi.callApiGetAddress();

            }
            Log.d("Location", "Address: " + address);

            // Lưu thông tin vào Image
            image.setDate(dateTime);
            image.setSize(sizeOfImage);
            image.setDescription("A favorite image");
            //image.setLocation(address);
            //Image image = new Image(dateTime, sizeOfImage, "A favorite image", address, uri);
        }
    }
}