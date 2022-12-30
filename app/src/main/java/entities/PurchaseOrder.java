package entities;

import java.io.Serializable;

public class PurchaseOrder implements Serializable {
    private String idUser, status, orderDate, purchaseOrderNumber;
    private int payment;
    private String idAddress;

    public PurchaseOrder(String purchaseOrderNumber, String idUser, String status,
                         int payment, String orderDate, String idAddress) {
        this.idUser = idUser;
        this.status = status;
        this.orderDate = orderDate;
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.payment = payment;
        this.idAddress = idAddress;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }
}