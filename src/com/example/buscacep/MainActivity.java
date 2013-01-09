package com.example.buscacep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.buscacep.MESSAGE";
	private ProcuraCEP procuraCEP;
	private DadosCEP dadosCEP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void BuscarCEP(View view)
	{
		/*ProgressBar search = new ProgressBar(this);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.layouts);
		ImageView im = (ImageView) findViewById(R.id.imageView1);
		rl.removeView(im);
		rl.addView(search, 0);*/

		this.dadosCEP = new DadosCEP();
		EditText cep = (EditText) findViewById(R.id.caixaCEP);
		int cepInt = Integer.parseInt(cep.getText().toString());

		procuraCEP = new ProcuraCEP(cepInt/*, this*/);

		if(procuraCEP.TamanhoCEP())
		{
			procuraCEP.start();

			/* Aguardando a Thread finalizar a execução para capturar os dados do CEP ou 
			o Null retornado*/ 
			while(procuraCEP.isAlive())
			{}

			this.dadosCEP = procuraCEP.getDadosCEP();

			if (this.dadosCEP == null)
				Toast.makeText(this, "CEP inexistente!", Toast.LENGTH_SHORT).show();
			else
			{
				Intent intentEndereco = new Intent("ENDLOCAL");
				intentEndereco.putStringArrayListExtra(EXTRA_MESSAGE, dadosCEP);

				startActivity(intentEndereco);
			}
		}
		else
			Toast.makeText(this, "CEP incorreto!", Toast.LENGTH_SHORT).show();
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

	public ProcuraCEP getProcuraCEP() {
		return procuraCEP;
	}

	public DadosCEP getDadosCEP() {
		return dadosCEP;
	}

}