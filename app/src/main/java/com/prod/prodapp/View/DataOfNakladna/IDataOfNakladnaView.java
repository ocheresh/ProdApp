package com.prod.prodapp.View.DataOfNakladna;

import com.prod.prodapp.Model.Employe.Employe;
import com.prod.prodapp.Model.ProductsData;

import java.util.List;

public interface IDataOfNakladnaView {

    void pressSendGmail();

    void pressSave(List<ProductsData> list);

    void pressSend();

    void pressEdit(int position, List<ProductsData> list);

    void pressBack(final List<ProductsData> list);

    void pressLoadEmplInfo(Employe employe);

    void pressSummary(List<ProductsData> list);

}
