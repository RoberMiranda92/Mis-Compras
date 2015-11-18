package com.ubu.miscompras.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.ubu.miscompras.R;
import com.ubu.miscompras.activity.CropActivity;
import com.ubu.miscompras.comunication.WebService;

/**
 * Created by RobertoMiranda on 17/11/15.
 */
public class MainFragment extends Fragment {

    private Animation rotate_forward, rotate_backward;
    private FloatingActionButton addTicket_Button;
    private boolean isButtonClick = false;
    private FloatingActionButton addCamera_Button;
    private FloatingActionButton addImage_Button;
    private Animation fab_open;
    private Animation fab_close;

    private final int LOAD_IMAGE_GALLERY = 1;
    private final int LOAD_IMAGE_CAMERA = 2;
    private static final int CROP_PIC = 3;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.app_bar_main, container, false);

        imageView = (ImageView) mView.findViewById(R.id.imageView_recortada);


        addTicket_Button = (FloatingActionButton) mView.findViewById(R.id.FloattingButton_addTicket);
        addCamera_Button = (FloatingActionButton) mView.findViewById(R.id.FloattingButton_addCamera);
        addImage_Button = (FloatingActionButton) mView.findViewById(R.id.FloattingButton_addImage);

        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.FloattingButton_addTicket:
                        openButtonSelector();
                        break;
                    case R.id.FloattingButton_addCamera:
                        openCameraIntent();
                        break;
                    case R.id.FloattingButton_addImage:
                        openGalleryIntent();
                        break;

                }
            }
        };


        addTicket_Button.setOnClickListener(clickListener);
        addImage_Button.setOnClickListener(clickListener);
        addCamera_Button.setOnClickListener(clickListener);
        return mView;

    }


    private void openButtonSelector() {
        if (!isButtonClick) {
            addTicket_Button.startAnimation(rotate_forward);
            addImage_Button.startAnimation(fab_open);
            addCamera_Button.startAnimation(fab_open);
            isButtonClick = true;

        } else {
            addTicket_Button.startAnimation(rotate_backward);
            addImage_Button.startAnimation(fab_close);
            addCamera_Button.startAnimation(fab_close);
            isButtonClick = false;

        }
    }

    private void openGalleryIntent() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, LOAD_IMAGE_GALLERY);

    }

    private void openCameraIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, LOAD_IMAGE_CAMERA);//zero can be replaced with any action code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Uri uri = data.getData();
            switch (requestCode) {
                case CROP_PIC:
                    imageView.setImageURI(uri);
                    //startUpload(uri);
                    break;
                case LOAD_IMAGE_GALLERY:
                    performCrop(uri);
                    break;
                case LOAD_IMAGE_CAMERA:
                    performCrop(uri);
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Error al procesar imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void startUpload(Uri uri) {

        String picturePath = uri.getPath();
        WebService tars = new WebService();
        tars.execute(picturePath);
    }

    /**
     * this function does the crop operation.
     */
    private void performCrop(Uri source) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CropActivity.class);
        intent.setData(source);
        startActivityForResult(intent, CROP_PIC);
    }
}
