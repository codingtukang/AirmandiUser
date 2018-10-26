package com.pasyappagent.pasy.modul.editprofile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import de.hdodenhof.circleimageview.CircleImageView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.home.HomePageActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import in.mayanknagwanshi.imagepicker.imageCompression.ImageCompressionListener;
import in.mayanknagwanshi.imagepicker.imagePicker.ImagePicker;
import rx.functions.Action1;

/**
 * Created by Dhimas on 12/23/17.
 */

public class EditProfileActivity extends BaseActivity implements CommonInterface, EditProfileView{
    private EditText textName;
    private EditText textMobile;
    private EditText textEmail;
    private EditText textLastName;
    private EditText textUsername;
    private EditText textAddress;
    private EditText textBirthDate;
    private Switch genderSwtich;
    private TextView genderSwtichLabel;
    private CircleImageView avatarImageView;
    private ImageView avatarChangeImageView;
    private Button changeAvatarBtn;
    private String base64Avatar;
    private Calendar calendar;
    private ImagePicker imagePicker;
    private Button updateBtn;
    private EditProfilePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ImagePicker.setMinQuality(600, 600);
        imagePicker = new ImagePicker();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImagePicker.SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            //Add compression listener if withCompression is set to true
            imagePicker.addOnCompressListener(new ImageCompressionListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompressed(String filePath) {//filePath of the compressed image
                    //convert to bitmap easily
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    if (bitmap != null) {
                        avatarImageView.setImageBitmap(bitmap);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream .toByteArray();
                        base64Avatar = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    }
                }
            });
        }
//        //call the method 'getImageFilePath(Intent data)' even if compression is set to false
        try {
            String filePath = imagePicker.getImageFilePath(data);
            if (filePath != null) {//filePath will return null if compression is set to true
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                if (bitmap != null) {
                    avatarImageView.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    base64Avatar = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }
            }
        }
        catch (Exception e){

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

//    public void onPickImage(View view) {
//        // Click on image button
//        ImagePicker.pickImage(this, "Pilih avatar");
//    }

    @Override
    public void showProgressLoading() {
        progressBar.show(this, "", false, null);
    }

    @Override
    public void hideProgresLoading() {
        progressBar.getDialog().dismiss();
    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
        MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
        if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            goToLoginPage1(this);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.edit_profile_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitleWithSubmitButton("Ubah Akun");
        textName = findViewById(R.id.register_name);
        textMobile = findViewById(R.id.register_mobile);
        textEmail = findViewById(R.id.register_email);
        textLastName = findViewById(R.id.register_last_name);
        textUsername = findViewById(R.id.register_username);
        genderSwtich = findViewById(R.id.switch_gender);
        genderSwtichLabel = findViewById(R.id.gender_label);
        textBirthDate = findViewById(R.id.register_birthdate);
        changeAvatarBtn = findViewById(R.id.change_picture_btn);
        avatarImageView = findViewById(R.id.avatar_img);
        textAddress = findViewById(R.id.register_address);

        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBirthdateLabel();
            }
        };

        textBirthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditProfileActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
//        updateBtn = (Button) findViewById(R.id.process_register);
    }

    private void updateBirthdateLabel() {
        String format = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        textBirthDate.setText(sdf.format(calendar.getTime()));
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new EditProfilePresenterImpl(this, this);
        GAgent agent = PreferenceManager.getAgent();
        textName.setText(agent.name);
        textMobile.setText(agent.mobile);
        textEmail.setText(agent.email);
        textLastName.setText(agent.last_name);
        textUsername.setText(agent.username);
        genderSwtich.setChecked((agent.gender != null) ? agent.gender.equals("M") : false);
        genderSwtichLabel.setText(agent.gender != null && agent.gender.equals("M") ? "Laki-laki" : "Perempuan");
        textBirthDate.setText(agent.birthdate);
        textAddress.setText(agent.address);

        if (agent.avatar_base64 != null && !agent.avatar_base64.equals("")){
            base64Avatar = agent.avatar_base64;
            byte[] decodedString = Base64.decode(agent.avatar_base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatarImageView.setImageBitmap(decodedByte);
        }

        RxView.clicks(changeAvatarBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                imagePicker.withActivity(EditProfileActivity.this) //calling from activity
                        .withCompression(true)
                        .start();
            }
        });


//        RxView.clicks(updateBtn).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                mPresenter.updateProfile(textName.getText().toString(),
//                        textMobile.getText().toString(),
//                        textEmail.getText().toString());
//            }
//        });
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {
//        String name, String mobile, String email, String username, String lastName, String gender, String address, String birthdate, String avatarbase64
        mPresenter.updateProfile(textName.getText().toString(),
                textMobile.getText().toString(),
                textEmail.getText().toString(),
                textUsername.getText().toString(),
                textLastName.getText().toString(),
                (genderSwtich.isChecked() ? "M" : "W"),
                textAddress.getText().toString(),
                textBirthDate.getText().toString(),
                (base64Avatar != null ? base64Avatar : ""));
    }

    @Override
    public void onSuccessEdit() {
        MethodUtil.showCustomToast(this, "Profile berhasil di update", R.drawable.success);
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("openAgent", true);
        startActivity(intent);
        finish();
    }
}
