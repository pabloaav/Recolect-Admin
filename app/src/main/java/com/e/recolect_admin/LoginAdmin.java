package com.e.recolect_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import com.e.recolect_admin.presentacion.LoginMVP;
import com.e.recolect_admin.presentacion.LoginPresentador;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginAdmin extends AppCompatActivity implements LoginMVP.Vista {

    //region Atributos

    private LoginMVP.Presentacion loginPresentador;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextInputLayout textInputLayout_correo, textInputLayout_contrasena;
    private TextInputEditText textInputEditText_correo, textInputEditText_contrasena;
    private GoogleSignInClient mGoogleSignInClient;

    //endregion

    //region Metodos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Configuracion de inicio con Cuenta Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Elementos de la vista
        bindLayoutElements();

        //Inicializacion del Presentador del Login
        loginPresentador = new LoginPresentador(this, this, firebaseAuth, databaseReference);
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_loguear:
                String p_correoLogin = textInputEditText_correo.getText().toString().trim();
                String p_contrasena = textInputEditText_contrasena.getText().toString().trim();

                try {
                    loginPresentador.doLoginWhitEmailPassword(p_correoLogin, p_contrasena);
                } catch (Exception e) {
                    Toast.makeText(this, "Error en Base de Datos", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_loggoogle:
                signInConGoogle();
                break;

            case R.id.bt_registrarse:
                Toast.makeText(this, "Solicitud de ser administrador enviada", Toast.LENGTH_LONG).show();
                break;

            default:
                Toast.makeText(this, "No se pudo iniciar sesion", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is signed in (non-null) and update UI accordingly.
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    Intent menu = new Intent(LoginAdmin.this, MainActivity.class);
                    menu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(menu);
                    finish();
                }
            }

        }, 2000);
    }//Fin de onStart()

    private void bindLayoutElements() {
        textInputEditText_correo = findViewById(R.id.txt_correoLogin);
        textInputEditText_contrasena = findViewById(R.id.txt_contrasena);
        textInputLayout_correo = findViewById(R.id.til_correoLogin);
        textInputLayout_contrasena = findViewById(R.id.til_contrasena);
    }

    //endregion

    //region Inicio con Cuenta Google
    public void signInConGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginAdmin.this, "No se pudo iniciar sesion ", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "No se pudo inciar", Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        Intent menuAdmin = new Intent(LoginAdmin.this, MainActivity.class);
        menuAdmin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(menuAdmin);
        onSuccess();
    }

    //endregion

    //region Metodos de la Vista Login
    @Override
    public void onEmailValidationError(String errorType) {
        this.textInputLayout_correo.setError(errorType);
    }

    @Override
    public void onPassValidationError(String errorType) {
        this.textInputLayout_contrasena.setError(errorType);
    }

    @Override
    public void onError(String error) {
        View vista = findViewById(R.id.linearMainActivity);
        Snackbar.make(vista, error, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void onSuccess() {
        this.finish();
    }
    //endregion
}
