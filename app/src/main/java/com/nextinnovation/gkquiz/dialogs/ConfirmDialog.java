package com.nextinnovation.gkquiz.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.nextinnovation.gkquiz.R;
import com.nextinnovation.gkquiz.application.App;

/**
 * Created by rifqi on Mar 06, 2016.
 */
public abstract class ConfirmDialog extends DialogFragment {
    private int mTitleRes;
    private int mContentRes;

    public ConfirmDialog(int titleStrRes, int contentStrRes) {
        mTitleRes = titleStrRes;
        mContentRes = contentStrRes;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.show();

        // initialize views
        TextView mNoButton = (TextView)dialog.findViewById(R.id.confirm_dlg_no_button);
        TextView mYesButton = (TextView)dialog.findViewById(R.id.confirm_dlg_yes_button);
        TextView mTitle = (TextView)dialog.findViewById(R.id.confirm_dlg_title);
        TextView mContent = (TextView)dialog.findViewById(R.id.confirm_dlg_content);

        // apply typeface to all views
        mNoButton.setTypeface(App.getBoldTypeface());
        mYesButton.setTypeface(App.getBoldTypeface());
        mTitle.setTypeface(App.getRegularTypeface());
        mContent.setTypeface(App.getRegularTypeface());

        // apply content data
        mTitle.setText(getString(mTitleRes));
        mContent.setText(getString(mContentRes));

        // apply listeners
        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNoClicked();
                dismiss();
            }
        });
        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onYesClicked();
                dismiss();
            }
        });

        return dialog;
    }

    protected abstract void onYesClicked();
    protected abstract void onNoClicked();
}
