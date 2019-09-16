package com.e.recolect_admin.validaciones;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Validaciones de campos y operaciones
 *
 * @author Aguirre-Boronat
 */
public final class Validar {

    //region Atributos Estaticos


    //    Variable de expresion regular para validar password
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //al menos un digito numeral
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //cualquier letra
                    //"(?=.*[@#$%^&+=])" +    //al menos un caracter especial
                    "(?=\\S+$)" +           //sin espacios en blanco
                    ".{6,}" +               //cuatro caracteres como minimo
                    "$");
    //endregion


    /**
     * Constructor privado para impedir la creacion de instancias de la clase.
     */
    private Validar() {

    }

    public static boolean validarEmail(String p_email) {
        String email = p_email.trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean validarPassword(String p_password) {
        String password = p_password.trim();

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return false;
        } else {
            return true;
        }

    }


}

