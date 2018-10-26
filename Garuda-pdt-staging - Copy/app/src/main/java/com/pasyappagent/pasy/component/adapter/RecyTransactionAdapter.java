package com.pasyappagent.pasy.component.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.gson.GTransaction;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 9/25/17.
 */

public class RecyTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<GTransaction> transactions;
    private OnListClicked onListClicked;
    private ListActionLoadMore mListener;
    private boolean isPPOB;

    private enum ITEM_TYPE {
        ITEM_TYPE_TRANSACTION,
        ITEM_TYPE_LOADMORE
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_TRANSACTION.ordinal()) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction_layout, parent, false));
        }
        return new ViewHolderLoadMore(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loadmore, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final GTransaction transaction = transactions.get(position);

            if (transaction.service != null ) {
                if (transaction.service.provider != null &&
                        transaction.service.provider.logo != null) {
                    Glide.with(holder.itemView.getContext()).
                            load(transaction.service.provider.logo.base_url +"/" +
                                    transaction.service.provider.logo.path).dontAnimate().
                            into(((ViewHolder)holder).iconProduct);
                }

                ((ViewHolder)holder).topupText.setText(transaction.service.name);
                ((ViewHolder)holder).paketText.setText("Rp " + MethodUtil.toCurrencyFormat(transaction.default_price));
                ((ViewHolder)holder).infoText.setText(transaction.customer_no);
            } else {
//                if (PreferenceManager.getStatusAkupay()) {
//                    Glide.with(holder.itemView.getContext()).load(R.drawable.akupay_fix)
//                            .dontAnimate().into(((ViewHolder)holder).iconProduct);
//                } else {
                    Glide.with(holder.itemView.getContext()).load(R.drawable.pampasy_icon)
                            .dontAnimate().into(((ViewHolder)holder).iconProduct);
//                }
                ((ViewHolder)holder).topupText.setText(transaction.merchant_name);
                ((ViewHolder)holder).paketText.setText("Rp " + MethodUtil.toCurrencyFormat(transaction.amount_charged));
                ((ViewHolder)holder).infoText.setText(transaction.notes);
            }

            if (transaction.status_label != null) {
                if (transaction.status_label.equalsIgnoreCase("waiting payment")) {
                    ((ViewHolder) holder).statusText.setText("MENUNGGU");
                    ((ViewHolder) holder).statusText.setTextColor(Color.parseColor("#E0D000"));
                } else if (transaction.status_label.equalsIgnoreCase("success")) {
                    ((ViewHolder) holder).statusText.setText("BERHASIL");
                    ((ViewHolder) holder).statusText.setTextColor(Color.parseColor("#2CC013"));
                } else {
                    ((ViewHolder) holder).statusText.setText("GAGAL");
                    ((ViewHolder) holder).statusText.setTextColor(Color.parseColor("#A31627"));
                }
            } else {
                if (transaction.status.equalsIgnoreCase(Constant.TOPUP_STATUS_SUCCESS)) {
                    ((ViewHolder)holder).statusText.setText("BERHASIL");
                    ((ViewHolder)holder).statusText.setTextColor(Color.parseColor("#2CC013"));
                }/* else if (transaction.status.equalsIgnoreCase(Constant.TOPUP_STATUS_REJECT)) {
        holder.statusText.setText("GAGAL");
        holder.statusText.setTextColor(Color.parseColor("#A31627"));
        } */else {
                    ((ViewHolder)holder).statusText.setText("MENUNGGU");
                    ((ViewHolder)holder).statusText.setTextColor(Color.parseColor("#E0D000"));
                }
            }


            String[] dateTime = MethodUtil.formatDateAndTime(transaction.created_at);
            ((ViewHolder)holder).dateText.setText(dateTime[0]);
            ((ViewHolder)holder).timeText.setText(dateTime[1]);



            RxView.clicks(((ViewHolder)holder).container).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if (onListClicked != null) {
                        onListClicked.listClick(transaction);
                    }
                }
            });
        } else if (holder instanceof ViewHolderLoadMore) {
            if (mListener != null) {
                mListener.onLoadMoreList();
            }
        }
    }


    @Override
    public int getItemCount() {
        return transactions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return transactions.get(position) != null ? ITEM_TYPE.ITEM_TYPE_TRANSACTION.ordinal() :
                ITEM_TYPE.ITEM_TYPE_LOADMORE.ordinal();
    }

    public RecyTransactionAdapter(ListActionLoadMore mListener, boolean isPPOB) {
        transactions = new ArrayList<>();
        this.isPPOB = isPPOB;
        this.mListener = mListener;
    }

    public void setListenerOnClick(OnListClicked listenerOnClick) {
        onListClicked = listenerOnClick;
    }

    public void addAll(List<GTransaction> transactionList) {
        transactions.addAll(transactionList);
        notifyDataSetChanged();
    }

    public void removeLoadingList() {
        transactions.remove(transactions.size() - 1);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconProduct;
        private TextView topupText;
        private TextView paketText;
        private TextView infoText;
        private TextView statusText;
        private TextView dateText;
        private TextView timeText;
        private LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            iconProduct = (ImageView) itemView.findViewById(R.id.icon_transaction);
            topupText = (TextView) itemView.findViewById(R.id.title_transaction);
            paketText = (TextView) itemView.findViewById(R.id.amount_transaction);
            infoText = (TextView) itemView.findViewById(R.id.info_transaction);
            statusText = (TextView) itemView.findViewById(R.id.status_transaction);
            dateText = (TextView) itemView.findViewById(R.id.date_transaction);
            timeText = (TextView) itemView.findViewById(R.id.time_transaction);
            container = (LinearLayout) itemView.findViewById(R.id.container_transaction);
        }
    }

    class ViewHolderLoadMore extends RecyclerView.ViewHolder {
        ViewHolderLoadMore(View view) {
            super(view);
        }
    }

    public interface OnListClicked{
        void listClick(GTransaction transaction);
    }

}
