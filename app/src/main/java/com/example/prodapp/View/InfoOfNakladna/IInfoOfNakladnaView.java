package com.example.prodapp.View.InfoOfNakladna;

import com.example.prodapp.Model.InfoOfNakladna.InfoOfNakladna;

public interface IInfoOfNakladnaView {

    boolean pressSaveInfo(InfoOfNakladna infoOfNakladna);

    void pressChooseDogovir();

    void pressTypeNumber();

    void pressDate();

    void pressMarkaAvto();

    void pressNomerZnak();

    void pressNameDriver();

    void errorMessage(String string);

}
