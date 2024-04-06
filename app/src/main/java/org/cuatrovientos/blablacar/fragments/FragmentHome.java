package org.cuatrovientos.blablacar.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import org.cuatrovientos.blablacar.R;

import java.util.Calendar;

public class FragmentHome extends Fragment {
    EditText etPlannedDate;

    public FragmentHome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        etPlannedDate = (EditText) view.findViewById(R.id.etPlannedDate);

        // Define click listener for etPlannedDate
        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.etPlannedDate) {
                    // Create a new DatePickerFragment instance
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getParentFragmentManager(), "datePicker");
                }
            }
        });
        return view;
    }

    // Define DatePickerFragment class outside the listener
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            // Example: Update etPlannedDate text in FragmentHome
            FragmentHome fragmentHome = (FragmentHome) getParentFragment();
            if (fragmentHome != null) {
                fragmentHome.etPlannedDate.setText(day + " / " + (month + 1) + " / " + year);
            }
        }
    }



    public void renderData() {
        //TODO
        //crear un objeto de tipo Ruta y construirlo, luego el resto de la logica de la clase





    }
}
