package entities;

public class ImageReviewDetail {
    private String id, idReviewProduct, nameFile, defType;

    public ImageReviewDetail(String id, String idReviewProduct, String nameFile, String defType) {
        this.id = id;
        this.idReviewProduct = idReviewProduct;
        this.nameFile = nameFile;
        this.defType = defType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdReviewProduct() {
        return idReviewProduct;
    }

    public void setIdReviewProduct(String idReviewProduct) {
        this.idReviewProduct = idReviewProduct;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getDefType() {
        return defType;
    }

    public void setDefType(String defType) {
        this.defType = defType;
    }
}
