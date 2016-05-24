<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Fileupload extends Model
{
    
    protected $table = 'file_upload';
    protected $fillable = [
        'id', 'nama_file', 'ext', 'folder','mime',
    ];
    protected $primaryKey = 'id';
    public $timestamps = false;
}
