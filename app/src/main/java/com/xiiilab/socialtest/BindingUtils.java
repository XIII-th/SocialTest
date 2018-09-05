package com.xiiilab.socialtest;

import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by XIII-th on 06.09.2018
 * FIXME: {@link BindingAdapter} not worked on kotlin extensions yet
 */
public class BindingUtils {

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView view, String filePath) {
        if (filePath == null)
            view.setImageResource(R.drawable.ic_face_black);
        else
            Picasso.get().
                    load(new File(filePath)).
                    placeholder(R.drawable.ic_face_black).
                    error(R.drawable.ic_broken_image).
                    into(view);
    }
}
