package com.example.dictionary;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BookmarkFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FragmentListener fragmentListener;

    private String message = "Ban vua nhan vao BookmarkFragment";

    public BookmarkFragment() {
    }

    public void setOnFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Button myButton = (Button) view.findViewById(R.id.myBtn);
//        myButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (fragmentListener != null) {
//                    fragmentListener.onItemClick(message);
//                }
//            }
//        });
        ListView bookmarkList = (ListView) view.findViewById(R.id.bookmarkList);
        BookmarkAdapter adapter = new BookmarkAdapter(getActivity(), getListOfWords());
        bookmarkList.setAdapter(adapter);

        adapter.setOnItemClick(new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                if (fragmentListener != null) {
                    //hien thi tu khi click
                    fragmentListener.onItemClick(String.valueOf(adapter.getItem(position)));
                }
            }
        });

        //khi an vao delete thi hien thi xoa
        adapter.setOnItemDeleteClick(new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                String message = String.valueOf(adapter.getItem(position));
                Toast.makeText(getContext(), message + " đã được xoá khỏi bọokmark!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onDetach() {
        super.onDetach();
    }

    public String[] getListOfWords() {
        String[] words = new String[] {
                "a",
                "b",
                "c"
        };
        return words;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }
}