package com.yilos.nailstar.aboutme.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.Sdcard;

import java.util.ArrayList;

/**
 * @author alessandro.balocco
 */
public class SettingFolderAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<Sdcard> sdcardArrayList;
    private String sdcardName;

    public SettingFolderAdapter(Context context, ArrayList<Sdcard> sdcardArrayList, String sdcardName) {
        this.sdcardArrayList = sdcardArrayList;
        this.sdcardName = sdcardName;
        this.layoutInflater = LayoutInflater.from(context);
    }

  @Override
  public int getCount() {
    return sdcardArrayList.size();
  }

    @Override
    public Sdcard getItem(int position) {
        Sdcard sdcard = new Sdcard();
        sdcard = sdcardArrayList.get(position);
        return sdcard;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        Sdcard sdcard = new Sdcard();
        sdcard = getItem(position);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.setting_folder_dialog_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.sdcardText = (TextView) view.findViewById(R.id.sdcard_text);
            viewHolder.sdcardUsed = (TextView) view.findViewById(R.id.sdcard_use_info);
            viewHolder.sdcardChoose = (ImageView) view.findViewById(R.id.ic_sdcard_choose);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        viewHolder.sdcardText.setText(sdcard.getSdcardName() + "(" + sdcard.getSdcardPath() + ")");
        viewHolder.sdcardUsed.setText(sdcard.getAvailCountFormat() + "可用，" + sdcard.getBlockCountFormat());

        if (sdcard.getSdcardName().equals(sdcardName)) {
            viewHolder.sdcardChoose.setVisibility(View.VISIBLE);
        } else {
            viewHolder.sdcardChoose.setVisibility(View.GONE);
        }
        return view;
    }

    static class ViewHolder {
        TextView sdcardText;
        TextView sdcardUsed;
        ImageView sdcardChoose;
    }
}
