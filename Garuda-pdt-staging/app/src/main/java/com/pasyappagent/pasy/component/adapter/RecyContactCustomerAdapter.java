package com.pasyappagent.pasy.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.listener.ListActionListener;
import com.pasyappagent.pasy.component.network.gson.GCostumer;
import com.pasyappagent.pasy.component.network.gson.GPromo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class RecyContactCustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GCostumer> customers;
    private ListActionListener mListener;
    private LinkedHashMap<String, Integer> mMapIndex;
    private ArrayList<String> mSectionList;
    private String[] mSections;

    private void fillSections() {
        mMapIndex = new LinkedHashMap<String, Integer>();

        for (int x = 0; x < customers.size(); x++) {
            String fruit = customers.get(x).name;
            if (fruit.length() > 1) {
                String ch = fruit.substring(0, 1);
                ch = ch.toUpperCase();
                if (!mMapIndex.containsKey(ch)) {
                    mMapIndex.put(ch, x);
                }
            }
        }
        Set<String> sectionLetters = mMapIndex.keySet();
        // create a list from the set to sort
        mSectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(mSectionList);

        mSections = new String[mSectionList.size()];
        mSectionList.toArray(mSections);

    }
    private String getSection(GCostumer pCustomer) {
        return pCustomer.name.substring(0, 1).toUpperCase();
    }

    private GCostumer getItem(int pPosition) {
        return customers.get(pPosition);
    }

    public RecyContactCustomerAdapter(ListActionListener listener, List<GCostumer> pcustomers) {
        customers = pcustomers;
        mListener = listener;
        fillSections();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_customer, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final GCostumer customer = customers.get(position);

        ((ViewHolder) holder).name.setText(Html.fromHtml(customer.name));
        GCostumer lcustomer = getItem(position);
        String section = getSection(customer);
        ((ViewHolder) holder).bind(customer, section, mMapIndex.get(section) == position);

        ((ViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.itemClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void setDataCustomer(List<GCostumer> customerList) {
        customers = customerList;
        notifyDataSetChanged();
    }

    public void setListener(ListActionListener listClicked) {
        this.mListener = listClicked;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView avatar;
        private LinearLayout container;
        private TextView sectionTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.contact_avatar_iv);
            name = (TextView) itemView.findViewById(R.id.contact_name_tv);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            sectionTitle = (TextView) itemView.findViewById(R.id.section_title);
        }

        public void bind(GCostumer pItem, String pSection, boolean bShowSection) {
            name.setText(pItem.name);
            sectionTitle.setText(pSection);
            sectionTitle.setVisibility(bShowSection ? View.VISIBLE : View.GONE);
        }
    }
}