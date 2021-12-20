package com.example.dictionary;

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

public class DictionaryFragment extends Fragment {

    private FragmentListener fragmentListener;

    private ArrayAdapter<String> adapter;

    protected ListView words;

    private String message = "Ban vua an vao Dictionary Fragment";

    private ArrayList<String> mSource = new ArrayList<String>();

    private DBHelper dbHelper;

    public DictionaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dictionary, container, false);
    }

//    public void getData() {
//        mSource = dbHelper.getWordFromAV();
//    }

    //ham xu ly listview (khoi tao chu khi bat app)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //test thử
        //getData();

        words = view.findViewById(R.id.dictionaryList);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mSource);
        words.setAdapter(adapter);
        words.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (fragmentListener != null) {
                    fragmentListener.onItemClick(mSource.get(position));       //hien message ten tu vua click
                }
            }
        });
    }

    //2. thêm từ mới vào textview
    public void resetDataSource(ArrayList<String> new_words) {
        mSource = new_words;
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mSource);
        words.setAdapter(adapter);
    }

    public void filterValue(String value) {
        int size = adapter.getCount();
        for (int i = 0; i < size; i++) {
            if (adapter.getItem(i).startsWith(value)) {
                words.setSelection(i);
                break;
            }
        }
    }

//    public String[] getListOfWords() {
//        String[] words = new String[] {
//                "a",
//                "b",
//                "c"
//        };
//        return words;
//    }


    public void setOnFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
}