package entities;

public class ImageDetailCategory extends ImageDetailProduct {

    private String status;

    public ImageDetailCategory(String id, String nameFile, String defType, String idCategory, String status) {
        super(id, nameFile, defType, idCategory);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
