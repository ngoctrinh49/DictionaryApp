package com.example.dictionary;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HistoryFragment extends Fragment {
    private FragmentListener listener;
    private DBHelper mDBHelper;
    private HistoryAdapter adapter;

    public static HistoryFragment newInstance(DBHelper dbHelper) {
        HistoryFragment fragment = new HistoryFragment();
        fragment.mDBHelper = dbHelper;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        ListView historyList = (ListView) view.findViewById(R.id.historyList);             //hien thi lich su
        adapter = new HistoryAdapter(getActivity(), mDBHelper.getAllWordFromHistory());

        historyList.setAdapter(adapter);

        adapter.setOnItemClick(new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                if (listener != null) {
                    listener.onItemClick(String.valueOf(adapter.getItem(position)));
                }
            }
        });

        adapter.setOnItemDeleteClick(new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                String key = (String) adapter.getItem(position);
                mDBHelper.removeHistory(key);
                adapter.removeItem(position);
                adapter.notifyDataSetChanged();
            }
        });

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

    //clide vào item clear thi xoa het
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_clear, menu);
    }

    //clear all
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_clear){
            mDBHelper.clearHistory();
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

}