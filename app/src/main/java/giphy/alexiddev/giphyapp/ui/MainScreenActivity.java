package giphy.alexiddev.giphyapp.ui;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import giphy.alexiddev.giphyapp.R;

public class MainScreenActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        context = this;
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.battle:
                startNewActivity(ChooseBestActivity.class);
                break;
            case R.id.gif_by_tag:
                startNewActivity(FindGifByTagActivity.class);
                break;
            case R.id.favorite:
                startNewActivity(FavoriteActivity.class);
                break;
            default:
                break;
        }
    }

    private void startNewActivity(Class activityClass) {
        Intent i = new Intent(context, activityClass);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left_caused_activity, R.anim.slide_in_left_current_activity);
    }
}
