package entities;

import java.io.Serializable;

public class ReviewProduct implements Serializable {
    private String idReviewProduct, idUser, description, idProduct, date;
    private int starNumber;

    public ReviewProduct(String idReviewProduct, String idUser, String description, int starNumber, String idProduct, String date) {
        this.idReviewProduct = idReviewProduct;
        this.idUser = idUser;
        this.description = description;
        this.idProduct = idProduct;
        this.starNumber = starNumber;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdReviewProduct() {
        return idReviewProduct;
    }

    public void setIdReviewProduct(String idReviewProduct) {
        this.idReviewProduct = idReviewProduct;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int getStarNumber() {
        return starNumber;
    }

    public void setStarNumber(int starNumber) {
        this.starNumber = starNumber;
    }
}
