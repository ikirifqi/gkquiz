package com.nextinnovation.gkquiz.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextinnovation.gkquiz.R;
import com.nextinnovation.gkquiz.application.App;
import com.nextinnovation.gkquiz.application.GKHelper;
import com.nextinnovation.gkquiz.entities.QuestionCategoryEntity;

import java.util.List;

/**
 * Created by rifqi on Mar 03, 2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private static final int ITEM_IN_A_ROW_COUNT = 3;

    private int itemWidth;
    private List<QuestionCategoryEntity> categories;
    private CategoryAdapterListener listener;

    public CategoryAdapter(Activity activity, List<QuestionCategoryEntity> categories,
                           CategoryAdapterListener listener) {
        this.categories = categories;
        this.listener = listener;

        int deviceWidth = GKHelper.getDeviceDimensions(activity).widthPixels;
        itemWidth = deviceWidth / ITEM_IN_A_ROW_COUNT;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.list_category, parent, false);
        TextView categoryTitle = (TextView)v.findViewById(R.id.item_category_title);
        View checkmark = v.findViewById(R.id.item_category_checkmark);

        return new ViewHolder(v, categoryTitle, checkmark);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, final int position) {
        QuestionCategoryEntity category = categories.get(position);

        // set item width to match 1/3 of screen width
        holder.itemView.getLayoutParams().width = itemWidth;
        holder.itemView.requestLayout();

        // setup category font family and set the text
        holder.categoryTitle.setTypeface(App.getBoldTypeface());
        holder.categoryTitle.setText(category.getName());

        // checkmark to indicate answered question
        holder.checkmark.setVisibility(category.mark ? View.VISIBLE : View.GONE);

        // listen to item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface CategoryAdapterListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView categoryTitle;
        public View checkmark;

        public ViewHolder(View itemView, TextView categoryTitle, View checkmark) {
            super(itemView);
            this.itemView = itemView;
            this.categoryTitle = categoryTitle;
            this.checkmark = checkmark;
        }
    }
}
