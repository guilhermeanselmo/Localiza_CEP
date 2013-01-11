package com.example.buscacep;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.buscacep.MESSAGE";
	private ProcuraCEP procuraCEP;
	private ArrayList<String> dadosCEP;
	private boolean conexao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		conexao = verificaConexao();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@SuppressLint("NewApi")
	public void buscarCEP(View view)
	{
		if (conexao)
		{
			String numeroCep;
			this.dadosCEP = new ArrayList<String>();
			EditText cep = (EditText) findViewById(R.id.caixaCEP);
			numeroCep = cep.getText().toString();

			if (numeroCep.isEmpty() || numeroCep.equalsIgnoreCase(" "))
			{
				Toast.makeText(this, "Campo CEP vazio.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				try {
					int cepInt = Integer.parseInt(numeroCep);
					procuraCEP = new ProcuraCEP(cepInt);

					if(procuraCEP.tamanhoCEP())
					{
						procuraCEP.start();

						/* Aguardando a Thread finalizar a execução para capturar os dados do CEP ou 
						 * o Null retornado*/ 
						while(procuraCEP.isAlive())
						{}

						this.dadosCEP = procuraCEP.getDadosCEP();

						if (this.dadosCEP == null)
						{
							Toast.makeText(this, "CEP inexistente!", Toast.LENGTH_SHORT).show();
						}
						else
						{
							Intent intentEndereco = new Intent("ENDLOCAL");
							intentEndereco.putStringArrayListExtra(EXTRA_MESSAGE, dadosCEP);
							startActivity(intentEndereco);
						}
					}
					else
						Toast.makeText(this, "CEP incorreto!", Toast.LENGTH_SHORT).show();
				} catch (NumberFormatException nfe) {
					Toast.makeText(this, "Digite apenas números.", Toast.LENGTH_SHORT).show();
				}
			}
		}
		else
			Toast.makeText(this, "Dispositivo sem acesso à Internet!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_endereco, menu);
		return true;
	}

	public boolean verificaConexao() {
		ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nf = cn.getActiveNetworkInfo();
		if(nf != null && nf.isConnected()==true )
		{
			return true;
		}
		else
		{
			AlertDialog.Builder mensagem = new AlertDialog.Builder(MainActivity.this);
			mensagem.setTitle("Erro ao conectar à Internet.");
			mensagem.setMessage("O seu Android não está conectado à internet");
			mensagem.show();
			return false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sobre:
			Intent intentSobre = new Intent("SOBRE");
			startActivity(intentSobre);
			break;
		case R.id.sair: 
			finish();			
		}
		return super.onOptionsItemSelected(item);
	}

	public ProcuraCEP getProcuraCEP() {
		return procuraCEP;
	}

	public ArrayList<String> getDadosCEP() {
		return dadosCEP;
	}

}
