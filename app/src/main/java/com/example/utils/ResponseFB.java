package com.example.utils;

public class ResponseFB {
    private String last_name, first_name, id, email, name;
    private DataPicture picture;

    public ResponseFB(String last_name, String first_name, String id, String email, DataPicture picture, String name) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.id = id;
        this.email = email;
        this.picture = picture;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DataPicture getPicture() {
        return picture;
    }

    public void setPicture(DataPicture picture) {
        this.picture = picture;
    }

    public class DataPicture {
        private TypePicture data;

        public DataPicture(TypePicture data) {
            this.data = data;
        }

        public TypePicture getData() {
            return data;
        }

        public void setData(TypePicture data) {
            this.data = data;
        }
    }

    public class TypePicture {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
