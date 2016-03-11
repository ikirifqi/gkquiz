package com.nextinnovation.gkquiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nextinnovation.gkquiz.R;
import com.nextinnovation.gkquiz.adapters.CategoryAdapter;
import com.nextinnovation.gkquiz.adapters.CategoryAdapter.CategoryAdapterListener;
import com.nextinnovation.gkquiz.application.App;
import com.nextinnovation.gkquiz.application.Constants;
import com.nextinnovation.gkquiz.application.GKHelper;
import com.nextinnovation.gkquiz.application.dao.QuestionCategory;
import com.nextinnovation.gkquiz.entities.QuestionCategoryEntity;
import com.nextinnovation.gkquiz.interfaces.IDAOMultiDataListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapterListener {
    private static final String EXTRA_DIFFICULTY_INDEX = "eDifficultyIndex";

    private RecyclerView mCategoryList;
    private ImageView mLoadingNotifier;
    private TextView mCategoryLevelStatus;

    private List<QuestionCategoryEntity> mCategories;
    private CategoryAdapter mAdapter;
    private QuestionCategory mQuestionCategory;
    private int mReturnCategoryId;

    private boolean mShouldCheckForCheckmarks;

    public static List<Integer> sAnsweredCategories;    // to store answered category ids
    public static int sDifficultyIndex;                 // current difficulty index
    public static int sLevel;                           // current active level

    public static void start(Activity activity, int difficultyIndex) {
        Intent intent = new Intent(activity, CategoryActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY_INDEX, difficultyIndex);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // obtain activity parameters
        Intent intent = getIntent();
        sDifficultyIndex = intent.getIntExtra(EXTRA_DIFFICULTY_INDEX, -1);
        if(sDifficultyIndex < 0) {
            App.logError("CategoryActivity started without parameter", null);
            finish();
            return;
        }

        if(sAnsweredCategories == null) {
            sAnsweredCategories = new ArrayList<>();
        }
        else {
            sAnsweredCategories.clear();
        }

        sLevel = 1;
        mReturnCategoryId = -1;
        mQuestionCategory = new QuestionCategory(this, false);

        setContentView(R.layout.activity_category);
        ImageView background = (ImageView)findViewById(R.id.category_background);
        mCategoryLevelStatus = (TextView)findViewById(R.id.category_level_status);
        mCategoryList = (RecyclerView)findViewById(R.id.category_list);
        mLoadingNotifier = (ImageView)findViewById(R.id.category_loader);

        mCategoryLevelStatus.setTypeface(App.getBoldTypeface());

        Glide.with(this).load(R.drawable.main_background).crossFade().into(background);
        Glide.with(this).load(R.drawable.loader).asGif().into(mLoadingNotifier);

        mCategoryList.setLayoutManager(new GridLayoutManager(
                this, 2, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // get category data here
        mQuestionCategory.all(new IDAOMultiDataListener<QuestionCategoryEntity>() {
            @Override
            public void onSuccess(List<QuestionCategoryEntity> questionCategoryEntities) {
                mCategories = questionCategoryEntities;

                // when returning from question answer area
                if(mShouldCheckForCheckmarks) {
                    mShouldCheckForCheckmarks = false;
                    addCheckmarkToCategories();
                }

                mAdapter = new CategoryAdapter(
                        CategoryActivity.this, mCategories, CategoryActivity.this);
                mCategoryList.setHasFixedSize(true);
                mCategoryList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                String difficulty = getString(Constants.DIFFICULTIES_STRING_RES[sDifficultyIndex]);
                String levelStatus = getString(R.string.category_level_status, difficulty, sLevel);
                mCategoryLevelStatus.setText(levelStatus);

                mCategoryLevelStatus.setVisibility(View.VISIBLE);
                mCategoryList.setVisibility(View.VISIBLE);
                mLoadingNotifier.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(Exception e) {
                GKHelper.showToastLong(CategoryActivity.this, R.string.error_other);
                App.logError("Failed to fetch QuestionCategory", e);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.QUESTION_REQUEST_CODE &&
                resultCode == Constants.QUESTION_RESULT_CODE && data != null) {
            int categoryId = data.getIntExtra(Constants.QUESTION_RESPONSE_CATEGORY_ID, -1);
            sAnsweredCategories.add(categoryId);
            mShouldCheckForCheckmarks = true;
            sLevel++;
        }
    }

    @Override
    public void onItemClick(int position) {
        QuestionCategoryEntity category = mCategories.get(position);
        if(category.mark) {
            GKHelper.showToastShort(this, R.string.category_already_answered);
        }
        else {
            QuestionActivity.start(this, sDifficultyIndex, category.getName(),
                    category.getId(), Constants.QUESTION_REQUEST_CODE);
        }
    }

    private QuestionCategoryEntity findCategoryFromMemory(int id) {
        for(QuestionCategoryEntity category : mCategories) {
            if(category.getId() == id) return category;
        }

        return null;
    }

    private void addCheckmarkToCategories() {
        for(int categoryId : sAnsweredCategories) {
            QuestionCategoryEntity category = findCategoryFromMemory(categoryId);
            if(category != null) category.mark = true;
        }
    }
}
