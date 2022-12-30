package entities;

public class Address {
    private String idAddress, idUser, phone, city, district, ward, street, fullName;
    private int idCity, idDistrict, idWard;
    private Boolean isDefault;

    public Address(String idAddress, String idUser, String phone,
                   int idCity, String city,
                   int idDistrict, String district,
                   int idWard, String ward,
                   Boolean isDefault, String street, String fullName) {
        this.idAddress = idAddress;
        this.idUser = idUser;
        this.phone = phone;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.idCity = idCity;
        this.idDistrict = idDistrict;
        this.idWard = idWard;
        this.isDefault = isDefault;
        this.street = street;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }

    public int getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(int idDistrict) {
        this.idDistrict = idDistrict;
    }

    public int getIdWard() {
        return idWard;
    }

    public void setIdWard(int idWard) {
        this.idWard = idWard;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
