package com.example.listycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditCityFragment extends DialogFragment {

    public interface EditCityDialogListener {
        void editCity(City city, int position);
    }

    private static final String ARG_CITY = "city";
    private static final String ARG_POSITION = "position";

    private EditCityDialogListener listener;

    public static EditCityFragment newInstance(City city, int position) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        args.putInt(ARG_POSITION, position);

        EditCityFragment fragment = new EditCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof EditCityDialogListener) {
            listener = (EditCityDialogListener) context;
        } else {
            throw new RuntimeException(
                    context + " must implement EditCityDialogListener"
            );
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Inflate the existing add/edit layout
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_add_city, null);

        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("EditCityFragment requires arguments");
        }

        City city = (City) args.getSerializable(ARG_CITY);
        int position = args.getInt(ARG_POSITION, -1);

        if (city == null || position == -1) {
            throw new IllegalStateException("City or position missing in arguments");
        }

        // Pre-fill fields
        editCityName.setText(city.getName());
        editProvinceName.setText(city.getProvince());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newCityName = editCityName.getText().toString();
                    String newProvinceName = editProvinceName.getText().toString();

                    listener.editCity(
                            new City(newCityName, newProvinceName),
                            position
                    );
                })
                .create();
    }
}