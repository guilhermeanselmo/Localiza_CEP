package com.example.buscacep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class ProcuraCEP extends Thread{

	private int cep;
	private String capturaJson;
	private URL urlConsulta;
	private InputStreamReader iReader;
	private BufferedReader bReader;
	private JSONObject dados;
	private DadosCEP dadosCEP;
	//private MainActivity mainActivity;

	public ProcuraCEP(int cep/*, MainActivity mainActivity*/) {
		this.cep = cep;
		//this.mainActivity = mainActivity;
	}

	public boolean TamanhoCEP()
	{
		if (this.cep > 1000000 && this.cep < 99999999)
			return true;
		else
			return false;
	}

	public void ConsultaCEP()
	{
		//if(TamanhoCEP())
		//{
			this.dadosCEP = new DadosCEP();

			try {
				this.urlConsulta = new URL("http://cep.correiocontrol.com.br/"+ this.cep +".json");

				Log.d("Busca CEP", "Consulta site dos Correios feita");

				// Cria um stream de entrada do conteúdo.
				this.iReader = new InputStreamReader( this.urlConsulta.openStream() );
				this.bReader = new BufferedReader( this.iReader );

				this.capturaJson = "";

				// Capturando as linhas com a resposta da consulta ao site dos correios.
				while ( this.bReader.ready()){
					this.capturaJson += this.bReader.readLine();
				}

				if (!this.capturaJson.equalsIgnoreCase("correiocontrolcep({\"erro\":true});"))
				{
					// Instanciando um objeto JSON com os dados capturados.
					this.dados = new JSONObject(this.capturaJson);

					// Gravando as informações em um objeto da classe DadosCE
					this.dadosCEP.add(this.dados.getString("logradouro"));
					this.dadosCEP.add(this.dados.getString("bairro"));
					this.dadosCEP.add(this.dados.getString("localidade"));
					this.dadosCEP.add(this.dados.getString("uf"));

					//return this.dadosCEP;
				}
				else 
				{
					Log.d("Busca CEP", "CEP não encontrado na base dos correios");
					dadosCEP = null;
				}
			} catch (MalformedURLException e) {
				Log.d("Busca CEP", "");
				dadosCEP = null;
			} catch (IOException io) {
				io.printStackTrace();
				Log.d("Busca CEP", "Erro ao ler fluxo de entrada");
				dadosCEP = null;
			} catch (JSONException ej) {
				ej.printStackTrace();
				Log.d("Busca CEP", "Erro ao manipular o arquivo em JSON");
				dadosCEP = null;
			}		

			/*if (this.dadosCEP == null)
				mainActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mainActivity, "CEP não encontrado!", Toast.LENGTH_SHORT).show();						
					}
				});
			else
				mainActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mainActivity, "CEP encontrado!", Toast.LENGTH_SHORT).show();						
					}
				});
				
		}
		else
			Toast.makeText(mainActivity, "CEP incorreto!", Toast.LENGTH_SHORT).show();
			*/

	}

	@Override
	public void run() {
		ConsultaCEP();
	}

	public DadosCEP getDadosCEP() {
		return dadosCEP;
	}

}
