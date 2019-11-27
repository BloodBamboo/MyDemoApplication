package com.example.admin.myapplication.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/9.
 */

public class BluetoothAdapter extends RecyclerView.Adapter {
   public interface BluetoothAdapterListener {
       void onClick(View v, int pos);
   }

    public List<BluetoothDevice> list = new ArrayList<>();
    BluetoothAdapterListener listener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new BluetoothAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooch, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bluetoothname.setText("设备名称 : " + list.get(position).getName());
        viewHolder.uuid.setText("设备地址 : " + list.get(position).getAddress());
        viewHolder.itemView.setOnClickListener((v) -> {
            if (listener != null) {
                listener.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bluetoothname, uuid, status;
        public ViewHolder(View itemView) {
            super(itemView);
            bluetoothname = itemView.findViewById(R.id.bluetoothname);
            uuid = itemView.findViewById(R.id.uuid);
            status = itemView.findViewById(R.id.status);
        }
    }

    public void setListener(BluetoothAdapterListener listener) {
        this.listener = listener;
    }
}
