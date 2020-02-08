package com.prod.prodapp.Model.InfoOfNakladna;

public class InfoOfNakladna {

    private String nameDogovir = "";
    private String numberNakladna = "";
    private String dateNakladna = "";
    private String markaAvto = "";
    private String nomerAvto = "";
    private String nameDriver = "";
    private String KEKV = "";
    private String typePostach = "";
    private String nomerPlombi = "";
    private String createDate = "";


    public InfoOfNakladna() {};

    public String checkInfoClass()
    {
        String error_message = "Помилка:\n";

        if (nameDogovir.isEmpty())
            error_message += "Невірна назва договору\n";
        if (numberNakladna.isEmpty())
            error_message += "Невірний номер накладної\n";
        if (dateNakladna.isEmpty())
            error_message += "Невірна дата накладної\n";
        if (markaAvto.isEmpty())
            error_message += "Невірна марка авто\n";
        if (nomerAvto.isEmpty())
            error_message += "Невірний номер авто\n";
        if (nameDriver.isEmpty())
            error_message += "Невірні дані водія\n";
        if (nomerPlombi.isEmpty())
            error_message += "Невказано номер пломби\n";
        return (error_message);
    }

    public String getNomerPlombi() { return nomerPlombi; }

    public void setNomerPlombi(String nomerPlombi) { this.nomerPlombi = nomerPlombi; }

    public String getTypePostach() { return typePostach; }

    public void setTypePostach(String typePostach) { this.typePostach = typePostach; }

    public String getKEKV() { return KEKV; }

    public void setKEKV(String KEKV) { this.KEKV = KEKV; }

    public String getNameDriver() {
        return nameDriver;
    }

    public void setNameDriver(String nameDriver) {
        this.nameDriver = nameDriver;
    }

    public String getNameDogovir() {
        return nameDogovir;
    }

    public void setNameDogovir(String nameDogovir) {
        this.nameDogovir = nameDogovir;
    }

    public String getNumberNakladna() {
        return numberNakladna;
    }

    public void setNumberNakladna(String numberNakladna) {
        this.numberNakladna = numberNakladna;
    }

    public String getDateNakladna() {
        return dateNakladna;
    }

    public void setDateNakladna(String dateNakladna) {
        this.dateNakladna = dateNakladna;
    }

    public String getMarkaAvto() {
        return markaAvto;
    }

    public void setMarkaAvto(String markaAvto) {
        this.markaAvto = markaAvto;
    }

    public String getNomerAvto() {
        return nomerAvto;
    }

    public void setNomerAvto(String nomerAvto) {
        this.nomerAvto = nomerAvto;
    }

    public String getCreateDate() { return createDate; }

    public void setCreateDate(String createDate) { this.createDate = createDate; }
}
