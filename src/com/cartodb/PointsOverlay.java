package com.cartodb;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;



public class PointsOverlay extends ItemizedOverlay {

	public PointsOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	List<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
	
	public void addOverlay(OverlayItem overlay)
	{
		mapOverlays.add(overlay);
		populate();
	}
	@Override
	protected OverlayItem createItem(int i)
	{
		// TODO Auto-generated method stub
		return mapOverlays.get(i);
	}

	@Override
	public int size()
	{
		return mapOverlays.size();
	}

}
