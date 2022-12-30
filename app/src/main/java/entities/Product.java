package entities;

import java.io.Serializable;

public class Product implements Serializable {
    private String idProduct;
    private String nameProduct;
    private int currentPrice;
    private int oldPrice;
    private int sale;
    private String currency;
    private String nameFileMain;
    private String defType;
    private String idCategory;
    private String desc;

    public Product(String idProduct, String nameProduct, int currentPrice, int oldPrice, int sale, String currency, String nameFileMain, String defType,
                   String idCategory, String desc) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.currentPrice = currentPrice;
        this.oldPrice = oldPrice;
        this.sale = sale;
        this.currency = currency;
        this.nameFileMain = nameFileMain;
        this.defType = defType;
        this.desc = desc;
        this.idCategory = idCategory;
    }

    public Product() {
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getSale() {
        return sale;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNameFileMain() {
        return nameFileMain;
    }

    public void setNameFileMain(String nameFileMain) {
        this.nameFileMain = nameFileMain;
    }

    public String getDefType() {
        return defType;
    }

    public void setDefType(String defType) {
        this.defType = defType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
