package com.group_1.usege.syncing.activities;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.group_1.usege.R;
import com.group_1.usege.api.apiservice.ApiGoogleMap;

import com.group_1.usege.layout.adapter.ListAdapter;
import com.group_1.usege.layout.adapter.AlbumRadioAdapter;
import com.group_1.usege.layout.fragment.AlbumCardFragment;
import com.group_1.usege.layout.fragment.AlbumImageListFragment;
import com.group_1.usege.layout.fragment.AlbumListFragment;
import com.group_1.usege.layout.fragment.EmptyFilteringResultFragment;

import com.group_1.usege.manipulation.activities.ImageActivity;
import com.group_1.usege.modle.Album;
import com.group_1.usege.syncing.fragment.EmptyAlbumFragment;
import com.group_1.usege.syncing.fragment.EmptyAlbumImageFragment;
import com.group_1.usege.syncing.fragment.EmptyFragment;
import com.group_1.usege.layout.fragment.ImageCardFragment;
import com.group_1.usege.layout.fragment.ImageListFragment;
import com.group_1.usege.modle.Image;
import com.group_1.usege.syncing.impl.SendAndReceiveImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class LibraryActivity extends AppCompatActivity implements SendAndReceiveImage {

    FragmentTransaction ft;
    LinearLayout imageDisplayLayout;
    ImageCardFragment imageCardFragment;
    ImageListFragment imageListFragment;
    AlbumCardFragment albumCardFragment;
    AlbumListFragment albumListFragment;
    EmptyFragment emptyFragment = new EmptyFragment();

    EmptyAlbumImageFragment emptyAlbumImageFragment = new EmptyAlbumImageFragment();
    EmptyAlbumFragment emptyAlbumFragment = new EmptyAlbumFragment();
    EmptyFilteringResultFragment emptyFilteringResultFragment = new EmptyFilteringResultFragment();
    static RelativeLayout bottomMenu;
    ImageView imgViewUpload, imgViewCard, imgViewList, filterButton;
    List<Image> imgList = new ArrayList<>(); List<Image> clonedImgList = new ArrayList<>();

    Context context = this;

    int selectedImagePosition;


    Button albumButton, fileButton;
    List<Image> imgList = new ArrayList<>();
    List<Image> clonedImgList = new ArrayList<>();

    List<Album> albumList = new ArrayList<Album>() {
        {
            add(new Album("favorite")); // favorite album
            add(new Album("trash")); // trash album
        }
    };
    private String displayView = "card";
    private String mode = "image";
    // mode image or album
    private Boolean firstAccess = true;
    private Boolean filtered = false;
    public static List<Image> selectedImages = new ArrayList<>();

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
        filterButton = findViewById(R.id.image_view_search);
        albumButton = findViewById(R.id.btn_album);
        fileButton = findViewById(R.id.btn_file);
        bottomMenu = findViewById(R.id.layout_bottom_menu_for_selecting_images);

        imgViewCard.setEnabled(false);
        imgViewCard.setAlpha((float) 0.5);
        imgViewList.setEnabled(false);
        imgViewCard.setAlpha((float) 0.5);
        filterButton.setEnabled(false);
        filterButton.setAlpha((float) 0.5);


        fileButton.setOnClickListener(v -> {
            mode = "image";
            clickOpenImageList();
        });
        albumButton.setOnClickListener(v -> {
            mode = "album";
            clickOpenAlbumList();
        });
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

    // menu bottom functions
    private Album destinationAlbum;

    public void addToAlbum(View v) { // call in XML file
        bottomMenu.setVisibility(View.GONE);
        //  -------------------------
        Button btnConfirm;
        Button createAlbumButton;
        ImageView imageViewBackward;

        View viewDialog = getLayoutInflater().inflate(R.layout.layout_choose_destination_album, null);

        final BottomSheetDialog chooseAlbumBottomSheetDialog = new BottomSheetDialog(this);
        chooseAlbumBottomSheetDialog.setContentView(viewDialog);
        chooseAlbumBottomSheetDialog.show();

        createAlbumButton = viewDialog.findViewById(R.id.btn_create_album);
        btnConfirm = viewDialog.findViewById(R.id.btn_confirm);
        imageViewBackward = viewDialog.findViewById(R.id.image_view_backward);
        RecyclerView albumRadioRecyclerView = viewDialog.findViewById(R.id.rcv_album);

        // filter favortie and trash album
        ArrayList<Album> cloneAlbumList = new ArrayList<Album>(albumList);
        cloneAlbumList.removeIf(albumItem -> albumItem.getName() == "favorite" || albumItem.getName() == "trash");
        AlbumRadioAdapter albumRadioAdapter = new AlbumRadioAdapter(cloneAlbumList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        albumRadioRecyclerView.setLayoutManager(linearLayoutManager);
        albumRadioRecyclerView.setAdapter(albumRadioAdapter);
        imageViewBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAlbumBottomSheetDialog.dismiss();
            }
        });


        createAlbumButton.setOnClickListener(event -> {
            clickOpenAlbumCreateBottomSheet();
            chooseAlbumBottomSheetDialog.dismiss();
        });

        btnConfirm.setOnClickListener(event -> {
            destinationAlbum.getAlbumImages().addAll(selectedImages);
            // Đóng bottommsheet
            chooseAlbumBottomSheetDialog.dismiss();
        });
    }

    public void setDestinationAlbum(Album album) {
        destinationAlbum = album;
    }

    //    Start Album handler
    public void clickOpenAlbumList() {
        // check empty list
        if (albumList.size() == 0) {
            ft = getSupportFragmentManager().beginTransaction();
            emptyAlbumFragment = EmptyAlbumFragment.newInstance();
            ft.replace(R.id.layout_display_images, emptyAlbumFragment).commit();
            return;
        }
        if (Objects.equals(displayView, "card")) {
            ft = getSupportFragmentManager().beginTransaction();
            albumCardFragment = AlbumCardFragment.newInstance(albumList);
            ft.replace(R.id.layout_display_images, albumCardFragment).commit();
        } else {
            ft = getSupportFragmentManager().beginTransaction();
            albumListFragment = AlbumListFragment.newInstance(albumList);
            ft.replace(R.id.layout_display_images, albumListFragment).commit();
        }
    }

    public void clickOpenAlbumCreateBottomSheetAndAddImage(int position) {
        Button btnConfirm;
        ImageView imageViewBackward;
        EditText titleEditText, passwordEditText;

        View viewDialog = getLayoutInflater().inflate(R.layout.layout_create_album, null);


        final BottomSheetDialog createAlbumBottomSheetDialog = new BottomSheetDialog(this);
        createAlbumBottomSheetDialog.setContentView(viewDialog);
        createAlbumBottomSheetDialog.show();

        btnConfirm = viewDialog.findViewById(R.id.btn_confirm);
        imageViewBackward = viewDialog.findViewById(R.id.image_view_backward);
        titleEditText = viewDialog.findViewById(R.id.edit_text_title);
        passwordEditText = viewDialog.findViewById(R.id.edit_text_password);
        imageViewBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlbumBottomSheetDialog.dismiss();
            }
        });

        final String returnValue = "";

        btnConfirm.setOnClickListener(v -> {
            String title = String.valueOf(titleEditText.getText());
            String password = String.valueOf(passwordEditText.getText());

            System.out.println("title: " + title);
            System.out.println("password: " + password);

            albumList.add(new Album(title, new ArrayList<Image>() {{
                add(imgList.get(position));
            }}));

            Toast.makeText(this, "Created album success!", Toast.LENGTH_SHORT).show();

            // Đóng bottommsheet
            createAlbumBottomSheetDialog.dismiss();
        });
    }

    public void clickOpenAlbumCreateBottomSheet() {
        Button btnConfirm;
        ImageView imageViewBackward;
        EditText titleEditText, passwordEditText;

        View viewDialog = getLayoutInflater().inflate(R.layout.layout_create_album, null);

        final BottomSheetDialog createAlbumBottomSheetDialog = new BottomSheetDialog(this);
        createAlbumBottomSheetDialog.setContentView(viewDialog);
        createAlbumBottomSheetDialog.show();

        btnConfirm = viewDialog.findViewById(R.id.btn_confirm);
        imageViewBackward = viewDialog.findViewById(R.id.image_view_backward);
        titleEditText = viewDialog.findViewById(R.id.edit_text_title);
        passwordEditText = viewDialog.findViewById(R.id.edit_text_password);
        imageViewBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlbumBottomSheetDialog.dismiss();
            }
        });

        final String returnValue = "";

        btnConfirm.setOnClickListener(v -> {
            String title = String.valueOf(titleEditText.getText());
            String password = String.valueOf(passwordEditText.getText());

            albumList.add(new Album(title, selectedImages));

            Toast.makeText(this, "Created album success!", Toast.LENGTH_SHORT).show();

            // Đóng bottommsheet
            createAlbumBottomSheetDialog.dismiss();

            clickOpenAlbumList();
        });
    }

    public void clickOpenAlbumImageList(Album album) {
        if (album.getAlbumImages().size() > 0) {
            ft = getSupportFragmentManager().beginTransaction();
            AlbumImageListFragment albumImagesList = AlbumImageListFragment.newInstance(album);
            ft.replace(R.id.layout_display_images, albumImagesList).commit();
        } else {
            ft = getSupportFragmentManager().beginTransaction();
            emptyAlbumImageFragment = EmptyAlbumImageFragment.newInstance();
            ft.replace(R.id.layout_display_images, emptyAlbumImageFragment).commit();
        }
    }

    public void clickAddImageToAlbum() {

    }

    public void clickOpenImageList() {
        // check empty list
        if (imgList.size() == 0) {
            ft = getSupportFragmentManager().beginTransaction();
            emptyFragment = EmptyFragment.newInstance();
            ft.replace(R.id.layout_display_images, emptyFragment).commit();
            return;
        }
        if (Objects.equals(displayView, "card")) {
            ft = getSupportFragmentManager().beginTransaction();
            imageCardFragment = ImageCardFragment.newInstance(imgList);
            ft.replace(R.id.layout_display_images, imageCardFragment).commit();
        } else {
            ft = getSupportFragmentManager().beginTransaction();
            imageListFragment = ImageListFragment.newInstance(imgList);
            ft.replace(R.id.layout_display_images, imageListFragment).commit();
        }
    }

    public void triggerAlbumButton() {
        albumButton.callOnClick();
    }

    //    End Album handler


    public void clickOpenSetUpSyncingBottomSheetDialog() {
        Button btnConfirm;
        ImageView imageViewBackward;

        View viewDialog = getLayoutInflater().inflate(R.layout.layout_set_up_syncing, null);

        // Yêu cầu quyền truy cập
        Boolean result = requestPermission();

        if (result == true) {
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

            btnConfirm.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                //startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                launcher.launch(Intent.createChooser(intent, "Select Picture"));

                // Đóng bottommsheet
                setUpSyncingBottomSheetDialog.dismiss();
            });
        }
    }

    public void openFilterBottomSheet(View filterIcon) {
        if (filtered) {
            filtered = false;
            imgViewUpload.setEnabled(true);
            imgViewUpload.setAlpha((float) 1);
            imgViewCard.setEnabled(true);
            imgViewList.setEnabled(true);
            if (displayView == "card") {
                imgViewCard.setAlpha((float) 1);
                imgViewList.setAlpha((float) 0.7);
            } else {
                imgViewCard.setAlpha((float) 0.7);
                imgViewList.setAlpha((float) 1);
            }
            filterButton.setColorFilter(null);

            updateViewDisplay();
            return;
        }

        TextView backwardButton;
        AutoCompleteTextView imageTagAutoCompleteTextView;
        EditText descriptionEditText, creationDateEditText;
        Button openDatePickerButton, applyFiltersButton;
        TextView selectedLocationTextView;
        Spinner locationSpinner;
        String[] imageTags = {"coffee", "tree", "chair", "people", "shirt"};
        String[] locations = {"", "Ho Chi Minh", "Ha Noi", "Can Tho", "Vinh Long", "Thua Thien Hue"};

        View viewDialog = getLayoutInflater().inflate(R.layout.layout_filtering, null);

        final BottomSheetDialog filteringBottomSheetDialog = new BottomSheetDialog(this);
        filteringBottomSheetDialog.setContentView(viewDialog);
        filteringBottomSheetDialog.show();

        backwardButton = viewDialog.findViewById(R.id.backward_from_filter_bottom_sheet_text_view);
        openDatePickerButton = viewDialog.findViewById(R.id.open_date_picker_button);
        applyFiltersButton = viewDialog.findViewById(R.id.apply_filters_button);
        imageTagAutoCompleteTextView = viewDialog.findViewById(R.id.image_tag_auto_complete_text_view);
        descriptionEditText = viewDialog.findViewById(R.id.description_edit_text);
        creationDateEditText = viewDialog.findViewById(R.id.creation_date_edit_text);
        selectedLocationTextView = viewDialog.findViewById(R.id.selected_location_text_view);
        locationSpinner = viewDialog.findViewById(R.id.location_spinner);

        backwardButton.setOnClickListener(v -> filteringBottomSheetDialog.dismiss());
        openDatePickerButton.setOnClickListener(v -> {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            List<String> dayComponents = Arrays.asList(currentDate.split("-"));
            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
                if (monthOfYear < 10)
                    creationDateEditText.setText(dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year);
                else
                    creationDateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, Integer.parseInt(dayComponents.get(0)), Integer.parseInt(dayComponents.get(1)) - 1, Integer.parseInt(dayComponents.get(2)));
            datePickerDialog.show();
        });

        applyFiltersButton.setOnClickListener(v -> {
            String chosenImageTag = imageTagAutoCompleteTextView.getText().toString();
            String description = descriptionEditText.getText().toString();
            String creationDate = creationDateEditText.getText().toString();
            String location = selectedLocationTextView.getText().toString();

            if (!creationDate.isEmpty()) {
                if (!creationDate.contains("/")) {
                    creationDateEditText.setBackgroundResource(R.drawable.edit_text_error);
                    Toast.makeText(this, "Please use '/' symbol", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    formatter.setLenient(false);
                    Date date = formatter.parse(creationDate);
                } catch (ParseException e) {
                    creationDateEditText.setBackgroundResource(R.drawable.edit_text_error);
                    Toast.makeText(this, "Invalid creation date format", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<String> dayComponents = Arrays.asList(creationDate.split("/"));
                if (dayComponents.get(1).length() < 2) {
                    creationDateEditText.setBackgroundResource(R.drawable.edit_text_error);
                    Toast.makeText(this, "Please re-format the month with 2 digits", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            filtered = true;
            filteringBottomSheetDialog.dismiss();

            clonedImgList = new ArrayList<>(imgList);

            if (!chosenImageTag.isEmpty())
                clonedImgList.removeIf(e -> !e.getTags().contains(chosenImageTag));
            if (!description.isEmpty())
                clonedImgList.removeIf(e -> !e.getTags().contains(description));
            if (!creationDate.isEmpty())
                clonedImgList.removeIf(e -> e.getDate().isEmpty() || !e.getDate().contains(creationDate));
            if (!location.isEmpty()) clonedImgList.removeIf(e -> !e.getTags().contains(location));

            if (clonedImgList.size() > 0) {
                updateViewDisplay();
            } else {
                ft = getSupportFragmentManager().beginTransaction();
                emptyFilteringResultFragment = EmptyFilteringResultFragment.newInstance();
                ft.replace(R.id.layout_display_images, emptyFilteringResultFragment).commit();
                imgViewCard.setEnabled(false);
                imgViewList.setEnabled(false);
                imgViewList.setAlpha((float) 0.5);
                imgViewCard.setAlpha((float) 0.5);
            }
            imgViewUpload.setEnabled(false);
            imgViewUpload.setAlpha((float) 0.5);
            filterButton.setColorFilter(Color.parseColor("#45af7d"), PorterDuff.Mode.SRC_ATOP);
            filterButton.setAlpha((float) 1);
        });

        ArrayAdapter<String> imageTagAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, imageTags);
        imageTagAutoCompleteTextView.setAdapter(imageTagAdapter);
        imageTagAutoCompleteTextView.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && imageTagAutoCompleteTextView.getText().toString().isEmpty())
                view.setBackgroundResource(R.drawable.edit_text_lost_focus);
            else view.setBackgroundResource(R.drawable.edit_text_active);
        });

        descriptionEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && descriptionEditText.getText().toString().isEmpty())
                view.setBackgroundResource(R.drawable.edit_text_lost_focus);
            else view.setBackgroundResource(R.drawable.edit_text_active);
        });

        creationDateEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && creationDateEditText.getText().toString().isEmpty())
                view.setBackgroundResource(R.drawable.edit_text_lost_focus);
            else view.setBackgroundResource(R.drawable.edit_text_active);
        });

        ArrayAdapter locationAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int chosenIndex, long l) {
                if (chosenIndex == 0)
                    selectedLocationTextView.setBackgroundResource(R.drawable.edit_text_lost_focus);
                else selectedLocationTextView.setBackgroundResource((R.drawable.edit_text_active));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateViewDisplay() {
        if (!filtered) {
            clonedImgList = new ArrayList<>(imgList);
        }

        if (Objects.equals(mode, "image")) {
            if (displayView.equals("card")) {
                imgViewList.setAlpha(0.5F);
                imgViewCard.setAlpha(1F);
                imgViewList.setEnabled(true);
                imgViewCard.setEnabled(false);
                if (firstAccess == true) {
                    firstAccess = false;

                    ft = getSupportFragmentManager().beginTransaction();
                    imageCardFragment = ImageCardFragment.newInstance(clonedImgList);
                    ft.replace(R.id.layout_display_images, imageCardFragment).commit();
                } else {
//                imageCardFragment.recycleAdapter.notifyDataSetChanged();
//                imageCardFragment.rcvPhoto.setAdapter(new RecycleAdapter(clonedImgList, imageCardFragment.getContext(), "card"));
                    ft = getSupportFragmentManager().beginTransaction();
                    imageCardFragment = ImageCardFragment.newInstance(clonedImgList);
                    ft.replace(R.id.layout_display_images, imageCardFragment).commit();
                }
            } else if (displayView.equals("list")) {
                imgViewList.setAlpha(1F);
                imgViewCard.setAlpha(0.5F);
                imgViewList.setEnabled(false);
                imgViewCard.setEnabled(true);
                if (firstAccess == true) {
                    firstAccess = false;

                    ft = getSupportFragmentManager().beginTransaction();
                    imageListFragment = ImageListFragment.newInstance(clonedImgList);
                    ft.replace(R.id.layout_display_images, imageListFragment).commit();
                } else {
//                imageListFragment.recycleAdapter.notifyDataSetChanged();
//                imageListFragment.rcvPhoto.setAdapter(new RecycleAdapter(clonedImgList, imageListFragment.getContext(), "list"));
                    ft = getSupportFragmentManager().beginTransaction();
                    imageListFragment = ImageListFragment.newInstance(clonedImgList);
                    ft.replace(R.id.layout_display_images, imageListFragment).commit();

                }
            }
        } else if (Objects.equals(mode, "album")) {
            // album mode
            if (displayView.equals("card")) {
                imgViewList.setAlpha(0.5F);
                imgViewCard.setAlpha(1F);
                imgViewList.setEnabled(true);
                imgViewCard.setEnabled(false);
                ft = getSupportFragmentManager().beginTransaction();
                albumCardFragment = AlbumCardFragment.newInstance(albumList);
                ft.replace(R.id.layout_display_images, albumCardFragment).commit();
            } else if (displayView.equals("list")) {
                imgViewList.setAlpha(1F);
                imgViewCard.setAlpha(0.5F);
                imgViewList.setEnabled(false);
                imgViewCard.setEnabled(true);
                ft = getSupportFragmentManager().beginTransaction();
                albumListFragment = AlbumListFragment.newInstance(albumList);
                ft.replace(R.id.layout_display_images, albumListFragment).commit();

            }
        }
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
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
                            image.setDescription("A favorite image");
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
                        image.setDescription("A favorite image");
                        GetInformationThread getInformationThread = new GetInformationThread(image, imageURI);
                        getInformationThread.start();

                        imgList.add(image);
                        Log.e("NOTE", "LOCATION " + imgList.get(0).getLocation());
                        Log.e("NOTE", "LOCATION " + imgList.get(1).getLocation());

                    }

                    setStatusOfWidgets();
                }
                else {
                    Toast.makeText(this, "You haven't picked any images", Toast.LENGTH_LONG).show();
                }
                // Thread

                // Update Fragment View
                updateViewDisplay();
            });

    //Kiểm tra xem ứng dụng có quyền truy cập chưa, nếu chưa sẽ yêu cầu
    private Boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }
        return false;
    }


    public void setStatusOfWidgets() {
        if (imgList.size() > 0) {
            //imgViewCard.setAlpha(1F);
            //imgViewList.setAlpha(1F);
            imgViewCard.setEnabled(true);
            imgViewList.setEnabled(true);
            filterButton.setEnabled(true);
            filterButton.setAlpha((float) 1);

        } else {
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

    public List<String> getTagsOfImage(String imageURL) {
        // make API call to Eden AI
        List<String> imageTags = new ArrayList<>();
        imageTags.add("coffee");
        imageTags.add("chair");
        return imageTags;
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

        return null;
    }

    public String convertToString(float[] latLong) {
        String location = "";
        if (latLong != null) {
            location = String.valueOf(latLong[0]) + "," + String.valueOf(latLong[1]);
        }

        return location;
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

    @Override
    public void sendAndReceiveImage(Image image, int position) {
        selectedImagePosition = position;
        Intent intent = new Intent(context, ImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("object_image", image);
        intent.putExtras(bundle);
        launcherSendAndReceiveImage.launch(intent);
    }

    private final ActivityResultLauncher<Intent> launcherSendAndReceiveImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    // Nhận giá trị mới khi ảnh đã được cập nhật
                    Image selectedImage = (Image) bundle.getParcelable("return_image");

                    // Cập nhật ảnh đã chọn
                    imgList.get(selectedImagePosition).setDescription(selectedImage.getDescription());
                }
                else {
                    //Toast.makeText(this, "You haven't picked any images", Toast.LENGTH_LONG).show();
                }
            });

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
                //ApiGoogleMap callApi = new ApiGoogleMap(location, image, context);
                //callApi.callApiGetAddress();
            }
            //Log.d("Location", "Address: " + address);

            // Lưu thông tin vào Image
            image.setDate(dateTime);
            image.setSize(sizeOfImage);
            //image.setLocation(address);
            //Image image = new Image(dateTime, sizeOfImage, "A favorite image", address, uri);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imageCardFragment.cardAdapter != null || imageListFragment.listAdapter != null) {
            imageCardFragment.cardAdapter.release();
            imageListFragment.listAdapter.release();
        }
    }

    public static void openBottomMenu(Image image) {
        bottomMenu.setVisibility(View.VISIBLE);
        selectedImages.add(image);
        Log.d("selectedImages' size", String.valueOf(selectedImages.size()));
    }

    public void removeBottomMenu(View v) {
        bottomMenu.setVisibility(View.GONE);
        selectedImages.clear();
    }

    public void selectAllImages(View v) {
        imageDisplayLayout = findViewById(R.id.layout_display_images);
        LinearLayout test = (LinearLayout) imageDisplayLayout.getChildAt(0);
        RecyclerView test1 = (RecyclerView) test.getChildAt(0);
        int c = test1.getChildCount();
        for (int i = 0; i < c; ++i) {
            CardView cardView = (CardView) test1.getChildAt(i);
            ImageView imageView = (ImageView) cardView.getChildAt(0);
            imageView.setAlpha((float) 0.5);
        }
        selectedImages = new ArrayList<>(imgList);
    }

    public void showMoreOptions(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.image_selection_more_options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            Toast.makeText(getApplicationContext(), menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
            return true;
        });
        popupMenu.show();
    }


}