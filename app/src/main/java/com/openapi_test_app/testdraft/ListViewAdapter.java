package com.openapi_test_app.testdraft;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemArrayList = new ArrayList<>();

    @Override
    public int getCount() {
        return listViewItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        TextView AddressTextView = convertView.findViewById(R.id.listViewItem_textView1);
        TextView ResLonTextView = convertView.findViewById(R.id.listViewItem_textView2);
        TextView ResLatTextView = convertView.findViewById(R.id.listViewItem_textView3);

        ListViewItem listViewItem = listViewItemArrayList.get(position);

        AddressTextView.setText(listViewItem.getAddress());
        ResLonTextView.setText(listViewItem.getResLon());
        ResLatTextView.setText(listViewItem.getResLat());

        return convertView;
    }

    public void addItem(String address, String resLon, String resLat){
        ListViewItem item = new ListViewItem();

        item.setAddress(address);
        item.setResLon(resLon);
        item.setResLat(resLat);

        listViewItemArrayList.add(item);
    }
}
