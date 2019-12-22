package com.example.prodapp.Presenter.InfoOfNakladna;

import com.example.prodapp.Model.InfoOfNakladna.InfoOfNakladna;

public interface IInfoOfNakladnaPresenter {

    boolean onSaveInfo();

    void onChooseDogovir();

    void onTypeNumber();

    void onDate();

    void onMarkaAvto();

    void onNomerZnak();

    void onNameDriver();

    InfoOfNakladna getInfoOfNakladna();

    public void setInfoOfNakladna(InfoOfNakladna infoOfNakladna);

}
