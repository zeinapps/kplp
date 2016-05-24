package zein.apps.kplp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class data_listlaporan {
	ArrayList<list_laporan> list_laporan;
	
	public data_listlaporan(String stringjson){
		
		JSONArray data = null;
		list_laporan = new ArrayList<list_laporan>();
        
		try {
			JSONObject json = new JSONObject(stringjson);
			data = json.getJSONArray("data");
			for (int i = 0; i < data.length(); i++) {
				
				
				JSONObject item_laporan = data.getJSONObject(i);
				String id = item_laporan.getString("id");
				String nama = item_laporan.getString("nama");
				String foto = item_laporan.getString("foto");
				String status = item_laporan.getString("status");
				String email_pelapor = item_laporan.getString("email_pelapor");
				String jenis = item_laporan.getString("jenis");
				String waktu = item_laporan.getString("waktu");
				String lokasi = item_laporan.getString("lokasi_kejadian");
				
				list_laporan.add(new list_laporan(id, nama, foto, status, email_pelapor, jenis, waktu, lokasi));
			}
		} catch (Exception e) {
			Log.d("EROR",e.getMessage());
		}
		

	}
	
	
	public ArrayList<list_laporan> getlist_laporan(){
		return this.list_laporan;
	}
	
	
}
