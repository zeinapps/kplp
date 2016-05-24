package zein.apps.kplp;

public class Config {
    // File upload url (replace the ip with your server address)
	
    public static final String BASEURL = "http://kplp-tanjunguban.com";
    public static final String DOMAIN = "kplp.utomodesain.com";
    public static final String FILE_UPLOAD_URL = BASEURL+ "/api/v1/laporan";
    public static final String LOGIN_URL = BASEURL+ "/api/v1/login";
    public static final String LAPORAN = BASEURL+ "/api/v1/laporan";
    public static final String CEKLAPORAN = BASEURL+ "/api/v1/ceklaporan";
    public static final String ASSIGN_LAPORAN = BASEURL+ "/api/v1/laporan";
    public static final String FINISH_LAPORAN = BASEURL+ "/api/v1/laporan";
     
    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
}