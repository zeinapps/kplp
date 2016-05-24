package zein.apps.kplp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TheService extends Service {
	int counter = 0;
	static final int UPDATE_INTERVAL = 60000; //1 menit
	private Timer timer = new Timer();
	NotificationManager nman;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		doSomethingRepeatedly();
		return START_STICKY;
	}

	private void doSomethingRepeatedly() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				
				new cek_laporan(readFromFile("userid")).execute();
				
			}
		}, 0, UPDATE_INTERVAL);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
//		Toast.makeText(this, "The Service Destroyed", Toast.LENGTH_LONG).show();
	}
	
	public class cek_laporan extends AsyncTask<String, Integer, String> {
		
		String userid;
		
		public cek_laporan(String user_id) {
			this.userid = user_id;
	    }
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	       
	    }
	    @Override
	    protected String  doInBackground(String... arg0) {
	    	ServiceHandler sh = new ServiceHandler();
	        String StringJson = null;
	        
	        StringJson = sh.makeServiceCall(Config.CEKLAPORAN +"/"+ this.userid +"?random="+new Random() , ServiceHandler.GET);
	        
			if(!sh.getMessageError().equals("")){
				return null;
			}
	        return StringJson;
	    }
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        
	        try {
	        	JSONObject json = new JSONObject(result);
	        	String data = json.getString("data");
	        	int total =  new JSONObject(data).getInt("total");
	        	if (total > 0){
	        		nman=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    		Long when=System.currentTimeMillis();
		    		Notification n;
			    	Context context=getApplicationContext();
			    	String Title1= total + " Laporan Baru";
			    	String Text1="Ada "+ total +" laporan baru belum dilihat";
			    	Intent notifintent=new Intent(context,LaporanActivity.class);
			    	notifintent.putExtra("default_url", Config.CEKLAPORAN +"/"+ this.userid );
			    	notifintent.putExtra("title_activity", Title1 );
			    	PendingIntent pending;
			    	pending=PendingIntent.getActivity(context,0,notifintent,android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
			    	n=new Notification(R.drawable.ic_launcher,Title1,when);
			    	n.flags=Notification.FLAG_AUTO_CANCEL;
			    	n.setLatestEventInfo(context, Title1, Text1, pending);
			    	nman.notify(0,n);
	        	}
	            
			} catch (Exception e) {
				
			}
	        
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
	
}