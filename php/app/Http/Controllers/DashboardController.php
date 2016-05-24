<?php

namespace App\Http\Controllers;

use App\Http\Requests;
use Illuminate\Http\Request;
use App\Laporan;
use App\Fileupload;
use App\AssignLaporan;
use App\LihatLaporan;

class DashboardController extends Controller
{
    
    public function index()
    {
        $laporan_baru = Laporan::where('status', 1)->count();
        $laporan_dalam_proses = Laporan::where('status', 2)->count();
        $data = [
            'laporan_baru' => $laporan_baru,
            'laporan_dalam_proses' => $laporan_dalam_proses,
        ];
        return view('sbadmin2.index', $data);
    }
}
