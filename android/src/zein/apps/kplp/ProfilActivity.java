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
import android.app.Dialog;
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
//	private ImageView iv;
   private Matrix matrix = new Matrix();
   private float scale = 1f;
   private ScaleGestureDetector SGD;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profil);

		TextView text1 = (TextView)findViewById(R.id.textView1);
		TextView text2 = (TextView)findViewById(R.id.textView2);
		TextView text3 = (TextView)findViewById(R.id.textView3);
		TextView text4 = (TextView)findViewById(R.id.textView4);
		Button btn_kpal_1 = (Button)findViewById(R.id.btn_kapal_1);
		Button btn_kpal_2 = (Button)findViewById(R.id.btn_kapal_2);
		Button btn_kpal_3 = (Button)findViewById(R.id.btn_kapal_3);
		Button btn_kpal_4 = (Button)findViewById(R.id.btn_kapal_4);
		
		btn_kpal_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showimage(R.drawable.armada1);
            }
        });
		
		btn_kpal_2.setOnClickListener(new View.OnClickListener() {
			  
            @Override
            public void onClick(View v) {
                showimage(R.drawable.armada2);
            }
        });
		
		btn_kpal_3.setOnClickListener(new View.OnClickListener() {
			  
            @Override
            public void onClick(View v) {
                showimage(R.drawable.armada3);
            }
        });
		
		btn_kpal_4.setOnClickListener(new View.OnClickListener() {
			  
            @Override
            public void onClick(View v) {
                showimage(R.drawable.armada4);
            }
        });
		
		
		text1.setText("MAKSUD DAN TUJUAN\n" +
				"\nProfil Pangkalan PLP tanjung Uban ini disusun dengan maksud sebagai paparan  komprehensif Pangkalan Penjagaan Laut dan Pantai Kelas II Tanjung Uban mengenai tugas  dan fungsinya, sarana prasarana dan capaian hasil serta keberhasilan pelaksanaan tugas  dari Pangkalan PLP Tanjung Uban. Profil ini dibuat dengan tujuan lebih memperkenalkan  Penjagaan Laut dan Pantai Tanjung Uban sebagai bagian dari Kementerian Perhubungan  yang memiliki peran aktif dalam menjaga stabilitas keamanan laut serta pantai maritim  Indonesia." +
				"\n");
	
		text2.setText("TUGAS POKOK DAN FUNGSI (TUPOKSI)\n" +
				"\nSesuai pasal 3 Keputusasn Menteri Perhubungan Nomor KM.65 Tahun 2002, Disebutkan  bahwa Pangkalan Penjagaan Laut dan Pantai Kelas II Tg. Uban mempunyai fungsi sebagai  berikut :  " +
				"\n1.	Penyusunan rencana, program dan evaluasi" +
				"\n2.	Pelaksanaan operasi dan penegakan peraturan dibidang pelayaran" +
				"\n3.	Pelaksanaan penyelidikan dan penyidikan tindak pidana pelayaran" +
				"\n4.	Pelaksanaan pengawasan dan penertiban kegiatan salvage dan pekerjaan bawah air, penyelaman instalasi /eksplorasi dan eksploitasi, bangunan di atas dan dibawah air" +
				"\n5.	Pemberian bantuan pencarian dan pertolongan musibah di laut dan penanggulangan kebakaran" +
				"\n6.	Pelaksanaan pengamanan dan pengawasan sarana bantu navigasi pelayaran serta penanggulangan pencemaran" +
				"\n7.	Pelaksanaan pelatihan pengawakan kapal dan instalasi" +
				"\n8.	Pelaksanaan pengadaan, pemeliharaan perbaikan dan dukungan logistik serta melaksanakan administrasi dan kerumah tanggaan" +
				"\n");
		
		text3.setText("VISI DAN MISI\n" +
				"\nVisi: Menciptakan keselamatan Transportasi yang aman dan tertib serta perlindungan  lingkungan Maritim diseluruh wilayah perairan laut dan pantai Indonesia    " +
				"\n\nMisi:  Melakukan pengawasan transportasi laut yang handal Sesuai standar nasional maupun  internasional dengan meningkatkan keselamatan, pengamanan dan ketertiban serta  menegakkan hukum dibidang pelayaran diseluruh wilayah erairan laut dan pantai Indonesia" +
				"\n");
		
		text4.setText("BATAS-BATAS WILAYAH KERJA ARMADA PLP WILAYAH BARAT KPLP TANJUNG UBAN\n");

		
		
//	      iv=(ImageView)findViewById(R.id.imageView1);
//	      SGD = new ScaleGestureDetector(this,new ScaleListener());
//		
		
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

	Dialog dialog;
	ImageView ivw;
public void showimage(int drawable){
	dialog = new Dialog(ProfilActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    dialog.setContentView(R.layout.dialog_image_layout);
    dialog.setCancelable(true);
    ivw=(ImageView) dialog.findViewById(R.id.goProDialogImage);
    ivw.setImageResource(drawable);
 
    dialog.show();
   
}
	
}
