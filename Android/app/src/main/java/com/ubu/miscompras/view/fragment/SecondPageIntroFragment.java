package com.ubu.miscompras.view.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.ubu.miscompras.R;

/**
 * Created by RobertoMiranda on 21/1/16.
 */
public class SecondPageIntroFragment extends Fragment {

    private VideoView gifImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_secondpageintro, container, false);

        gifImage = (VideoView) rootView.findViewById(R.id.videoView_gif);

        String uriPath = "android.resource://com.ubu.miscompras/raw/video2";
        Uri uri = Uri.parse(uriPath);
        gifImage.setVideoURI(uri);

        gifImage.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        return rootView;


    }

    @Override
    public void onStart() {
        gifImage.start();
        super.onStart();
    }

    @Override
    public void onPause() {
        gifImage.pause();
        super.onPause();
    }
}