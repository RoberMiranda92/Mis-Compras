package com.ubu.miscompras.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ubu.miscompras.R;
import com.ubu.miscompras.activity.AddProductsActivity;
import com.ubu.miscompras.presenter.MainFragmentPresenter;

/**
 * Created by RobertoMiranda on 17/11/15.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private Animation rotate_forward, rotate_backward;
    private FloatingActionButton addTicket_Button;
    private boolean isButtonClick = false;
    private FloatingActionButton addCamera_Button;
    private FloatingActionButton addImage_Button;
    private Animation fab_open;
    private Animation fab_close;

    public final int LOAD_IMAGE_GALLERY = 1;
    public final int LOAD_IMAGE_CAMERA = 2;
    public static final int CROP_PIC = 3;
    private ImageView imageView;
    private TextView editText;

    private MainFragmentPresenter presenter;
    private ProgressDialog barProgressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        presenter = new MainFragmentPresenter(this);

        View mView = inflater.inflate(R.layout.fragment_main, container, false);

        imageView = (ImageView) mView.findViewById(R.id.imageView_recortada);


        addTicket_Button = (FloatingActionButton) mView.findViewById(R.id.FloattingButton_addTicket);
        addCamera_Button = (FloatingActionButton) mView.findViewById(R.id.FloattingButton_addCamera);
        addImage_Button = (FloatingActionButton) mView.findViewById(R.id.FloattingButton_addImage);

        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);


        addTicket_Button.setOnClickListener(this);
        addImage_Button.setOnClickListener(this);
        addCamera_Button.setOnClickListener(this);

        editText = (TextView) mView.findViewById(R.id.textoJson);
        return mView;

    }


    public void openButtonSelector() {
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CROP_PIC:
                if (resultCode == Activity.RESULT_OK)
                    presenter.getProducts(data.getData());
                break;
            case LOAD_IMAGE_GALLERY:
                if (resultCode == Activity.RESULT_OK)
                    presenter.startCropActivity(data.getData());
                break;
            case LOAD_IMAGE_CAMERA:
                if (resultCode == Activity.RESULT_OK)
                    presenter.startCropActivity(data.getData());
        }

    }


    public void setText(String text) {
        editText.setText(text);
        Intent i = new Intent();
        i.setClass(getActivity(), AddProductsActivity.class);
        i.putExtra("productos", text);
        startActivity(i);
    }

    public void showError(String s) {
        if (barProgressDialog != null && barProgressDialog.isShowing())
            barProgressDialog.dismiss();
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void showProgresBar() {
        barProgressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialog);
        barProgressDialog.setTitle("Subiendo...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setIndeterminate(false);
        barProgressDialog.setMax(100);
        barProgressDialog.setProgress(0);
        barProgressDialog.show();


    }

    public void hideProgressBar() {
        barProgressDialog.dismiss();

    }

    public void setProgressPercentage(Integer progres) {
        barProgressDialog.setProgress(progres);
    }


    public void setProgressBarTitle(String progressBarText) {
        barProgressDialog.setTitle(progressBarText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FloattingButton_addTicket:
                openButtonSelector();
                break;
            case R.id.FloattingButton_addCamera:
                presenter.startCameraIntent();
                break;
            case R.id.FloattingButton_addImage:
                presenter.startGalleryIntent();
                break;
        }
    }
}
