package com.example.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.middle_group2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import entities.ImageReviewDetail;

public class ReviewImageAdapter extends RecyclerView.Adapter<ReviewImageAdapter.ReviewImageHolder>{
    Context context;
    ArrayList<ImageReviewDetail> data;
    public ReviewImageAdapter(Context context, ArrayList<ImageReviewDetail> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ReviewImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_image, parent, false);
        ReviewImageHolder reviewImageHolder = new ReviewImageHolder(view);
        return reviewImageHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewImageHolder holder, int position) {
        ImageReviewDetail item = data.get(position);
        if (item.getDefType().equals("drawable")) {
            int resId = context.getResources().getIdentifier(item.getNameFile(), item.getDefType(), context.getPackageName());
            holder.ivReview.setImageResource(resId);
        } else if (item.getDefType().equals("uri")) {
            try {
                Uri uri = Uri.parse(item.getNameFile());
                System.out.println("uri: " + uri);
                InputStream input = context.getContentResolver().openInputStream(uri);
                Drawable drawable = Drawable.createFromStream(input, uri.toString());
                holder.ivReview.setImageDrawable(drawable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (item.getDefType().equals("firebase")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + item.getNameFile());
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    System.out.println("uri: " + uri);
                    Picasso.get().load(uri.toString()).into(holder.ivReview);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("failed while loading: " + e);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ReviewImageHolder extends RecyclerView.ViewHolder {
        private ImageView ivReview;
        public ReviewImageHolder(@NonNull View itemView) {
            super(itemView);
            ivReview = itemView.findViewById(R.id.ivReview);
        }
    }
}
