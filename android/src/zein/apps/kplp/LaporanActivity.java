package zein.apps.kplp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LaporanActivity extends Activity {

	ListView listview ;
	View v_footer;
	int curentpage = 1;
	String URL = Config.LAPORAN;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.laporan_list);
		Bundle extras = getIntent().getExtras();
		URL = extras.getString("default_url");
		setTitle(extras.getString("title_activity"));
		listview = (ListView) findViewById(R.id.listView);
		v_footer =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.progress_list, null, false);
        
		new loadMoreListView(URL,curentpage,true).execute();
		
	}
	
	 
	Adapter_listlaporan adapter_listbarang = null;
	View footer_progress;
	private final int AUTOLOAD_THRESHOLD = 1;
    private  int MAXIMUM_ITEMS = 50;
    private boolean mIsLoading = true;
    private boolean mMoreDataAvailable = true;
	public class loadMoreListView extends AsyncTask<String, Integer, String> {
		boolean isfirst = true;
		int page;
		
		
		public loadMoreListView(String URL, int page, boolean is_first) {
			this.isfirst = is_first;
			this.page = page;
	    }
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        
	        if(this.page == 1){
	        	if(this.isfirst == true){
	        		pDialog = new ProgressDialog(LaporanActivity.this);
			        pDialog.setMessage("Harap Tunggu...");
			        pDialog.setCancelable(false);
			        pDialog.show();
	        	}
	        	
	        }else{
	        	listview.addFooterView(v_footer);
	        }
	    }
	    @Override
	    protected String  doInBackground(String... arg0) {
	    	ServiceHandler sh = new ServiceHandler();
	        String StringJson = null;
	        
	        StringJson = sh.makeServiceCall(URL + "?page="+this.page+"&random="+new Random(), ServiceHandler.GET);
	        
			if(!sh.getMessageError().equals("")){
				return null;
			}
	        return StringJson;
	    }
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        if (pDialog.isShowing())
	            pDialog.dismiss();
	        
	        JSONObject json = null;
	        String data = "";
	        int total = 0;
	        try {
	        	json = new JSONObject(result);
	        	data = json.getString("data");
	        	total =  new JSONObject(data).getInt("total");
			} catch (Exception e) {
				// TODO: handle exception
			}
	        
	        if(MAXIMUM_ITEMS > total){
	        	MAXIMUM_ITEMS = total;
	        }
	        
	        int currentPosition = listview.getFirstVisiblePosition();
	        data_listlaporan data_listbarang = new data_listlaporan(data);
	        ArrayList<list_laporan> list_brg = data_listbarang.getlist_laporan();
	        if(adapter_listbarang == null){
	        	adapter_listbarang = new Adapter_listlaporan(LaporanActivity.this, 0,
						list_brg);
	        }else{
	        	for (int i = 0; i < list_brg.size(); i++) {
		        	adapter_listbarang.add(list_brg.get(i));
				}
	        }

        	adapter_listbarang.notifyDataSetChanged();
	        listview.setAdapter(adapter_listbarang);
	        
			listview.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {
	                new display_detilbarang(adapter_listbarang.getItem(position).id).execute();
	            }
	        });
			curentpage++;
			listview.setOnScrollListener(new AbsListView.OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
					if (!mIsLoading && mMoreDataAvailable) {
			            if (totalItemCount >= MAXIMUM_ITEMS) {
			                mMoreDataAvailable = false;
			            } else if (totalItemCount - AUTOLOAD_THRESHOLD <= firstVisibleItem + visibleItemCount) {
			                mIsLoading = true;
			                new loadMoreListView(URL,curentpage,false).execute();
			            }
			        }
					
					
				}
			});
			if(listview.getFooterViewsCount() > 0){
				listview.removeFooterView(v_footer);
			}
			
			listview.setSelection(currentPosition);
	        mIsLoading = false;
	    }
	    
	}
	Button btnassign;
	ProgressDialog pDialog;
	String labe_finish = "Finish";
	String labe_finish_sementara = "";
	public class display_detilbarang extends AsyncTask<String, Integer, String> {
		Dialog dialog;
		String id_laporan;
		
		public display_detilbarang(String id_laporan) {
			this.id_laporan = id_laporan;
	    }
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(LaporanActivity.this);
	        pDialog.setMessage("Harap Tunggu...");
	        pDialog.setCancelable(false);
	        pDialog.show();
	    }
	    @Override
	    protected String  doInBackground(String... arg0) {
	    	ServiceHandler sh = new ServiceHandler();
	        String StringJson = null;
	        
	        StringJson = sh.makeServiceCall(Config.LAPORAN +"/"+ this.id_laporan+"/"+ readFromFile("userid"), ServiceHandler.GET);
	        
			if(!sh.getMessageError().equals("")){
				return null;
			}
	        return StringJson;
	    }
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        if (pDialog.isShowing())
	            pDialog.dismiss();
	        
	        data_detillaporan data_detilbarang = new data_detillaporan(result);
	        ArrayList<detil_laporan> list_brg = data_detilbarang.getdetilbarang();

	        
	        dialog = new Dialog(LaporanActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.detil_laporan);
            dialog.setCancelable(true);
            final String phone_number = list_brg.get(0).hp;
            final String id_laporan = list_brg.get(0).id;
        	TextView txt_nama = (TextView) dialog.findViewById(R.id.txt_detil_nama);
        	TextView txt_hp = (TextView) dialog.findViewById(R.id.txt_detil_hp);
        	TextView txt_lokasi_pelapor = (TextView) dialog.findViewById(R.id.txt_detil_lokasi_pelapor);
        	TextView txt_lokasi_kejadian = (TextView) dialog.findViewById(R.id.txt_detil_lokasi_kejadian);
        	TextView txt_lat = (TextView) dialog.findViewById(R.id.txt_detil_lat);
        	TextView txt_lng = (TextView) dialog.findViewById(R.id.txt_detil_long);
        	TextView txt_deskripsi = (TextView) dialog.findViewById(R.id.txt_detil_deskripsi);
        	TextView txt_status = (TextView) dialog.findViewById(R.id.txt_detil_status);
        	TextView txt_jenis = (TextView) dialog.findViewById(R.id.txt_detil_jenis);
        	TextView txt_email_pelapor = (TextView) dialog.findViewById(R.id.txt_detil_email);
        	TextView txt_email_waktu = (TextView) dialog.findViewById(R.id.txt_detil_waktu);
        	TextView txt_user = (TextView) dialog.findViewById(R.id.txt_list_userlap);
        	ImageView image_detil_foto = (ImageView) dialog.findViewById(R.id.img_detil_foto);
//        	ListView listviewuser = (ListView) dialog.findViewById(R.id.listViewUser); 
        	
        	txt_nama.setText(list_brg.get(0).nama);
        	
        	txt_hp.setText(list_brg.get(0).hp);
        	txt_lokasi_pelapor.setText(list_brg.get(0).lokasi_pelapor);
        	txt_lokasi_kejadian.setText(list_brg.get(0).lokasi_kejadian);
        	txt_lat.setText(list_brg.get(0).lat);
        	txt_lng.setText(list_brg.get(0).lng);
        	txt_deskripsi.setText(list_brg.get(0).deskripsi);
        	txt_status.setText(list_brg.get(0).status);
        	txt_jenis.setText(list_brg.get(0).jenis);
        	txt_email_pelapor.setText(list_brg.get(0).email_pelapor);
        	txt_email_waktu.setText(list_brg.get(0).waktu);
        	if(!list_brg.get(0).foto.equals("") && list_brg.get(0).foto != null){
        		new LoadImage(image_detil_foto).execute(list_brg.get(0).foto);
        	}
        	
        	
        	Button btncall = (Button) dialog.findViewById(R.id.btn_detil_call);
        	btncall.setOnClickListener(new View.OnClickListener() {
  			  
                @Override
                public void onClick(View v) {
                	
        	        call(phone_number);

                }
            });
        	
        	btnassign = (Button) dialog.findViewById(R.id.btn_detil_assign);
        	labe_finish_sementara = btnassign.getText().toString();
        	btnassign.setOnClickListener(new View.OnClickListener() {
  			  
                @Override
                public void onClick(View v) {
                	String id_user = readFromFile("userid");
                	if(labe_finish_sementara.equals(labe_finish)){
                		if(!id_user.equals("") && id_user != null){
                    		new finish_laporan(id_laporan, id_user ).execute();
                    	}
                	}else{
                		if(!id_user.equals("") && id_user != null){
                    		new assign_laporan(id_laporan, id_user ).execute();
                    	}
                	}
                	

                }
            });
        	if(list_brg.get(0).status.equals("3")){
        		btnassign.setEnabled(false);
        		labe_finish_sementara = labe_finish;
        		btnassign.setText(labe_finish_sementara);
        	}
        	
//        	Adapter_listuser adapter_user = null;
        	data_userlaporan data_userlaporan = new data_userlaporan(list_brg.get(0).user);
	        ArrayList<user_laporan> user_lap = data_userlaporan.getuserlaporan();
	        String users = "";
	        if(user_lap.isEmpty()){
	        	users = "Belum ada penanganan";
	        }
	        String existing_user = readFromFile("userid");
	        int nom = 0;
	        for (user_laporan object: user_lap) {
	        	nom++;
	        	users += nom +".) "+ object.name +"\n";
	        	if(existing_user.equals(object.userid)){
	        		labe_finish_sementara = labe_finish;
	        		btnassign.setText(labe_finish_sementara);
	        	}
	        }
	        txt_user.setText(users);
//        	adapter_user = new Adapter_listuser(LaporanActivity.this, 0,
//	        			user_lap);
	        
//        	listviewuser.setAdapter(adapter_user);
        	
            dialog.show();

	    }
	    
	}
	
	public class assign_laporan extends AsyncTask<String, Integer, String> {
		Dialog dialog;
		String idlaporan;
		String userid;
		
		public assign_laporan(String id_laporan,String user_id) {
			this.idlaporan = id_laporan;
			this.userid = user_id;
	    }
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(LaporanActivity.this);
	        pDialog.setMessage("Harap Tunggu...");
	        pDialog.setCancelable(false);
	        pDialog.show();
	    }
	    @Override
	    protected String  doInBackground(String... arg0) {
	    	ServiceHandler sh = new ServiceHandler();
	        String StringJson = null;
	        
	        StringJson = sh.makeServiceCall(Config.ASSIGN_LAPORAN +"/"+ this.idlaporan+"/assign/"+this.userid , ServiceHandler.GET);
	        
			if(!sh.getMessageError().equals("")){
				return null;
			}
	        return StringJson;
	    }
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        if (pDialog.isShowing())
	            pDialog.dismiss();
	        try {
	        	JSONObject json = new JSONObject(result);
	            int status = json.getInt("status");
	            JSONArray message = json.getJSONArray("message");
	            String pesan = message.getString(0);
	            if(status == 1){
	            	showAlert("Penanganan tercatat");
	            	labe_finish_sementara = labe_finish;
	        		btnassign.setText(labe_finish_sementara);
	        		
	        		
	        		listview.setAdapter(null);
                	listview = (ListView) findViewById(R.id.listView);
                	curentpage = 1;
                	adapter_listbarang = null;
                	mMoreDataAvailable = true;
                	mIsLoading = true;
                	new loadMoreListView(URL,curentpage,false).execute();
	        		
	            }else{
	            	showAlert(pesan);
	            }
			} catch (Exception e) {
				showAlert("Terjadi kesalahan");
			}
	        
	    }
	    
	}
	
	public class finish_laporan extends AsyncTask<String, Integer, String> {
		Dialog dialog;
		String idlaporan;
		String userid;
		
		public finish_laporan(String id_laporan,String user_id) {
			this.idlaporan = id_laporan;
			this.userid = user_id;
	    }
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(LaporanActivity.this);
	        pDialog.setMessage("Harap Tunggu...");
	        pDialog.setCancelable(false);
	        pDialog.show();
	    }
	    @Override
	    protected String  doInBackground(String... arg0) {
	    	ServiceHandler sh = new ServiceHandler();
	        String StringJson = null;
	        
	        StringJson = sh.makeServiceCall(Config.FINISH_LAPORAN +"/"+ this.idlaporan+"/finish/"+this.userid , ServiceHandler.GET);
	        
			if(!sh.getMessageError().equals("")){
				return null;
			}
	        return StringJson;
	    }
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        if (pDialog.isShowing())
	            pDialog.dismiss();
	        try {
	        	JSONObject json = new JSONObject(result);
	            int status = json.getInt("status");
	            JSONArray message = json.getJSONArray("message");
	            String pesan = message.getString(0);
	            if(status == 1){
	            	showAlert("Terima kasih");
	            	btnassign.setEnabled(false);
	            	
	            	listview.setAdapter(null);
                	listview = (ListView) findViewById(R.id.listView);
                	curentpage = 1;
                	adapter_listbarang = null;
                	mMoreDataAvailable = true;
                	mIsLoading = true;
                	new loadMoreListView(URL,curentpage,false).execute();
	            	
	            	
	            }else{
	            	showAlert(pesan);
	            }
			} catch (Exception e) {
				showAlert("Terjadi kesalahan");
			}
	        
	    }
	    
	}
	
	private class LoadImage extends AsyncTask<String, String, Bitmap> {
		ImageView img;
		public LoadImage(ImageView img){
			this.img = img;
		}
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           
 
        }
         protected Bitmap doInBackground(String... args) {
        	 Bitmap bitmap = null;
             try {
            	 bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
 
            } catch (Exception e) {
                  e.printStackTrace();
            }
            return bitmap;
         }
 
         protected void onPostExecute(Bitmap image) {
 
             if(image != null){
            	 this.img.setImageBitmap(image);
             }
         }
     }
	
	private void call(String hp) {
	      Intent in=new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + hp));
	      try{
	         startActivity(in);
	      }
	      
	      catch (android.content.ActivityNotFoundException ex){
	         Toast.makeText(getApplicationContext(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
	      }
	   }
	
	public String readFromFile(String namafile) {

	    String ret = "";

	    try {
	        InputStream inputStream = openFileInput(namafile+".txt");

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
	}
	
	private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Info")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
