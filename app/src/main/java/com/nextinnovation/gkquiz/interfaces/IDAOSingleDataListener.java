package com.nextinnovation.gkquiz.interfaces;

/**
 * Created by rifqi on Feb 15, 2016.
 */
public interface IDAOSingleDataListener<TEntity> {
    void onSuccess(TEntity entity);
    void onFailed(Exception e);
}
