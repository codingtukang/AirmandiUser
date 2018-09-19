package com.pasyappagent.pasy.modul.promo.detail;

import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GPromo;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;

/**
 * Created by Dhimas on 2/18/18.
 */

public class PromoDetailActivity extends BaseActivity implements CommonInterface, PromoDetailPresenterImpl.SuccessPromo {
    private PromoDetailPresenter mPresenter;
    private ImageView promoImg;
    private TextView promoTitle;
    private TextView promoContent;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.promo_detail_activity_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Promo Detail");
        promoImg = (ImageView) findViewById(R.id.img_promo);
        promoTitle = (TextView) findViewById(R.id.title);
        promoContent = (TextView) findViewById(R.id.content);
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new PromoDetailPresenterImpl(this, this);
        String promoid = getIntent().getStringExtra("promoId");
        mPresenter.getPromoDetail(promoid);
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

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
    public void setData(GPromo promo) {
        if (promo.image != null) {
            Glide.with(this).load(promo.image.base_url + "/" + promo.image.path).into(promoImg);
        }
        promoTitle.setText(promo.title);
        promoContent.setText(Html.fromHtml(promo.content));
    }
}
