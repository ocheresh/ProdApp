package com.example.prodapp.Presenter.Register;

import com.example.prodapp.Model.Employe;
import com.example.prodapp.Model.Register.Register;
import com.example.prodapp.View.Register.IRegisterView;

public class RegisterPresenter implements IRegisterPresenter {


    IRegisterView registerView;
    Employe employe = null;

    public RegisterPresenter(IRegisterView registerView)
    {
        this.registerView = registerView;
        employe = new Employe();
    }

    @Override
    public void onRegister(String path) {
        Register register = new Register(path);
        boolean isRegister = register.isValidRegister();

        if (isRegister)
            registerView.onRegisterResult("Success");
        else
            registerView.onRegisterResult("Error");
    }

    @Override
    public void onDownoload(String path) {
        registerView.pressDownoload(employe);
    }

    @Override
    public void onFindPath() {
        registerView.pressFindPath();
    }

    @Override
    public void onCheckExistFile() {
        employe = new Employe();

//                employe = new Employe();
//        employe.setFirstName("Олег");
//        employe.setLastName("Позняков");
//        employe.setContactNo("+380964587398");
//        employe.setEmail("oleg@gmail.com");
//        employe.setMilitaryRank("сержант");
//        employe.setMilitaryUnit("93 бригада");
//        employe.setUnitofMilitaryUnit("1 батальйон");
//        employe.setAdress("Слов'янськ");
//        employe.setEmailToSend("sashachereshnevy@gmail.com");

        registerView.checkFileExist(employe);
    }

    @Override
    public Employe getEmploye() {
        return employe;
    }

}
