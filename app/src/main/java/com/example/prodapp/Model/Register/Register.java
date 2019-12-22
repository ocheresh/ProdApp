package com.example.prodapp.Model.Register;

public class Register implements IRegister {

    private String path;

    public Register(String path) {
        this.path = path;
    }

    @Override
    public boolean isValidRegister() {
        if (path.isEmpty())
            return (false);
        return (true);
    }
}
