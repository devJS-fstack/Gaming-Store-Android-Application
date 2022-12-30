package entities;

public class ImageDetailProduct {
    private String id;
    private String nameFile;
    private String defType;
    private String idProduct;

    public ImageDetailProduct(){}

    public ImageDetailProduct(String id, String nameFile, String defType, String idProduct) {
        this.id = id;
        this.nameFile = nameFile;
        this.defType = defType;
        this.idProduct = idProduct;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }
}
