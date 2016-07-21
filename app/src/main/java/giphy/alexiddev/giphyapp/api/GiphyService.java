package giphy.alexiddev.giphyapp.api;


import giphy.alexiddev.giphyapp.model.Example;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by alexiddev on 12.07.16.
 */
public interface GiphyService {

    @GET("/v1/stickers/random?api_key=dc6zaTOxFJmzC")
    Call<Example> randomGif();

    @GET("/v1/stickers/random?api_key=dc6zaTOxFJmzC&")
    Call<Example> findGifByTag(@Query("tag") String tag);

    class GiphyApi {

        private static String BASE_URL = "http://api.giphy.com";
        private static GiphyService service;

        public static GiphyService getInstance() {

            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                GiphyService service = retrofit.create(GiphyService.class);
                return service;
            } else {
                return service;
            }
        }

    }


}
