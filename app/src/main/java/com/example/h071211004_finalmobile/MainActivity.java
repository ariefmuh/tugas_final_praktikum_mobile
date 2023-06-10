package com.example.h071211004_finalmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView movie_iv, series_iv, favorites_iv;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        movie_iv = findViewById(R.id.movie_iv);
        series_iv = findViewById(R.id.series_iv);
        favorites_iv = findViewById(R.id.favorite_iv);

        movie_iv.setOnClickListener(this);
        series_iv.setOnClickListener(this);
        favorites_iv.setOnClickListener(this);

        showFragment(new MovieFragment());
    }

    @Override
    public void onClick(View v) {
        final Drawable drawable;
        Fragment fragment;
        if (v == movie_iv) {
            drawable = movie_iv.getDrawable();
            fragment = new MovieFragment();
        } else if (v == series_iv) {
            drawable = series_iv.getDrawable();
            fragment = new TvShowFragment();
        } else if (v == favorites_iv) {
            drawable = favorites_iv.getDrawable();
            fragment = new FavoriteFragment();
        } else {
            return;
        }
        if (drawable != null) {
            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            ((ImageView) v).setImageDrawable(drawable);

            new Handler().postDelayed(() -> {
                drawable.clearColorFilter();
                ((ImageView) v).setImageDrawable(drawable);
            }, 200);

            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            showFragment(fragment);
        }
    }

    private void showFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
