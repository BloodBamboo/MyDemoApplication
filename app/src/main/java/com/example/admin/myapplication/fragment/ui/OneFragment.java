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
import android.widget.ImageView;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/6/6.
 */

public class OneFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_transition, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.image1);
        view.findViewById(R.id.image1).setOnClickListener((v) -> {
            TwoFragment twoFragment = new TwoFragment();
            twoFragment.setSharedElementEnterTransition(new ChangeBounds().setDuration(500));
            twoFragment.setEnterTransition(new Slide(Gravity.RIGHT));
//            twoFragment.setReenterTransition(new Explode().excludeChildren(v, true));
            getFragmentManager().beginTransaction()
                    .replace(R.id.content, twoFragment)
//                    .addToBackStack(null)
                    .addSharedElement(v, "shared_image")
                    .commit();
        });

        return view;
    }
}
