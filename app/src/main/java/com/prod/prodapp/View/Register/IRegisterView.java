package com.prod.prodapp.View.Register;

import android.app.Activity;

import com.prod.prodapp.Model.Employe.Employe;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public interface IRegisterView {

    void onRegisterResult(String message);

    void checkFileExist(Employe employe);

    void readFileXml(Employe employe);

    void processParsing_userData(XmlPullParser parser, Employe employe) throws IOException, XmlPullParserException;

    void pressFindPath();

    void verifyStoragePermissions(Activity activity);

    void pressDownoload(Employe employe);
}
