package bd.com.universal.eparking.seeker.WebApis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DirectionService {
    @GET()
    Call<DirectionResponse> getAllDistances(@Url String urlString);
}
