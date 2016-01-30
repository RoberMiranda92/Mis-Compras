/*
*   Copyright (C) 2015 Roberto Miranda.
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
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
 * Primer fragment del manual.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
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
