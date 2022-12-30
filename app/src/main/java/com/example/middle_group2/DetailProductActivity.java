package com.example.middle_group2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.ColorAdapter;
import com.example.adapter.ImageDetailAdapter;
import com.example.adapter.ProductAdapter;
import com.example.adapter.ReviewImageAdapter;
import com.example.implement.CartOrderImp;
import com.example.implement.DetailImageProductImp;
import com.example.implement.ImageReviewDetailImp;
import com.example.implement.ProductImp;
import com.example.implement.PurchaseOrderImp;
import com.example.implement.ReviewProductImp;
import com.example.utils.Constants;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import entities.CartOrder;
import entities.ImageDetailProduct;
import entities.ImageReviewDetail;
import entities.Product;
import entities.PurchaseOrder;
import entities.ReviewProduct;
import me.relex.circleindicator.CircleIndicator;

public class DetailProductActivity extends AppCompatActivity {
    private TextView tvDescProduct, tvNameUserReview, contentReview, tvSeeMore;
    private RatingBar ratingReview, totalRating;
    private TextView nameDetailProduct, priceDetail, titleDetailProduct, numberTotalRating, totalReview;
    private Button btnAddToCard;
    private RecyclerView listColorProduct, listImageReview, listRelateProduct;
    private ColorAdapter colorAdapter;
    private ArrayList<Integer> listColor = new ArrayList<>();
    private ImageView ivUserReviewAvatar;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ImageButton addReview;

    // adapter
    private ImageDetailAdapter imageDetailAdapter;
    private ReviewImageAdapter reviewImageAdapter;

    // implement
    private DetailImageProductImp detailImageProductImp = new DetailImageProductImp();
    private ReviewProductImp reviewProductImp = new ReviewProductImp();
    private ImageReviewDetailImp imageReviewDetailImp = new ImageReviewDetailImp();
    private ProductImp productImp = new ProductImp();
    private PurchaseOrderImp purchaseOrderImp = new PurchaseOrderImp();
    private CartOrderImp cartOrderImp = new CartOrderImp();
    ArrayList<ImageDetailProduct> imageDetailProducts = new ArrayList<>();
    ArrayList<HashMap<String, Object>> listReviewProduct = new ArrayList<>();
    Product product;
    CardView cvAvatarReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_product);
        product = (Product) getIntent().getExtras().getSerializable("product");
        imageDetailProducts = detailImageProductImp.findAll("WHERE id_product = '" + product.getIdProduct() + "'");
        setControl();
        initial();
    }

    private void initial() {
        initialDetailProduct();
        initialReviewProduct();
        initialColor();
        initialViewPager();
        initialRelateProduct();
        handleAddToCard();
        onClickSeeMore();
        onClickAddReview();
    }

    void onClickAddReview() {
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProductActivity.this, WriteReviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idProduct", product.getIdProduct());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initialDetailProduct() {
        String descProduct = product.getDesc() != null ? product.getDesc() : "This product doesn't have any description yet";
        titleDetailProduct.setText(product.getNameProduct());
        nameDetailProduct.setText(product.getNameProduct());
        tvDescProduct.setText(descProduct);
        priceDetail.setText(product.getCurrency() + String.valueOf(product.getOldPrice()));
    }

    private void initialReviewProduct() {
        String queryListReview = "JOIN users ON product_reviews.id_user = users.id_user and id_product = '" + product.getIdProduct() + "'";
        listReviewProduct = reviewProductImp.findManyType(queryListReview);
        if(listReviewProduct.size() > 0) {
            HashMap<String, Object> map = listReviewProduct.get(0);
            ReviewProduct reviewProduct = (ReviewProduct) map.get("review");
            int resId = this.getResources().getIdentifier(map.get("nameFile").toString(), map.get("defType").toString(), this.getPackageName());
            tvNameUserReview.setText(map.get("firstName").toString() + " " + map.get("lastName").toString());
            ratingReview.setRating(reviewProduct.getStarNumber());
            contentReview.setText(reviewProduct.getDescription());
            ivUserReviewAvatar.setImageResource(resId);
            float totalRatingNumber = totalStarRating(listReviewProduct);
            totalRating.setRating(totalRatingNumber);
            numberTotalRating.setText(String.valueOf(totalRatingNumber));
            String reviewNumber = listReviewProduct.size() == 1 ? "(1 Review)" : ("(" + String.valueOf(listReviewProduct.size()) + " Reviews)");
            totalReview.setText(reviewNumber);
            ArrayList<ImageReviewDetail> listImgReviewDetail = imageReviewDetailImp.findAll("WHERE id_review_product = '" + reviewProduct.getIdReviewProduct() + "'");
            if(listImgReviewDetail.size() > 0) {
                reviewImageAdapter = new ReviewImageAdapter(this, listImgReviewDetail);
                listImageReview.setAdapter(reviewImageAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                listImageReview.setLayoutManager(linearLayoutManager);
            }
        } else {
            ratingReview.setVisibility(View.INVISIBLE);
            cvAvatarReview.setVisibility(View.INVISIBLE);
            totalRating.setVisibility(View.INVISIBLE);
            numberTotalRating.setVisibility(View.INVISIBLE);
            totalReview.setVisibility(View.INVISIBLE);
            contentReview.setText("This product doesn't have any reviews yet");
        }
    }

    private float totalStarRating(ArrayList<HashMap<String, Object>> listReviewProduct) {
        int total = 0;
        for (int i = 0 ; i < listReviewProduct.size(); i++) {
            HashMap<String, Object> map = listReviewProduct.get(i);
            ReviewProduct reviewProduct = (ReviewProduct) map.get("review");
            total+=reviewProduct.getStarNumber();
        }

        return total/listReviewProduct.size();
    }

    private void initialRelateProduct() {
        String query = "WHERE id_category = '" + product.getIdCategory() + "' and not id_product = '" + product.getIdProduct() + "' ORDER BY id_product OFFSET 0 ROWS FETCH NEXT 5 ROWS ONLY";
        ArrayList<Product> productsRelate = productImp.findAll(query);
        ProductAdapter productAdapter = new ProductAdapter(this);
        productAdapter.setData(productsRelate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        listRelateProduct.setAdapter(productAdapter);
        listRelateProduct.setLayoutManager(linearLayoutManager);
    }

    private void initialViewPager() {
        if(imageDetailProducts.size() == 0) {
            imageDetailProducts.add(new ImageDetailProduct("", product.getNameFileMain(), product.getDefType(), ""));
        }

        imageDetailAdapter = new ImageDetailAdapter(this, imageDetailProducts, null);
        viewPager.setAdapter(imageDetailAdapter);
        circleIndicator.setViewPager(viewPager);
        imageDetailAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    private void initialColor(){
        listColor.add(R.drawable.blue);
        listColor.add(R.drawable.dark);
        listColor.add(R.drawable.green);
        listColor.add(R.drawable.purple);
        listColor.add(R.drawable.red);
        listColor.add(R.drawable.yellow);
        colorAdapter = new ColorAdapter(this, listColor);
        colorAdapter.colorSelect = 0;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        listColorProduct.setAdapter(colorAdapter);
        listColorProduct.setLayoutManager(linearLayoutManager);
    }

    private void handleAddToCard() {
        btnAddToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isSave = colorAdapter.colorSelect != 0 ? cartOrderImp.save(prepareDataForAddToCard()) : false;
                String msg = colorAdapter.colorSelect != 0 ? "" : "Please select color first";
                if(isSave) {
                    MainActivity.badgeDrawable.setNumber(MainActivity.badgeDrawable.getNumber() + 1);
                    MainActivity.badgeDrawable.setVisible(true);
                    Toast.makeText(DetailProductActivity.this, "Add to your card successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailProductActivity.this, msg.equals("") ? "Add to your card failed. Please try again !" : msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private CartOrder prepareDataForAddToCard() {
        String idCart = generateId();
        String idUser = LoginActivity.idUserLogin;
        String idProduct = product.getIdProduct();
        return new CartOrder(idCart, idUser, idProduct, 1, "", Constants.STATUS_ADD_CART );
    }

    private String generateRandomString() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        alphabet = alphabet.toLowerCase(Locale.ROOT);
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 6;
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    private String generateId() {
        String id;
        Boolean isExistID = false;
        do {
            id = generateRandomString();
            isExistID = cartOrderImp.isExistId(id, "id_cart_order");
        } while(isExistID);

        return id;
    }

    private void onClickSeeMore() {
        tvSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProductActivity.this, MoreReview.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listReviewProduct", listReviewProduct);
                bundle.putSerializable("idProduct", product.getIdProduct());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        listColorProduct = findViewById(R.id.listColorProduct);
        viewPager = findViewById(R.id.viewPagerProduct);
        circleIndicator = findViewById(R.id.indicatorProduct);
        tvDescProduct = findViewById(R.id.tvDescProduct);
        nameDetailProduct = findViewById(R.id.nameDetailProduct);
        priceDetail = findViewById(R.id.priceDetail);
        titleDetailProduct = findViewById(R.id.titleDetailProduct);
        tvNameUserReview = findViewById(R.id.tvNameUserReview);
        ratingReview = findViewById(R.id.ratingReview);
        contentReview = findViewById(R.id.contentReview);
        ivUserReviewAvatar = findViewById(R.id.ivUserReviewAvatar);
        listImageReview = findViewById(R.id.listImageReview);
        listRelateProduct = findViewById(R.id.listRelateProduct);
        cvAvatarReview = findViewById(R.id.cvAvatarReview);
        tvSeeMore = findViewById(R.id.tvSeeMore);
        totalRating = findViewById(R.id.totalRating);
        numberTotalRating = findViewById(R.id.numberTotalRating);
        totalReview = findViewById(R.id.totalReview);
        btnAddToCard = findViewById(R.id.addToCard);
        addReview = findViewById(R.id.addReview);
    }
}