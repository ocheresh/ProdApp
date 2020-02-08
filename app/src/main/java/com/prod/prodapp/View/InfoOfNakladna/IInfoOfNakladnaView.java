package com.prod.prodapp.View.InfoOfNakladna;

import com.prod.prodapp.Model.InfoOfNakladna.InfoOfNakladna;

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
