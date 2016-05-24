package zein.apps.kplp;

public class detil_laporan {
	public String id = "";
	public String hp = "";
	public String lokasi_pelapor = "";
	public String lokasi_kejadian = "";
	public String nama = "";
	public String lat = "";
	public String lng = "";
	public String deskripsi = "";
	public String foto = "";
	public String status = "";
	public String jenis = "";
	public String email_pelapor = "";
	public String waktu = "";
	public String user = "";

	public detil_laporan (
			String id, 
			String hp, 
			String lokasi_pelapor, 
			String lokasi_kejadian, 
			String nama,
			String lat,
			String lng,
			String deskripsi,
			String foto,
			String status,
			String jenis,
			String email_pelapor,
			String waktu,
			String user){
		
		this.id = id;
		this.hp = hp;
		this.lokasi_pelapor = lokasi_pelapor;
		this.lokasi_kejadian = lokasi_kejadian;
		this.nama = nama;
		this.lat = lat;
		this.lng = lng;
		this.deskripsi = deskripsi;
		this.foto = foto;
		this.email_pelapor = email_pelapor;
		this.waktu = waktu;
		this.user = user;
		this.status = status;
		this.jenis = jenis;
	}
}
