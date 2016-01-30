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
 * Tercer fragment del manual.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
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
