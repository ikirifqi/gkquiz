package com.nextinnovation.gkquiz.application.dao;

import android.app.ProgressDialog;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.nextinnovation.gkquiz.application.App;
import com.nextinnovation.gkquiz.application.Config;
import com.nextinnovation.gkquiz.application.Constants;
import com.nextinnovation.gkquiz.application.GKHelper;
import com.nextinnovation.gkquiz.entities.QuestionEntity;
import com.nextinnovation.gkquiz.interfaces.IDAOSingleDataListener;
import com.nextinnovation.gkquiz.interfaces.IDefaultStatusListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rifqi on Mar 06, 2016.
 */
public class Question {
    private Context context;
    private Dao<QuestionEntity, ?> dao;
    private ProgressDialog progress;
    private boolean needToShowProgress;
    private QuestionCategory category;

    /** NOTE: context is required only if needToShowProgress set to true */
    public Question(Context context, boolean needToShowProgress) {
        this.category = new QuestionCategory(context, needToShowProgress);
        this.needToShowProgress = needToShowProgress;

        if(needToShowProgress) {
            this.context = context;
            this.progress = new ProgressDialog(context);
        }

        try {
            this.dao = App.getDatabase().getDao(QuestionEntity.class);
        }
        catch(SQLException e) {
            App.logError("Cannot initialize DAO", e);
        }
    }

    public void getRandomQuestion(final int questionCategoryId, final int difficulty,
                                  final IDAOSingleDataListener<QuestionEntity> listener) {
        toggleProgressDialog(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<QuestionEntity> questions;

                QueryBuilder<QuestionEntity, ?> queryBuilder = dao.queryBuilder();
                Where<QuestionEntity, ?> where = queryBuilder.where();
                try {
                    where.eq("question_category_id", questionCategoryId);
                    where.eq("difficulty", difficulty);
                    where.eq("answered", false);
                    where.and(3);
                    questions = queryBuilder.query();
                }
                catch(final SQLException e) {
                    App.logError("Cannot retrieve data from database", e);

                    if(listener != null) {
                        App.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listener.onFailed(e);
                                toggleProgressDialog(false);
                            }
                        });
                    }

                    return;
                }

                // get random question from questions bank
                Random random = new Random();
                int index = random.nextInt(questions.size());
                final QuestionEntity question = questions.get(index);

                if(listener != null) {
                    App.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(question);
                            toggleProgressDialog(false);
                        }
                    });
                }
            }
        }).start();
    }

    public void seed(final IDefaultStatusListener listener) {
        toggleProgressDialog(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean hasSuccess;
                Exception returnException;
                String json = GKHelper.loadJSONFromAsset(Config.STATIC_QUESTION_LIST);
                ArrayList<QuestionEntity> questions = new ArrayList<>();

                // parsing categories JSON object
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("questions");

                    int jsonArrayLength = jsonArray.length();
                    for(int i = 0; i < jsonArrayLength; i++) {
                        QuestionEntity question = new QuestionEntity();
                        JSONObject jsonQuestion = jsonArray.getJSONObject(i);
                        question.setDifficulty(jsonQuestion.getInt("difficulty"));
                        question.setQuestion(jsonQuestion.getString("question"));
                        question.setAnswer(jsonQuestion.getInt("answer"));
                        question.setQuestionCategoryId(jsonQuestion.getInt("question_category_id"));

                        String options = "";
                        JSONArray jsonOptions = jsonQuestion.getJSONArray("options");
                        int jsonOptionArrayLength = jsonOptions.length();
                        for(int j = 0; j < jsonOptionArrayLength; j++) {
                            options += jsonOptions.getString(j);
                            if((j + 1) != jsonOptionArrayLength) {
                                options += Constants.OPTIONS_SEPARATOR_CODE;
                            }
                        }

                        question.setOptions(options);
                        question.setIsAnswered(false);
                        questions.add(question);
                    }

                    hasSuccess = true;
                    returnException = null;
                }
                catch(JSONException e) {
                    hasSuccess = false;
                    returnException = e;
                    App.logError("Failed to parse JSON object", e);
                }

                // insert it to database
                if(hasSuccess) {
                    try {
                        dao.create(questions);
                        hasSuccess = true;
                        returnException = null;
                    }
                    catch(SQLException e) {
                        hasSuccess = false;
                        returnException = e;
                        App.logError("Failed to seed QuestionCategory", e);
                    }
                }

                // call the listener
                if(hasSuccess && listener != null) {
                    App.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess();
                            toggleProgressDialog(false);
                        }
                    });
                }
                else if(!hasSuccess && listener != null) {
                    final Exception finalException = returnException;
                    App.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailed(finalException);
                            toggleProgressDialog(false);
                        }
                    });
                }
            }
        }).start();
    }

    /** INTERNAL HELPER: function to show or hide progress dialog */
    private void toggleProgressDialog(boolean needToShow) {
        if(needToShowProgress && context != null && needToShow && !progress.isShowing()) {
            progress.show();
        }
        else if(needToShowProgress && context != null && !needToShow && progress.isShowing()) {
            progress.dismiss();
        }
    }
}
