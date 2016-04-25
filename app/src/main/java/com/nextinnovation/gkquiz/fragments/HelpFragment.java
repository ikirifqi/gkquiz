package com.nextinnovation.gkquiz.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextinnovation.gkquiz.R;
import com.nextinnovation.gkquiz.application.App;

/**
 * Created by rifqi on Apr 25, 2016.
 */
public class HelpFragment extends Fragment implements View.OnClickListener {
    private View mDismissBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate fragment layout ane grab view references
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        TextView title = (TextView)view.findViewById(R.id.help_title);
        TextView content = (TextView)view.findViewById(R.id.help_content);
        mDismissBtn = view.findViewById(R.id.help_dismiss);

        // set font family to text
        Typeface boldTypeface = App.getBoldTypeface();
        title.setTypeface(boldTypeface);
        content.setTypeface(boldTypeface);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // attaching event listeners
        mDismissBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mDismissBtn) {
            getActivity().onBackPressed();
        }
    }
}
