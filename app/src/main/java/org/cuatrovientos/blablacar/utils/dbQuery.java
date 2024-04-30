package org.cuatrovientos.blablacar.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

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

    public void getUserDataFromFirestore(String userEmail) {
        db.collection("users")
                .whereEqualTo("mail", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        HashMap<String, Object> userData = (HashMap<String, Object>) documentSnapshot.getData();
                        if (userData != null) {
                            userDataListener.onUserDataReceived(userData);
                        } else {
                            userDataListener.onUserDataError("No se encontraron datos válidos para el usuario.");
                        }
                    } else {
                        userDataListener.onUserDataError("No se encontró ningún usuario con el correo electrónico proporcionado.");
                    }
                })
                .addOnFailureListener(e -> userDataListener.onUserDataError("Error al consultar la base de datos: " + e.getMessage()));
    }

    private void showErrorToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
