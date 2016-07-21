package giphy.alexiddev.giphyapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.io.File;
import giphy.alexiddev.giphyapp.R;
import giphy.alexiddev.giphyapp.model.Example;
import giphy.alexiddev.giphyapp.util.Util;

/**
 * Created by v.aleksandrenko on 18.07.2016.
 */
public abstract class UtilActivity extends AppCompatActivity {

    protected void setImageInView(Example example, ImageView view, Context context) {

        Ion.with(context)
                .load(example.getData().getImageUrl())
                .withBitmap()
                .placeholder(R.drawable.progress_animation)
                .intoImageView(view);
    }

    protected void share(Example example) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, example.getData().getImageUrl());
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));
    }

    protected void saveGif(Example example, final View view, Context context) {
        final ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(getString(R.string.downloading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        String directory = Environment.getExternalStorageDirectory() + getString(R.string.storage_name);
        File dir = new File(directory);

        if (!(dir.exists() && dir.isDirectory())) {
            dir.mkdirs();
        }

        Ion.with(context)
                .load(example.getData().getImageUrl())
                .progressDialog(mProgressDialog)
                .write(new File(directory, Util.randomIdentifier() + ".gif"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        mProgressDialog.dismiss();
                        Util.snackbarView(view, R.string.downloaded);
                    }
                });
    }
}
