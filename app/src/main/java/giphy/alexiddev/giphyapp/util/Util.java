package giphy.alexiddev.giphyapp.util;

import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import giphy.alexiddev.giphyapp.R;

/**
 * Created by v.aleksandrenko on 14.07.2016.
 */
public class Util {

    final static String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    static final java.util.Random RAND = new java.util.Random();
    static final Set<String> IDENTIFIERS = new HashSet<>();

    public static String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = RAND.nextInt(5)+5;
            for(int i = 0; i < length; i++)
                builder.append(lexicon.charAt(RAND.nextInt(lexicon.length())));
            if(IDENTIFIERS.contains(builder.toString())) builder = new StringBuilder();
        }
        return builder.toString();
    }

    public static String buildStringForRequest(String fromFindTag){
        return fromFindTag.replace(' ', '-').toString();
    }

    public static void snackbarView(View view, Integer message){
        final Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
        snackbar.show();
    }
}
