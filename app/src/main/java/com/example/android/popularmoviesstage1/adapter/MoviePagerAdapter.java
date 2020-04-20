package com.example.android.popularmoviesstage1.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.android.popularmoviesstage1.fragment.ReviewFragment;
import com.example.android.popularmoviesstage1.fragment.TrailerFragment;

public class MoviePagerAdapter extends FragmentStateAdapter {

    private final int movieId;

    public MoviePagerAdapter(@NonNull FragmentActivity fragmentActivity, int movieId) {
        super(fragmentActivity);
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0 :
                return new TrailerFragment(movieId);
            case 1 :
                return new ReviewFragment(movieId);
            default:
                return new TrailerFragment(0);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
