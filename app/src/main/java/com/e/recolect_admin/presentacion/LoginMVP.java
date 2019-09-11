package com.e.recolect_admin.presentacion;

public interface LoginMVP {
    interface Vista {
        void onEmailValidationError(String errorType);

        void onPassValidationError(String errorType);

        void onError(String error);

        void onSuccess();
    }

    interface Presentacion {
        void doLoginWhitEmailPassword(String email, String password);

        boolean validarEmailPassword(String email, String password);

        void onEmailValidationError(String errorType);

        void onPassValidationError(String errorType);

        void onLoginError(String error);

    }


}
