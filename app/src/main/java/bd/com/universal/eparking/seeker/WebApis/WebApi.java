package bd.com.universal.eparking.seeker.WebApis;
import bd.com.universal.eparking.seeker.DataModels.NearByPlaceModel.NearbyPlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Mobile App Develop on 5/22/2018.
 */

public interface WebApi {
    @GET
    Call<NearbyPlaceResponse> getNearbyPlaces(@Url String urlString);



}
