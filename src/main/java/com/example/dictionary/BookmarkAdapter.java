package com.example.dictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookmarkAdapter extends BaseAdapter {
    private ListItemListener listener;
    private ListItemListener listenerBtnDelete;

    Context mContext;
    String[] mSource;

    public BookmarkAdapter(Context context, String[] source) {
        this.mContext = context;
        this.mSource = source;
    }

    @Override
    public int getCount() {
        return mSource.length;
    }

    //ham return tu click
    @Override
    public Object getItem(int position) {
        return mSource[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.bookmark_layout_item, parent, false);
            viewHolder.textView = convertView.findViewById(R.id.tvWord);
            viewHolder.btnDelete = convertView.findViewById(R.id.btnDelete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(mSource[position]);

        //viewHolder.textView.setOnClickListener(new View.OnClickListener() {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

        //khi an vao delete
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listenerBtnDelete.onItemClick(position);
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView textView;
        ImageView btnDelete;
    }

    public void setOnItemClick(ListItemListener listItemListener) {
        this.listener = listItemListener;
    }

    public void setOnItemDeleteClick(ListItemListener listItemListener) {
        this.listenerBtnDelete = listItemListener;
    }

}
