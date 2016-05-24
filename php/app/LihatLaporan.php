<?php

namespace App;
use Illuminate\Database\Eloquent\Model;

class LihatLaporan extends Model
{
    protected $table = 'lihat_laporan';
    protected $fillable = [
        'id', 'user_id', 'laporan_id', 'waktu'
    ];
    protected $primaryKey = 'id';
    public $timestamps = false;
    protected $hidden = ['pivot'];
}
