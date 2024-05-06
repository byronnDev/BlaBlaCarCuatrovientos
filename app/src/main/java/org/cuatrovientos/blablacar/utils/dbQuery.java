package org.cuatrovientos.blablacar.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class dbQuery {
    private FirebaseFirestore db;
    private Context context;
    private UserDataListener userDataListener;

    public dbQuery(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public interface UserDataListener {
        void onUserDataReceived(HashMap<String, Object> userData);

        void onUserDataError(String errorMessage);
    }

    public void setUserDataListener(UserDataListener listener) {
        this.userDataListener = listener;
    }

    public String getCurrentUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public interface UserDataSuccessListener {
        void onUserDataReceived(Map<String, Object> userData);
        void onUserDataError(String errorMessage);
    }

    public void getUserDataFromFirestore(String userEmail, UserDataSuccessListener listener) {
        db.collection("users")
                .whereEqualTo("mail", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> userData = documentSnapshot.getData();
                        if (userData != null) {
                            listener.onUserDataReceived(userData);
                        } else {
                            listener.onUserDataError("No se encontraron datos válidos para el usuario.");
                        }
                    } else {
                        listener.onUserDataError("No se encontró ningún usuario con el correo electrónico proporcionado.");
                    }
                })
                .addOnFailureListener(e -> listener.onUserDataError("Error al consultar la base de datos: " + e.getMessage()));
    }

    // Ejemplo de uso
    /*
    getUserDataFromFirestore("correo@example.com", new UserDataSuccessListener() {
        @Override
        public void onUserDataReceived(Map<String, Object> userData) {
            // Manejar los datos del usuario aquí
        }

        @Override
        public void onUserDataError(String errorMessage) {
            // Manejar el error aquí
        }
    });
     */


    private void showErrorToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
