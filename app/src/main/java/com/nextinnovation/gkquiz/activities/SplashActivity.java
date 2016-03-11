package com.nextinnovation.gkquiz.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nextinnovation.gkquiz.R;
import com.nextinnovation.gkquiz.application.GKHelper;
import com.nextinnovation.gkquiz.application.dao.Question;
import com.nextinnovation.gkquiz.application.dao.QuestionCategory;
import com.nextinnovation.gkquiz.interfaces.IDefaultStatusListener;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_WAIT_TIME = 6000;  // in second

    private ImageView mSplashLogo;
    private ImageView mProdiLogo;
    private ImageView mTelkomLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // get view reference
        ImageView loadingNotifier = (ImageView)findViewById(R.id.splash_loader);
        mSplashLogo = (ImageView)findViewById(R.id.splash_logo);
        mProdiLogo = (ImageView)findViewById(R.id.splash_prodi_logo);
        mTelkomLogo = (ImageView)findViewById(R.id.splash_telkom_logo);

        // apply images
        Glide.with(this).load(R.drawable.loader).asGif().into(loadingNotifier);
        Glide.with(this).load(R.drawable.splash_logo).crossFade().into(mSplashLogo);
        Glide.with(this).load(R.drawable.d3_informatics_logo).crossFade().into(mProdiLogo);
        Glide.with(this).load(R.drawable.telkom_logo).crossFade().into(mTelkomLogo);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final QuestionCategory category = new QuestionCategory(null, false);
        final Question question = new Question(null, false);

        // simple check to determine we need to proceed or seed data
        if(category.count() <= 0) {
            category.seed(new IDefaultStatusListener() {
                @Override
                public void onSuccess() {
                    question.seed(new IDefaultStatusListener() {
                        @Override
                        public void onSuccess() {
                            startBlockingThread(true);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            GKHelper.showToastLong(SplashActivity.this, R.string.error_other);
                        }
                    });
                }

                @Override
                public void onFailed(Exception e) {
                    GKHelper.showToastLong(SplashActivity.this, R.string.error_other);
                }
            });
        }
        else {
            startBlockingThread(true);
        }
    }

    /** this function used to block off UI thread in background so it's appear as loading in UI */
    private void startBlockingThread(final boolean shouldStepNextSplash) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(SPLASH_WAIT_TIME / 2);
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(shouldStepNextSplash) {
                    mSplashLogo.setVisibility(View.GONE);
                    mProdiLogo.setVisibility(View.VISIBLE);
                    mTelkomLogo.setVisibility(View.VISIBLE);
                    startBlockingThread(false);
                }
                else {
                    MainActivity.start(SplashActivity.this, true);
                }
            }
        }.execute();
    }
}
