package zein.apps.kplp;

import java.io.BufferedReader;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import android.os.BatteryManager;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
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
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProfilActivity extends Activity {
	private static final String TAG = ProfilActivity.class.getSimpleName();
	private ImageView iv;
   private Matrix matrix = new Matrix();
   private float scale = 1f;
   private ScaleGestureDetector SGD;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profil);
		

	      iv=(ImageView)findViewById(R.id.imageView1);
	      SGD = new ScaleGestureDetector(this,new ScaleListener());
		
		
//		this.webview = (WebView) findViewById(R.id.webView1);
//
//        WebSettings settings = webview.getSettings();
//        settings.setJavaScriptEnabled(true);
//        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//
//        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//
//        progressBar = ProgressDialog.show(ProfilActivity.this, "Harap Tunggu", "Loading...");
//
//        webview.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i(TAG, "Processing webview url click...");
//                view.loadUrl(url);
//                return true;
//            }
//
//            public void onPageFinished(WebView view, String url) {
//                Log.i(TAG, "Finished loading URL: " + url);
//                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//                }
//            }
//
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Log.e(TAG, "Error: " + description);
//                Toast.makeText(ProfilActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
//                alertDialog.setTitle("Error");
//                alertDialog.setMessage(description);
//                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        return;
//                    }
//                });
//                alertDialog.show();
//            }
//        });
//        String pdf = Config.DOMAIN+"/profil.pdf";
//        webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);

	}

	private class ScaleListener extends ScaleGestureDetector.
	   
	   SimpleOnScaleGestureListener {
	      @Override
	      public boolean onScale(ScaleGestureDetector detector) {
	         scale *= detector.getScaleFactor();
	         scale = Math.max(0.1f, Math.min(scale, 5.0f));
	         
	         matrix.setScale(scale, scale);
	         iv.setImageMatrix(matrix);
	         return true;
	      }
	   }
	
	public class AppWebViewClients extends WebViewClient {
	     private ProgressBar progressBar;

	    public AppWebViewClients(ProgressBar progressBar) {
	        this.progressBar=progressBar;
	        progressBar.setVisibility(View.VISIBLE);
	    }
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        // TODO Auto-generated method stub
	        view.loadUrl(url);
	        return true;
	    }

	    @Override
	    public void onPageFinished(WebView view, String url) {
	        // TODO Auto-generated method stub
	        super.onPageFinished(view, url);
	        progressBar.setVisibility(View.GONE);
	    }
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
	
	
}
