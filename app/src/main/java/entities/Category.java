package entities;

public class Category {
    private String idCategory;
    private String nameCategory;
    private String defType;
    private String nameFile;

    public Category() {
    }

    public Category(String idCategory, String nameCategory, String nameFile, String defType) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
        this.nameFile = nameFile;
        this.defType = defType;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getDefType() {
        return defType;
    }

    public void setDefType(String defType) {
        this.defType = defType;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
}
