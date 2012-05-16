/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cartodb;

import java.util.List;
import java.util.Map;

import com.cartodb.impl.CartoDBClient;
import com.cartodb.model.CartoDBResponse;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * Based on mapviewdemo form android SDK
 */
public class CartodbActivity extends MapActivity {

	MapView mapView;
	PointsOverlay itemizedoverlay;
	
	/**
	 * task to download points from cartodb using the cartodb-java-client library
	 */
	private class FetchPointsFromCartoDB extends AsyncTask<Void, Void, List<Map<String, Double>>> {
		protected List<Map<String, Double>> doInBackground(Void... params) {
			// you can see the raw data with your browser in
			// http://examples.cartodb.com/api/v1/sql?q=select * from nyc_wifi
			
			CartoDBClientIF cartoDBCLient= new CartoDBClient("examples");
			try {
				String sql = "SELECT st_y(the_geom) as lat, st_x(the_geom) as lng FROM nyc_wifi LIMIT 50";
				CartoDBResponse<Map<String, Double>> res = cartoDBCLient.request(sql);
				return res.getRows();
			} catch (CartoDBException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(List<Map<String, Double>> positions) {
			// add the markers
			for(Map<String, Double> v: positions) {
				Log.v("LOC", v.get("lat") + "," + v.get("lng"));
				GeoPoint point = new GeoPoint((int)(v.get("lat")*1e6),(int) (v.get("lng")*1e6));
				OverlayItem overlayitem = new OverlayItem(point, "", "");
				itemizedoverlay.addOverlay(overlayitem);
			}
		}

	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);
        
        // zoom controls
        mapView = (MapView) findViewById(R.id.map);
        mapView.setBuiltInZoomControls(true);
        
        // set map in NY
        MapController controller = mapView.getController();
        
        controller.setZoom(15);
        controller.animateTo(new GeoPoint((int)(1e6*40.7248057566452), (int)(1e6*-74.003)));
         
        // markers!
        addMarkers();
    }

    public void addMarkers() {
    	 
    	// create points overlay
    	Drawable drawable = getResources().getDrawable(R.drawable.marker);
    	itemizedoverlay = new PointsOverlay(drawable);
    	
    	// add it to maps layers
    	List<Overlay> mapOverlays = mapView.getOverlays();
    	mapOverlays.add(itemizedoverlay);
    	
    	//fetch data
    	
    	new FetchPointsFromCartoDB().execute();
    	
    	
    }
    
    @Override
    protected boolean isRouteDisplayed() { return false; }

	
}
