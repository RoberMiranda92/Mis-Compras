package com.ubu.miscompras.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.VideoView;

import com.ubu.miscompras.R;

/**
 * Created by RobertoMiranda on 21/1/16.
 */
public class DefaultPageIntroFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_defaultpageintro, container, false);

        Animation rotate =  AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        //Animation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotate.setDuration(3000);
        ImageView image = (ImageView) rootView.findViewById(R.id.imageView_icon);
        image.startAnimation(rotate);

        return rootView;


    }

}
