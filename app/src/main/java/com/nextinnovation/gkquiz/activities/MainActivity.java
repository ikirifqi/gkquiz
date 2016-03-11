package com.nextinnovation.gkquiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nextinnovation.gkquiz.R;
import com.nextinnovation.gkquiz.fragments.AboutFragment;
import com.nextinnovation.gkquiz.fragments.DifficultyFragment;
import com.nextinnovation.gkquiz.fragments.MenuFragment;
import com.nextinnovation.gkquiz.application.GKHelper;

public class MainActivity extends AppCompatActivity {
    public static void start(Activity activity, boolean shouldClearStack) {
        Intent intent = new Intent(activity, MainActivity.class);
        if(shouldClearStack) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        activity.startActivity(intent);
        activity.finish();
    }

    public enum MainMenu { PRIMARY, ABOUT, PLAY }

    private MenuFragment mMenuFragment;
    private AboutFragment mAboutFragment;
    private DifficultyFragment mDifficultyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get view references
        ImageView background = (ImageView)findViewById(R.id.main_background);
        ImageView logo = (ImageView)findViewById(R.id.main_logo);

        // apply images in the background
        Glide.with(this).load(R.drawable.main_background).crossFade().into(background);
        Glide.with(this).load(R.drawable.primary_logo).crossFade().into(logo);

        // create fragments object
        mMenuFragment = new MenuFragment();
        mAboutFragment = new AboutFragment();
        mDifficultyFragment = new DifficultyFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // show main menu at the very start
        gotoMenu(MainMenu.PRIMARY);
    }

    @Override
    public void onBackPressed() {
        int stackCount = getFragmentManager().getBackStackEntryCount();

        if(stackCount > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    public void gotoMenu(MainMenu menu) {
        switch(menu) {
            case PRIMARY:
                GKHelper.runFragment(this, R.id.main_frame, mMenuFragment, false, true);
                break;

            case ABOUT:
                GKHelper.runFragment(this, R.id.main_frame, mAboutFragment, true, true);
                break;

            case PLAY:
                GKHelper.runFragment(this, R.id.main_frame, mDifficultyFragment, true, true);
                break;
        }
    }
}
