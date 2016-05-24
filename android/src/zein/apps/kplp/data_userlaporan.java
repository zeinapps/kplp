package zein.apps.kplp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;

public class data_userlaporan {
	ArrayList<user_laporan> user_laporan;
	
	public data_userlaporan(String stringjson){
		
		JSONObject data = null;
		user_laporan = new ArrayList<user_laporan>();
        
		try {
			JSONArray json = new JSONArray(stringjson);
//			 data = json.getJSONObject("data");
			 for (int i = 0; i < json.length(); i++) {
			 JSONObject item_laporan = json.getJSONObject(i);
				String userid = item_laporan.getString("user_id");
				String name = item_laporan.getString("name");
				String email = item_laporan.getString("email");
				String laporanid = item_laporan.getString("laporan_id");
				String statusid = item_laporan.getString("status_id");
				String waktu_mulai = item_laporan.getString("waktu_mulai");
				String waktu_selesai = item_laporan.getString("waktu_selesai");
				
				user_laporan.add(new user_laporan(userid, name, email, laporanid, statusid, waktu_mulai, waktu_selesai));
			 }
		} catch (Exception e) {
			Log.d("EROR",e.getMessage());
		}
		

	}
	
	
	public ArrayList<user_laporan> getuserlaporan(){
		return this.user_laporan;
	}
	
	
}
