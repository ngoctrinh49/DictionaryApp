package com.example.dictionary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DictionaryFragment extends Fragment {

    private FragmentListener fragmentListener;

    private String message = "Ban vua an vao Dictionary Fragment";

    public DictionaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dictionary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button myButton = (Button) view.findViewById(R.id.myBtn);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onItemClick(message);
                }
            }
        });
    }

    public void setOnFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
}