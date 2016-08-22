package io.harry.zealot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import io.harry.zealot.R;

public class GagFragment extends Fragment {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gag, null);

        ((ImageView)view.findViewById(R.id.gag_image)).setImageResource(R.drawable.ajae_2);

        return view;
    }
}
