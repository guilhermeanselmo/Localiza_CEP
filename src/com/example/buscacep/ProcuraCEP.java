package com.example.buscacep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class ProcuraCEP extends Thread{

	private int cep;
	private String capturaJson, mensagem;
	private URL urlConsulta;
	private InputStreamReader iReader;
	private BufferedReader bReader;
	private JSONObject dados;
	private ArrayList<String> dadosCEP;

	public ProcuraCEP(int cep) {
		this.cep = cep;
	}
	
	public boolean tamanhoCEP()
	{
		if (this.cep > 1000000 && this.cep < 99999999)
			return true;
		else
			return false;
	}

	public void consultaCEP()
	{
		this.dadosCEP = new ArrayList<String>();

		try {
			this.urlConsulta = new URL("http://cep.correiocontrol.com.br/"+ this.cep +".json");

			mensagem = "Consulta CEP ao site dos correios feita.";
			
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

			}
			else 
			{
				mensagem = "CEP não encontrado na base dos correios";
				dadosCEP = null;
			}
			
		} catch (MalformedURLException e) {
			mensagem = "Erro ao formar URL consulta CEP" + e.getMessage();
			dadosCEP = null;
		} catch (IOException io) {
			io.printStackTrace();
			mensagem = "Erro ao ler fluxo de entrada. Mensagem: " + io.getMessage();
			dadosCEP = null;
		} catch (JSONException ej) {
			ej.printStackTrace();
			mensagem = "Erro ao manipular o arquivo em JSON. Mensagem: " + ej.getMessage();
			dadosCEP = null;
		}
		
		Log.d("Busca CEP", mensagem);
	}

	@Override
	public void run() {
		consultaCEP();
	}

	public ArrayList<String> getDadosCEP() {
		return dadosCEP;
	}

}
