package com.example.admin.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.admin.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by admin on 2017/2/13.
 */
public class LeadPagerFragment extends Fragment {


    public static LeadPagerFragment newInstant(int pos) {
        LeadPagerFragment fragment = new LeadPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.image_bg)
    ImageView image_bg;

    Unbinder un;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lead_pager_layout, null);
        un = ButterKnife.bind(this, view);
        switch (getArguments().getInt("pos")) {
            case 0:
                image_bg.setImageResource(R.drawable.a);
                break;
            case 1:
                image_bg.setImageResource(R.drawable.b);
                break;
            case 2:
                image_bg.setImageResource(R.drawable.c);
                break;
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart", "onStart: -------------------------");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        un.unbind();
    }
}
