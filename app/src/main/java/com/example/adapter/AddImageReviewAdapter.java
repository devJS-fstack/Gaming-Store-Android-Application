package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.middle_group2.R;
import com.example.middle_group2.WriteReviewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddImageReviewAdapter extends RecyclerView.Adapter<AddImageReviewAdapter.AddImageReviewAdapterViewHolder> {

    private Context context;
    private ArrayList<String> listImg;

    public AddImageReviewAdapter(Context context, ArrayList<String> listImg) {
        this.context = context;
        this.listImg = listImg;
    }

    @NonNull
    @Override
    public AddImageReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review_image, parent, false);

        return new AddImageReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddImageReviewAdapterViewHolder holder, int position) {
        String uri = listImg.get(position);
        int i = position;
        Picasso.get().load(uri).into(holder.ivReview);

        holder.ivReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listImg.remove(i);
                WriteReviewActivity.listUri.remove(i);
                notifyItemRemoved(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImg.size();
    }

    public class AddImageReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivReview;
        public AddImageReviewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ivReview = itemView.findViewById(R.id.ivReview);
        }
    }
}
