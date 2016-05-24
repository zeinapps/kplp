<?php

namespace App;
use Illuminate\Database\Eloquent\Model;

class AssignLaporan extends Model
{
    protected $table = 'assign_laporan';
    protected $fillable = [
        'id', 'user_id', 'laporan_id', 'status_id','waktu_mulai','waktu_selesai'
    ];
    protected $primaryKey = 'id';
    public $timestamps = false;
    protected $hidden = ['pivot'];
}
