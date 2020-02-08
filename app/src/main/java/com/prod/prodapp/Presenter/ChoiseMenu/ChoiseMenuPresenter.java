package com.prod.prodapp.Presenter.ChoiseMenu;

import com.prod.prodapp.View.ChoiseMenu.IChoiseMenuView;

public class ChoiseMenuPresenter implements IChoiseMenuPresenter {

    IChoiseMenuView iChoiseMenuView;

    public ChoiseMenuPresenter(IChoiseMenuView iChoiseMenuView)
    {
        this.iChoiseMenuView = iChoiseMenuView;
    }

    @Override
    public void onCreateNakladna() {
        iChoiseMenuView.pressCreateNaladna();
    }

    @Override
    public void onSettings() {
        iChoiseMenuView.pressSettings();
    }
}
