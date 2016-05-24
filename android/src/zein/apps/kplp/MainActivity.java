package zein.apps.kplp;

import java.io.BufferedReader;
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
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	 private Button btn_laporkan, btn_login, btn_profil, btn_emergency;

    private EditText txtEmail;
    private EditText txtPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn_laporkan = (Button) findViewById(R.id.btn_laporkan);
		btn_laporkan.setOnClickListener(new View.OnClickListener() {
	  
	            @Override
	            public void onClick(View v) {
	                // capture picture
	            	
	            	if(!cek_status_internet(MainActivity.this)){
	            		showAlert("Connect Internet Please");
	            		return;
	            	}
	            	
	            	
	            	Intent i = new Intent(MainActivity.this, UploadActivity.class);
	    	        startActivity(i);
	            	
	            }
	        });
        
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setText("Rekap Laporan");
		
		
		btn_login.setOnClickListener(new View.OnClickListener() {
			  
            @Override
            public void onClick(View v) {
            	
            	if(!cek_status_internet(MainActivity.this)){
            		showAlert("Connect Internet Please");
            		return;
            	}
            	
            	if(!readFromFile("userid").equals("")){
            		Intent i = new Intent(MainActivity.this, AdminActivity.class);
        	        startActivity(i);
        	        return;
            	}
    	        
            	LayoutInflater li = LayoutInflater.from(MainActivity.this);
				View promptsView = li.inflate(R.layout.login, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

				// set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);

				txtEmail = (EditText) promptsView.findViewById(R.id.txt_login_email);
				txtPassword = (EditText) promptsView.findViewById(R.id.txt_login_password);
				
				// set dialog message
				alertDialogBuilder
					.setCancelable(false)
					.setTitle("Login")
					.setPositiveButton("Login",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
					    	new Login().execute();
					    }
					  })
					.setNegativeButton("Batal",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					    }
					  });

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
            }
        });
		
		btn_profil = (Button) findViewById(R.id.btn_profil);
		btn_profil.setOnClickListener(new View.OnClickListener() {
			  
            @Override
            public void onClick(View v) {
                // capture picture
            	Intent i = new Intent(MainActivity.this, ProfilActivity.class);
    	        startActivity(i);
//            	displayfilepdf();
            }
        });
		
		btn_emergency = (Button) findViewById(R.id.btn_menu_emergency);
		btn_emergency.setOnClickListener(new View.OnClickListener() {
			  
            @Override
            public void onClick(View v) {
                // capture picture
            	Intent i = new Intent(MainActivity.this, EmergencyActivity.class);
    	        startActivity(i);
            	
            }
        });
		
		if(!readFromFile("userid").equals("")){
    		if(!isMyServiceRunning(TheService.class)){
    			startService(new Intent(getBaseContext(), TheService.class));
    		}
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
	
	ProgressDialog pDialog;
	private class Login extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setCancelable(false);
	        pDialog.show();
        }
 
        @Override
        protected void onProgressUpdate(Integer... progress) {
            
        }
 
        @Override
        protected String doInBackground(Void... params) {
            return login();
        }
 
        @SuppressWarnings("deprecation")
        private String login() {
            String responseString = null;
 
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.LOGIN_URL);
 
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                
                            }
                        });
 
 
              	String email = txtEmail.getText().toString();
              	String password = txtPassword.getText().toString();
                
                // Extra parameters if you want to pass to server
                entity.addPart("email", new StringBody(email));
                entity.addPart("password", new StringBody(password));
 
//                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
 
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
 
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
 
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
 
            return responseString;
 
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if (pDialog.isShowing())
                pDialog.dismiss();
            try {
            	Log.d("response",result);
            	JSONObject json = new JSONObject(result);
                int status = json.getInt("status");
                JSONArray message = json.getJSONArray("message");
                String pesan = message.getString(0);
                if(status == 1){
                    JSONObject data = json.getJSONObject("data");
                	String userid = data.getString("id");
                	String nama = data.getString("name");
                	String hp = data.getString("hp");
                	String email = data.getString("email");
                	String jabatan = data.getString("jabatan");
                	writeToFile("userid", userid);
                	writeToFile("nama", nama);
                	writeToFile("myprofil_nama", nama);
                	writeToFile("myprofil_hp", hp);
                	writeToFile("myprofil_email", email);
                	writeToFile("myprofil_jabatan", jabatan);
                	startService(new Intent(getBaseContext(), TheService.class));
                	
                	Intent i = new Intent(MainActivity.this, AdminActivity.class);
        	        startActivity(i);
                }else{
                	showAlert(pesan);
                }
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				showAlert("Terjadi kesalahan");
			}
            
 
            super.onPostExecute(result);
        }
 
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
	
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	

	void displayfilepdf(){
		 
		 String fileneme = "profil.pdf";
			String folder = "profil_kplp";
			File fileBrochure = new File("/sdcard/"+folder+"/"+fileneme);
		    if (!fileBrochure.exists())
		    {
		         CopyAssetsbrochure(folder,fileneme);
		    } 

		    /** PDF reader code */
		    File file = new File("/sdcard/"+folder+"/"+fileneme);        

		    Intent intent = new Intent(Intent.ACTION_VIEW);
		    intent.setDataAndType(Uri.fromFile(file),"application/pdf");
		    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    
		    try 
		    {
		        getApplicationContext().startActivity(intent);
		    } 
		    catch (ActivityNotFoundException e) 
		    {
		         Toast.makeText(MainActivity.this, "NO Pdf Viewer", Toast.LENGTH_SHORT).show();
		    }
		    
	 }
	
	private void CopyAssetsbrochure(String folder, String filename) {
	     AssetManager assetManager = getAssets();
	     String[] files = null;
	     try 
	     {
	         files = assetManager.list(folder);
	     } 
	     catch (IOException e)
	     {
	         Log.e("tag", e.getMessage());
	     }
	     createDirIfNotExists(folder);
	     for(int i=0; i<files.length; i++)
	     {
	         String fStr = files[i];
	         if(fStr.equalsIgnoreCase(filename))
	         {
	             InputStream in = null;
	             OutputStream out = null;
	             try 
	             {
	               in = assetManager.open(folder+"/"+files[i]);
	               out = new FileOutputStream("/sdcard/"+ folder +"/"+ files[i]);
	               copyFile(in, out);
	               in.close();
	               in = null;
	               out.flush();
	               out.close();
	               out = null;
	               break;
	             } 
	             catch(Exception e)
	             {
	                 Log.e("tag", e.getMessage());
	             } 
	         }
	     }
	 }

	private void copyFile(InputStream in, OutputStream out) throws IOException {
	     byte[] buffer = new byte[1024];
	     int read;
	     while((read = in.read(buffer)) != -1){
	       out.write(buffer, 0, read);
	     }
	 }

	public boolean createDirIfNotExists(String path) {
		    boolean ret = true;
		    String[] paths = path.split("/");
		    String pathgab = "";
		    for (String string : paths) {
		    	pathgab += string+"/";
		    	File file = new File(Environment.getExternalStorageDirectory(), pathgab);
			    if (!file.exists()) {
			        if (!file.mkdirs()) {
			            Log.e("TravellerLog :: ", "Problem creating Image folder");
			            ret = false;
			        }
			    }
			}
		    
		    return ret;
		}

	public boolean cek_status_internet(Context cek) {
    	ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo info = cm.getActiveNetworkInfo();

		if (info != null && info.isConnected())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
