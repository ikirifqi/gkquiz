package com.nextinnovation.gkquiz.application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.nextinnovation.gkquiz.entities.QuestionCategoryEntity;
import com.nextinnovation.gkquiz.entities.QuestionEntity;

import java.sql.SQLException;

/**
 * Created by rifqi on Feb 15, 2016.
 */
public class Database extends OrmLiteSqliteOpenHelper {
    // list of tables
    private static final Class TABLES[] = {
            QuestionEntity.class,
            QuestionCategoryEntity.class
    };

    public Database(Context context) {
        super(context, Config.APP_DATABASE_NAME, null, Config.APP_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            for(Class clazz : TABLES) {
                TableUtils.createTableIfNotExists(connectionSource, clazz);
            }
        }
        catch(SQLException e) {
            App.logError("Cannot create table", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource,
                          int i, int i1) {
        try {
            for(Class clazz : TABLES) {
                TableUtils.dropTable(connectionSource, clazz, true);
            }
        }
        catch(SQLException e) {
            App.logError("Cannot drop table", e);
        }

        onCreate(sqLiteDatabase, connectionSource);
    }
}
