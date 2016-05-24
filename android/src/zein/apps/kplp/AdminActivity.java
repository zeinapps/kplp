package zein.apps.kplp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import zein.apps.kplp.AndroidMultiPartEntity.ProgressListener;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends Activity {
	private static final String TAG = AdminActivity.class.getSimpleName();
	 private Button btn_kembali, btn_logout, btn_laporan, btn_profilsaya;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		
		btn_kembali = (Button) findViewById(R.id.btn_admin_back);
		btn_logout = (Button) findViewById(R.id.btn_admin_logout);
		btn_laporan = (Button) findViewById(R.id.btn_admin_laporan);
		btn_profilsaya = (Button) findViewById(R.id.btn_admin_myprofil);
		
		btn_kembali.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	finish();
	            }
        });
		btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	stopService(new Intent(getBaseContext(), TheService.class));
            	writeToFile("userid", "");
            	writeToFile("nama", "");
            	finish();
            }
	    });
		btn_laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent i = new Intent(AdminActivity.this, LaporanActivity.class);
        		i.putExtra("default_url", Config.LAPORAN);
        		i.putExtra("title_activity", "Rekap Laporan" );
    	        startActivity(i);
            }
	    });
		
		btn_profilsaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent i = new Intent(AdminActivity.this, MyprofilActivity.class);
                startActivity(i);
            }
	    });
		
		
        
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void writeToFile(String namafile,String data) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(namafile+".txt", Context.MODE_PRIVATE));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	    	Log.d("Exception Zein",e.toString());
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}

}
