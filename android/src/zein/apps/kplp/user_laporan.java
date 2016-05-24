package zein.apps.kplp;

public class user_laporan {
	public String userid = "";
	public String name = "";
	public String email = "";
	public String laporanid = "";
	public String statusid = "";
	public String waktu_mulai = "";
	public String waktu_selesai = "";

	public user_laporan (
			String userid, 
			String name, 
			String email, 
			String laporanid, 
			String statusid , 
			String waktu_mulai , 
			String waktu_selesai 
			){
		
		this.userid = userid;
		this.name = name;
		this.email = email;
		this.laporanid = laporanid;
		this.statusid = statusid;
		this.waktu_mulai = waktu_mulai;
		this.waktu_selesai = waktu_selesai;
	}
}
