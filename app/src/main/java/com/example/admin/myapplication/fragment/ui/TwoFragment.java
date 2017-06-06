package com.example.admin.myapplication.fragment.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/6/6.
 */

public class TwoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two_transition, null);
        view.findViewById(R.id.image2).setOnClickListener((v) -> {
            OneFragment oneFragment = new OneFragment();
            oneFragment.setSharedElementEnterTransition(new ChangeBounds().setDuration(500));
//            oneFragment.setEnterTransition(new Slide(Gravity.LEFT));
//            oneFragment.setReenterTransition(new Explode());
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, oneFragment)
                    .addSharedElement(v, "shared_image")
                    .commit();
        });
        return view;
    }
}
