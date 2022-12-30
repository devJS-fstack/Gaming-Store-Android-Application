package entities;

public class CartOrder {
    private String idCartOrder;
    private String idUser;
    private String idProduct, dateOrder;
    private int quantityCart;
    private String status;

    public CartOrder(String idCartOrder, String idUser, String idProduct, int quantityCart, String dateOrder, String status) {
        this.idCartOrder = idCartOrder;
        this.idUser = idUser;
        this.idProduct = idProduct;
        this.quantityCart = quantityCart;
        this.dateOrder = dateOrder;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getIdCartOrder() {
        return idCartOrder;
    }

    public void setIdCartOrder(String idCartOrder) {
        this.idCartOrder = idCartOrder;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantityCart() {
        return quantityCart;
    }

    public void setQuantityCart(int quantityCart) {
        this.quantityCart = quantityCart;
    }
}
