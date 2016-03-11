package com.nextinnovation.gkquiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nextinnovation.gkquiz.R;
import com.nextinnovation.gkquiz.adapters.AnswerAdapter;
import com.nextinnovation.gkquiz.application.App;
import com.nextinnovation.gkquiz.application.Constants;
import com.nextinnovation.gkquiz.application.GKHelper;
import com.nextinnovation.gkquiz.application.dao.Question;
import com.nextinnovation.gkquiz.dialogs.ConfirmDialog;
import com.nextinnovation.gkquiz.entities.QuestionEntity;
import com.nextinnovation.gkquiz.interfaces.IDAOSingleDataListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionActivity extends AppCompatActivity
        implements AnswerAdapter.AnswerAdapterListener {
    private static final String EXTRA_DIFFICULTY_INDEX = "eDifficultyIndex";
    private static final String EXTRA_CATEGORY_INDEX = "eCategoryIndex";
    private static final String EXTRA_CATEGORY_TITLE = "eCategoryTitle";

    private RecyclerView mAnswerList;
    private TextView mCategoryText;
    private TextView mQuestionText;
    private ImageView mLoadingNotifier;

    private QuestionEntity mQuestion;
    private AnswerAdapter mAdapter;
    private Question mQuestionDB;

    private int mDifficultyIndex;
    private int mCategoryId;

    public static void start(Activity activity, int difficultyIndex, String categoryTitle,
                             int categoryIndex, int requestCode) {
        Intent intent = new Intent(activity, QuestionActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY_INDEX, difficultyIndex);
        intent.putExtra(EXTRA_CATEGORY_INDEX, categoryIndex);
        intent.putExtra(EXTRA_CATEGORY_TITLE, categoryTitle);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // obtain activity parameters
        Intent intent = getIntent();
        String categoryTitle = intent.getStringExtra(EXTRA_CATEGORY_TITLE);
        mDifficultyIndex = intent.getIntExtra(EXTRA_DIFFICULTY_INDEX, -1);
        mCategoryId = intent.getIntExtra(EXTRA_CATEGORY_INDEX, -1);
        if(mDifficultyIndex < 0 || mCategoryId < 0) {
            App.logError("QuestionActivity started without parameter", null);
            finish();
            return;
        }

        mQuestionDB = new Question(this, false);

        setContentView(R.layout.activity_question);
        mAnswerList = (RecyclerView)findViewById(R.id.question_answers);
        mCategoryText = (TextView)findViewById(R.id.question_category);
        mQuestionText = (TextView)findViewById(R.id.question_content);
        mLoadingNotifier = (ImageView)findViewById(R.id.question_loader);

        Glide.with(this).load(R.drawable.loader).asGif().into(mLoadingNotifier);

        mCategoryText.setText(categoryTitle);

        mAnswerList.setLayoutManager(new GridLayoutManager(this, 2));

        // apply custom fonts
        mCategoryText.setTypeface(App.getBoldTypeface());
        mQuestionText.setTypeface(App.getRegularTypeface());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mQuestionDB.getRandomQuestion(mCategoryId, mDifficultyIndex,
                new IDAOSingleDataListener<QuestionEntity>() {
                    @Override
                    public void onSuccess(QuestionEntity questionEntity) {
                        String[] rawOptions = TextUtils.split(
                                questionEntity.getOptions(), Constants.OPTIONS_SEPARATOR_CODE);
                        List<String> options = new ArrayList<>();
                        options.addAll(Arrays.asList(rawOptions));

                        mQuestion = questionEntity;
                        mAdapter = new AnswerAdapter(options, QuestionActivity.this);
                        mAnswerList.setHasFixedSize(true);
                        mAnswerList.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                        mQuestionText.setText(questionEntity.getQuestion());

                        mAnswerList.setVisibility(View.VISIBLE);
                        mCategoryText.setVisibility(View.VISIBLE);
                        mQuestionText.setVisibility(View.VISIBLE);
                        mLoadingNotifier.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        GKHelper.showToastLong(QuestionActivity.this, R.string.error_other);
                        App.logError("Failed to fetch QuestionActivity", e);
                        finish();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        if(mQuestion.getAnswer() == position) {
            GKHelper.showToastShort(this, R.string.question_correct);

            Intent result = new Intent();
            result.putExtra(Constants.QUESTION_RESPONSE_CATEGORY_ID, mCategoryId);
            setResult(Constants.QUESTION_RESULT_CODE, result);
            finish();
        }
        else {
            GKHelper.showToastShort(this, R.string.question_wrong);

            MainActivity.start(this, true);
        }
    }

    @Override
    public void onBackPressed() {
        new ConfirmDialog(R.string.question_back_dlg_title, R.string.question_back_dlg_content) {
            @Override
            protected void onYesClicked() {
                MainActivity.start(QuestionActivity.this, true);
            }

            @Override
            protected void onNoClicked() {
                // nothing needed here
            }
        }.show(getFragmentManager(), null);
    }
}
