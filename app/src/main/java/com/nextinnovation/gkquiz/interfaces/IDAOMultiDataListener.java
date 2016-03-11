package com.nextinnovation.gkquiz.interfaces;

import java.util.List;

/**
 * Created by rifqi on Feb 15, 2016.
 */
public interface IDAOMultiDataListener<TEntity> {
    void onSuccess(List<TEntity> entities);
    void onFailed(Exception e);
}
