package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class fragment_home extends Fragment {
    ViewFlipper viewFlipper;
    float xAtDown, xAtUp;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /**자동 이미지 배너*/
        final ViewFlipper viewFlipper;
        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper1);
        viewFlipper.setAutoStart(true);
        /** 자동이미지 터치로 넘기기*/
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v != viewFlipper) return false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xAtDown = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    xAtUp = event.getX();
                    if (xAtUp < xAtDown) {
                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in));
                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_out));
                        viewFlipper.showNext();
                    } else if (xAtUp > xAtDown) {
                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_in));
                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_out));
                        viewFlipper.showPrevious();
                    }
                }
                return true;
            }
        });
        viewFlipper.setFlipInterval(3000);      //3초마다 자동 이미지 전환
        viewFlipper.startFlipping();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // destroy all menu and re-call onCreateOptionsMenu
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_top_navigation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mypage_btn :
                Intent intent = new Intent(getActivity(), activity_mypage.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
