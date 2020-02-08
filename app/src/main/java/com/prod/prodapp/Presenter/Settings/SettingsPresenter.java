package com.prod.prodapp.Presenter.Settings;

import com.prod.prodapp.Model.Employe.Employe;
import com.prod.prodapp.View.Settings.ISettingView;

public class SettingsPresenter implements ISettingsPresenter {

    ISettingView iSettingView;
    Employe employe = null;

    public SettingsPresenter(ISettingView iSettingView)
    {
        this.iSettingView = iSettingView;
        employe = new Employe();
    }

    @Override
    public void onReadInfo() {
        iSettingView.pressRead(employe);;
    }

    @Override
    public void onSetInfo() {
        iSettingView.pressSetInfo(employe);
    }

    @Override
    public void onSaveInfo() {
        iSettingView.pressSaveInfo(employe);
    }
}
