package com.prod.prodapp.Presenter.InfoOfNakladna;

import com.prod.prodapp.Model.InfoOfNakladna.InfoOfNakladna;
import com.prod.prodapp.View.InfoOfNakladna.IInfoOfNakladnaView;

public class InfoOfNakladnaPresenter implements IInfoOfNakladnaPresenter {

    IInfoOfNakladnaView iInfoOfNakladnaView;

    static public InfoOfNakladna infoOfNakladna = null;

    public InfoOfNakladnaPresenter(IInfoOfNakladnaView iInfoOfNakladnaView)
    {
        this.iInfoOfNakladnaView = iInfoOfNakladnaView;
        infoOfNakladna = new InfoOfNakladna();

    }

    public void setInfoOfNakladna(InfoOfNakladna infoOfNakladna) {
        InfoOfNakladnaPresenter.infoOfNakladna = infoOfNakladna;
    }

    @Override
    public InfoOfNakladna getInfoOfNakladna()
    {
        return (infoOfNakladna);
    }

    @Override
    public boolean onSaveInfo() {
        return (iInfoOfNakladnaView.pressSaveInfo(infoOfNakladna));
    }

    @Override
    public void onChooseDogovir() {
        iInfoOfNakladnaView.pressChooseDogovir();
    }

    @Override
    public void onTypeNumber() {
        iInfoOfNakladnaView.pressTypeNumber();
    }

    @Override
    public void onDate() {
        iInfoOfNakladnaView.pressDate();
    }

    @Override
    public void onMarkaAvto() {
        iInfoOfNakladnaView.pressMarkaAvto();
    }

    @Override
    public void onNomerZnak() {
        iInfoOfNakladnaView.pressNomerZnak();
    }

    @Override
    public void onNameDriver() {
        iInfoOfNakladnaView.pressNameDriver();
    }
}
