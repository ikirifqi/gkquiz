package com.nextinnovation.gkquiz.application.dao;

import android.app.ProgressDialog;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.nextinnovation.gkquiz.application.App;
import com.nextinnovation.gkquiz.application.Config;
import com.nextinnovation.gkquiz.application.GKHelper;
import com.nextinnovation.gkquiz.entities.QuestionCategoryEntity;
import com.nextinnovation.gkquiz.interfaces.IDAOMultiDataListener;
import com.nextinnovation.gkquiz.interfaces.IDAOSingleDataListener;
import com.nextinnovation.gkquiz.interfaces.IDefaultStatusListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rifqi on Feb 15, 2016.
 */
public class QuestionCategory {
    private Context context;
    private Dao<QuestionCategoryEntity, ?> dao;
    private ProgressDialog progress;
    private boolean needToShowProgress;

    /** NOTE: context is required only if needToShowProgress set to true */
    public QuestionCategory(Context context, boolean needToShowProgress) {
        this.needToShowProgress = needToShowProgress;

        if(needToShowProgress) {
            this.context = context;
            this.progress = new ProgressDialog(context);
        }

        try {
            this.dao = App.getDatabase().getDao(QuestionCategoryEntity.class);
        }
        catch(SQLException e) {
            App.logError("Cannot initialize DAO", e);
        }
    }

    public void all(final IDAOMultiDataListener<QuestionCategoryEntity> listener) {
        toggleProgressDialog(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<QuestionCategoryEntity> result;

                try {
                    result = dao.queryForAll();
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

                if(listener != null) {
                    App.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(result);
                            toggleProgressDialog(false);
                        }
                    });
                }
            }
        }).start();
    }

    public void find(final int id, final IDAOSingleDataListener<QuestionCategoryEntity> listener) {
        toggleProgressDialog(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean hasSuccess;
                Exception exception;
                QuestionCategoryEntity result;

                try {
                    result = dao.queryBuilder().where().eq("id", id).queryForFirst();
                    hasSuccess = true;
                    exception = null;
                }
                catch(SQLException e) {
                    App.logError("Cannot retrieve data from database", e);
                    result = null;
                    hasSuccess = false;
                    exception = e;
                }

                // call the listener
                if(hasSuccess && listener != null) {
                    final QuestionCategoryEntity finalResult = result;
                    App.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(finalResult);
                            toggleProgressDialog(false);
                        }
                    });
                }
                else if(!hasSuccess && listener != null) {
                    final Exception finalException = exception;
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

    /** find using synchronize method (in current thread) */
    public QuestionCategoryEntity _find(int id) {
        QuestionCategoryEntity result;

        try {
            result = dao.queryBuilder().where().eq("id", id).queryForFirst();
        }
        catch(SQLException e) {
            App.logError("Cannot retrieve data from database", e);
            return null;
        }

        return result;
    }

    public void seed(final IDefaultStatusListener listener) {
        toggleProgressDialog(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean hasSuccess;
                Exception returnException;
                String json = GKHelper.loadJSONFromAsset(Config.STATIC_CATEGORY_LIST);
                ArrayList<QuestionCategoryEntity> categories = new ArrayList<>();

                // parsing categories JSON object
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("categories");

                    int jsonArrayLength = jsonArray.length();
                    for(int i = 0; i < jsonArrayLength; i++) {
                        QuestionCategoryEntity categoryEntity = new QuestionCategoryEntity();
                        categoryEntity.setName(jsonArray.getString(i));
                        categories.add(categoryEntity);
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
                        dao.create(categories);
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

    public long count() {
        try {
            return dao.countOf();
        }
        catch(SQLException e) {
            App.logError("Failed to retrieve data count", e);
        }

        return -1;
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
