package com.jnu.last_application;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.jnu.last_application.data.ShopLoader;
import com.jnu.last_application.data.ShopLocation;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapViewFragment extends Fragment {

    private MapView mapView;

    public MapViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment BaiduMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapViewFragment newInstance() {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_baidu_map, container, false);
        mapView =  rootView.findViewById(R.id.bmapView);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(18.0f);
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        LatLng cenpt = new LatLng(22.255925,113.541112);

        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(15)
                .build();
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
//
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_home);


        new Thread(new Runnable() {
            @Override
            public void run() {
                ShopLoader dataLoader = new ShopLoader();
                String shopJsonData = dataLoader.download("http://file.nidama.net/class/mobile_develop/data/bookstore2022.json");
                List<ShopLocation> locations = dataLoader.parsonJson(shopJsonData);

                MapViewFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AddMarkersOnMap(locations, bitmap);
                    }
                });


            }
        }).start();



        mapView.getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapViewFragment.this.getContext(),"Marker clicked",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return rootView;
    }

    private void AddMarkersOnMap(List<ShopLocation> locations, BitmapDescriptor bitmap) {
        for (ShopLocation shop :
                locations) {
            LatLng cenpt_ = new LatLng(shop.getLatitude(),shop.getLongitude());



            OverlayOptions options = new MarkerOptions().position(cenpt_).icon(bitmap);
//        //将maker添加到地图
            mapView.getMap().addOverlay(options);
            mapView.getMap().addOverlay(new TextOptions().bgColor(0xAAFFFF00)
                    .fontSize(24)
                    .fontColor(0xFFFF00FF)
                    .text(shop.getName()).position(cenpt_));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
}