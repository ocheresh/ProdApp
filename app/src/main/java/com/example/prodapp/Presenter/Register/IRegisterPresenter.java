package com.example.prodapp.Presenter.Register;

import com.example.prodapp.Model.Employe;

import java.io.File;

public interface IRegisterPresenter {

    void onRegister(String path);

    void onDownoload(String path);

    void onFindPath();

    void onCheckExistFile();

    Employe getEmploye();

}
