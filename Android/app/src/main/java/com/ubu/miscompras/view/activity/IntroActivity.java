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
package com.ubu.miscompras.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ubu.miscompras.R;
import com.ubu.miscompras.view.fragment.DefaultPageIntroFragment;
import com.ubu.miscompras.view.fragment.FirstPageIntroFragment;
import com.ubu.miscompras.view.fragment.SecondPageIntroFragment;
import com.ubu.miscompras.view.fragment.ThirdPageIntroFragment;


/**
 * Fragment Activity para el manual de usuario.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class IntroActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final int NUM_PAGES = 4;


    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;
    private FloatingActionButton nextButton;
    private LinearLayout dotsLayout;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        getWindow()
                .setNavigationBarColor(getResources().getColor(R.color.colorPrimary));

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.addOnPageChangeListener(this);
        mPager.setAdapter(mPagerAdapter);

        nextButton = (FloatingActionButton) findViewById(R.id.float_nextPage);
        nextButton.setOnClickListener(this);

        dotsLayout = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        dots = new ImageView[NUM_PAGES];
        for (int i = 0; i < NUM_PAGES; i++) {
            dots[i] = new ImageView(this);
            if (i == 0) {
                dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selectedpage_dot));
            } else
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselectedpage_icon));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            dotsLayout.addView(dots[i], params);

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < NUM_PAGES; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselectedpage_icon));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selectedpage_dot));

        if (mPager.getCurrentItem() == NUM_PAGES - 1) {
            nextButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
        } else {
            nextButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_white_24dp));
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onClick(View v) {
        if (mPager.getCurrentItem() == NUM_PAGES - 1) {
            startApp();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }

    /**
     * Intent para iniciar la aplicación.
     */

    private void startApp() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.pref_manual_key), false);
        editor.apply();

        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Adaptador de página.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new DefaultPageIntroFragment();
                case 1:
                    return new FirstPageIntroFragment();
                case 2:
                    return new SecondPageIntroFragment();
                case 3:
                    return new ThirdPageIntroFragment();
                default:
                    return new FirstPageIntroFragment();
            }

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
