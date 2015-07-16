package com.erikbuto.workoutprogram.Home;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erikbuto.workoutprogram.Manage.ManageProgramActivity;
import com.erikbuto.workoutprogram.R;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class HomeFragment extends Fragment {

    public static final String ARG_PROGRAM_ID = "program_id";
    public static final String ARG_PROGRAM_NAME = "program_name";

    private long mProgramId;

    public HomeFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mProgramId = getArguments().getLong(ARG_PROGRAM_ID);

        CardView cardManage = (CardView) rootView.findViewById(R.id.card_view_manage);
        cardManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), ManageProgramActivity.class);
                intent.putExtra(ManageProgramActivity.ARG_PROGRAM_ID, mProgramId);
                startActivity(intent);
            }
        });



        // ((ImageView) rootView.findViewById(R.id.image)).setImageResource(R.drawable.noprogram);
        getActivity().setTitle(getArguments().getString(ARG_PROGRAM_NAME));
        return rootView;
    }
}