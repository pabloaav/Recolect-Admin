package com.e.recolect_admin.presentacion;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.e.recolect_admin.MainActivity;
import com.e.recolect_admin.validaciones.Validar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class LoginPresentador implements LoginMVP.Presentacion {

    //region Atributos
    private LoginMVP.Vista vista;
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    //endregion

    //region Constructores, Sets y Gets

    public LoginPresentador(LoginMVP.Vista vista, Context context, FirebaseAuth mAuth, DatabaseReference mDatabaseReference) {
        this.vista = vista;
        this.context = context;
        this.mAuth = mAuth;
        this.mDatabaseReference = mDatabaseReference;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public DatabaseReference getmDatabaseReference() {
        return mDatabaseReference;
    }

    public void setmDatabaseReference(DatabaseReference mDatabaseReference) {
        this.mDatabaseReference = mDatabaseReference;
    }

    public LoginMVP.Vista getVista() {
        return vista;
    }

    public void setVista(LoginMVP.Vista vista) {
        this.vista = vista;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
//endregion

    //region Metodos

    //Metodo de Login con correo y contraseña
    @Override
    public void doLoginWhitEmailPassword(String email, String password) {
        if (this.validarEmailPassword(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent menu = new Intent(context, MainActivity.class);
                                menu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                context.startActivity(menu);
                                vista.onSuccess();

//                                Intent menu = new Intent(context, MenuInicio.class);
//                                context.startActivity(menu);
//                                Toast.makeText(context, "Bienvenido: " + getmAuth().getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(context, "Falló la autenticación. Verifique los datos",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        } else {
            //En error de Login, pasamos a la vista el mensaje
            onLoginError("Usuario Inexistente");
        }

    }

    @Override
    public boolean validarEmailPassword(String email, String password) {
        boolean correOk;
        boolean passOk;
        boolean result = false;
        // Validamos el correo
        if (email.isEmpty()) {
            onEmailValidationError("Ingresa tu correo por favor");
            correOk = false;
        } else if (!Validar.validarEmail(email)) {
            onEmailValidationError("Ingresa un correo valido: nombre@ejemplo.com");
            correOk = false;
        } else {
            correOk = true;
        }
//        Validamos la contrasena
        if (password.isEmpty()) {
            onPassValidationError("Ingresa tu contraseña por favor");
            passOk = false;
        } else if (!Validar.validarPassword(password)) {
            onPassValidationError("Debes ingresar al menos 6 caracteres, incluido un numero");
            passOk = false;
        } else {
            passOk = true;
        }
        if (correOk && passOk) {
            result = true;
        }
        return result;
    }

    @Override
    public void onEmailValidationError(String errorType) {
        this.getVista().onEmailValidationError(errorType);
    }

    @Override
    public void onPassValidationError(String errorType) {
        this.getVista().onPassValidationError(errorType);
    }

    @Override
    public void onLoginError(String error) {
        this.getVista().onError(error);
    }

    //endregion

}
