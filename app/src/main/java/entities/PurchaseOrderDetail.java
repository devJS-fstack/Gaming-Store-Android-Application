package entities;

import java.io.Serializable;

public class PurchaseOrderDetail implements Serializable {
    private String poNumberDetail;
    private int poQuantity;
    private int totalPrice;
    private String idProduct;
    private String poNumber;

    public PurchaseOrderDetail(String poNumberDetail, int poQuantity, int totalPrice, String idProduct, String poNumber) {
        this.poNumberDetail = poNumberDetail;
        this.poQuantity = poQuantity;
        this.totalPrice = totalPrice;
        this.idProduct = idProduct;
        this.poNumber = poNumber;
    }

    public String getPoNumberDetail() {
        return poNumberDetail;
    }

    public void setPoNumberDetail(String poNumberDetail) {
        this.poNumberDetail = poNumberDetail;
    }

    public int getPoQuantity() {
        return poQuantity;
    }

    public void setPoQuantity(int poQuantity) {
        this.poQuantity = poQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }
}
