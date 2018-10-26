package com.pasyappagent.pasy.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.gson.GBank;
import com.pasyappagent.pasy.component.network.gson.GBanks;
import com.pasyappagent.pasy.modul.register.RegisterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhimas on 11/28/17.
 */

public class RecyBanksAdapter extends RecyclerView.Adapter<RecyBanksAdapter.ViewHolder>{
    private List<GBanks> listBank;
    private OnClickItem onClickItem;

    public RecyBanksAdapter(OnClickItem onClick) {
        listBank = new ArrayList<>();
        onClickItem = onClick;
    }

    public void setData(List<GBanks> list) {
        listBank = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyBanksAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_banks_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GBanks bank = listBank.get(position);
        holder.bankInfo.setText(bank.name);
        holder.backAccount.setText(bank.accounts.account_no);
        if (bank.logo != null) {
            Glide.with(holder.itemView.getContext()).load(bank.logo.base_url + "/" + bank.logo.path)
                    .dontAnimate().into(holder.bankImg);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItem != null) {
                    onClickItem.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBank.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView bankImg;
        private TextView bankInfo;
        private LinearLayout container;
        private TextView backAccount;

        public ViewHolder(View itemView) {
            super(itemView);
            bankImg = (ImageView) itemView.findViewById(R.id.bank_icon);
            bankInfo = (TextView) itemView.findViewById(R.id.bank_name);
            backAccount = (TextView) itemView.findViewById(R.id.bank_account);
            container = (LinearLayout) itemView.findViewById(R.id.container_banks_list);
        }
    }

    public interface OnClickItem {
        void onClick(int position);
    }
}
