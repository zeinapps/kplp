package zein.apps.kplp;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
 
public class UploadActivity extends Activity {
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

	public static final int IMAGE_FILE = 322;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri;
	
//    private ProgressBar progressBar;
    private String filePath = "";
    private ImageView imgPreview;
    private Button btnUpload, btnCancel, btnLampiran;
    long totalSize = 0;
 

    private EditText txtNama;
    private EditText txtHP;
    private EditText txtEmail;
    private EditText txtLokasiPelapor;
    private EditText txtLokasiKejadian;
    private EditText txtLat;
    private EditText txtLong;
    private EditText txtDeskripsi;
    private Spinner spin_jenis;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnLampiran = (Button) findViewById(R.id.btnLampiran);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        
        txtNama = (EditText) findViewById(R.id.txt_nama);
        txtHP = (EditText) findViewById(R.id.txt_hp);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtLokasiPelapor = (EditText) findViewById(R.id.txt_lokasi_pelapor);
        txtLokasiKejadian = (EditText) findViewById(R.id.txt_lokasi_kejadian);
        txtLat = (EditText) findViewById(R.id.txt_lat);
        txtLong = (EditText) findViewById(R.id.txt_long);
        txtDeskripsi = (EditText) findViewById(R.id.txt_deskripsi);
 
        btnLampiran.setOnClickListener(new View.OnClickListener() {
        	 
            @Override
            public void onClick(View v) {
            	AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            	builder.setMessage("Pilih Sumber Gambar")
            	   .setCancelable(false)
            	   .setPositiveButton("Kamera", new DialogInterface.OnClickListener() {
            	       public void onClick(DialogInterface dialog, int id) {
            	    	   captureImage();
            	       }
            	   })
            	   .setNegativeButton("SD Card", new DialogInterface.OnClickListener() {
            	       public void onClick(DialogInterface dialog, int id) {
            	    	   Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
       	       	     	startActivityForResult(galleryIntent, IMAGE_FILE);
            	       }
            	   });
            	AlertDialog alert = builder.create();
            	alert.show();
            }
        });
        
        btnUpload.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // uploading the file to server
            	GPSTracker tracker = new GPSTracker(UploadActivity.this);
                if (tracker.canGetLocation()) {
                	txtLat.setText(tracker.getLatitude()+"");
                    txtLong.setText(tracker.getLongitude()+"");
                }
            	if(is_valid_input()){
            		new UploadFileToServer().execute();
            	}else{
            		showAlert2("Periksa kembali inputan");
            	}
                
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
        	 
            @Override
            public void onClick(View v) {
                // uploading the file to server
            	finish();
            }
        });
        
        txtEmail.setText(getEmail(this));
        
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            txtLat.setText(tracker.getLatitude()+"");
            txtLong.setText(tracker.getLongitude()+"");
        }
        
        spin_jenis = (Spinner) findViewById(R.id.spin_jenis);
    	List<String> list = new ArrayList<String>();
    	list.add("Kecelakaan Kapal");
    	list.add("Kecelakaan Laut");
    	list.add("Pencemaran");
    	list.add("Perompakan");
    	list.add("Orang Tenggelam");
    	list.add("Alat Sarana Bantu Navigasi Hilang/Rusak");
    	list.add("Lain-lain");
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    		android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spin_jenis.setAdapter(dataAdapter);
    	
    	if(!readFromFile("namapelapor").equals("")){
    		txtNama.setText(readFromFile("namapelapor"));
    	}
    	
    	if(!readFromFile("emailpelapor").equals("")){
    		txtEmail.setText(readFromFile("emailpelapor"));
    	}
    	
    	if(!readFromFile("hppelapor").equals("")){
    		txtHP.setText(readFromFile("hppelapor"));
    	}
    	
        
    }
 
    /**
     * Displaying captured image/video on the screen
     * */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);
        }
    }
 
    /**
     * Uploading the file to server
     * */
    ProgressDialog pDialog;
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            pDialog = new ProgressDialog(UploadActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setCancelable(false);
	        pDialog.show();
        }
 
        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
//            progressBar.setVisibility(View.VISIBLE);
 
            // updating progress bar value
//            progressBar.setProgress(progress[0]);
 
            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }
 
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
 
        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
 
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);
 
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new ProgressListener() {
 
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                if(!filePath.equals("")){
                	File sourceFile = new File(filePath);
                    entity.addPart("image", new FileBody(sourceFile));
                }
                
 
            	
            	
                
                String nama = txtNama.getText().toString();
              	String email = txtEmail.getText().toString();
              	String hp = txtHP.getText().toString();
              	String jenis = String.valueOf((spin_jenis.getSelectedItemPosition()+1));
              	String lokasi_pelapor = txtLokasiPelapor.getText().toString();
              	String lokasi_kejadian = txtLokasiKejadian.getText().toString();
              	String lat = txtLat.getText().toString();
              	String longitude = txtLong.getText().toString();
              	String deskripsi = txtDeskripsi.getText().toString();
                

              	writeToFile("namapelapor", nama);
              	writeToFile("hppelapor", hp);
              	writeToFile("emailpelapor", email);
              	
                // Extra parameters if you want to pass to server
                entity.addPart("lat", new StringBody(lat));
                entity.addPart("long", new StringBody(longitude));
                entity.addPart("hp", new StringBody(hp));
                entity.addPart("jenis", new StringBody(jenis));
                entity.addPart("nama", new StringBody(nama));
                
                if(!email.equals("")){
                	entity.addPart("email_pelapor", new StringBody(email));
                }
                
                entity.addPart("lokasi_pelapor", new StringBody(lokasi_pelapor));
                entity.addPart("lokasi_kejadian", new StringBody(lokasi_kejadian));
                entity.addPart("deskripsi", new StringBody(deskripsi));
 
                totalSize = entity.getContentLength();
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
            Log.e(TAG, "Response from server: " + result);
            if (pDialog.isShowing())
                pDialog.dismiss();
 
            // showing the server response in an alert dialog
            showAlert("Laporan Telah Terkirim");
 
            super.onPostExecute(result);
        }
 
    }
 
    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void showAlert2(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Validate")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private boolean isValidEmail(CharSequence email) {
	    if (!TextUtils.isEmpty(email)) {
	        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
	    }
	    return false;
	}

	private boolean isValidPhoneNumber(CharSequence phoneNumber) {
	    if (!TextUtils.isEmpty(phoneNumber)) {
	        return Patterns.PHONE.matcher(phoneNumber).matches();
	    }
	    return false;
	}
	
	static String getEmail(Context context) {
	    AccountManager accountManager = AccountManager.get(context); 
	    Account account = getAccount(accountManager);

	    if (account == null) {
	      return null;
	    } else {
	      return account.name;
	    }
	  }

	  private static Account getAccount(AccountManager accountManager) {
	    Account[] accounts = accountManager.getAccountsByType("com.google");
	    Account account;
	    if (accounts.length > 0) {
	      account = accounts[0];      
	    } else {
	      account = null;
	    }
	    return account;
	  }
	  
	  public boolean isValidLatLng(double lat, double lng){
		    if(lat < -90 || lat > 90)
		    {
		        return false;
		    }
		    else if(lng < -180 || lng > 180)
		    {
		        return false;
		    }
		    return true;
		}
	  
	  boolean is_valid_input(){

	    String nama = txtNama.getText().toString();
      	String email = txtEmail.getText().toString();
      	String hp = txtHP.getText().toString();
      	String lokasi_pelapor = txtLokasiPelapor.getText().toString();
      	String lokasi_kejadian = txtLokasiKejadian.getText().toString();
      	String lintang = txtLat.getText().toString();
      	String bujur = txtLong.getText().toString();
      	
     
      	if( nama.equals("") || 
  			!isValidPhoneNumber(hp)  || 
  			lokasi_pelapor.equals("") || 
  			lokasi_kejadian.equals("") || 
  			lintang.equals("") || 
  			bujur.equals("") 
      			){
      		return false;
      	}
		  
      	try {
      		double lat = Double.parseDouble(txtLat.getText().toString());
          	double longitude = Double.parseDouble(txtLong.getText().toString());
          	if(!isValidLatLng(lat, longitude) ){
          		return false;
          	}
		} catch (Exception e) {
			return false;
		}
      	
      	
      	
		  return true;
	  }
	  
	  
	  
	  
	  
	  
	  
	  

		private void captureImage() {
	        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	  
	        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
	  
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	  
	        // start the image capture Intent
	        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	    }

		public Uri getOutputMediaFileUri(int type) {
	        return Uri.fromFile(getOutputMediaFile(type));
	    }
	 
	 private static File getOutputMediaFile(int type) {
		  
	        // External sdcard location
	        File mediaStorageDir = new File(
	                Environment
	                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
	                Config.IMAGE_DIRECTORY_NAME);
	  
	        // Create the storage directory if it does not exist
	        if (!mediaStorageDir.exists()) {
	            if (!mediaStorageDir.mkdirs()) {
	                Log.d(TAG, "Oops! Failed create "
	                        + Config.IMAGE_DIRECTORY_NAME + " directory");
	                return null;
	            }
	        }
	  
	        // Create a media file name
	        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
	                Locale.getDefault()).format(new Date());
	        File mediaFile;
	        if (type == MEDIA_TYPE_IMAGE) {
	            mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                    + "IMG_" + timeStamp + ".jpg");
	        } else if (type == MEDIA_TYPE_VIDEO) {
	            mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                    + "VID_" + timeStamp + ".mp4");
	        } else {
	            return null;
	        }
	  
	        return mediaFile;
	    }
	 
	 
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        // if the result is capturing Image
		 
		 
		 
	        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
	            if (resultCode == RESULT_OK) {
	            	filePath = fileUri.getPath();
	                boolean isImage = true;
	                if (filePath != null) {
	                    previewMedia(isImage);
	                } else {
	                    Toast.makeText(getApplicationContext(),
	                            "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
	                }
	            } else if (resultCode == RESULT_CANCELED) {
	                 
	                // user cancelled Image capture
	                Toast.makeText(getApplicationContext(),
	                        "User cancelled image capture", Toast.LENGTH_SHORT)
	                        .show();
	             
	            } else {
	                // failed to capture image
	                Toast.makeText(getApplicationContext(),
	                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
	                        .show();
	            }
	         
	        } else if(requestCode == IMAGE_FILE && resultCode == RESULT_OK) {
	    		Uri selectedImageUri = data.getData();
	            filePath = getPath(selectedImageUri);
	            boolean isImage = true;
                if (filePath != null) {
                    previewMedia(isImage);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
                }
	    	}
	    }
	 public String getPath(Uri uri) {
	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = managedQuery(uri, projection, null, null, null);
	        if (cursor != null) {

	            int column_index = cursor
	                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	            cursor.moveToFirst();
	            return cursor.getString(column_index);
	        } else
	            return null;
	    }
	    
	 
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	  
	        // save file url in bundle as it will be null on screen orientation
	        // changes
	        outState.putParcelable("file_uri", fileUri);
	    }
	  
	    @Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	        super.onRestoreInstanceState(savedInstanceState);
	  
	        // get the file url
	        fileUri = savedInstanceState.getParcelable("file_uri");
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
		
	  
}