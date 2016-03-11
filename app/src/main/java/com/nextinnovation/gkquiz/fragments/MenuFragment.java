package com.nextinnovation.gkquiz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nextinnovation.gkquiz.R;
import com.nextinnovation.gkquiz.activities.MainActivity;

/**
 * Created by rifqi on Feb 15, 2016.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {
    private View mAboutBtn;
    private View mPlayBtn;
    private MainActivity mMainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate fragment layout ane grab view references
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        mAboutBtn = view.findViewById(R.id.main_about_button);
        mPlayBtn = view.findViewById(R.id.main_play_button);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // attaching event listeners
        mAboutBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);

        // grab main activity reference
        mMainActivity = (MainActivity)getActivity();
    }

    @Override
    public void onClick(View v) {
        if(v == mAboutBtn) {
            mMainActivity.gotoMenu(MainActivity.MainMenu.ABOUT);
        }
        else if(v == mPlayBtn) {
            mMainActivity.gotoMenu(MainActivity.MainMenu.PLAY);
        }
    }
}
