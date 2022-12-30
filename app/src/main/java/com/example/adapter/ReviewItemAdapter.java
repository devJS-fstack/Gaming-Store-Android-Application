package com.example.adapter;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.implement.ImageReviewDetailImp;
import com.example.middle_group2.MoreReview;
import com.example.middle_group2.R;

import java.util.ArrayList;
import java.util.HashMap;

import entities.ImageReviewDetail;
import entities.ReviewProduct;

public class ReviewItemAdapter extends RecyclerView.Adapter<ReviewItemAdapter.ReviewItemAdapterViewHolder> {

    private Context context;
    Boolean isSet = false;
    ArrayList<HashMap<String, Object>> listReviewProduct;
    ArrayList<HashMap<String, Object>> listReviewProductFilter = new ArrayList<>();
    ImageReviewDetailImp imageReviewDetailImp = new ImageReviewDetailImp();
    public static int one = 0, two = 0, three = 0, four = 0, five = 0;


    public ReviewItemAdapter(Context context, ArrayList<HashMap<String, Object>> listReviewProduct) {
        this.context = context;
        this.listReviewProduct = listReviewProduct;
        listReviewProductFilter.addAll(listReviewProduct);
    }

    @NonNull
    @Override
    public ReviewItemAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return new ReviewItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewItemAdapterViewHolder holder, int position) {
        HashMap<String, Object> map = listReviewProduct.get(position);
        ReviewProduct reviewProduct = (ReviewProduct) map.get("review");
        int resId = context.getResources().getIdentifier(map.get("nameFile").toString(), map.get("defType").toString(), context.getPackageName());
        holder.tvNameUserReview.setText(map.get("firstName").toString() + " " + map.get("lastName").toString());
        holder.ratingReview.setRating(reviewProduct.getStarNumber());
        holder.contentReview.setText(reviewProduct.getDescription());
        holder.ivUserReviewAvatar.setImageResource(resId);
        holder.tvDateReview.setText(reviewProduct.getDate());
        ArrayList<ImageReviewDetail> listImgReviewDetail = imageReviewDetailImp.findAll("WHERE id_review_product = '" + reviewProduct.getIdReviewProduct() + "'");
        if(listImgReviewDetail.size() > 0) {
            ReviewImageAdapter reviewImageAdapter = new ReviewImageAdapter(context, listImgReviewDetail);
            holder.listImageReview.setAdapter(reviewImageAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
            holder.listImageReview.setLayoutManager(linearLayoutManager);
        }
        switch (reviewProduct.getStarNumber()) {
            case 1:
                one++;
                break;
            case 2:
                two++;
                break;
            case 3:
                three++;
                break;
            case 4:
                four++;
                break;
            case 5:
                five++;
                break;
        }

        if(position == listReviewProductFilter.size() - 1 && !isSet) {
            MoreReview.btnOne.setText(MoreReview.btnOne.getText() + " (" + ReviewItemAdapter.one + ")");
            MoreReview.btnTwo.setText(MoreReview.btnTwo.getText() + " (" + ReviewItemAdapter.two + ")");
            MoreReview.btnThree.setText(MoreReview.btnThree.getText() + " (" + ReviewItemAdapter.three + ")");
            MoreReview.btnFour.setText(MoreReview.btnFour.getText() + " (" + ReviewItemAdapter.four + ")");
            MoreReview.btnFive.setText(MoreReview.btnFive.getText() + " (" + ReviewItemAdapter.five + ")");
            isSet = true;
        }
    }

    @Override
    public int getItemCount() {
        return listReviewProduct == null ? 0 : listReviewProduct.size();
    }

    public void filter(int star) {
        listReviewProduct.clear();
        if(star == 0) {
            listReviewProduct.addAll(listReviewProductFilter);
        } else {
            filterByStar(star);
        }

        notifyDataSetChanged();
    }

    private void filterByStar(int star) {
        for(HashMap<String, Object> map : listReviewProductFilter) {
            ReviewProduct reviewProduct = (ReviewProduct) map.get("review");
            if (reviewProduct.getStarNumber() == star) {
                listReviewProduct.add(map);
            }
        }
    }

    public class ReviewItemAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tvNameUserReview, contentReview, tvDateReview;
        ImageView ivUserReviewAvatar;
        RatingBar ratingReview;
        RecyclerView listImageReview;
        public ReviewItemAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameUserReview = itemView.findViewById(R.id.tvNameUserReview);
            ivUserReviewAvatar = itemView.findViewById(R.id.ivUserReviewAvatar);
            ratingReview = itemView.findViewById(R.id.ratingReview);
            contentReview = itemView.findViewById(R.id.contentReview);
            listImageReview = itemView.findViewById(R.id.listImageReview);
            tvDateReview = itemView.findViewById(R.id.tvDateReview);
        }
    }
}
