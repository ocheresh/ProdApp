package com.example.prodapp.Presenter.ChoiseMenu;

import com.example.prodapp.View.ChoiseMenu.IChoiseMenuView;

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
