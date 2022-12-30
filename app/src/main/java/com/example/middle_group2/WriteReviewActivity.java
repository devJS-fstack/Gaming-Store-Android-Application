package com.example.middle_group2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.AddImageReviewAdapter;
import com.example.implement.ImageReviewDetailImp;
import com.example.implement.ReviewProductImp;
import com.example.utils.Helper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import entities.ImageReviewDetail;
import entities.ReviewProduct;

public class WriteReviewActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    ImageButton addImage;
    RecyclerView rvImageReview;
    RatingBar ratingBar;
    TextView tvNumStar;
    Button btnAddReview;
    EditText edtReview;
    private ArrayList<String> listImg = new ArrayList<>();
    public static ArrayList<Uri> listUri = new ArrayList<>();
    private ArrayList<String> listFileName = new ArrayList<>();
    private AddImageReviewAdapter addImageReviewAdapter;
    private int star = 0;
    private ReviewProductImp reviewProductImp = new ReviewProductImp();
    private ImageReviewDetailImp imageReviewDetailImp = new ImageReviewDetailImp();
    private String idProduct = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_review);
        idProduct = (String) getIntent().getExtras().getSerializable("idProduct");
        System.out.println("id product: " + idProduct);
        setControl();
        initial();
    }

    private void initial() {
        initialAdapter();
        handleOnClickAddImage();
        handleOnChangeRating();
        handleSubmit();
    }

    private void handleSubmit() {
        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(star == 0 || edtReview.getText().toString().equals("")) {
                    Toast.makeText(WriteReviewActivity.this, "Please enter your review", Toast.LENGTH_SHORT).show();
                } else {
                    if (listImg.size() > 0) {
                        generateFileName(listImg.size());
                        uploadImgToFireBase();
                    } else {
                        insertData();
                    }
                }
            }
        });
    }

    private void uploadImgToFireBase() {
        ProgressDialog progressDialog = new ProgressDialog(WriteReviewActivity.this);
        progressDialog.show();
        progressDialog.setMessage("Processing your review. Please wait a few minutes ^^");
        for (int i = 0; i < listUri.size(); i++) {
            int position = i;
            String fileName = listFileName.get(i);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);
            storageReference.putFile(listUri.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("position: " + position);
                    System.out.println("is boolean : " + (position == listUri.size() - 1) );
                    if((position == listUri.size() - 1)) {
                        progressDialog.dismiss();
                        Toast.makeText(WriteReviewActivity.this, "Submit successfully", Toast.LENGTH_SHORT).show();
                        insertData();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Upload failed: " + e);
                    Toast.makeText(WriteReviewActivity.this, "Submit failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private synchronized void generateFileName(int size) {
        listFileName.clear();
        System.out.println("size: " + size);

        for (int i = 0 ;i<size;i++) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            listFileName.add(String.valueOf(timestamp.getTime() + i));
        }
    }

    private void handleOnChangeRating() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                star = Math.round(v);
                tvNumStar.setText(star + "/5");
            }
        });
    }

    private void initialAdapter() {
        addImageReviewAdapter = new AddImageReviewAdapter(this, listImg);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvImageReview.setAdapter(addImageReviewAdapter);
        rvImageReview.setLayoutManager(linearLayoutManager);
    }


    private void handleOnClickAddImage() {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listImg.size() >= 3 ) {
                    Toast.makeText(WriteReviewActivity.this, "Just maximum 3 images", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(WriteReviewActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(WriteReviewActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            } else {
                Toast.makeText(WriteReviewActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            try {
                InputStream input = getApplicationContext().getContentResolver().openInputStream(data.getData());
                Uri uri = data.getData();
//                getApplicationContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                if (isExistImg(uri.toString())) {
                    Toast.makeText(this, "Image already exist", Toast.LENGTH_SHORT).show();
                } else {
                    listImg.add(uri.toString());
                    listUri.add(uri);
                    addImageReviewAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Boolean isExistImg(String uri) {
        for (String imgUri : listImg) {
            if (imgUri.equals(uri)) {
                return true;
            }
        }

        return false;
    }

    private void insertData() {
        insertReview();
    }

    private void insertReview() {
        String idReview = generateIdReview();
        String idUser = LoginActivity.idUserLogin;
        String description = edtReview.getText().toString();
        ReviewProduct reviewProduct = new ReviewProduct(idReview, idUser, description, star, idProduct, "");
        if (reviewProductImp.save(reviewProduct)) {
            if (insertImgReviewProduct(idReview)) {
                Toast.makeText(this, "Post your review successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MoreReview.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("idProduct", idProduct);
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
            }
        }
    }

    private Boolean insertImgReviewProduct(String idReview) {
        ArrayList<ImageReviewDetail> listImgReview = new ArrayList<>();
        for (String fileName : listFileName) {
            String id = generateIdImgReview();
            ImageReviewDetail imgReview = new ImageReviewDetail(id, idReview, fileName, "firebase");
            listImgReview.add(imgReview);
        }
        if (listImgReview.size() == 0) return true;
        return imageReviewDetailImp.insertMany(listImgReview);
    }

    private String generateIdReview() {
        String id;
        Boolean isExistID = false;
        do {
            id = new Helper().generateRandomString();
            isExistID = reviewProductImp.isExistId(id, "id_review_product");
        } while(isExistID);

        return id;
    }

    private String generateIdImgReview() {
        String id;
        Boolean isExistID = false;
        do {
            id = new Helper().generateRandomString();
            isExistID = imageReviewDetailImp.isExistId(id, "id");
        } while(isExistID);

        return id;
    }

    private void setControl() {
        addImage = findViewById(R.id.addImage);
        rvImageReview = findViewById(R.id.rvImageReview);
        ratingBar = findViewById(R.id.ratingBar);
        tvNumStar = findViewById(R.id.tvNumStar);
        btnAddReview = findViewById(R.id.btnAddReview);
        edtReview = findViewById(R.id.edtReview);
    }
}