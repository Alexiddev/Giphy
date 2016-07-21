package giphy.alexiddev.giphyapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import giphy.alexiddev.giphyapp.R;
import giphy.alexiddev.giphyapp.api.GiphyService;
import giphy.alexiddev.giphyapp.database.SQLiteHelper;
import giphy.alexiddev.giphyapp.model.Example;
import giphy.alexiddev.giphyapp.util.Util;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import mabbas007.tagsedittext.TagsEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindGifByTagActivity extends UtilActivity {


    private static final String KEY_LINK = "LINK";
    private ImageView findImage;
    private TagsEditText findTag;
    private Context context;
    private FabSpeedDial fabSpeedDial;
    private LinearLayout mainLayout;
    private Example gifResponce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_gif_by_tag);

        context = this;
        init();
        initMenu();

        if (savedInstanceState != null) {
            gifResponce = savedInstanceState.getParcelable(KEY_LINK);
            fabSpeedDial.setVisibility(View.VISIBLE);
            setImageInView(gifResponce, findImage, context);
            checkBackgroundColor(findImage);
        }
    }

    private void initMenu() {

        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_share:
                        share(gifResponce);
                        break;
                    case R.id.action_save:
                        saveGif(gifResponce, mainLayout, context);
                        break;
                    case R.id.action_add_to_favorites:
                        addToFavorite(gifResponce);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void init() {
        findImage = (ImageView) findViewById(R.id.find_gif_image);
        findTag = (TagsEditText) findViewById(R.id.find_gif_tag);
        fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        mainLayout = (LinearLayout) findViewById(R.id.find_gif_layout);
    }

    public void onClick(View view) {
        if (findTag.getText().length() != 0) {
            GiphyService.GiphyApi.getInstance().findGifByTag(Util.buildStringForRequest(findTag.getText().toString())).enqueue(new Callback<Example>() {
                @Override
                public void onResponse(Call<Example> call, Response<Example> response) {


                    gifResponce = response.body();
                    fabSpeedDial.setVisibility(View.VISIBLE);
                    setImageInView(gifResponce, findImage, context);
                    checkBackgroundColor(findImage);
                }

                @Override
                public void onFailure(Call<Example> call, Throwable t) {
                    Toast.makeText(context, R.string.not_found, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, R.string.enter_term, Toast.LENGTH_SHORT).show();
        }
    }

    private void addToFavorite(Example example) {
        SQLiteHelper sQLiteHelper = new SQLiteHelper(FindGifByTagActivity.this);
        sQLiteHelper.insertGif(example);
        Util.snackbarView(mainLayout, R.string.addes_to_favorite);
    }

    protected void checkBackgroundColor(ImageView findImage) {
        ColorDrawable buttonColor = ((ColorDrawable) findImage.getBackground());
        int colorId = buttonColor.getColor();
        if (colorId != Color.TRANSPARENT) {
            findImage.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LINK, gifResponce);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right_current_activity, R.anim.slide_in_right_caused_activity);
    }
}
