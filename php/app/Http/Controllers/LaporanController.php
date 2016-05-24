<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Laporan;
use PDF;
use App\LihatLaporan;
use Excel;

class LaporanController extends Controller {

    public function index(Request $request) {
        
        $query = Laporan::join('m_jenis_laporan', 'laporan.jenis', '=', 'm_jenis_laporan.id')
                ->select('laporan.*', 'm_jenis_laporan.keterangan as jenis');
        if($request->status){
            $query->where('status',$request->status);
        }
        $order = 'DESC';
        $orderby = 'waktu';
        $limit = $request->limit ? $request->limit : 10;
        $query = $this->order($query, $orderby, $order);
        $query = $query->paginate($limit);
        $query->appends($request->all());
        $Data = $query;
        $Items = array();
        foreach ($query->items() as $value) {
            $value->waktu= date('d M Y',$value->waktu);
            $Items[] = $value;
        }
        $page = $request->page ? $request->page : 1;
        $no = ($page-1) * $limit + 1;
        $Data->data = $Items;
        $data = [
            'data'  => $Data->data,
            'pagination' => $query,
            'no' => $no,
        ];
        return view('sbadmin2.laporan.index', $data);
        
    }
    
    public function show(Request $request, $id) {
        $user_login = $request->user();
        $lihat = LihatLaporan::where('laporan_id',$id)
                ->where('user_id',$user_login->id)->first();
        
        if(!$lihat){
            $new = new LihatLaporan;
            $new->user_id = $user_login->id;
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
        $query->waktu= date('d M Y',$query->waktu);
//        dd($query);
        return view('sbadmin2.laporan.show', $query);
    }
    
    public function downloadlist(Request $request) {
        
        $query = Laporan::join('m_jenis_laporan', 'laporan.jenis', '=', 'm_jenis_laporan.id')
                ->select('laporan.id as nomer', 'laporan.*', 'm_jenis_laporan.keterangan as jenis');
        if($request->status){
            $query->where('status',$request->status);
        }
        $order = 'DESC';
        $orderby = 'waktu';
        $query = $this->order($query, $orderby, $order);
        $query = $query->paginate($request->limit ? $request->limit : 10);
      
        $Items = array();
        $nomer = 1;
        foreach ($query->items() as $value) {
            $value->nomer = $nomer++;
            $value->waktu= date('d M Y H:i:s',$value->waktu);
            
            unset($value->foto);
            unset($value->id);
            $value = $value->toArray();
            $Items[] = $value;
        }
        
        
        return Excel::create('laporan', function($excel) use($Items) {

        $excel->sheet('Sheetname', function($sheet) use($Items) {

            $sheet->fromArray($Items);

        });

        })->export('xls');
        
    }

}
