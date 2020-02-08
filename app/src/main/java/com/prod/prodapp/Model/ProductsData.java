package com.prod.prodapp.Model;

public class ProductsData {
    private String name = "";
    private double price = 0;
    private double kilbkistb = 0;
    private String unit = "";
    private String kod = "";
    private String EdYch = "";

    private String dataStart = "";
    private String dataFinish = "";
    private String dataTrival = "";


    private String addphoto = "false";



    private String numberphoto = "1";

    public ProductsData()
    {

    }

    public ProductsData(ProductsData data)
    {
        this.name = data.getName();
        this.price = data.getPrice();
        this.kilbkistb = data.getKilbkistb();
        this.unit = data.getUnit();
        this.kod = data.getKod();
        this.addphoto = data.getAddphoto();
        this.numberphoto = data.getNumberphoto();
    }

    public ProductsData(String name)
    {
        this.name = name;
    }

    public ProductsData(String name, Double price)
    {
        this.name = name;
        this.price = price;
    }

    public String getNumberphoto() {
        return numberphoto;
    }

    public void setNumberphoto(String numberphoto) {
        this.numberphoto = numberphoto;
    }

    public String getAddphoto() {
        return addphoto;
    }

    public void setAddphoto(String addphoto) {
        this.addphoto = addphoto;
    }


    public String getDataTrival() { return dataTrival; }

    public void setDataTrival(String dataTrival) { this.dataTrival = dataTrival; }

    public String getDataFinish() { return dataFinish; }

    public void setDataFinish(String dataFinish) { this.dataFinish = dataFinish; }

    public String getDataStart() { return dataStart; }

    public void setDataStart(String dataStart) { this.dataStart = dataStart; }

    public String getInfoName()
    {
        return (kod + " " + name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }

    public double getKilbkistb() { return kilbkistb; }

    public void setKilbkistb(double kilbkistb) { this.kilbkistb = kilbkistb; }

    public String getUnit() {
        return unit;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {this.kod = kod;}

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEdYch() {
        return EdYch;
    }

    public void setEdYch(String edYch) {
        EdYch = edYch;
    }

    public static class Builder {

        private ProductsData productsData;

        public Builder() {
            productsData = new ProductsData();
        }

        public Builder setName(String name) {
            productsData.name = name;
            return this;
        }

        public Builder setPrice(Double price) {
            productsData.price = price;
            return this;
        }

        public Builder setKilbkistb(double kilbkistb) {
            productsData.kilbkistb = kilbkistb;
            return this;
        }

        public Builder setUnit(String unit) {
            productsData.unit = unit;
            return this;
        }

        public Builder setEduch(String EdYch) {
            productsData.EdYch = EdYch;
            return this;
        }

        public Builder setKod(String kod) {
            productsData.kod = kod;
            return this;
        }

        public Builder setDateStart(String start)
        {
            productsData.dataStart = start;
            return this;
        }

        public Builder setDateFinish(String finish)
        {
            productsData.dataFinish = finish;
            return this;
        }

        public Builder setDateTriv(String triv)
        {
            productsData.dataTrival = triv;
            return this;
        }

        public Builder setAddPhoto(String photo)
        {
            productsData.addphoto = photo;
            return this;
        }

        public Builder setNumberOfphoto(String photo)
        {
            productsData.numberphoto = photo;
            return this;
        }

        public ProductsData build() {
            return productsData;
        }

    }
}
