package com.pasyappagent.pasy.component.network;

import com.google.gson.JsonObject;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.network.gson.GBalance;
import com.pasyappagent.pasy.component.network.gson.GBanks;
import com.pasyappagent.pasy.component.network.gson.GCard;
import com.pasyappagent.pasy.component.network.gson.GCashback;
import com.pasyappagent.pasy.component.network.gson.GCashbackRedeem;
import com.pasyappagent.pasy.component.network.gson.GCreditCard;
import com.pasyappagent.pasy.component.network.gson.GDeals;
import com.pasyappagent.pasy.component.network.gson.GLogo;
import com.pasyappagent.pasy.component.network.gson.GMerchant;
import com.pasyappagent.pasy.component.network.gson.GPost;
import com.pasyappagent.pasy.component.network.gson.GPromo;
import com.pasyappagent.pasy.component.network.gson.GRedeem;
import com.pasyappagent.pasy.component.network.gson.GReward;
import com.pasyappagent.pasy.component.network.gson.GTopup;
import com.pasyappagent.pasy.component.network.gson.GVoucher;
import com.pasyappagent.pasy.component.network.response.AgentResponse;
import com.pasyappagent.pasy.component.network.response.BankTransferResponse;
import com.pasyappagent.pasy.component.network.response.CalculateResponse;
import com.pasyappagent.pasy.component.network.response.CashbackResponse;
import com.pasyappagent.pasy.component.network.response.CreditCardResponse;
import com.pasyappagent.pasy.component.network.response.DealsResponse;
import com.pasyappagent.pasy.component.network.response.MerchantResponse;
import com.pasyappagent.pasy.component.network.response.MessageResponse;
import com.pasyappagent.pasy.component.network.response.ParkingResponse;
import com.pasyappagent.pasy.component.network.response.PostCommentResponse;
import com.pasyappagent.pasy.component.network.response.PostsResponse;
import com.pasyappagent.pasy.component.network.response.PromoResponse;
import com.pasyappagent.pasy.component.network.response.QRTransactionResponse;
import com.pasyappagent.pasy.component.network.response.ServicesResponse;
import com.pasyappagent.pasy.component.network.response.SinglePostResponse;
import com.pasyappagent.pasy.component.network.response.SyncContactResponse;
import com.pasyappagent.pasy.component.network.response.TopupResponse;
import com.pasyappagent.pasy.component.network.response.TransactionHistoryResponse;
import com.pasyappagent.pasy.component.network.response.TransactionParkingResponse;
import com.pasyappagent.pasy.component.network.response.TransactionResponse;
import com.pasyappagent.pasy.component.network.response.TransferRequestLogGroupResponse;
import com.pasyappagent.pasy.component.network.response.TransferRequestLogResponse;
import com.pasyappagent.pasy.component.network.response.TransferRequestResponse;
import com.pasyappagent.pasy.component.network.response.VoucherResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Dhimas on 10/6/17.
 */

public interface NetworkService {

    @FormUrlEncoded
    @POST("customers/create")
    Observable<MessageResponse> register(@Field("name") String name,
                                         @Field("mobile") String mobile,
                                         @Field("email") String email,
                                         @Field("referral_id") String refferalId
    );

    @FormUrlEncoded
    @PUT("customers/info")
    Observable<GAgent> updateProfile(@Field("name") String name,
                                     @Field("mobile") String mobile,
                                     @Field("email") String email,
                                     @Field("username") String username,
                                     @Field("last_name") String last_name,
                                     @Field("gender") String gender,
                                     @Field("address") String address,
                                     @Field("birthdate") String birthdate,
                                     @Field("avatar_base64") String avatar_base64);

    @FormUrlEncoded
    @POST("customers/verify")
    Observable<AgentResponse> verifyAgent(@Field("mobile") String mobile,
                                          @Field("code") String code);

    @FormUrlEncoded
    @PUT("customers/passcode")
    Observable<MessageResponse> setPasscode(@Field("passcode") String passcode,
                                            @Field("passcode_confirmation") String passcodeConfirm);

    @GET("customers/passcode/{nomormobile}")
    Observable<MessageResponse> resendCode(@Path("nomormobile") String mobile);

    @FormUrlEncoded
    @POST("customers/auth")
    Observable<AgentResponse> login(@Field("mobile") String mobile,
                                    @Field("passcode") String passcode,
                                    @Field("device_active") String imei);

    @GET("customers/balance")
    Observable<GBalance> getBalance();

    @FormUrlEncoded
    @POST("topup")
    Observable<GTopup> topupBalance(@Field("amount") String amount,
                                    @Field("voucher_id") String voucherId,
                                    @Field("method_payment") String method);

    @GET("banks")
    Observable<List<GBanks>> getBanks();

    @FormUrlEncoded
    @POST("topup/transfer")
    Observable<MessageResponse> transferBalance(@Field("mobile") String mobile,
                                                @Field("amount") String amount,
                                                @Field("notes") String notes);

    @FormUrlEncoded
    @POST("topup/request")
    Observable<MessageResponse> requestBalance(@Field("to_customer_id") String destCustId,
                                               @Field("amount") String amount,
                                               @Field("notes") String notes);

    @FormUrlEncoded
    @PUT("topup/confirm")
    Observable<BankTransferResponse> bankTransfer(@Field("order_id") String orderId,
                                                  @Field("bank_id") String bankId,
                                                  @Field("account_id") String accountId);

    @GET("vouchers")
    Observable<VoucherResponse> checkVoucher(@Query("code") String code,
                                             @Query("amount") String amount);

    @GET("customers/{code}")
    Observable<AgentResponse> checkRefferalCode(@Path("code") String code);

    @GET("customers/premium")
    Observable<JsonObject> checkPremium();

    @FormUrlEncoded
    @POST("premiums/subscribe")
    Observable<MessageResponse> subscribePremium(@Field("referral_id") String refferalId);

    @GET("services")
    Observable<ServicesResponse> getService(@Query("type") String type,
                                            @Query("amount") String amount,
                                            @Query("customer_no") String customerNo,
                                            @Query("category") String cat);

    @FormUrlEncoded
    @POST("transactionsppob/pay")
    Observable<TransactionResponse> payTransaction(@Field("transaction_id") String transactionId);

    @FormUrlEncoded
    @POST("transactionsppob/inquiry")
    Observable<TransactionResponse> setInquiry(@Field("customer_no") String customerNo,
                                               @Field("service_id") String serviceId,
                                               @Field("amount") String amount);

    @FormUrlEncoded
    @POST("transactionsppob")
    Observable<TransactionResponse> getTransactionWithoutInquiry(@Field("customer_no") String customerNo,
                                                                 @Field("service_id") String serviceId);

    @GET("cashbacks")
    Observable<GCashback> getCashback();

    @GET("customers/total_downlines")
    Observable<JsonObject> getTotalDownline();

    @GET("rewards")
    Observable<List<GReward>> getReward();

    @GET("customers/transactions")
    Observable<TransactionHistoryResponse> getTransaction(@Query("page") int page);

    @GET("customers/transactionsppob")
    Observable<TransactionHistoryResponse> getTransactionPPOB(@Query("page") int page);

    @POST("cashbacks/redeem")
    Observable<GCashbackRedeem> redeemCashback();

    @FormUrlEncoded
    @POST("rewards/redeem")
    Observable<GRedeem> redeemReward(@Field("rewards_id") String rewardId);

    @Multipart
    @POST("uploads/image")
    Observable<List<GLogo>> uploadChat(@Part("ref") RequestBody agents,
                                       @Part("type") RequestBody chat,
                                       @Part MultipartBody.Part file);

    @Multipart
    @POST("uploads/image")
    Observable<List<GLogo>> uploadChat(@Part MultipartBody.Part file);

    @Multipart
    @POST("uploads/image")
    Observable<List<GLogo>> uploadChat(@Part("ref") RequestBody ref,
                                       @Part("type") RequestBody type,
                                       @Part("file[]") RequestBody file);

    @GET("customers/topups")
    Observable<TopupResponse> getTopupTransaction(@Query("page") int page);

    @GET("customers/cashbacks")
    Observable<CashbackResponse> getCashbackList(@Query("page") int page);

    @FormUrlEncoded
    @PUT("topup/bank")
    Observable<BankTransferResponse> updateBank(@Field("order_id") String orderId,
                                                @Field("bank_id") String bankId,
                                                @Field("account_id") String accountId);

    @GET("transactions/qr/{code}")
    Observable<QRTransactionResponse> getQrData(@Path("code") String code);

    @POST("transactions/midtrans")
    Observable<GCreditCard> postTransactionRecipientMidtrans(@Body RequestBody body);

    @FormUrlEncoded
    @PUT("transactions/confirm")
    Observable<QRTransactionResponse> transactionConfirm(@Field("transaction_id") String transactionId);

    @FormUrlEncoded
    @POST("customers/cards")
    Observable<JsonObject> attachCreditCard(@Field("card") String tokenId);

    @FormUrlEncoded
    @POST("customers/cards")
    Observable<JsonObject> addCreditCard(@Query("provider") String provider,
                                         @Field("card_token") String tokenId,
                                         @Field("cardhash") String cardHash,
                                         @Field("card_name") String cardName,
                                         @Field("expiry_month") String expiryMonth,
                                         @Field("expiry_year") String expiryYear,
                                         @Field("card_expiry_at") String expireToken);

    @FormUrlEncoded
    @POST("customers/cards/setdefault")
    Observable<CreditCardResponse> setDefaultCard(@Field("token") String token);

    @FormUrlEncoded
    @POST("transactions/calculate_charge")
    Observable<CalculateResponse> calculateAmount(@Field("amount") String amount,
                                                  @Field("card_type") String cardType,
                                                  @Field("merchant_id") String merchantId);

    @GET("customers/qr")
    Observable<GLogo> getQr();

    @GET("merchants/{code}")
    Observable<GMerchant> getMerchantDetail(@Path("code") String code);

    @GET("merchants")
    Observable<MerchantResponse> getMerchantList(@Query("page") int page,
                                                 @Query("q") String search);

    @GET("customers/cards/")
    Observable<List<GCard>> getCreditCardMidtransList(@Query("provider") String provider);

    @GET("customers/cards")
    Observable<List<GCard>> getCreditCardList();

    @DELETE("customers/cards/{card_id}")
    Observable<JsonObject> deleteCard(@Path("card_id") String card_id);

    @DELETE("customers/cards/{card_id}")
    Observable<JsonObject> deleteCardMidtrans(@Path("card_id") String card_id,
                                              @Query("provider") String provider);

    @GET("content/promo")
    Observable<PromoResponse> getPromo();

    @GET("content/promo/{promo_id}")
    Observable<GPromo> getPromoDetail(@Path("promo_id") String promo_id);

    @FormUrlEncoded
    @POST("transactionparkings")
    Observable<ParkingResponse> startParking(@Field("merchant_id") String merchantId,
                                             @Field("type_vehicle") String typeVehicle);

    @GET("merchants/parking_qr/{code}")
    Observable<JsonObject> startParkingByCode(@Path("code") String code);

    @FormUrlEncoded
    @PUT("transactionparkings/update_ticket/{transaction_id}")
    Observable<TransactionParkingResponse> finishParking(@Path("transaction_id") String transaction_id,
                                                         @Field("ticket_no") String ticket_no);

    @FormUrlEncoded
    @POST("deals/redeem")
    Observable<JsonObject> redeem(@Field("deal_id") String dealId);

    @GET("deals")
    Observable<DealsResponse> getDeals();

    @FormUrlEncoded
    @PUT("customers/logout")
    Observable<JsonObject> logout(@Field("customer_id") String costumerId);

    @FormUrlEncoded
    @POST("topup/request")
    Observable<TransferRequestResponse> request(@Field("to_customer_id") String to_customer_id,
                                                @Field("amount") String amount);

    @FormUrlEncoded
    @POST("customers/syncContact")
    Observable<SyncContactResponse> syncContact(@Field("mobiles") String mobiles);


    @GET("posts/index")
    Observable<PostsResponse> fetchFeed(@Query("page") int page);

    @GET("posts/detail/{post_id}")
    Observable<SinglePostResponse> getPostDetail(@Path("post_id") String post_id);

    @FormUrlEncoded
    @POST("posts/addComment/{post_id}")
    Observable<SinglePostResponse> addComment(@Path("post_id") String post_id, @Field("full_post_return") boolean full_post_return, @Field("text") String text);

    @FormUrlEncoded
    @POST("posts/deleteComment/{comment_id}")
    Observable<SinglePostResponse> deleteComment(@Path("post_id") String comment_id, @Field("full_post_return") boolean full_post_return);

    @GET("posts/deletePost/{post_id}")
    Observable<MessageResponse> deletePost(@Path("post_id") String post_id);

    @FormUrlEncoded
    @POST("posts/like/{post_id}")
    Observable<SinglePostResponse> likeDislikePost(@Path("post_id") String post_id, @Field("full_post_return") boolean full_post_return);

    @FormUrlEncoded
    @POST("posts/create")
    Observable<SinglePostResponse> createPost(@Field("text") String text);

    @GET("topup/transferRequetsLog/{to_customer_id}")
    Observable<TransferRequestLogResponse> getTransferRequestLog(@Path("to_customer_id") String to_customer_id);

    @GET("topup/transferRequetsLogGroups")
    Observable<TransferRequestLogGroupResponse> getTransferRequestLogGroup();

}
