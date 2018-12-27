package com.esprit.projetespritfood;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SplashScreenFragment extends Fragment {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_splashscreen, container, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        }, SPLASH_DISPLAY_LENGTH);
        return root;
    }
}
