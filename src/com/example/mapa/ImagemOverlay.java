package com.example.mapa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ImagemOverlay extends Overlay {
	private int idImagem;
	private GeoPoint ponto;
	
	public ImagemOverlay(int imagemID, GeoPoint ponto) {
		this.idImagem = imagemID;
		this.ponto = ponto;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		Point p = mapView.getProjection().toPixels(ponto, null);
		Bitmap bmp = BitmapFactory.decodeResource(mapView.getResources(), this.idImagem);
		canvas.drawBitmap(bmp, p.x, p.y-30, new Paint());
	}
	
}
