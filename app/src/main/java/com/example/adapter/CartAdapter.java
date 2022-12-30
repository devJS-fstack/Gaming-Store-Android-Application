package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.implement.CartOrderImp;
import com.example.implement.UserImp;
import com.example.middle_group2.AddressActivity;
import com.example.middle_group2.MainActivity;
import com.example.middle_group2.R;

import java.util.ArrayList;
import java.util.HashMap;

import entities.CartOrder;
import entities.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, Object>> listCartOrder;
    private TextView tvTempPrice;
    private TextView tvDiscountPrice;
    private TextView tvTotalPrice, cartTotal;
    private int tempPrice = 0, discountPrice = 0;
    public static int totalPrice = 0;
    private CartOrderImp cartOrderImp;
    private Button btnNextCart;

    private ArrayList<Boolean> listToHandleChecked = new ArrayList<>();
    public static ArrayList<HashMap<String, Object>> listProductChecked = new ArrayList<>();

    public CartAdapter(Context context, ArrayList<HashMap<String, Object>> listCartOrder,
                       TextView tempPrice, TextView discountPrice,
                       TextView totalPrice, CartOrderImp cartOrderImp, TextView cartTotal,
                       Button btnNextCart) {
        this.context = context;
        this.listCartOrder = listCartOrder;
        this.tvTempPrice = tempPrice;
        this.tvDiscountPrice = discountPrice;
        this.tvTotalPrice = totalPrice;
        this.cartOrderImp = cartOrderImp;
        this.cartTotal = cartTotal;
        this.btnNextCart = btnNextCart;
        this.addToListChecked(listCartOrder.size());
    }

    @NonNull
    @Override
    public CartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        CartAdapter.CartAdapterViewHolder cartOrderHolder = new CartAdapter.CartAdapterViewHolder(view);
        return cartOrderHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapterViewHolder holder, int position) {
        int index = position;
        HashMap<String, Object> map = listCartOrder.get(position);
        CartOrder cartOrder = (CartOrder) map.get("cart");
        Product product = (Product) map.get("product");
        String currency = product.getCurrency();
        int resId = context.getResources().getIdentifier(product.getNameFileMain(), product.getDefType(), context.getPackageName());
        holder.ivCartProduct.setImageResource(resId);
        holder.tvNameProductCart.setText(product.getNameProduct());
        holder.tvOldPriceCart.setText(currency + String.valueOf(product.getOldPrice()));
        holder.tvCurrentPriceCart.setText(currency  +  String.valueOf(product.getCurrentPrice()));
        holder.quantityCartProduct.setText(String.valueOf(cartOrder.getQuantityCart()));

        if(cartOrder.getQuantityCart() == 1) {
            disableMinus(holder.minusQuantity);
        }


        // handle event
        holder.checkBoxCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBoxCart.isChecked()) {
                    tempPrice += product.getOldPrice() * cartOrder.getQuantityCart();
                    discountPrice +=  (product.getCurrentPrice() - product.getOldPrice()) * cartOrder.getQuantityCart();
                    listToHandleChecked.set(index, true);
                } else {
                    tempPrice -= product.getOldPrice() * cartOrder.getQuantityCart();
                    discountPrice -=  (product.getCurrentPrice() - product.getOldPrice()) * cartOrder.getQuantityCart();
                    listToHandleChecked.set(index, false);
                }
                changePrice(currency);

                if(totalPrice == 0) {
                    btnNextCart.setVisibility(View.INVISIBLE);
                } else {
                    btnNextCart.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.plusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartOrderImp.updateQuantityCart("+", cartOrder.getIdCartOrder());
                cartOrder.setQuantityCart(cartOrder.getQuantityCart() + 1);
                notifyDataSetChanged();
                enableMinus(holder.minusQuantity);
                if(holder.checkBoxCart.isChecked()) {
                    tempPrice += product.getOldPrice();
                    discountPrice +=  (product.getCurrentPrice() - product.getOldPrice());
                    changePrice(currency);
                }
            }
        });

        holder.minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = cartOrder.getQuantityCart() - 1;
                if(currentQuantity == 0) {
                    disableMinus(holder.minusQuantity);
                } else {
                    cartOrderImp.updateQuantityCart("-", cartOrder.getIdCartOrder());
                    cartOrder.setQuantityCart(currentQuantity);
                    notifyDataSetChanged();
                    if(holder.checkBoxCart.isChecked()) {
                        tempPrice -= product.getOldPrice();
                        discountPrice -=  (product.getCurrentPrice() - product.getOldPrice());
                        changePrice(currency);
                    }
                }
            }
        });

        holder.imgBtnRemoveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBoxCart.isChecked()) {
                    tempPrice -= product.getOldPrice() * cartOrder.getQuantityCart();
                    discountPrice -=  (product.getCurrentPrice() - product.getOldPrice()) * cartOrder.getQuantityCart();
                    changePrice(currency);
                }
                cartOrderImp.delete(cartOrder.getIdCartOrder());
                listCartOrder.remove(index);
                notifyItemRemoved(index);
                notifyItemRangeRemoved(index, listCartOrder.size());
                cartTotal.setText("Cart (" + listCartOrder.size() + ")");
                if(listCartOrder.size() == 0) {
                    MainActivity.badgeDrawable.setVisible(false);
                }
                MainActivity.badgeDrawable.setNumber(listCartOrder.size());
            }
        });

        handleOnClickNext();



    }

    private void disableMinus(ImageButton minus) {
        minus.setBackgroundColor(Color.parseColor("#EBEBE4"));
    }

    private void enableMinus(ImageButton minus) {
        minus.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    private void changePrice(String currency) {
        totalPrice = tempPrice + discountPrice;
        tvTempPrice.setText(currency + String.valueOf(tempPrice));
        tvDiscountPrice.setText(currency + String.valueOf(discountPrice));
        tvTotalPrice.setText(currency + String.valueOf(totalPrice));
    }

    private void handleOnClickNext() {
        btnNextCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddressActivity.class);
                AddressItemAdapter.isOrder = true;
                addToListProductChecked();
                context.startActivity(intent);
            }
        });
    }

    private void addToListChecked(int size) {
        for (int i = 0; i < size; i++) {
            listToHandleChecked.add(false);
        }
    }

    private void addToListProductChecked() {
        listProductChecked.clear();
        for (int i = 0; i < this.listCartOrder.size(); i++ ) {
            if(listToHandleChecked.get(i)) {
                Product product = (Product) listCartOrder.get(i).get("product");
                CartOrder cartOrder = (CartOrder) listCartOrder.get(i).get("cart");
                HashMap<String, Object> map = new HashMap<>();
                map.put("product", product);
                map.put("quantity", String.valueOf(cartOrder.getQuantityCart()));
                map.put("idCartOrder", String.valueOf(cartOrder.getIdCartOrder()));
                listProductChecked.add(map);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listCartOrder == null ? 0 : listCartOrder.size();
    }

    public class CartAdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCartProduct;
        private TextView tvNameProductCart,
                tvOldPriceCart, tvCurrentPriceCart,
                quantityCartProduct;
        private CheckBox checkBoxCart;
        private ImageButton minusQuantity, plusQuantity, imgBtnRemoveCart;
        public CartAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCartProduct = itemView.findViewById(R.id.ivCartProduct);
            tvNameProductCart = itemView.findViewById(R.id.tvNameProductCart);
            tvOldPriceCart = itemView.findViewById(R.id.tvOldPriceCart);
            tvCurrentPriceCart = itemView.findViewById(R.id.tvCurrentPriceCart);
            quantityCartProduct = itemView.findViewById(R.id.quantityCartProduct);
            checkBoxCart = itemView.findViewById(R.id.checkBoxCart);
            minusQuantity = itemView.findViewById(R.id.minusQuantity);
            plusQuantity = itemView.findViewById(R.id.plusQuantity);
            imgBtnRemoveCart = itemView.findViewById(R.id.imgBtnRemoveCart);
        }
    }
}
