package com.example.dictionary;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetialFragment extends Fragment {
    private String message = "allll";

    public DetialFragment() {
    }

    public static DetialFragment getNewInstance(String message) {
        DetialFragment detialFragment = new DetialFragment();
        detialFragment.message = message;
        return detialFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getContext(), this.message, Toast.LENGTH_SHORT).show();
    }
}
