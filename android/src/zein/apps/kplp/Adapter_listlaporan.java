package zein.apps.kplp;

import java.util.ArrayList;

import zein.apps.imageloader.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_listlaporan extends ArrayAdapter<list_laporan> {
    private Activity activity;
    private ArrayList<list_laporan> lList;
    private static LayoutInflater inflater = null;

    public ImageLoader imageLoader; 
    
    public Adapter_listlaporan (Activity activity, int textViewResourceId,ArrayList<list_laporan> al_List) {
        super(activity, textViewResourceId, al_List);
        try {
            this.activity = activity;
            this.lList = al_List;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageLoader = new ImageLoader(activity.getApplicationContext());
        } catch (Exception e) {

        }
    }
    

    public int getCount() {
        return lList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView nama;
        public TextView jenis;
        public TextView lokasi;
        public TextView tgl;
        public ImageView gambar;
        public ImageView status;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.row_list, null);
                holder = new ViewHolder();

                holder.nama = (TextView) vi.findViewById(R.id.txt_list_namapelapor);
                holder.jenis = (TextView) vi.findViewById(R.id.txt_list_jenis);
                holder.tgl = (TextView) vi.findViewById(R.id.txt_list_tgl);
                holder.lokasi = (TextView) vi.findViewById(R.id.txt_list_lokasi);
                holder.gambar = (ImageView) vi.findViewById(R.id.image_list_foto);
                holder.status = (ImageView) vi.findViewById(R.id.image_list_status);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            holder.nama.setText(lList.get(position).nama);
            holder.tgl.setText(lList.get(position).waktu);
            holder.lokasi.setText(lList.get(position).lokasi);
            holder.jenis.setText(lList.get(position).jenis);
            
            int draw  = R.drawable.status_red;
            if(lList.get(position).status.equals("1")){
            	draw =  R.drawable.status_red;
            }else if(lList.get(position).status.equals("2")){
            	draw =  R.drawable.status_yellow;
            }else if(lList.get(position).status.equals("3")){
            	draw =  R.drawable.status_green;
            }
            holder.status.setImageDrawable(this.activity.getResources().getDrawable(draw));
            if(!lList.get(position).foto.equals("")){
            	imageLoader.DisplayImage(lList.get(position).foto+"?jenis_gambar=list", holder.gambar);
            }
            
        } catch (Exception e) {
        	//Log.d("LIST EROR", e.getMessage());
        }
        return vi;
    }
    
}
