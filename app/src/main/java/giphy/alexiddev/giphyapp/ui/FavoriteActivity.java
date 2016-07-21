package giphy.alexiddev.giphyapp.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.List;
import giphy.alexiddev.giphyapp.R;
import giphy.alexiddev.giphyapp.database.SQLiteHelper;
import giphy.alexiddev.giphyapp.model.Example;
import giphy.alexiddev.giphyapp.util.Util;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class FavoriteActivity extends UtilActivity {


    private static final String KEY_LINK = "LINK";
    private ImageView favoriteImage;
    private Context context;
    private FabSpeedDial fabSpeedDial;
    private LinearLayout mainLayout;
    private Example gifFavorite;
    private SQLiteHelper sqLiteHelper;
    private List favoritGifs;
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_gifs);

        context = this;
        init();
        initMenu();

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt(KEY_LINK, 0);
            setImageInView((Example) favoritGifs.get(count), favoriteImage, context);
        }
    }

    private void initMenu() {

        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_share:
                        share(gifFavorite);
                        break;
                    case R.id.action_save:
                        saveGif(gifFavorite, mainLayout, context);
                        break;
                    case R.id.action_delete:
                        deleteGif((Example) favoritGifs.get(count));
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void init() {
        sqLiteHelper = new SQLiteHelper(FavoriteActivity.this);
        favoritGifs = sqLiteHelper.getAllRecords();
        fabSpeedDial = (FabSpeedDial) findViewById(R.id.favorite_gif_menu);
        mainLayout = (LinearLayout) findViewById(R.id.favorite_gif_layout);
        favoriteImage = (ImageView) findViewById(R.id.favorite_gif_image);

        if (favoritGifs.size() != 0) {
            gifFavorite = (Example) favoritGifs.get(0);
            setImageInView((Example) favoritGifs.get(0), favoriteImage, context);
        } else {
            fabSpeedDial.setVisibility(View.INVISIBLE);
            Util.snackbarView(mainLayout, R.string.empty);
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (favoritGifs.size() == 0) {
                    Util.snackbarView(mainLayout, R.string.empty);
                } else if (count == favoritGifs.size() - 1) {
                    count = 0;
                    setImageInView((Example) favoritGifs.get(count), favoriteImage, context);
                } else {
                    count++;
                    setImageInView((Example) favoritGifs.get(count), favoriteImage, context);
                }
                break;
            case R.id.previous:
                if (favoritGifs.size() == 0) {
                    Util.snackbarView(mainLayout, R.string.empty);
                } else if (count > 0) {
                    count--;
                    setImageInView((Example) favoritGifs.get(count), favoriteImage, context);
                } else {
                    count = favoritGifs.size() - 1;
                    setImageInView((Example) favoritGifs.get(count), favoriteImage, context);
                }
                break;
        }
    }

    private void deleteGif(Example example) {
        SQLiteHelper sQLiteHelper = new SQLiteHelper(FavoriteActivity.this);
        sQLiteHelper.deleteGif(example);
        Util.snackbarView(mainLayout, R.string.deleted_from_favorite);
        favoritGifs = sqLiteHelper.getAllRecords();
        if (favoritGifs.size() == 0) {
            Util.snackbarView(mainLayout, R.string.empty);
            Drawable myDrawable = getResources().getDrawable(R.drawable.find_back);
            favoriteImage.setImageDrawable(myDrawable);
        } else if (count == favoritGifs.size() - 1 | count == 0) {
            count = 0;
            setImageInView((Example) favoritGifs.get(count), favoriteImage, context);
        } else {
            count--;
            setImageInView((Example) favoritGifs.get(count), favoriteImage, context);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_LINK, count);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right_current_activity, R.anim.slide_in_right_caused_activity);
    }
}
