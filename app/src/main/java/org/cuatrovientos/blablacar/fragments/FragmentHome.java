package org.cuatrovientos.blablacar.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import org.cuatrovientos.blablacar.R;
import java.util.Calendar;

public class FragmentHome extends Fragment {
    public EditText etPlannedDate;
    Button btnFind;

    public FragmentHome() {
    }

    // Define una interfaz para comunicar la fecha seleccionada al Activity principal
    public interface OnDateSelectedListener {
        void onDateSelected(String date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        etPlannedDate = view.findViewById(R.id.etPlannedDate);
        btnFind = view.findViewById(R.id.btnFind);

        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(), "datePicker");
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del FragmentAddRoutes
                FragmentAddRoutes fragmentAddRoutes = new FragmentAddRoutes();

                // Pasar la fecha seleccionada como un argumento al FragmentAddRoutes
                Bundle bundle = new Bundle();
                bundle.putString("selectedDate", etPlannedDate.getText().toString());
                // TODO: Pasar el resto de los datos necesarios
                fragmentAddRoutes.setArguments(bundle);

                // Realizar la transacción del fragmento
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.defaultView, fragmentAddRoutes)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    // Clase DatePickerFragment
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(requireActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            FragmentHome fragmentHome = (FragmentHome) getParentFragment();
            if (fragmentHome != null) {
                String selectedDate = day + " / " + (month + 1) + " / " + year;
                fragmentHome.onDateSelected(selectedDate);
                fragmentHome.etPlannedDate.setText(selectedDate);
            }
        }
    }

    // Método para enviar la fecha seleccionada al Activity principal
    private void onDateSelected(String date) {
        if (getActivity() instanceof OnDateSelectedListener) {
            ((OnDateSelectedListener) getActivity()).onDateSelected(date);
        }
    }
}
