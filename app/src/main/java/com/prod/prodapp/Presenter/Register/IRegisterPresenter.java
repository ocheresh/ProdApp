package com.prod.prodapp.Presenter.Register;

import com.prod.prodapp.Model.Employe.Employe;

public interface IRegisterPresenter {

    void onRegister(String path);

    void onDownoload(String path);

    void onFindPath();

    void onCheckExistFile();

    Employe getEmploye();

}
