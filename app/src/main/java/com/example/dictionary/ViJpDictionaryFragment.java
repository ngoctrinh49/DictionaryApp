package com.example.dictionary;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class ViJpDictionaryFragment extends Fragment {

    private String value = "Hello";
    private FragmentListener listener;
    ListView dictList;
    ArrayAdapter<String> adapter;

    private ArrayList<String> mSource = new ArrayList<>();

    public ViJpDictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.viet_eng_dictionary, container, false);    //viet eng
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dictList = view.findViewById(R.id.viet_eng_dictionary);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mSource);
        dictList.setAdapter(adapter);   //load du lieu
        dictList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onItemClick(mSource.get(position));
                }
            }
        });
    }

    public void resetDataSource(ArrayList<String> source) {
        //dictList.setAdapter(null);
        mSource = source;
        if (getActivity()!=null) {
            adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, source);
            dictList.setAdapter(null);
            dictList.setAdapter(adapter);
        }
    }

    public void filterValue(String value) {
        //adapter.getFilter().filter(value);
        int size = adapter.getCount();
        for (int i = 0; i < size; i++) {
            if (adapter.getItem(i).startsWith(value)) {
                dictList.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setOnFragmentListener(FragmentListener listener) {
        this.listener = listener;
    }
}
