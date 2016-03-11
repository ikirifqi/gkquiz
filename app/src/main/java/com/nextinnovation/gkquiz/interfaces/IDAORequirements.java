package com.nextinnovation.gkquiz.interfaces;

import java.util.ArrayList;

/**
 * Created by rifqi on Feb 15, 2016.
 */
public interface IDAORequirements<TEntity> {
    /** these functions must be implemented in order to organized structure */
    void insert(final TEntity entity, final IDAOSingleDataListener<TEntity> listener);
    void insert(final ArrayList<TEntity> entities, final IDAOMultiDataListener<TEntity> listener);
    void all(final IDAOMultiDataListener<TEntity> listener);
    void find(final int id, final IDAOSingleDataListener listener);
    void where(final String col, final Object val, final IDAOMultiDataListener<TEntity> listener);
    void update(final TEntity entity, final IDAOSingleDataListener<TEntity> listener);
    void destroy(final int id, final IDAOSingleDataListener<TEntity> listener);
    long count();
}
