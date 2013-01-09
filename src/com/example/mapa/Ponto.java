package com.example.mapa;

import android.location.Location;

import com.google.android.maps.GeoPoint;

public class Ponto extends GeoPoint {

	public Ponto(int latitude, int longitude) {
		super(latitude, longitude);
	}
	
	public Ponto(double latitude, double longitude) {
		this((int) (latitude*1E6), (int) (longitude*1E6));
	}
	
	public Ponto(Location location) {
		this(location.getLatitude(), location.getLongitude());
	}

}
