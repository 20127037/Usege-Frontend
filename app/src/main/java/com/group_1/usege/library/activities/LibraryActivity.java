package com.group_1.usege.library.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.group_1.usege.R;
import com.group_1.usege.album.fragments.AlbumCardFragment;
import com.group_1.usege.album.fragments.AlbumImageListFragment;
import com.group_1.usege.album.fragments.AlbumListFragment;
import com.group_1.usege.album.services.AlbumServiceGenerator;
import com.group_1.usege.api.apiservice.ApiUpdateFile;
import com.group_1.usege.api.apiservice.ApiUploadFile;
import com.group_1.usege.api.apiservice.FileServiceGenerator;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.dto.ImageDto;
import com.group_1.usege.dto.LoadFileRequestDto;
import com.group_1.usege.layout.adapter.AlbumRadioAdapter;
import com.group_1.usege.layout.fragment.EmptyFilteringResultFragment;
import com.group_1.usege.layout.fragment.ImageCardFragment;
import com.group_1.usege.layout.fragment.ImageListFragment;
import com.group_1.usege.library.adapter.ImagesAdapter;
import com.group_1.usege.library.fragment.EmptyAlbumFragment;
import com.group_1.usege.library.fragment.EmptyAlbumImageFragment;
import com.group_1.usege.library.fragment.EmptyFragment;
import com.group_1.usege.library.service.MasterAlbumService;
import com.group_1.usege.library.service.MasterAlbumServiceGenerator;
import com.group_1.usege.library.service.MasterFileService;
import com.group_1.usege.library.service.MasterFileServiceGenerator;
import com.group_1.usege.library.service.MasterTrashService;
import com.group_1.usege.library.service.MasterTrashServiceGenerator;
import com.group_1.usege.library.service.TrashServiceGenerator;
import com.group_1.usege.manipulation.activities.ImageActivity;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.Image;
import com.group_1.usege.model.UserAlbum;
import com.group_1.usege.model.UserFile;
import com.group_1.usege.model.UserFileInAlbum;
import com.group_1.usege.realPath.RealPathUtil;
import com.group_1.usege.userInfo.model.UserInfo;
import com.group_1.usege.userInfo.repository.UserInfoRepository;
import com.group_1.usege.userInfo.services.MasterUserServiceGenerator;
import com.group_1.usege.utilities.activities.NavigatedAuthApiCallerActivity;
import com.group_1.usege.utilities.interfaces.ClickItemReceiver;
import com.group_1.usege.utilities.interfaces.LongClickItemReceiver;
import com.group_1.usege.utilities.view.DialogueUtilities;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.inject.Inject;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;

@AndroidEntryPoint
public class LibraryActivity extends NavigatedAuthApiCallerActivity<UserInfo> implements
        ClickItemReceiver<Image, ImagesAdapter.ImageViewHolder>, LongClickItemReceiver<Image, ImagesAdapter.ImageViewHolder> {
    Context context = this;
    DrawerLayout rootDrawerLayout;
    FragmentTransaction ft;
    LinearLayout imageDisplayLayout;
    ImageCardFragment imageCardFragment;
    ImageListFragment imageListFragment;
    AlbumCardFragment albumCardFragment;
    AlbumListFragment albumListFragment;
    EmptyFragment emptyFragment = new EmptyFragment();
    @Inject
    public MasterFileServiceGenerator masterFileServiceGenerator;
    @Inject
    public MasterUserServiceGenerator masterServiceGenerator;
    @Inject
    public UserInfoRepository userInfoRepository;
    @Inject
    public FileServiceGenerator fileServiceGenerator;
    @Inject
    public TrashServiceGenerator trashServiceGenerator;
    @Inject
    public MasterTrashServiceGenerator masterTrashServiceGeneratior;
    EmptyAlbumImageFragment emptyAlbumImageFragment = new EmptyAlbumImageFragment();
    EmptyAlbumFragment emptyAlbumFragment = new EmptyAlbumFragment();
    EmptyFilteringResultFragment emptyFilteringResultFragment = new EmptyFilteringResultFragment();
    static RelativeLayout bottomMenu;
    ImageView imgViewUpload, imgViewCard, imgViewList, filterButton;
    // card list mode: image, album, imageInAlbum
    public static final String imageMode = "image";
    public static final String albumMode = "album";

    public int LIMIT = 999;
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
    private LoadFileRequestDto loadFileRequestDto;
    public static List<Image> selectedImages = new ArrayList<>();

    public static List<UserFile> currentImagesInAlbum = new ArrayList<>();

    public static UserAlbum currentSelectAlbum;

    private static final int Read_Permission = 101;

    RelativeLayout layoutLibFunctions;

    Button libraryCancelButton;

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

        // bottom menu functions
        layoutLibFunctions = findViewById(R.id.layout_library_functions);
        moveToAlbum = findViewById(R.id.text_view_move_to_album);
        addToAlbum = findViewById(R.id.text_view_add_to_album);
        deleteImage = findViewById(R.id.text_view_delete_in_file);
        cutImage = findViewById(R.id.text_view_delete_in_album);
        libraryCancelButton = findViewById(R.id.library_cancel_button);

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

    @Override
    public int navigateId() {
        return R.id.nav_library;
    }

    // ============ handle navigation drawer ============
    public void openNavigationDrawer(View v) {
        System.out.println("HELLO");
        rootDrawerLayout.openDrawer(GravityCompat.START);
    }

    //    ============= Start Album handler =============
    @Inject
    public AlbumServiceGenerator albumServiceGenerator;
    @Inject
    public MasterAlbumServiceGenerator masterAlbumServiceGenerator;
    @Inject
    public TokenRepository tokenRepository;

    // menu bottom functions
    private UserAlbum destinationAlbum = new UserAlbum();

    public void addToAlbum(View v) {
        Single<MasterAlbumService.QueryResponse<UserAlbum>> results = getAlbums();
        results
                .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe(this::handleAlbumAfterCall);
    }

    private void handleAlbumAfterCall(MasterAlbumService.QueryResponse<UserAlbum> response, Throwable throwable) {
        if (throwable != null)
            System.out.println("Get Album error!");
        else {
            List<UserAlbum> albums = response.getResponse();
            System.out.println("Album size " + albums.size());

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
            ArrayList<UserAlbum> cloneAlbumList = new ArrayList<UserAlbum>(albums);
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
                String[] imageNames = selectedImages.stream().map(Image::getId).toArray(String[]::new);

                Single<Response<List<UserFileInAlbum>>> createAlbumResult = albumServiceGenerator.getService().addImagesToAlbum(tokenRepository.getToken().getUserId(), destinationAlbum.getName(), imageNames);

                createAlbumResult
                        .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                        .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                        .subscribe(this::handleAfterAddAlbumCall);
                // Đóng bottommsheet
                chooseAlbumBottomSheetDialog.dismiss();
                selectedImages.clear();
            });
        }
    }

    public  void handleAfterAddAlbumCall(Response<List<UserFileInAlbum>> response, Throwable throwable) {
        if (throwable != null)
            System.out.println("Create Album APi  error");
        else {
            Toast.makeText(this, "Add image to album success", Toast.LENGTH_SHORT).show();
            List<UserFileInAlbum> album = response.body();
        }
    }

    private Single<MasterAlbumService.QueryResponse<UserAlbum>> getAlbums() {
        return masterAlbumServiceGenerator
                .getService()
                .getAlbums(tokenRepository.getToken().getUserId(), 999);
    }

    public void moveToAlbum(View v) { // call in XML file
        Single<MasterAlbumService.QueryResponse<UserAlbum>> results = getAlbums();
        results
                .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe(this::handleAlbumAfterCallMove);
        bottomMenu.setVisibility(View.GONE);
    }

    private void handleAlbumAfterCallMove(MasterAlbumService.QueryResponse<UserAlbum> response, Throwable throwable) {
        if (throwable != null)
            System.out.println("Get Album error!");
        else {
            List<UserAlbum> albums = response.getResponse();
            System.out.println("Album size " + albums.size());

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
            ArrayList<UserAlbum> cloneAlbumList = new ArrayList<UserAlbum>(albums);
            cloneAlbumList.removeIf(albumItem -> albumItem.getName() == "favorite" || albumItem.getName() == "trash" || albumItem.getName() == currentSelectAlbum.getName());
            AlbumRadioAdapter albumRadioAdapter = new AlbumRadioAdapter(cloneAlbumList, btnConfirm, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            albumRadioRecyclerView.setLayoutManager(linearLayoutManager);
            albumRadioRecyclerView.setAdapter(albumRadioAdapter);
            imageViewBackward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseAlbumBottomSheetDialog.dismiss();
                    selectedImages.clear();
                }
            });


            createAlbumButton.setVisibility(View.GONE);

            btnConfirm.setOnClickListener(event -> {
                String[] imageNames = selectedImages.stream().map(Image::getId).toArray(String[]::new);

                System.out.println(imageNames);

                Single<Response<List<UserFileInAlbum>>> moveToAlbumResult = albumServiceGenerator.getService().moveImagesToAlbum(tokenRepository.getToken().getUserId(), destinationAlbum.getName(), currentSelectAlbum.getName(), imageNames);

                moveToAlbumResult
                        .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                        .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                        .subscribe(this::handleAfterMoveAlbumCall);
                // Đóng bottommsheet
                chooseAlbumBottomSheetDialog.dismiss();
                selectedImages.clear();
                // Đóng bottommsheet
                chooseAlbumBottomSheetDialog.dismiss();
            });
        }
    }

    public  void handleAfterMoveAlbumCall(Response<List<UserFileInAlbum>> response, Throwable throwable) {
        if (throwable != null)
            System.out.println("Move Album APi  error");
        else {
            Toast.makeText(this, "Move images to album success", Toast.LENGTH_SHORT).show();
            List<UserFileInAlbum> album = response.body();
            clickOpenAlbumImageList(currentSelectAlbum);
            libraryCancelButton.performClick();
            System.out.println("Album open name: " + currentSelectAlbum.getName());
        }
    }

    public void setDestinationAlbum(UserAlbum album) {
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
                selectedImages.clear();
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
            if (title.trim().isEmpty())
                return;
            // check existed album name
            Album result = albumList.stream().filter(s -> (s.getName().equals(title))).findFirst().orElse(null);
            if (result != null) {
                Toast.makeText(this, "This album album is existed!", Toast.LENGTH_SHORT).show();
                return;
            }

            String password = String.valueOf(passwordEditText.getText());
            if (password.trim().isEmpty())
                password = null;
            Single<Response<UserAlbum>> createAlbumResult = albumServiceGenerator.getService()
                    .createAlbum(tokenRepository.getToken().getUserId(), title, UserAlbum.builder().password(password).build());
            createAlbumResult
                    .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                    .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                    .subscribe(this::handleAfterCreateAlbumCall);

            selectedImages.clear();
        });
    }

    public  void handleAfterCreateAlbumCall(Response<UserAlbum> response, Throwable throwable) {
        if (throwable != null)
        System.out.println("Create Album APi  error");
        else {
            UserAlbum album = response.body();
            Toast.makeText(this, "Create Album successfully!", Toast.LENGTH_SHORT).show();
            System.out.println(String.format("Created album: %s", album.getName()));
        }
    }

    public void clickOpenAlbumImageList(UserAlbum selectedAlbum) {
//        isOpeningAlbum = album;
        mode = imageInAlbumMode;
        layoutLibFunctions.setVisibility(View.GONE);
        if (selectedAlbum.getName().equals("trash")) {
            Single<MasterTrashService.QueryResponse<UserFile>> results = getTrashFiles();
            results.observeOn(AndroidSchedulers.from(Looper.myLooper()))
                    .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                    .subscribe((res, err) -> handleAfterCallTrash(res, err, selectedAlbum));
        }
        else if (selectedAlbum.getName().equals("favorite")){
            Single<MasterFileService.QueryResponse<UserFile>> results = getFiles();
            results.observeOn(AndroidSchedulers.from(Looper.myLooper()))
                    .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                    .subscribe((res, err) -> handleAfterCallFavorite(res, err, selectedAlbum));
        }
        else {

            Single<MasterAlbumService.QueryResponse2<UserFile>> results = getAlbumFiles(selectedAlbum.getName());
            results
                    .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                    .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                    .subscribe((res, err) -> handleAfterCall(res, err, selectedAlbum));
        }
    }

    private void handleAfterCall(MasterAlbumService.QueryResponse2<UserFile> response, Throwable throwable, UserAlbum selectedAlbum) {
        if (throwable != null)
            System.out.println("Get file in album error!");
        else {
            List<UserFile> files = response.getResponse();
            System.out.println("File size " + files.size());
            ft = getSupportFragmentManager().beginTransaction();
            currentImagesInAlbum = files;
            currentSelectAlbum = selectedAlbum;
            AlbumImageListFragment albumImagesList = AlbumImageListFragment.newInstance(files, selectedAlbum, "card");
            ft.replace(R.id.layout_display_images, albumImagesList).commit();
        }
    }

    private void handleAfterCallTrash(MasterTrashService.QueryResponse<UserFile> response, Throwable throwable, UserAlbum selectedAlbum) {
        if (throwable != null)
            System.out.println("Get file in album error!");
        else {
            List<UserFile> files = response.getResponse();
            System.out.println("File size " + files.size());
            ft = getSupportFragmentManager().beginTransaction();
            currentImagesInAlbum = files;
            currentSelectAlbum = selectedAlbum;
            AlbumImageListFragment albumImagesList = AlbumImageListFragment.newInstance(files, selectedAlbum, "card");
            ft.replace(R.id.layout_display_images, albumImagesList).commit();
        }
    }

    private void handleAfterCallFavorite(MasterFileService.QueryResponse<UserFile> response, Throwable throwable, UserAlbum selectedAlbum) {
        if (throwable != null)
            System.out.println("Get file in album error!");
        else {
            List<UserFile> files = response.getResponse();
            System.out.println("File size " + files.size());
            ft = getSupportFragmentManager().beginTransaction();
            currentImagesInAlbum = files;
            currentSelectAlbum = selectedAlbum;
            AlbumImageListFragment albumImagesList = AlbumImageListFragment.newInstance(files, selectedAlbum, "card");
            ft.replace(R.id.layout_display_images, albumImagesList).commit();
        }
    }

    private Single<MasterAlbumService.QueryResponse2<UserFile>> getAlbumFiles(String albumName) {
        return masterAlbumServiceGenerator
                .getService()
                .getAlbumFiles(tokenRepository.getToken().getUserId(), albumName, LIMIT);
    }

    private Single<MasterTrashService.QueryResponse<UserFile>> getTrashFiles() {
        return masterTrashServiceGeneratior
                .getService()
                .getTrashFiles(tokenRepository.getToken().getUserId(), LIMIT, null);
    }

    private Single<MasterFileService.QueryResponse<UserFile>> getFiles() {
        return masterFileServiceGenerator
                .getService()
                .getFiles(tokenRepository.getToken().getUserId(), true, LIMIT, null, null);
    }

    public void showEmptyImagesInAlbum() {
        ft = getSupportFragmentManager().beginTransaction();
        EmptyFragment emptyFragment = EmptyFragment.newInstance(mode, true);
        ft.replace(R.id.layout_display_images, emptyFragment).commit();
    }

    public void setShowLayoutLibFuntions() {
        layoutLibFunctions.setVisibility(View.VISIBLE);
    }

    public void clickOpenImageList() {
        // check empty list
        mode = imageMode;

        updateImageViewDisplay();
    }

    public void triggerAlbumButton() {
        albumButton.callOnClick();
    }


    private void deleteImages(String[] fileNames)
    {
        trashServiceGenerator.getService()
                .deleteFiles(tokenRepository.getToken().getUserId(), fileNames)
                .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe((res, err) -> {
                    if (err != null || !res.isSuccessful())
                        return;

                    startCallApi(masterServiceGenerator.getService().getUserInfo(tokenRepository.getToken().getUserId()));
                    updateImageViewDisplay();
                });
    }

    private void restoreImagesFromTrash(String[] fileNames)
    {
        trashServiceGenerator.getService()
                .restoreFiles(tokenRepository.getToken().getUserId(), fileNames)
                .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe((res, err) -> {
                    if (err != null || !res.isSuccessful())
                        return;
                });
    }

    public void restoreAllTrash() {
        trashServiceGenerator.getService()
                .restoreAllFiles(tokenRepository.getToken().getUserId())
                .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe((res, err) -> {
                    if (err != null || !res.isSuccessful())
                        return;

                    startCallApi(masterServiceGenerator.getService().getUserInfo(tokenRepository.getToken().getUserId()));
                    updateImageViewDisplay();
                    Toast.makeText(context, "Restore all image successfully!", Toast.LENGTH_SHORT).show();

                });
    }

    private void clearImagesFromTrash(String[] fileNames)
    {
        trashServiceGenerator.getService()
                .clearFiles(tokenRepository.getToken().getUserId(), fileNames)
                .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe((res, err) -> {
                    if (err != null || !res.isSuccessful())
                        return;
                });
    }

    public void clearTrash() {
        trashServiceGenerator.getService()
                .clearAllFiles(tokenRepository.getToken().getUserId())
                .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe((res, err) -> {
                    if (err != null || !res.isSuccessful())
                        return;

                    startCallApi(masterServiceGenerator.getService().getUserInfo(tokenRepository.getToken().getUserId()));
                    updateImageInAlbumViewDisplay();
                    Toast.makeText(context, "Clear trash bin successfully!", Toast.LENGTH_SHORT).show();
                });
    }

    public void renameAlbum(UserAlbum renamedAlbum) {
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

            String oldName = currentSelectAlbum.getName();

            currentSelectAlbum.setName(newName);

            Single<Response<UserAlbum>> updateAlbumResult = albumServiceGenerator.getService().updateAlbum(tokenRepository.getToken().getUserId(), oldName, currentSelectAlbum);

            updateAlbumResult
                    .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                    .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                    .subscribe(this::handleAfterUpdateAlbumCall);

            createAlbumBottomSheetDialog.dismiss();
        });

        backIcon.setOnClickListener(v -> createAlbumBottomSheetDialog.dismiss());
    }

    public  void handleAfterUpdateAlbumCall(Response<UserAlbum> response, Throwable throwable) {
        if (throwable != null)
            System.out.println("Update Album APi  error");
        else {
            Toast.makeText(this, "Rename album successfully!", Toast.LENGTH_SHORT).show();
//            UserAlbum album = response.body();
        }
    }

    public void addToFavorite() {

        String userId = tokenRepository.getToken().getUserId();
        String[] imgs = selectedImages.stream().map(Image::getId).toArray(String[]::new);
        for (String img : imgs)
        {
            fileServiceGenerator.getService().updateFile(tokenRepository.getToken().getUserId(), UserFile.builder()
                            .userId(userId)
                            .isFavourite(true)
                            .fileName(img)
                    .build())
                    .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                    .subscribe((res, err) -> {
                        if (err != null || !res.isSuccessful())
                            return;
                        updateImageViewDisplay();
                    });
        }
        bottomMenu.setVisibility(View.GONE);
        selectedImages.clear();
        Toast.makeText(context, "Add to favorite success", Toast.LENGTH_SHORT).show();
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
        layoutLibFunctions.setVisibility(View.VISIBLE);
        if (!filtered) {
            clonedImgList = new ArrayList<>(imgList);
        }

        if (userInfoRepository.getInfo().getImgCount() == 0){
            ft = getSupportFragmentManager().beginTransaction();
            emptyFragment = EmptyFragment.newInstance(imageMode, true);
            ft.replace(R.id.layout_display_images, emptyFragment).commit();
        }
        else {
            if (displayView.equals("card")) {
                imgViewList.setAlpha(0.5F);
                imgViewCard.setAlpha(1F);

                ft = getSupportFragmentManager().beginTransaction();
                imageCardFragment = ImageCardFragment.newInstance();
                ft.replace(R.id.layout_display_images, imageCardFragment).commit();
            }
            else if (displayView.equals("list")) {
                imgViewList.setAlpha(1F);
                imgViewCard.setAlpha(0.5F);

                ft = getSupportFragmentManager().beginTransaction();
                imageListFragment = ImageListFragment.newInstance();
                ft.replace(R.id.layout_display_images, imageListFragment).commit();
            }

        }
        setStatusOfWidgets();
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
        if (currentImagesInAlbum.size() > 0) {

            if (Objects.equals(displayView, "card")) {
                ft = getSupportFragmentManager().beginTransaction();
                AlbumImageListFragment albumImagesList = AlbumImageListFragment.newInstance(currentImagesInAlbum, currentSelectAlbum, "card");
                ft.replace(R.id.layout_display_images, albumImagesList).commit();
            } else {
                ft = getSupportFragmentManager().beginTransaction();
                AlbumImageListFragment albumImagesList = AlbumImageListFragment.newInstance(currentImagesInAlbum, currentSelectAlbum, "list");
                ft.replace(R.id.layout_display_images, albumImagesList).commit();
            }
        } else {
            ft = getSupportFragmentManager().beginTransaction();
            emptyAlbumImageFragment = EmptyAlbumImageFragment.newInstance();
            ft.replace(R.id.layout_display_images, emptyAlbumImageFragment).commit();
        }
    }

    private final ActivityResultLauncher<Intent> launcherPickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Intent data = result.getData();

        if (result.getResultCode() == RESULT_OK && data != null) {

            // Lấy nhiều ảnh
            if (data.getClipData() != null) {
                int countOfImages = data.getClipData().getItemCount();

                for (int i = 0; i < countOfImages; i++) {
                    // Thêm dữ liệu
                    Uri imageURI = data.getClipData().getItemAt(i).getUri();

                    Image image = new Image();
                    image.setUri(imageURI);
                    image.setLocation("");
                    image.setDescription("");


                    GetInformationThread getInformationThread = new GetInformationThread(this, image, imageURI);
                    getInformationThread.start();

                    Log.e("NOTE", "URI1 " + image.getUri());

                    //imgList.add(0, image);
                }

                // Lấy 1 ảnh
            } else {
                Uri imageURI = data.getData();
                // Thêm dữ liệu
                Image image = new Image();
                image.setUri(imageURI);
                image.setLocation("");
                image.setDescription("");
                GetInformationThread getInformationThread = new GetInformationThread(this, image, imageURI);
                getInformationThread.start();
            }
        } else {
            Toast.makeText(this, "You haven't picked any images", Toast.LENGTH_LONG).show();
        }

        // Update Fragment View
    });

    //Kiểm tra xem ứng dụng có quyền truy cập chưa, nếu chưa sẽ yêu cầu
    private Boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }
        return false;
    }


    public void setStatusOfWidgets() {
        if (userInfoRepository.getInfo().getImgCount() > 0) {
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

//    public String getImagePath(Uri imageURI) throws IOException {
//        String imagePath = "";
//
//        // lấy InputStream từ Uri của ảnh
//        InputStream inputStream = getContentResolver().openInputStream(imageURI);
//
//        // tạo tập tin tạm thời để lưu ảnh
//        File tempFile = null;
//        tempFile = File.createTempFile("temp", null, getCacheDir());
//        tempFile.deleteOnExit();
//
//        // copy dữ liệu từ InputStream vào tập tin tạm thời
//        copyInputStreamToFile(inputStream, tempFile);
//
//        // lấy đường dẫn tới tập tin tạm thời
//        imagePath = tempFile.getAbsolutePath();
//
//        return imagePath;
//    }

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

    private final ActivityResultLauncher<Intent> launcherSendAndReceiveImageInAlbum = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
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

    private final ActivityResultLauncher<Intent> launcherSendAndReceiveImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
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
                    fileServiceGenerator.getService()
                            .updateFile(tokenRepository.getToken().getUserId(), UserFile.builder()
                                    .fileName(selectedImage.getId())
                                    .description(selectedImage.getDescription()).build())
                            .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                            .subscribe((res, err) -> {
                                if (err != null || !res.isSuccessful())
                                    return;
                                //updateImageViewDisplay();
                            });
                    break;
                }

                case DELETE_IMAGE: {
                    // delete image
                    //imgList.remove(position);

                    //List<Image> lstdeletedImage = new ArrayList<>();
                    //lstdeletedImage.add(selectedImage);
                    //trashBin.getAlbumImages().add(selectedImage);
                    selectedImages.add(selectedImage);
                    deleteImages(selectedImages.stream().map(Image::getId).toArray(String[]::new));
                    break;
                }
            }
        } else {
            //Toast.makeText(this, "You haven't picked any images", Toast.LENGTH_LONG).show();
        }
    });


    protected void onResume() {
        super.onResume();

        try {
            startCallApi(masterServiceGenerator.getService().getUserInfo(tokenRepository.getToken().getUserId()));
        } catch (Exception e) {
            Log.e("Library", e.getMessage());
        }
    }

    @Override
    protected void handleCallSuccess(UserInfo body) {

        userInfoRepository.setInfo(body);

        updateImageViewDisplay();
    }


    @Override
    public void view(Image item, ImagesAdapter.ImageViewHolder viewHolder, int pos) {
        ImageView imageView = viewHolder.getImgView();
        if (imageView.getColorFilter() != null) {
            // FOR UI
            imageView.clearColorFilter();
            // FOR LOGIC
            removeSingleImageAndRemoveBottomMenuIfNoImageLeft(item);
        } else {
            sendAndReceiveImage(item, pos);
        }
    }

    @Override
    public void longView(Image item, ImagesAdapter.ImageViewHolder viewHolder, int pos) {
        ImageView imageView = viewHolder.getImgView();
        if (imageView.getColorFilter() == null) {
            // FOR UI
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.chosen_image));
            // FOR LOGIC
            selectSingleImageAndOpenBottomMenuIfNotYet(item);
        }
    }


    public class GetInformationThread extends Thread {
        private Image image;
        private Uri uri;
        private LibraryActivity libraryActivity;

        private GetInformationThread(LibraryActivity libraryActivity, Image image, Uri uri) {
            this.image = image;
            this.uri = uri;
            this.libraryActivity = libraryActivity;
        }

        @Override
        public void run() {
            String imagePath = null;
            //imagePath = getImagePath(uri);
            imagePath = RealPathUtil.getRealPath(context, uri);

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
            image.setLocation(location);

            ImageDto imageDto = new ImageDto(null,
                                            image.getTags(),
                                            image.getDescription(),
                                            image.getDate(),
                                            image.getSize(),
                                            image.getLocation(),
                                            image.getUri().toString());

            // Call Api Upload File
            ApiUploadFile apiUploadFile = new ApiUploadFile(fileServiceGenerator, tokenRepository.getToken().getUserId(), imageDto, imagePath);
            apiUploadFile.callApiUploadFile(libraryActivity::updateImageViewDisplay);

            //Image image = new Image(dateTime, sizeOfImage, "A favorite image", address, uri);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (imageCardFragment.cardAdapter != null) {
//            imageCardFragment.cardAdapter.release();
//            //imageListFragment.listAdapter.release();
//        }
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
        RecyclerView libraryRecyclerView = null;
        if (mode == imageMode)
            libraryRecyclerView = (RecyclerView) libraryLinearLayout.getChildAt(3);
        else if (mode == imageInAlbumMode)
            libraryRecyclerView = (RecyclerView) libraryLinearLayout.getChildAt(2);
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
                    addToFavorite();
                    return true;
                case R.id.combine_image_menu_item:
                    combineImages();
                    return true;
                case R.id.compress_image_menu_item:
                    // Do something when the "Compress" item is clicked
                    return true;
                case R.id.make_a_presentation_menu_item:
                    presentImages();
                    return true;
                default:
                    return super.onMenuItemSelected(0, item);

            }
        });
        popupMenu.show();
    }

    private void presentImages() {
        Intent intent = new Intent(this, ImagePresentationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) selectedImages);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private final ActivityResultLauncher<Intent> imageCombinationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Intent intent = result.getData();

        if (result.getResultCode() == RESULT_OK && intent != null) {
            String action = intent.getStringExtra("action");

            if (Objects.equals(action, "back")) {
                removeBottomMenuAndAllImages(null);
            } else if (Objects.equals(action, "add more")) {
                Toast.makeText(this, "Now you can continue selecting images", Toast.LENGTH_LONG).show();
            } else if (Objects.equals(action, "combine ok")) {
                Toast.makeText(this, "Successfully combing images", Toast.LENGTH_LONG).show();
            }
        } else {
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
        deleteImages(selectedImages.stream().map(Image::getId).toArray(String[]::new));
        //trashBin.getAlbumImages().addAll(selectedImages);
        //imgList.removeAll(selectedImages);
        selectedImages.clear();
        bottomMenu.setVisibility(View.GONE);
        updateImageViewDisplay();
    }

    // ========== delete from album ========
    public void deleteFromAlbum(View v) {
        String[] imageNames = selectedImages.stream().map(Image::getId).toArray(String[]::new);
        System.out.println("Delete size: " + imageNames.length);
        System.out.println("Delete file name 0: " + imageNames[0]);
        Single<Response<List<UserFileInAlbum>>> deleteFromAlbumResult = albumServiceGenerator.getService().removeImagesFromAlbum(tokenRepository.getToken().getUserId(), currentSelectAlbum.getName(), imageNames);

        deleteFromAlbumResult
                .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe(this::handleAfterDeleteFromAlbumCall);
    }

    public void handleAfterDeleteFromAlbumCall(Response<List<UserFileInAlbum>> response, Throwable throwable) {
        if (throwable != null)
            System.out.println("Delete from Album APi  error");
        else {
            Toast.makeText(this, "Remove images from album success", Toast.LENGTH_SHORT).show();
            List<UserFileInAlbum> album = response.body();
            libraryCancelButton.performClick();
            clickOpenAlbumImageList(currentSelectAlbum);
            System.out.println("Album open name: " + currentSelectAlbum.getName());
        }
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