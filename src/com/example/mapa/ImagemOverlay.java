package com.example.mapa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

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
		RectF retangulo = new RectF(p.x, p.y, p.x+bmp.getWidth(), p.y+bmp.getHeight());
		canvas.drawBitmap(bmp, null, retangulo, new Paint());
	}
	
}
