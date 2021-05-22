package com.example.mauthunhat;

public class SanPham {
    private VarFinal mVarFinal = new VarFinal();
    private Integer Id;
    private String Name;
    private String Description;
    private String Image;
    private boolean Status;

    public SanPham(Integer id, String name, String description, String image, boolean status) {
        this.mVarFinal = mVarFinal;
        Id = id;
        Name = name;
        Description = description;
        Image = image;
        Status = status;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

}
