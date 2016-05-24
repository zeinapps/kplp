<?php

namespace App\Http\Controllers\api\v1;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\User;
use App\Laporan;
use App\Fileupload;
use App\AssignLaporan;
use App\LihatLaporan;
use Validator;
use Storage;
use File;
use Auth;
use Mail;
use Image;

class LaporanController extends Controller {

    public function index(Request $request) {
        
        $query = Laporan::where('status', '!=', 4)
                ->join('m_jenis_laporan', 'laporan.jenis', '=', 'm_jenis_laporan.id')
                ->select('laporan.*', 'm_jenis_laporan.keterangan as jenis');
        $order = 'DESC';
        $orderby = 'waktu';
        $query = $this->order($query, $orderby, $order);
        $query = $query->paginate($request->limit ? $request->limit : 10);
        $query->appends($request->all());
        $Data = $query;
        $Items = array();
        foreach ($query->items() as $value) {
            $value->waktu= date('d M Y',$value->waktu);
            $Items[] = $value;
        }
        $Data->data = $Items;
        return $this->SendData($Data);
        
    }
    
    public function cekLaporan(Request $request) {
        $user_id = $request->user_id;
        $query = Laporan::whereNotIn('laporan.id',function($q) use ($user_id){
            $q->select('laporan_id')
              ->from('lihat_laporan')
              ->where('user_id', $user_id);
         })
         ->join('m_jenis_laporan', 'laporan.jenis', '=', 'm_jenis_laporan.id')
        ->select('laporan.*', 'm_jenis_laporan.keterangan as jenis')
         ->where('status', '!=', 3);
        $order = 'DESC';
        $orderby = 'waktu';
        $query = $this->order($query, $orderby, $order);
        $query = $query->paginate($request->limit ? $request->limit : 10);
        $query->appends($request->all());
        $Data = $query;
        $Items = array();
        foreach ($query->items() as $value) {
            $value->waktu= date('d M Y',$value->waktu);
            $Items[] = $value;
        }
        $Data->data = $Items;
        return $this->SendData($Data);
        
    }

    public function create() {
        abort(404);
    }

    public function store(Request $request) {
        $validator = Validator::make($request->all(), [
            'lat' => 'numeric',
            'long' => 'numeric',
            'hp' => 'required',
            'jenis' => 'required|numeric',
            'nama' => 'required',
            'lokasi_pelapor' => 'required',
            'lokasi_kejadian' => 'required',
            'image' => 'image',
        ]);
        
        if ($validator->fails()) {
            $Error = $validator->messages();
            return $this->SendError($Error->all());
        }
        
        $link = "";
        $time = time();
        if($request->image){
            //upload file
            $file = $request->file('image');
            $content = File::get($file);
            $extension = $file->getClientOriginalExtension();
            $mime = $file->getMimeType();
            $name = $file->getFilename();
            $destinationPath = 'laporan/'.date('Y/m/d',$time);
            $fileName = $name. '-' . $time . '.' . $extension;
            Storage::makeDirectory($destinationPath);
//            $img = Storage::put($destinationPath . '/' . $fileName, $content);

            $img = Image::make($file);
            $width  = $img->width();
            $height  = $img->height();
            while ($width > 1000) {
                $width = $width/2;
                $height = $height/2;
                $img->resize($width,$height);
            }
            $img->save(storage_path('app').'/'.$destinationPath . '/' . $fileName);
            
            if ($img) {
                $upload = new Fileupload;
                $upload->nama_file = $fileName;
                $upload->mime = $mime;
                $upload->ext = $extension;
                $upload->folder = $destinationPath;
                $upload->save();
                $link = route('download',['id' => $upload->id ]);

            }else{
                return $this->SendError(['upload file gagal']);
            }
        }
        
        
        $query = new Laporan;
        $query->waktu = $time;
        $query->hp = $request->hp;
        $query->jenis = $request->jenis;
        $query->nama = $request->nama;
        $query->email_pelapor = $request->email_pelapor ? $request->email_pelapor : '';
        $query->lokasi_pelapor = $request->lokasi_pelapor;
        $query->lokasi_kejadian = $request->lokasi_kejadian;
        $query->foto = $link;
        $query->deskripsi = $request->deskripsi?$request->deskripsi:'';
        $query->lat = $request->lat?$request->lat:null;
        $query->long = $request->long?$request->long:null;
        $query->save();
        
        $data = [
            'informasi' => 'Laporan anda akan kami tindak lanjuti.',
        ];
        Mail::send('email', $data, function ($message) use ($query) {
            $message->to($query->email_pelapor)
                    ->subject('Confirmasi');
        });
        
        return $this->SendData($query->toArray(), 201);
    }

    public function show($id,$user_id) {
        
        $lihat = LihatLaporan::where('laporan_id',$id)
                ->where('user_id',$user_id)->first();
        
        if(!$lihat){
            $new = new LihatLaporan;
            $new->user_id = $user_id;
            $new->laporan_id = $id;
            $new->waktu = time();
            $new->save();
        }
        
        $query = Laporan::with(['user' => function($q){
            $q->join('users','users.id', '=', 'assign_laporan.user_id')
            ->select('assign_laporan.*', 'users.name', 'users.email' );
                        }])
                ->join('m_jenis_laporan', 'laporan.jenis', '=', 'm_jenis_laporan.id')
                ->join('m_status_laporan', 'laporan.status', '=', 'm_status_laporan.id')
                ->select('laporan.*', 'm_jenis_laporan.keterangan as jenis', 'm_status_laporan.keterangan as status')->find($id);
        $query->waktu= date('d M Y',$query->waktu);;
        return $this->SendData($query, 201);
    }

    public function assign($laporan_id,$user_id) {
        
        $is_finish = Laporan::where('id', '=', $laporan_id)
                ->where('status', '=', 3)
                ->first();
        
        if($is_finish){
            return $this->SendError(['sudah selesai'], 200);
        }
        
        $is_assign = AssignLaporan::where('laporan_id', '=', $laporan_id)
                ->where('user_id', '=', $user_id)
                ->first();
        
        if(!$is_assign){
             $query = new AssignLaporan;
             $query->laporan_id = $laporan_id;
             $query->user_id = $user_id;
             $query->status_id = 2;
             $query->waktu_mulai = time();
             $query->save();
             
             Laporan::where('id', $laporan_id)
                ->update(['status' => 2]);
        }
        
        return $this->SendData(null, 200);
    }
    
    
    public function finish($laporan_id,$user_id) {
        
        $is_finish = Laporan::where('id', '=', $laporan_id)
                ->where('status', '=', 3)
                ->first();
        
        if($is_finish){
            return $this->SendError(['sudah selesai'], 200);
        }
        
        $is_assign = AssignLaporan::where('laporan_id', '=', $laporan_id)
                ->where('user_id', '=', $user_id)
                ->first();
        
        if(!$is_assign){
            return $this->SendError(['Anda tidak assgin ke laporan'], 200);
        }
        
        $query = AssignLaporan::find($is_assign->id);
        $query->status_id = 3;
        $query->waktu_selesai = time();
        $query->save();

        Laporan::where('id', $laporan_id)
           ->update(['status' => 3]);
       
        
        return $this->SendData(null, 200);
    }
    
    public function unassign($laporan_id,$user_id) {
        
        $is_finish = Laporan::where('id', '=', $laporan_id)
                ->where('status', '=', 3)
                ->first();
        
        if($is_finish){
            return $this->SendError(['sudah selesai'], 200);
        }
        
        $assign = AssignLaporan::where('laporan_id',$laporan_id)
                ->where('user_id',$user_id)->first();
        
        if($assign){
            $query = AssignLaporan::find($assign->id);
            $query->delete();
        }
        
             
        if(!AssignLaporan::where('laporan_id',$laporan_id)->first()){
            Laporan::where('id', $laporan_id)
                ->update(['status' => 1]);
        }
        
        return $this->SendData(null, 200);
    }
    public function edit($id) {
        abort(404);
    }

    public function update(Request $request, $id) {
        abort(404);
    }

    public function destroy($id) {
        abort(404);
    }

}
