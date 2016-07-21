package giphy.alexiddev.giphyapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import giphy.alexiddev.giphyapp.R;
import giphy.alexiddev.giphyapp.api.GiphyService;
import giphy.alexiddev.giphyapp.model.Example;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseBestActivity extends AppCompatActivity {

    private static final String KEY_LINK_FIRST = "LINK_1";
    private static final String KEY_LINK_SECOND = "LINK_2";
    private String firstGifLink;
    private String secondGifLink;
    private String randomGifLink;
    private ImageView firstGifImage;
    private ImageView secondGifImage;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_best);
        context = this;
        initView();

        if (savedInstanceState != null) {
            firstGifLink = savedInstanceState.getString(KEY_LINK_FIRST, "");
            secondGifLink = savedInstanceState.getString(KEY_LINK_SECOND, "");
            initAfterReCreate(firstGifImage, firstGifLink);
            initAfterReCreate(secondGifImage, secondGifLink);
        } else {
            getRandomGif(firstGifImage);
            getRandomGif(secondGifImage);
        }
    }

    private void initView() {
        firstGifImage = (ImageView) findViewById(R.id.firstImage);
        secondGifImage = (ImageView) findViewById(R.id.secondImage);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.firstImage:
                getRandomGif(secondGifImage);
                break;
            case R.id.secondImage:
                getRandomGif(firstGifImage);
                break;
            default:
                break;
        }
    }

    private void getRandomGif(final ImageView view) {

        GiphyService.GiphyApi.getInstance().randomGif().enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                randomGifLink = response.body().getData().getImageUrl();

                switch (view.getId()) {
                    case R.id.firstImage:
                        firstGifLink = randomGifLink;
                        break;
                    case R.id.secondImage:
                        secondGifLink = randomGifLink;
                        break;
                }

                Ion.with(context)
                        .load(randomGifLink)
                        .withBitmap()
                        .placeholder(R.drawable.progress_animation)
                        .intoImageView(view);
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });
    }

    private void initAfterReCreate(ImageView imageView, String gifLink) {
        Ion.with(context)
                .load(gifLink)
                .withBitmap()
                .placeholder(R.drawable.progress_animation)
                .intoImageView(imageView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_LINK_FIRST, firstGifLink);
        outState.putString(KEY_LINK_SECOND, secondGifLink);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right_current_activity, R.anim.slide_in_right_caused_activity);
    }
}
