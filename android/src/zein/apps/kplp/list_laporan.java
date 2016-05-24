package zein.apps.kplp;

public class list_laporan {
	
	public String id = "";
	public String nama = "";
	public String jenis = "";
	public String waktu = "";
	public String lokasi = "";
	public String foto = "";
	public String status = "";
	public String email_pelapor = "";
	

	public list_laporan (String id, String nama, String foto, String status, String email_pelapor, String jenis, String waktu, String lokasi){
		this.id = id;
		this.nama = nama;
		this.foto = foto;
		this.status = status;
		this.email_pelapor = email_pelapor;
		this.jenis = jenis;
		this.waktu = waktu;
		this.lokasi = lokasi;
	}
}
