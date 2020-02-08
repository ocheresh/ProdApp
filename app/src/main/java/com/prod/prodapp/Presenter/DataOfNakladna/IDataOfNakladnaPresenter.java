package com.prod.prodapp.Presenter.DataOfNakladna;

import com.prod.prodapp.Model.Employe.Employe;
import com.prod.prodapp.Model.ProductsData;
import com.prod.prodapp.View.DataOfNakladna.IDataOfNakladnaView;

import java.util.List;

public interface IDataOfNakladnaPresenter {

    void onSave();

    void onSend();

    void onSendGmail();

    void onEdit(int position);

    void onBack();

    List<ProductsData> getList();

    void onloadEmplInfo();

    public Employe getEmploye();

    void onSummary();

    IDataOfNakladnaView getIDataOfNakladnaView();

    public void setList(List<ProductsData> list);

}
