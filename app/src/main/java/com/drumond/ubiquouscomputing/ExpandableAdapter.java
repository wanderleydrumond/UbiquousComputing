package com.drumond.ubiquouscomputing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private List<String> listGroup;
    private HashMap<String, List<String>> listData;
    private LayoutInflater layoutInflater;

    public ExpandableAdapter(Context context, List<String> listGroup, HashMap<String, List<String>> listData) {
        this.listGroup = listGroup;
        this.listData = listData;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listData.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listData.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup viewHolderGroup;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.header_expandable_list_view, null);
            viewHolderGroup = new ViewHolderGroup();
            convertView.setTag(viewHolderGroup);

            viewHolderGroup.tvGroup = (TextView) convertView.findViewById(R.id.tv_group);
        } else {
            viewHolderGroup = (ViewHolderGroup) convertView.getTag();
        }

        viewHolderGroup.tvGroup.setText(listGroup.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolderItem;
        String value = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_expandable_list_view, null);
            viewHolderItem = new ViewHolderItem();
            convertView.setTag(viewHolderItem);

            viewHolderItem.tvItem = convertView.findViewById(R.id.tv_item);
        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        viewHolderItem.tvItem.setText(value);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolderGroup {
        TextView tvGroup;
    }

    class ViewHolderItem {
        TextView tvItem;
    }

}