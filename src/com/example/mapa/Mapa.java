package com.example.mapa;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.buscacep.EndLocalActivity;
import com.example.buscacep.R;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

@SuppressLint("NewApi")
public class Mapa extends MapActivity {

	private MapView mapa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intentLocal = getIntent();
		ArrayList<String> dadosCEP = intentLocal.getStringArrayListExtra
				(EndLocalActivity.EXTRA_MESSAGE);
		
		Geocoder geocoder = new Geocoder(this);
		String local;
		
		if (dadosCEP.get(0).equalsIgnoreCase("") || dadosCEP.get(0).equalsIgnoreCase(" "))
			local = dadosCEP.get(2);
		else
			local = dadosCEP.get(0) + ", " + dadosCEP.get(2);

		try {

			Address localidade = geocoder.getFromLocationName(local, 1).get(0);

			if (localidade != null) 
			{
				mapa = (MapView) findViewById(R.id.mapa);
				mapa.setSatellite(true);

				ImagemOverlay imaOverlay = new ImagemOverlay(R.drawable.marcadorf, new Ponto
						(localidade.getLatitude(), localidade.getLongitude()));
				mapa.getOverlays().add(imaOverlay);

				mapa.getController().animateTo(new Ponto(localidade.getLatitude(),
						localidade.getLongitude()));
				mapa.getController().setZoom(16);
				mapa.setBuiltInZoomControls(true);
			}
			else
			{
				Log.d("Mapa marca CEP", "Não foi possível encontrar uma localidade no mapa para este cep.");
			}
		} catch (IOException e) {
			Log.d("Mapa marca CEP", "Não foi possível obter localidade." + e.getMessage());
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mapa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.sobre:
			Intent intentSobre = new Intent("SOBRE");
			startActivity(intentSobre);
			break;
		case R.id.sair:
			finishAffinity();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_S) {
			mapa.setSatellite(true);
		} else if (keyCode == KeyEvent.KEYCODE_R) {
			mapa.setSatellite(false);
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
