package com.teamnoname.streetartzone.StreetStage;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamnoname.streetartzone.R;

public class StageMapDialog extends android.support.v4.app.DialogFragment implements OnMapReadyCallback {

    private GoogleMap map_stageInfo;
    private StageInfo stageInfo;
    private TextView tv_address;

    private SupportMapFragment mapFragment;

    public static StageMapDialog newInstance(StageInfo stageInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("STAGE_INFO", stageInfo);
        StageMapDialog dialog = new StageMapDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            stageInfo = (StageInfo) getArguments().getSerializable("STAGE_INFO");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(mapFragment ==null){
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }

        android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.dialog_stagemap_map,mapFragment).commit();

        View view = inflater.inflate(R.layout.dialog_stagemap, container, false);

        tv_address = (TextView)view.findViewById(R.id.dialog_stagemap_tv_address);
        tv_address.setText(stageInfo.getAddress());

        Dialog dialog = this.getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mapFragment != null){
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map_stageInfo = googleMap;

        if (stageInfo != null) {
            LatLng stageLocation = new LatLng(stageInfo.getLat(), stageInfo.getLot());
            MarkerOptions options = new MarkerOptions();
            options
                    .position(stageLocation)
                    .title(stageInfo.getPlaceName());
            map_stageInfo.addMarker(options);
            map_stageInfo.moveCamera(CameraUpdateFactory.newLatLngZoom(stageLocation, 16f));
        }

    }
}