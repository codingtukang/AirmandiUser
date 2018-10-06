package com.pasyappagent.pasy.modul.customer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.paging.listview.PagingListView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.RecyContactCustomerAdapter;
import com.pasyappagent.pasy.component.adapter.RecyFeedCommentAdapter;
import com.pasyappagent.pasy.component.adapter.RecyFeedTrxAdapter;
import com.pasyappagent.pasy.component.listener.ListActionListener;
import com.pasyappagent.pasy.component.network.gson.GComment;
import com.pasyappagent.pasy.component.network.gson.GCostumer;

import java.util.ArrayList;
import java.util.List;

public class ContactCustomerActivity extends BaseActivity implements ListActionListener {
    private static final String TAG = "CUSTOMER CONTACT" ;
    private RecyclerView listView;
    private RecyContactCustomerAdapter listAdapter;
    private List<GCostumer> contactLiost;
    private List<GCostumer> customerData;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.contact_customer_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitleWithCloseButton("TULIS");
    }


    @Override
    protected void onCreateAtChild() {
        initData();
    }

    private void initData() {
        listAdapter = new RecyContactCustomerAdapter(this, loadCustomerData());

        listView = (RecyclerView) findViewById(R.id.contact_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(listAdapter);

        getContactList();

    }

    private List<GCostumer> loadCustomerData(){
        List<GCostumer> customerData = new ArrayList<>();
        for(int i=0;i<5;i++){
            GCostumer customer = new GCostumer();
            customer.name = "Ujang Dulag";
            customer.mobile = "081220794742";
            customerData.add(customer);
        }
        return customerData;
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    public void itemClicked(int position) {

    }

    @Override
    public void itemDeleted(int position) {

    }

    private void getContactList() {

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Log.i(TAG, "Name: " + name);
                            Log.i(TAG, "Phone Number: " + phoneNo);

                            GCostumer contact = new GCostumer();
                            contact.name = name;
                            contact.mobile = phoneNo;
                        }
                        pCur.close();
                    }
                }
            }
            if(cur!=null){
                cur.close();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getContactList();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
