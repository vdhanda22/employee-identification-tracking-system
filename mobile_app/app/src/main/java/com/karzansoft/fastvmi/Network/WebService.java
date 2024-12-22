package com.karzansoft.fastvmi.Network;


import com.karzansoft.fastvmi.Models.AccessToken;
import com.karzansoft.fastvmi.Models.AccessoryItem;
import com.karzansoft.fastvmi.Models.Contact;
import com.karzansoft.fastvmi.Models.LocalizeText;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Models.VehicleModel;
import com.karzansoft.fastvmi.Models.VehicleService;
import com.karzansoft.fastvmi.Models.VehicleServiceAlert;
import com.karzansoft.fastvmi.Models.Voucher;
import com.karzansoft.fastvmi.Network.Entities.Request.AddVehicleRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.ContactSearchRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.CustomerSearchRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.GetQuestionsTemplateRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.InspectionRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.LoginRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.ModelSearchRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.RegistrationRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.SaveMovementRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.SavePurchaseOrderRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.SyncRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.TranslationLanguageRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.ValidateOperationRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.VehicleMarksRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.VehicleMarksUpdateRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.VehicleSearchRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.VehicleServiceRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.VoucherRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.WorkshopMovementRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.FirebaseAuthResponse;
import com.karzansoft.fastvmi.Network.Entities.Response.GetQuestionTemplateResponse;
import com.karzansoft.fastvmi.Network.Entities.Response.GetTariffGroupResponse;
import com.karzansoft.fastvmi.Network.Entities.Response.LoginResponse;
import com.karzansoft.fastvmi.Network.Entities.Response.RegistrationResponse;
import com.karzansoft.fastvmi.Network.Entities.Response.SMSResponse;
import com.karzansoft.fastvmi.Network.Entities.Response.SaveMovementResponse;
import com.karzansoft.fastvmi.Network.Entities.Response.SaveVehicleReponse;
import com.karzansoft.fastvmi.Network.Entities.Response.SyncResponse;
import com.karzansoft.fastvmi.Network.Entities.Response.ValidateOperationResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Yasir on 3/21/2016.
 */
public interface WebService {


    @Headers({"Content-Type: application/json"})
    @POST("api/Account")
    Call<WebResponse<String>> login(@Body LoginRequest loginRequest);

    //do not need this ..
    @Headers({"Content-Type: application/json"})
    @GET("api/Movement/DefaultCheckList")
    Call<WebResponse<WebResponseList<AccessoryItem>>> getAccessoryItems(@Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @GET("api/Sync")
    Call<WebResponse<SyncResponse>> getSyncItems(@Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/synch/SyncData")
    Call<WebResponse<SyncResponse>> getSyncItems(@Body SyncRequest syncRequest, @Header("Authorization") String auth);


    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/vehicle/SearchExternalVehicles")
    Call<WebResponse<WebResponseList<Vehicle>>> searchVehicle(@Body VehicleSearchRequest vehicleSearchRequest, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/vehicle/SearchVehicles")
    Call<WebResponse<WebResponseList<Vehicle>>> searchVehicles(@Body VehicleSearchRequest vehicleSearchRequest, @Header("Authorization") String auth);


    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/customer/SearchExternalCustomers")
    Call<WebResponse<WebResponseList<Contact>>> searchCustomer(@Body CustomerSearchRequest customerSearchRequest, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/customer/GetCustomerDetailByCode")
    Call<WebResponse<Contact>> getCustomerDetailByCode(@Body Contact customerSearchRequest, @Header("Authorization") String auth);


    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/movement/ProcessValidation")
    Call<WebResponse<ValidateOperationResponse>> validateOperation(@Body ValidateOperationRequest operationRequest, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/movement/SaveMovement")
    Call<WebResponse<SaveMovementResponse>> saveMovement(@Body SaveMovementRequest saveMovementRequest, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/workshopMovement/CreateOrUpdateWorkshopMovement")
    Call<WebResponse<SaveMovementResponse>> saveWorkshopMovement(@Body WorkshopMovementRequest saveMovementRequest, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/workshopMovement/CloseWorkshopMovement")
    Call<WebResponse<SaveMovementResponse>> closeWorkshopMovement(@Body WorkshopMovementRequest saveMovementRequest, @Header("Authorization") String auth);


    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/azureStorage/InitializeUpload")
    Call<WebResponse<AccessToken>> getStorageAccessToken(@Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/vehicleModel/SearchVehicleModels")
    Call<WebResponse<WebResponseList<VehicleModel>>> searchVehicleModels(@Body ModelSearchRequest modelSearchRequest, @Header("Authorization") String auth);


    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/vehicle/CreateOrUpdateVehicle")
    Call<WebResponse<SaveVehicleReponse>> saveVehicle(@Body AddVehicleRequest addVehcleRequest, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/Registration/Register")
    Call<WebResponse<RegistrationResponse>> registerAccount(@Body RegistrationRequest registrationRequest, @Header("Authorization") String auth);

//    @Headers({"Content-Type: application/json"})
//    @POST("TenantRegistration/SendVerificationCode")
//    Call<SmsCodeDto> sendCodeSMS(@Body SmsCodeDto sendCodeSMSDto, @Header("Authorization") String auth);
//
//    @Headers({"Content-Type: application/json"})
//    @POST("TenantRegistration/VerifyCode")
//    Call<SmsCodeDto> verifyCode(@Body SmsCodeDto sendCodeSMSDto, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/vehicle/GetVehicleMarks")
    Call<WebResponse<Vehicle>> getVehicleMarks(@Body VehicleMarksRequest request, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/vehicle/UpdateVehicleMarks")
    Call<WebResponse<Object>> updateVehicleMarks(@Body VehicleMarksUpdateRequest request, @Header("Authorization") String auth);


/*
 @Headers({"Content-Type: application/json"})
 @POST("api/services/app/synch/GetLanguageText")
 Call<WebResponse<WebResponseList<LocalizeText>>> getLanguageText(@Query("cultureName") String calture, @Header("Authorization") String auth);
*/

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/synch/GetLanguageTexts")
    Call<WebResponse<WebResponseList<LocalizeText>>> getLanguageText(@Body TranslationLanguageRequest caltureReuest, @Header("Authorization") String auth);


    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/firebaseAuth/GetToken")
    Call<WebResponse<FirebaseAuthResponse>> getAccessToken(@Header("Authorization") String auth);

    // copied from crs  //the commented by rameel
//
//    @Headers({"Content-Type: application/json"})
//    @POST("api/services/app/directory/Search")
//    Call<WebResponse<WebResponseList<Contact>>> searchContact(@Body ContactSearchRequest contactSearchRequest, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/directory/SearchExternalDrivers")
    Call<WebResponse<WebResponseList<Contact>>> searchContact(@Body ContactSearchRequest contactSearchRequest, @Header("Authorization") String auth);


    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/commonLookup/SearchVouchers")
    Call<WebResponse<WebResponseList<Voucher>>> searchVouchers(@Body VoucherRequest request, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/vehicleService/GetVehicleServicesByModelOrDefault")
    Call<WebResponse<WebResponseList<VehicleService>>> getVehicleServices(@Body VehicleServiceRequest request, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/vehicleAlerts/GetServiceAlerts")
    Call<WebResponse<WebResponseList<VehicleServiceAlert>>> getVehicleServicesAlert(@Body VehicleServiceRequest request, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/purchaseOrder/CreateOrUpdatePurchaseOrder")
    Call<WebResponse<Voucher>> savePurchaseOrder(@Body SavePurchaseOrderRequest request, @Header("Authorization") String auth);


    /// Google Api

    @Headers({"Content-Type: application/json"})
    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
//mode=driving
    Call<ResponseBody> getPlaceId(@Query("location") String location, @Query("radius") String radius, @Query("key") String apiKey);

    @Headers({"Content-Type: application/json"})
    @GET("https://us-central1-speed-1490596329045.cloudfunctions.net/api/getlocation?")
//mode=driving
    Call<ResponseBody> getPlaceIdByLoc(@Query("location") String location, @Query("radius") String radius, @Header("Authorization") String auth);

    //https://www.googleapis.com/language/translate/v2?

    @Headers({"Content-Type: application/json"})
    @GET("PdfReport/EmailCheckCard/{id}")
    Call<ResponseBody> emailCheckCard(@Path("id") String movementId, @Header("Authorization") String auth);

    @FormUrlEncoded
    @POST("https://api.twilio.com/2010-04-01/Accounts/ACb065ab1868cd02d2ad4308bc75e5cab6/Messages.json")
    Call<SMSResponse> sendActivationCode(@Field("To") String to, @Field("From") String from, @Field("Body") String text, @Header("Authorization") String auth);


    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/safetyCheckTemplate/GetTemplateByVehicle")
    Call<WebResponse<GetQuestionTemplateResponse>> GetTemplateByVehicle(@Body GetQuestionsTemplateRequest operationRequest, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/tariffGroup/GetTariffGroups")
    Call<WebResponse<WebResponseList<GetTariffGroupResponse>>> GetTariffGroups(@Body Object operationRequest, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json"})
    @POST("api/services/app/inspection/SaveInspection")
    Call<WebResponse<ResponseBody>> SaveInspection(@Body InspectionRequest operationRequest, @Header("Authorization") String auth);


}
