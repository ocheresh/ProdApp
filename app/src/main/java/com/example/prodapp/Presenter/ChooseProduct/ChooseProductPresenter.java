package com.example.prodapp.Presenter.ChooseProduct;

import com.example.prodapp.Presenter.ChoiseMenu.ChoiseMenuPresenter;
import com.example.prodapp.View.ChooseProduct.IChooseProductView;

public class ChooseProductPresenter implements IChooseProductPresenter {

    IChooseProductView iChooseProductView;

    public ChooseProductPresenter(IChooseProductView iChooseProductView)
    {
        this.iChooseProductView = iChooseProductView;
    }

    @Override
    public void onReadData() {
        iChooseProductView.pressReadData();
    }

    @Override
    public void onAddElement(int position) {
        iChooseProductView.pressAddElement(position);
    }
}
