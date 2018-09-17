package com.nerdcastle.eparking.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nerdcastle.eparking.R;

public class AddVehicleFragment extends Fragment {

    public interface  AddVehicleFragmentInterface {
        public void goToAddVehicle ();
    }

    public AddVehicleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);
        return view;
    }

}