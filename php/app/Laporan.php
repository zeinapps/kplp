<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Laporan extends Model
{
    
    protected $table = 'laporan';
    protected $fillable = [
        'id','hp', 'lokasi_pelapor', 'lokasi_kejadian', 'nama', 'lat', 'long', 'deskripsi', 'foto', 'status','email_pelapor','waktu'
    ];
    protected $primaryKey = 'id';
    public $timestamps = false;
    protected $hidden = ['pivot'];
    
    public function user()
    {
        return $this->hasMany('App\AssignLaporan');
    }
}
