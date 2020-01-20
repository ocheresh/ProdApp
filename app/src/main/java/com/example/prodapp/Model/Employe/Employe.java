package com.example.prodapp.Model.Employe;

public class Employe {

    private String FirstName = "";
    private String LastName = "";
    private String ContactNo = "";
    private String Email = "";
    private String MilitaryRank = "";
    private String MilitaryUnit = "";
    private String UnitofMilitaryUnit = "";
    private String Adress = "";
    private String EmailToSend = "";


    public Employe() { }

    public String get_info()
    {
        String info = "";

        info += "First name: " + FirstName + "\n";
        info += "LastName: " + LastName + "\n";
        info += "ContactNo: " + ContactNo + "\n";
        info += "Email: " + Email + "\n";
        info += "MilitaryRank: " + MilitaryRank + "\n";
        info += "MilitaryUnit: " + MilitaryUnit + "\n";
        info += "UnitofMilitaryUnit: " + UnitofMilitaryUnit + "\n";
        info += "Adress: " + Adress + "\n";
        info += "EmailToSend: " + EmailToSend + "\n";

        return (info);
    }

    public boolean check_class()
    {
        if (FirstName.length() == 0)
            return false;
        if (LastName.length() == 0)
            return false;
        if (ContactNo.length() == 0)
            return false;
        if (Email.length() == 0)
            return false;
        if (MilitaryRank.length() == 0)
            return false;
        if (MilitaryUnit.length() == 0)
            return false;
        if (UnitofMilitaryUnit.length() == 0)
            return false;
        if (Adress.length() == 0)
            return false;
        if (EmailToSend.length() == 0)
            return false;
        return true;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMilitaryRank() {
        return MilitaryRank;
    }

    public void setMilitaryRank(String militaryRank) {
        MilitaryRank = militaryRank;
    }

    public String getMilitaryUnit() {
        return MilitaryUnit;
    }

    public void setMilitaryUnit(String militaryUnit) {
        MilitaryUnit = militaryUnit;
    }

    public String getUnitofMilitaryUnit() {
        return UnitofMilitaryUnit;
    }

    public void setUnitofMilitaryUnit(String unitofMilitaryUnit) {
        UnitofMilitaryUnit = unitofMilitaryUnit;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public String getEmailToSend() {
        return EmailToSend;
    }

    public void setEmailToSend(String emailToSend) {
        EmailToSend = emailToSend;
    }
}
