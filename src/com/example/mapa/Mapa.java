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

		double latitude, longitude;		
		Intent intentLocal = getIntent();
		
		// Recebendo a activity EndLocalActivity as informações de endereço referente ao CEP
		ArrayList<String> dadosCEP = intentLocal.getStringArrayListExtra
				(EndLocalActivity.EXTRA_MESSAGE);
		
		/*
		 * Componente da API de Localização responsável por retornar informações geográficas
		 * (latitude/longitude) de acordo com uma determinada descrição de localização.
		 */
		Geocoder geocoder = new Geocoder(this);
				
		String local;
		
		// Alguns CEP não retornam logradouro somente a cidade e o estado.
		if (dadosCEP.get(0).equalsIgnoreCase("") || dadosCEP.get(0).equalsIgnoreCase(" "))
			local = dadosCEP.get(2);
		else
			local = dadosCEP.get(0) + ", " + dadosCEP.get(2);

		try {

			// Capturando as informações geográficas de determinado endereço.
			Address localidade = geocoder.getFromLocationName(local, 1).get(0);

			if (localidade != null) 
			{
				latitude = localidade.getLatitude();
				longitude = localidade.getLongitude();
				mapa = (MapView) findViewById(R.id.mapa);
				mapa.setSatellite(true);

				/*
				 * Instânciando objeto com a imagem e a referência do ponto para determinado 
				endereço no mapa.
				*/
				ImagemOverlay imaOverlay = new ImagemOverlay(R.drawable.marcadorf, 
						new Ponto(latitude, longitude));
				
				// Adicionando imagem de marcador o mapa
				mapa.getOverlays().add(imaOverlay);
				
				mapa.getController().animateTo(new Ponto(latitude, longitude));
				mapa.getController().setZoom(16);
				mapa.setBuiltInZoomControls(true);
				
			}
			else
			{
				Log.d("Mapa marca CEP", "Localidade não encontrada no mapa para este CEP.");
			}
		} catch (IOException e) {
			Log.d("Mapa marca CEP", "Não foi possível obter localidade. Mensagem: " + e.getMessage());
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// Modificando visão do mapa de Satélite para Mapa
		if(keyCode == KeyEvent.KEYCODE_S) {
			mapa.setSatellite(true);
		} else if (keyCode == KeyEvent.KEYCODE_R) {
			mapa.setSatellite(false);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
