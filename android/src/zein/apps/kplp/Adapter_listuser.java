package zein.apps.kplp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_listuser extends ArrayAdapter<user_laporan> {
    private Activity activity;
    private ArrayList<user_laporan> lList;
    private static LayoutInflater inflater = null;

    
    public Adapter_listuser (Activity activity, int textViewResourceId,ArrayList<user_laporan> al_List) {
        super(activity, textViewResourceId, al_List);
        try {
            this.activity = activity;
            this.lList = al_List;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
        } catch (Exception e) {

        }
    }
    
//    public void addMoreItems(int count) {
//        notifyDataSetChanged();
//    }

    public int getCount() {
        return lList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView nama;
        public TextView email;
        public ImageView gambar;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.row_user, null);
                holder = new ViewHolder();

                holder.nama = (TextView) vi.findViewById(R.id.txt_userlap_nama);
                holder.email = (TextView) vi.findViewById(R.id.txt_userlap_email);
                holder.gambar = (ImageView) vi.findViewById(R.id.image_userlap);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            holder.nama.setText(lList.get(position).name);
            holder.email.setText(lList.get(position).email);
            
        } catch (Exception e) {
        	Log.d("LIST EROR", e.getMessage());
        }
        return vi;
    }
    
}
