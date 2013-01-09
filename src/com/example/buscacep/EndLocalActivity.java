package com.example.buscacep;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

@SuppressLint("NewApi")
public class EndLocalActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.buscacep.MESSAGE";
	private ArrayList<String> dadosCEP;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_endereco);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intentEndereco = getIntent();
		dadosCEP = intentEndereco.getStringArrayListExtra(MainActivity.EXTRA_MESSAGE);
		
		ArrayList<TextView> viewEnderecos = new ArrayList<TextView>();
		ArrayList<TextView> viewLabel = new ArrayList<TextView>();
		
		/* 
		 * Referenciando campos à serem escritos na Activity que apresenta os dados de
		 * determinado endereço.
		*/
		viewEnderecos.add((TextView) findViewById(R.id.editLogradouro));
		viewEnderecos.add((TextView) findViewById(R.id.editBairro));
		viewEnderecos.add((TextView) findViewById(R.id.editCidade));
		viewEnderecos.add((TextView) findViewById(R.id.editEstado));
		
		// Alguns Cep's não retornam logradouro e bairro somente cidade e estado.
		viewLabel.add((TextView) findViewById(R.id.textLogradouro));
		viewLabel.add((TextView) findViewById(R.id.textBairro));
		
		for (int i = 0; i < viewEnderecos.size(); i++)
		{
			// Verificando se campos estão vazios para não exibir o Label correspondente.
			if (dadosCEP.get(i).equalsIgnoreCase("") || dadosCEP.get(i).equalsIgnoreCase(" "))
				viewLabel.get(i).setText("");
			else
				viewEnderecos.get(i).setText(dadosCEP.get(i));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_endereco, menu);
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
	
	public void marcaMapa(View view)
	{
		Intent intentMarca = new Intent("MAPA");
		Log.d("Busca CEP", "Botão Mapa pressionado.");
		intentMarca.putStringArrayListExtra(EXTRA_MESSAGE, dadosCEP);
		startActivity(intentMarca);
	}

}
