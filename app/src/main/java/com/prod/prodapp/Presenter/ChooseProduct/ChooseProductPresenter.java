package com.prod.prodapp.Presenter.ChooseProduct;

import com.prod.prodapp.View.ChooseProduct.IChooseProductView;

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
