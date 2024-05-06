package org.cuatrovientos.blablacar.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.models.User;

import java.util.ArrayList;
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
        void onUserDataReceived(User userData);

        void onUserDataError(String errorMessage);
    }

    public void setUserDataListener(UserDataListener listener) {
        this.userDataListener = listener;
    }

    public String getCurrentUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public interface UserDataSuccessListener {
        void onUserDataReceived(User userData);
        void onUserDataError(String errorMessage);
    }
    private ArrayList<Route> convertHashMapToArrayList(HashMap<String, Route> hashMap) {
        ArrayList<Route> arrayList = new ArrayList<>();
        if (hashMap != null) {
            for (Map.Entry<String, Route> entry : hashMap.entrySet()) {
                arrayList.add(entry.getValue());
            }
        }
        return arrayList;
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
                            User user = new User(
                                    (String) userData.get("name"),
                                    (String) userData.get("surname"),
                                    (String) userData.get("mail"),
                                    (String) userData.get("phone"),
                                    (int) (long) userData.get("O2Points"),
                                    (ArrayList<Route>) userData.get("Routes"),
                                    (ArrayList<Route>) userData.get("routesSubscribed"),
                                    (ArrayList<Route>) userData.get("routesBaned")
//                                    convertHashMapToArrayList((HashMap<String, Route>) userData.get("Routes")),
//                                    convertHashMapToArrayList((HashMap<String, Route>) userData.get("routesSubscribed")),
//                                    convertHashMapToArrayList((HashMap<String, Route>) userData.get("routesBaned"))
                            );
                            listener.onUserDataReceived(user);
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
        public void onUserDataReceived(User userData) {
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
