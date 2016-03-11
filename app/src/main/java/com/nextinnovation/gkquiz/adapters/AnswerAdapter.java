package com.nextinnovation.gkquiz.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextinnovation.gkquiz.R;
import com.nextinnovation.gkquiz.application.App;

import java.util.List;

/**
 * Created by rifqi on Mar 06, 2016.
 */
public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
    private List<String> answers;
    private AnswerAdapterListener listener;

    public AnswerAdapter(List<String > answers, AnswerAdapterListener listener) {
        this.answers = answers;
        this.listener = listener;
    }

    @Override
    public AnswerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_answer, parent, false);
        TextView answerText = (TextView)v.findViewById(R.id.item_answer_text);

        return new ViewHolder(v, answerText);
    }

    @Override
    public void onBindViewHolder(AnswerAdapter.ViewHolder holder, final int position) {
        String option = answers.get(position);

        // set item width to match 1/2 of RecyclerView width
        holder.itemView.requestLayout();

        // setup answer font family and set the text
        holder.answerText.setTypeface(App.getRegularTypeface());
        holder.answerText.setText(option);

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
        return answers.size();
    }

    public interface AnswerAdapterListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView answerText;

        public ViewHolder(View itemView, TextView answerText) {
            super(itemView);
            this.answerText = answerText;
            this.itemView = itemView;
        }
    }
}
