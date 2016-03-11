package com.nextinnovation.gkquiz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextinnovation.gkquiz.R;
import com.nextinnovation.gkquiz.activities.CategoryActivity;
import com.nextinnovation.gkquiz.application.App;
import com.nextinnovation.gkquiz.application.Constants;

/**
 * Created by rifqi on Feb 15, 2016.
 */
public class DifficultyFragment extends Fragment implements View.OnClickListener {
    private View mDismissBtn;
    private View mEasyBtn;
    private View mNormalBtn;
    private View mHardBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate fragment layout ane grab view references
        View view = inflater.inflate(R.layout.fragment_difficulty, container, false);
        TextView difficultyTitle = (TextView)view.findViewById(R.id.difficulty_title);
        TextView difficultyEasy = (TextView)view.findViewById(R.id.difficulty_easy);
        TextView difficultyNormal = (TextView)view.findViewById(R.id.difficulty_normal);
        TextView difficultyHard = (TextView)view.findViewById(R.id.difficulty_hard);
        mDismissBtn = view.findViewById(R.id.difficulty_dismiss);

        // set font family to text view
        difficultyTitle.setTypeface(App.getBoldTypeface());
        difficultyEasy.setTypeface(App.getBoldTypeface());
        difficultyNormal.setTypeface(App.getBoldTypeface());
        difficultyHard.setTypeface(App.getBoldTypeface());

        // copy references to global variables
        mEasyBtn = difficultyEasy;
        mNormalBtn = difficultyNormal;
        mHardBtn = difficultyHard;

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // attaching event listeners
        mDismissBtn.setOnClickListener(this);
        mEasyBtn.setOnClickListener(this);
        mNormalBtn.setOnClickListener(this);
        mHardBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mDismissBtn) {
            getActivity().onBackPressed();
        }
        else if(v == mEasyBtn) {
            CategoryActivity.start(getActivity(), 0);
        }
        else if(v == mNormalBtn) {
            CategoryActivity.start(getActivity(), 1);
        }
        else if(v == mHardBtn) {
            CategoryActivity.start(getActivity(), 2);
        }
    }
}
