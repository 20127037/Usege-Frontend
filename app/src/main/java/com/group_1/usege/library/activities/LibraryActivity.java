package com.group_1.usege.library.activities;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.group_1.usege.layout.adapter.AlbumRadioAdapter;
import com.group_1.usege.layout.fragment.AlbumCardFragment;
import com.group_1.usege.layout.fragment.AlbumImageListFragment;
import com.group_1.usege.layout.fragment.AlbumListFragment;
import com.group_1.usege.layout.fragment.EmptyFilteringResultFragment;
import com.group_1.usege.layout.fragment.ImageCardFragment;
import com.group_1.usege.layout.fragment.ImageListFragment;
import com.group_1.usege.library.fragment.EmptyAlbumFragment;
import com.group_1.usege.library.fragment.EmptyAlbumImageFragment;
import com.group_1.usege.library.fragment.EmptyFragment;
import com.group_1.usege.manipulation.activities.ImageActivity;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.Image;
import com.group_1.usege.userInfo.model.UserInfo;
import com.group_1.usege.userInfo.repository.UserInfoRepository;
import com.group_1.usege.userInfo.services.MasterServiceGenerator;
import com.group_1.usege.utilities.activities.AuthApiCallerActivity;

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

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

@AndroidEntryPoint
public class LibraryActivity extends AuthApiCallerActivity<UserInfo> {

    Context context = this;
    FragmentTransaction ft;
    LinearLayout imageDisplayLayout;
    ImageCardFragment imageCardFragment;
    ImageListFragment imageListFragment;
    AlbumCardFragment albumCardFragment;
    AlbumListFragment albumListFragment;
    EmptyFragment emptyFragment = new EmptyFragment();
    @Inject
    public MasterServiceGenerator masterServiceGenerator;
    @Inject
    public UserInfoRepository userInfoRepository;
    EmptyAlbumImageFragment emptyAlbumImageFragment = new EmptyAlbumImageFragment();
    EmptyAlbumFragment emptyAlbumFragment = new EmptyAlbumFragment();
    EmptyFilteringResultFragment emptyFilteringResultFragment = new EmptyFilteringResultFragment();
    static RelativeLayout bottomMenu;
    ImageView imgViewUpload, imgViewCard, imgViewList, filterButton;
    // card list mode: image, album, imageInAlbum
    public static final String imageMode = "image";
    public static final String albumMode = "album";
    public static final String imageInAlbumMode = "imageInAlbum";
    public TextView moveToAlbum, addToAlbum, deleteImage, cutImage;
    public Album isOpeningAlbum;
    Button albumButton, fileButton;
    List<Image> imgList = new ArrayList<>();
    List<Image> clonedImgList = new ArrayList<>();

    List<Album> albumList = new ArrayList<Album>() {
        {
            add(new Album("favorite")); // favorite album
            add(new Album("trash")); // trash album
        }
    };

    Album trashBin = albumList.get(1);
    private String displayView = "card";
    private String mode = imageMode;
    // mode image or album
    private Boolean firstAccess = true;
    private Boolean filtered = false;

    private Album selectedAlbum;
    public static List<Image> selectedImages = new ArrayList<>();

    private static final int Read_Permission = 101;

    RelativeLayout layoutLibFunctions;


    private static final int UPDATE_IMAGE = 1;
    private static final int DELETE_IMAGE = 2;

    public LibraryActivity() {
        super(R.layout.activity_library);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ft = getSupportFragmentManager().beginTransaction();
        emptyFragment = EmptyFragment.newInstance(mode, false);
        ft.replace(R.id.layout_display_images, emptyFragment).commit();

        imageDisplayLayout = findViewById(R.id.layout_display_images);

        imgViewCard = findViewById(R.id.icon_card);
        imgViewList = findViewById(R.id.icon_list);
        imgViewUpload = findViewById(R.id.icon_cloud_upload);
        filterButton = findViewById(R.id.image_view_search);

        albumButton = findViewById(R.id.btn_album);
        fileButton = findViewById(R.id.btn_file);
        bottomMenu = findViewById(R.id.layout_bottom_menu_for_selecting_images);

        // bootom menu functions
        layoutLibFunctions = findViewById(R.id.layout_library_functions);
        moveToAlbum = findViewById(R.id.text_view_move_to_album);
        addToAlbum = findViewById(R.id.text_view_add_to_album);
        deleteImage = findViewById(R.id.text_view_delete_in_file);
        cutImage = findViewById(R.id.text_view_delete_in_album);

        imgViewCard.setEnabled(false);
        imgViewCard.setAlpha((float) 0.5);
        imgViewList.setEnabled(false);
        imgViewList.setAlpha((float) 0.5);
        filterButton.setEnabled(false);
        filterButton.setAlpha((float) 0.5);


        fileButton.setOnClickListener(v -> {
            fileButton.setTextColor(getResources().getColor(R.color.white));
            fileButton.setBackground(getResources().getDrawable(R.drawable.file_button_border));
            albumButton.setTextColor(getResources().getColor(R.color.black));
            albumButton.setBackground(getResources().getDrawable(R.drawable.album_button_border));
            clickOpenImageList();
        });
        albumButton.setOnClickListener(v -> {
            albumButton.setTextColor(getResources().getColor(R.color.white));
            albumButton.setBackground(getResources().getDrawable(R.drawable.file_button_border));
            fileButton.setTextColor(getResources().getColor(R.color.black));
            fileButton.setBackground(getResources().getDrawable(R.drawable.album_button_border));
            clickOpenAlbumList();
        });
        imgViewUpload.setOnClickListener(v -> clickOpenSetUplibraryBottomSheetDialog());

        imgViewCard.setOnClickListener(v -> {
            displayView = "card";
            imgViewCard.setEnabled(false);
            imgViewCard.setAlpha((float) 1);
            imgViewList.setEnabled(true);
            imgViewList.setAlpha((float) 0.5);
            firstAccess = true;
            switch (mode) {
                case imageMode:
                    updateImageViewDisplay();
                    break;
                case albumMode:
                    updateAlbumViewDisplay();
                    break;
                case imageInAlbumMode:
                    updateImageInAlbumViewDisplay();
                    break;
            }
        });

        imgViewList.setOnClickListener(v -> {
            displayView = "list";
            imgViewList.setEnabled(false);
            imgViewList.setAlpha((float) 1);
            imgViewCard.setEnabled(true);
            imgViewCard.setAlpha((float) 0.5);
            firstAccess = true;
            switch (mode) {
                case imageMode:
                    updateImageViewDisplay();
                    break;
                case albumMode:
                    updateAlbumViewDisplay();
                    break;
                case imageInAlbumMode:
                    updateImageInAlbumViewDisplay();
                    break;
            }
        });
    }

    //    Start Album handler
    // menu bottom functions
    private Album destinationAlbum;

    public void addToAlbum(View v) {
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
        btnConfirm.setEnabled(false);
        btnConfirm.setAlpha((float) 0.5);
        imageViewBackward = viewDialog.findViewById(R.id.image_view_backward);
        RecyclerView albumRadioRecyclerView = viewDialog.findViewById(R.id.rcv_album);

        // filter favortie and trash album
        ArrayList<Album> cloneAlbumList = new ArrayList<Album>(albumList);
        cloneAlbumList.removeIf(albumItem -> albumItem.getName() == "favorite" || albumItem.getName() == "trash");
        AlbumRadioAdapter albumRadioAdapter = new AlbumRadioAdapter(cloneAlbumList, btnConfirm, this);
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
            destinationAlbum.getAlbumImages().addAll(new ArrayList<Image>(selectedImages));
            // Đóng bottommsheet
            chooseAlbumBottomSheetDialog.dismiss();
            Toast.makeText(this, "Add image success", Toast.LENGTH_SHORT).show();
            selectedImages.clear();
        });
    }

    public void moveToAlbum(Album fromAlbum) { // call in XML file
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
        btnConfirm.setEnabled(false);
        btnConfirm.setAlpha((float) 0.5);
        imageViewBackward = viewDialog.findViewById(R.id.image_view_backward);
        RecyclerView albumRadioRecyclerView = viewDialog.findViewById(R.id.rcv_album);

        // filter favortie and trash album
        ArrayList<Album> cloneAlbumList = new ArrayList<Album>(albumList);
        cloneAlbumList.removeIf(albumItem -> albumItem.getName() == "favorite" || albumItem.getName() == "trash");
        AlbumRadioAdapter albumRadioAdapter = new AlbumRadioAdapter(cloneAlbumList, btnConfirm, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        albumRadioRecyclerView.setLayoutManager(linearLayoutManager);
        albumRadioRecyclerView.setAdapter(albumRadioAdapter);
        imageViewBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAlbumBottomSheetDialog.dismiss();
            }
        });


        createAlbumButton.setVisibility(View.GONE);

        btnConfirm.setOnClickListener(event -> {
            fromAlbum.getAlbumImages().removeIf(s -> {
                for (int i = 0; i < selectedImages.size(); i++) {
                    if (s.getUri() == selectedImages.get(i).getUri()) {
                        return true;
                    }
                }
                return false;
            });
            System.out.println(String.format("before: %d", destinationAlbum.getAlbumImages().size()));
            destinationAlbum.getAlbumImages().addAll(selectedImages);
            System.out.println(String.format("after: %d", destinationAlbum.getAlbumImages().size()));
            Toast.makeText(this, "move image success!", Toast.LENGTH_SHORT).show();
            selectedImages.clear();
            clickOpenAlbumImageList(fromAlbum);
            // Đóng bottommsheet
            chooseAlbumBottomSheetDialog.dismiss();
        });
    }

    public void setDestinationAlbum(Album album) {
        destinationAlbum = album;
    }

    public void clickOpenAlbumList() {
        mode = albumMode;
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
            // check log
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
        btnConfirm.setEnabled(false);
        btnConfirm.setAlpha((float) 0.5);
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

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newText = editable.toString();
                if (newText.length() > 0) {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setAlpha((float) 1);
                } else {
                    btnConfirm.setEnabled(false);
                    btnConfirm.setAlpha((float) 0.5);
                }
            }
        });

        btnConfirm.setOnClickListener(v -> {
            String title = String.valueOf(titleEditText.getText());
            String password = String.valueOf(passwordEditText.getText());

            albumList.add(new Album(title, new ArrayList<Image>(selectedImages)));
//            System.out.println("select add size " + selectedImages.size());
            selectedImages.clear();

            Toast.makeText(this, "Created album success!", Toast.LENGTH_SHORT).show();

            // Đóng bottommsheet
            createAlbumBottomSheetDialog.dismiss();

            triggerAlbumButton();
        });
    }

    public void clickOpenAlbumImageList(Album album) {
        isOpeningAlbum = album;
        mode = imageInAlbumMode;
//        if(album.getName() != "favorite" && album.getName() != "trash") {
        layoutLibFunctions.setVisibility(View.GONE);
//        }
        if (album.getAlbumImages().size() > 0) {
            ft = getSupportFragmentManager().beginTransaction();
            AlbumImageListFragment albumImagesList = AlbumImageListFragment.newInstance(album, displayView);
            ft.replace(R.id.layout_display_images, albumImagesList).commit();
        } else {
            ft = getSupportFragmentManager().beginTransaction();
            EmptyFragment emptyFragment = EmptyFragment.newInstance(mode, true);
            ft.replace(R.id.layout_display_images, emptyFragment).commit();
        }
    }

    public void setShowLayoutLibFuntions() {
        layoutLibFunctions.setVisibility(View.VISIBLE);
    }

    public void clickOpenImageList() {
        // check empty list
        mode = imageMode;
        if (imgList.size() == 0) {
            ft = getSupportFragmentManager().beginTransaction();
            emptyFragment = EmptyFragment.newInstance(mode, true);
            ft.replace(R.id.layout_display_images, emptyFragment).commit();
            return;
        }
        if (Objects.equals(displayView, "card")) {
            ft = getSupportFragmentManager().beginTransaction();
            List<Image> favoriteImgList = albumList.get(0).getAlbumImages();
            imageCardFragment = ImageCardFragment.newInstance(imgList, favoriteImgList);
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

    public void restoreAllTrash() {
        imgList.addAll(new ArrayList<Image>(albumList.get(1).getAlbumImages()));
        albumList.get(1).getAlbumImages().clear();
        clickOpenAlbumImageList(albumList.get(1));
        Toast.makeText(context, "Restore all image successfully!", Toast.LENGTH_SHORT).show();
    }

    public void clearTrash() {
        albumList.get(1).getAlbumImages().clear();
        clickOpenAlbumImageList(albumList.get(1));
        Toast.makeText(context, "Clear trash bin successfully!", Toast.LENGTH_SHORT).show();
    }

    public void deleteAlbum(Album deleteAlbum) {
        albumList.removeIf(v -> Objects.equals(v.getName(), deleteAlbum.getName()));
        Toast.makeText(this, "Delete album successfully!", Toast.LENGTH_SHORT).show();
        triggerAlbumButton();
    }
    public  void renameAlbum(Album renamedAlbum) {
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_rename_album, null);

        final BottomSheetDialog createAlbumBottomSheetDialog = new BottomSheetDialog(this);
        createAlbumBottomSheetDialog.setContentView(viewDialog);
        createAlbumBottomSheetDialog.show();

        EditText editTextName = viewDialog.findViewById(R.id.edit_text_new_name);
        Button btnConfirm = viewDialog.findViewById(R.id.btn_confirm);
        ImageView backIcon = viewDialog.findViewById(R.id.image_view_backward);
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newText = editable.toString();
                if (newText.length() > 0) {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setAlpha((float) 1);
                } else {
                    btnConfirm.setEnabled(false);
                    btnConfirm.setAlpha((float) 0.5);
                }
            }
        });

        btnConfirm.setOnClickListener(e -> {
            String newName = String.valueOf(editTextName.getText());
            Album album = albumList.stream().filter(v -> Objects.equals(v.getName(), renamedAlbum.getName())).findFirst().orElse(null);
            if(album != null) {
                album.setName(newName);
                Toast.makeText(this, "Rename album successfully!", Toast.LENGTH_SHORT).show();
                clickOpenAlbumImageList(album);
            } else {
                Toast.makeText(this, "There is some error, please try again!", Toast.LENGTH_SHORT).show();
            }
            createAlbumBottomSheetDialog.dismiss();
        });

        backIcon.setOnClickListener(v -> createAlbumBottomSheetDialog.dismiss());
    }

    // ======== End album handler

    public void clickOpenSetUplibraryBottomSheetDialog() {
        Button btnConfirm;
        ImageView imageViewBackward;

        View viewDialog = getLayoutInflater().inflate(R.layout.layout_set_up_syncing, null);

        // Yêu cầu quyền truy cập
        Boolean result = requestPermission();

        if (result == true) {
            final BottomSheetDialog setUplibraryBottomSheetDialog = new BottomSheetDialog(this);
            setUplibraryBottomSheetDialog.setContentView(viewDialog);
            setUplibraryBottomSheetDialog.show();

            btnConfirm = viewDialog.findViewById(R.id.btn_confirm);
            imageViewBackward = viewDialog.findViewById(R.id.image_view_backward);
            imageViewBackward.setOnClickListener(v -> setUplibraryBottomSheetDialog.dismiss());

            btnConfirm.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                //startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                launcherPickImage.launch(Intent.createChooser(intent, "Select Picture"));

                // Đóng bottommsheet
                setUplibraryBottomSheetDialog.dismiss();
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

            updateImageViewDisplay();
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
                updateImageViewDisplay();
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
    public void updateImageViewDisplay() {
        if (!filtered) {
            clonedImgList = new ArrayList<>(imgList);
        }

        if (imgList.size() == 0) {
            setStatusOfWidgets();
            ft = getSupportFragmentManager().beginTransaction();
            emptyFragment = EmptyFragment.newInstance(imageMode, true);
            ft.replace(R.id.layout_display_images, emptyFragment).commit();
            return;
        }


        if (displayView.equals("card")) {
            imgViewList.setAlpha(0.5F);
            imgViewCard.setAlpha(1F);

            ft = getSupportFragmentManager().beginTransaction();
            imageCardFragment = ImageCardFragment.newInstance(clonedImgList, new ArrayList<Image>(albumList.get(0).getAlbumImages()));
            ft.replace(R.id.layout_display_images, imageCardFragment).commit();

        } else if (displayView.equals("list")) {
            imgViewList.setAlpha(1F);
            imgViewCard.setAlpha(0.5F);

            ft = getSupportFragmentManager().beginTransaction();
            imageListFragment = ImageListFragment.newInstance(clonedImgList);
            ft.replace(R.id.layout_display_images, imageListFragment).commit();

        }
    }

    public void updateAlbumViewDisplay() {
        // album mode
        if (displayView.equals("card")) {
            imgViewList.setAlpha(0.5F);
            imgViewCard.setAlpha(1F);
            ft = getSupportFragmentManager().beginTransaction();
            albumCardFragment = AlbumCardFragment.newInstance(albumList);
            ft.replace(R.id.layout_display_images, albumCardFragment).commit();
        } else if (displayView.equals("list")) {
            imgViewList.setAlpha(1F);
            imgViewCard.setAlpha(0.5F);
            ft = getSupportFragmentManager().beginTransaction();
            albumListFragment = AlbumListFragment.newInstance(albumList);
            ft.replace(R.id.layout_display_images, albumListFragment).commit();
        }
    }

    public void updateImageInAlbumViewDisplay() {
        layoutLibFunctions.setVisibility(View.GONE);
        if (isOpeningAlbum.getAlbumImages().size() > 0) {

            if (Objects.equals(displayView, "card")) {
                ft = getSupportFragmentManager().beginTransaction();
                AlbumImageListFragment albumImagesList = AlbumImageListFragment.newInstance(isOpeningAlbum, "card");
                ft.replace(R.id.layout_display_images, albumImagesList).commit();
            } else {
                ft = getSupportFragmentManager().beginTransaction();
                AlbumImageListFragment albumImagesList = AlbumImageListFragment.newInstance(isOpeningAlbum, "list");
                ft.replace(R.id.layout_display_images, albumImagesList).commit();
            }
        } else {
            ft = getSupportFragmentManager().beginTransaction();
            emptyAlbumImageFragment = EmptyAlbumImageFragment.newInstance();
            ft.replace(R.id.layout_display_images, emptyAlbumImageFragment).commit();
        }
    }

    private final ActivityResultLauncher<Intent> launcherPickImage = registerForActivityResult(
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
                            Log.e("NOTE", "URI1 " + image.getUri());

                            imgList.add(0, image);
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

                        imgList.add(0, image);
//                        Log.e("NOTE", "LOCATION " + imgList.get(0).getLocation());
//                        Log.e("NOTE", "LOCATION " + imgList.get(1).getLocation());

                    }

                    setStatusOfWidgets();
                } else {
                    Toast.makeText(this, "You haven't picked any images", Toast.LENGTH_LONG).show();
                }
                // Thread

                // Update Fragment View
                updateImageViewDisplay();
            });

    //Kiểm tra xem ứng dụng có quyền truy cập chưa, nếu chưa sẽ yêu cầu
    private Boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
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

    public void sendAndReceiveImageInAlbum(Image image, int position, Album album) {
        selectedAlbum = album;

        Intent intent = new Intent(context, ImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putParcelable("object_image", image);
        bundle.putParcelable("object_album", album);
        //bundle.putSerializable("object_album", (Serializable) albumList);
        intent.putExtras(bundle);
        launcherSendAndReceiveImageInAlbum.launch(intent);
    }

    private final ActivityResultLauncher<Intent> launcherSendAndReceiveImageInAlbum = registerForActivityResult(
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
                    int position = bundle.getInt("position");
                    int task = bundle.getInt("task");
                    Album album = (Album) bundle.getParcelable("return_album");

                    // Lấy vị trí album đang mở
                    int index = albumList.indexOf(selectedAlbum);
                    Album openedAlbum = albumList.get(index);

                    switch (task) {
                        case UPDATE_IMAGE: {
                            // update description
                            openedAlbum.setName(album.getName());
                            openedAlbum.setAlbumImages(album.getAlbumImages());

                            break;
                        }

                        case DELETE_IMAGE: {
                            // delete image in album
                            openedAlbum.getAlbumImages().remove(position);

                            break;
                        }
                    }
                    updateImageInAlbumViewDisplay();
                } else {
                    //Toast.makeText(this, "You haven't picked any images", Toast.LENGTH_LONG).show();
                }
            });

    public void sendAndReceiveImage(Image image, int position) {
        Intent intent = new Intent(context, ImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putParcelable("object_image", image);
        //bundle.putSerializable("object_album", (Serializable) albumList);
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
                    int position = bundle.getInt("position");
                    Image selectedImage = (Image) bundle.getParcelable("return_image");
                    int task = bundle.getInt("task");

                    switch (task) {
                        case UPDATE_IMAGE: {
                            // update description
                            imgList.get(position).setDescription(selectedImage.getDescription());
                            break;
                        }

                        case DELETE_IMAGE: {
                            // delete image
                            imgList.remove(position);

                            //List<Image> lstdeletedImage = new ArrayList<>();
                            //lstdeletedImage.add(selectedImage);
                            trashBin.getAlbumImages().add(selectedImage);

                            updateImageViewDisplay();

                            break;
                        }
                    }
                } else {
                    //Toast.makeText(this, "You haven't picked any images", Toast.LENGTH_LONG).show();
                }
            });


    protected void onResume()
    {
        super.onResume();
        try {
            startCallApi(masterServiceGenerator
                    .getService(tokenRepository.getToken().getAccessToken())
                    .getUserInfo(tokenRepository.getToken().getUserId()));
        }
        catch (Exception e)
        {
            Log.e("Library", e.getMessage());
        }
    }

    @Override
    protected void handleCallSuccess(UserInfo body) {
        userInfoRepository.setInfo(body);
    }

    public class GetInformationThread extends Thread {
        private Image image;
        private Uri uri;

        private GetInformationThread(Image image, Uri uri) {
            this.image = image;
            this.uri = uri;
        }

        @Override
        public void run() {
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

    public void selectSingleImageAndOpenBottomMenuIfNotYet(Image image) {
        // FOR UI
        switch (mode) {
            case imageInAlbumMode:
                System.out.println("mode to album!");
                moveToAlbum.setVisibility(View.VISIBLE);
                addToAlbum.setVisibility(View.GONE);

                cutImage.setVisibility(View.VISIBLE);
                deleteImage.setVisibility(View.GONE);
                break;
            default:
                moveToAlbum.setVisibility(View.GONE);
                addToAlbum.setVisibility(View.VISIBLE);

                cutImage.setVisibility(View.GONE);
                deleteImage.setVisibility(View.VISIBLE);
        }

        imageDisplayLayout.setPadding(0, 0, 0, 500);
        bottomMenu.setVisibility(View.VISIBLE);

        // FOR CODE
        selectedImages.add(image);
        Log.d("selectedImages' size", String.valueOf(selectedImages.size()));
    }

    public void removeSingleImageAndRemoveBottomMenuIfNoImageLeft(Image image) {
        // FOR LOGIC
        selectedImages.remove(image);
        // FOR UI
        if (selectedImages.size() < 1) {
            bottomMenu.setVisibility(View.GONE);
            imageDisplayLayout.setPadding(0, 0, 0, 0);
        }
    }

    public RecyclerView getRecyclerViewOfImageLibrary() {
        LinearLayout libraryLinearLayout = (LinearLayout) imageDisplayLayout.getChildAt(0);
        RecyclerView libraryRecyclerView = (RecyclerView) libraryLinearLayout.getChildAt(0);
        return libraryRecyclerView;
    }

    public void removeBottomMenuAndAllImages(View v) {
        // FOR UI
        bottomMenu.setVisibility(View.GONE);
        imageDisplayLayout.setPadding(0, 0, 0, 0);
        RecyclerView libraryRecyclerView = getRecyclerViewOfImageLibrary();
        int c = libraryRecyclerView.getChildCount();
        for (int i = 0; i < c; ++i) {
            CardView cardView = (CardView) libraryRecyclerView.getChildAt(i);
            ImageView imageView = (ImageView) cardView.getChildAt(0);
            imageView.clearColorFilter();
        }
        // FOR LOGIC CODE
        selectedImages.clear();
    }

    public void selectAllImages(View v) {
        // FOR UI
        RecyclerView libraryRecycleView = getRecyclerViewOfImageLibrary();
        int c = libraryRecycleView.getChildCount();
        for (int i = 0; i < c; ++i) {
            CardView cardView = (CardView) libraryRecycleView.getChildAt(i);
            ImageView imageView = (ImageView) cardView.getChildAt(0);
            imageView.setColorFilter(ContextCompat.getColor(this, R.color.chosen_image));
        }
        // FOR LOGIC CODE
        selectedImages = new ArrayList<>(imgList);
    }

    public void showMoreOptions(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.image_selection_more_options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.favorite_image_menu_item:
                    // Do something when the "Favorite" item is clicked
                    albumList.get(0).getAlbumImages().addAll(new ArrayList<Image>(selectedImages));
                    bottomMenu.setVisibility(View.GONE);
                    selectedImages.clear();
                    updateImageViewDisplay();
                    Toast.makeText(context, "Add to favorite success", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.combine_image_menu_item:
                    combineImages();
                    return true;
                case R.id.compress_image_menu_item:
                    // Do something when the "Compress" item is clicked
                    return true;
                case R.id.make_a_presentation_menu_item:
                    // Do something when the "Make a presentation" item is clicked
                    return true;
                default:
                    return super.onMenuItemSelected(0, item);

            }
        });
        popupMenu.show();
    }

    private final ActivityResultLauncher<Intent> imageCombinationLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();

                if (result.getResultCode() == RESULT_OK && intent != null) {
                    String action = intent.getStringExtra("action");

                    if (Objects.equals(action, "back")) {
                        removeBottomMenuAndAllImages(null);
                    }
                    else if (Objects.equals(action, "add more")) {
                        Toast.makeText(this, "Now you can continue selecting images", Toast.LENGTH_LONG).show();
                    }
                    else if (Objects.equals(action, "combine ok")) {
                        Toast.makeText(this, "Successfully combing images", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this, "Something wrong", Toast.LENGTH_LONG).show();
                }
            });

    public void combineImages() {
        int selectedImagesSize = selectedImages.size();

        if (selectedImagesSize > 9) {
            Toast.makeText(context, "You has reached the limit of 9 limit", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ImageCombinationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) selectedImages);
        intent.putExtras(bundle);
        imageCombinationLauncher.launch(intent);
    }

    public void deleteImages(View v) {
        trashBin.getAlbumImages().addAll(selectedImages);
        imgList.removeAll(selectedImages);
        selectedImages.clear();
        bottomMenu.setVisibility(View.GONE);
        updateImageViewDisplay();
    }

    public void cutImages(View v) {
        isOpeningAlbum.getAlbumImages().removeAll(selectedImages);
        selectedImages.clear();
        bottomMenu.setVisibility(View.GONE);
        updateImageInAlbumViewDisplay();
    }

    public void shareImages(View v) {
        ArrayList<Uri> imageUris = new ArrayList<>();
        selectedImages.forEach((image) -> {
            imageUris.add(image.getUri());
        });
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, null));
    }
}