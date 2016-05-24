package zein.apps.kplp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;

public class data_detillaporan {
	ArrayList<detil_laporan> detil_laporan;
	
	public data_detillaporan(String stringjson){
		
		JSONObject data = null;
		detil_laporan = new ArrayList<detil_laporan>();
        
		try {
			JSONObject json = new JSONObject(stringjson);
			 data = json.getJSONObject("data");
			 
				String id = data.getString("id");
				String hp = data.getString("hp");
				String lokasi_pelapor = data.getString("lokasi_pelapor");
				String lokasi_kejadian = data.getString("lokasi_kejadian");
				String nama = data.getString("nama");
				String lat = data.getString("lat");
				String lng = data.getString("long");
				String deskripsi = data.getString("deskripsi");
				String foto = data.getString("foto");
				String status = data.getString("status");
				String jenis = data.getString("jenis");
				String email_pelapor = data.getString("email_pelapor");
				String waktu = data.getString("waktu");
				String user = data.getString("user");
				
				detil_laporan.add(new detil_laporan(id, hp, lokasi_pelapor, lokasi_kejadian, nama, lat, lng, deskripsi, foto, status,jenis, email_pelapor, waktu, user));
			
		} catch (Exception e) {
			Log.d("EROR",e.getMessage());
		}
		

	}
	
	
	public ArrayList<detil_laporan> getdetilbarang(){
		return this.detil_laporan;
	}
	
	
}
