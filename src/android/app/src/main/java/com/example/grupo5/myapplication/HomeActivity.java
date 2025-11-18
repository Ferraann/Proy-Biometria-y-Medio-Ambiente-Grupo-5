package com.example.grupo5.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;


import com.example.grupo5.androidapp.R;

import java.util.concurrent.Executor;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // ----------------------
        // TU CÓDIGO ORIGINAL
        // ----------------------
        TextView bienvenida = findViewById(android.R.id.text1); // cámbialo si tu TextView tiene otro ID

        String emailUsuario = getIntent().getStringExtra("email_usuario");
        if (emailUsuario != null) {
            bienvenida.setText("¡Bienvenido, " + emailUsuario + "!");
        }

        // ----------------------
        // RECORDATORIO DE HUELLA
        // ----------------------
        showFingerprintReminder();
    }

    private void showFingerprintReminder() {
        SharedPreferences sharedPref = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        boolean fingerprintEnabled = sharedPref.getBoolean("fingerprint_enabled", false);
        boolean fingerprintDeclined = sharedPref.getBoolean("fingerprint_declined", false);

        // Si ya está activada o ya dijo que NO → no preguntar más
        if (fingerprintEnabled || fingerprintDeclined) return;

        new AlertDialog.Builder(this)
                .setTitle("Recordatorio")
                .setMessage("¿Quieres activar el login por huella?")
                .setCancelable(false)
                .setPositiveButton("Sí", (dialog, which) -> {
                    dialog.dismiss();

                    BiometricManager biometricManager = BiometricManager.from(this);
                    if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                            == BiometricManager.BIOMETRIC_SUCCESS) {
                        showBiometricPromptForRegistration();
                    } else {
                        Toast.makeText(this,
                                "No se puede usar la huella. Registra una huella en ajustes si quieres usarla.",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                    sharedPref.edit().putBoolean("fingerprint_declined", true).apply();
                    Toast.makeText(this, "Seguirás usando contraseña.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showBiometricPromptForRegistration() {
        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);

                        getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                                .edit()
                                .putBoolean("fingerprint_enabled", true)
                                .apply();

                        Toast.makeText(HomeActivity.this,
                                "Huella activada correctamente.",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode,
                                                      @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(HomeActivity.this,
                                "Error al registrar huella: " + errString,
                                Toast.LENGTH_SHORT).show();
                    }
                });

        BiometricPrompt.PromptInfo promptInfo =
                new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Registrar huella")
                        .setSubtitle("Coloca tu dedo en el sensor")
                        .setNegativeButtonText("Cancelar")
                        .build();

        biometricPrompt.authenticate(promptInfo);
    }
}
