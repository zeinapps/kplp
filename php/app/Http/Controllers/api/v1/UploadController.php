<?php

namespace App\Http\Controllers\api\v1;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Fileupload;
use Storage;
use File;
use Auth;
use App\User;
use Image;

class UploadController extends Controller {
    public function __construct(){
//        if( !($user = Auth::user()) ){
//            global $userlogin_id;
//            $user = User::find($userlogin_id);
//        }
//        $this->userAuth = $user;
    }
    
    public function upload(Request $request) {

        $file = $request->file('file');
        $content = File::get($file);
        $extension = $file->getClientOriginalExtension();
        $mime = $file->getMimeType();
        $time = time();
        $destinationPath = 'uploads'.date('Y/m/d',$time);
        $fileName = $time . '.' . $extension;
        Storage::makeDirectory($destinationPath);
//        $upload_success = Storage::put($destinationPath . '/' . $fileName, $content);
        
        $img = Image::make($file);
        $width  = $img->width();
        $height  = $img->height();
        while ($width > 1000) {
            $width = $width/2;
            $height = $height/2;
            $img->resize($width,$height);
        }
        $img->save($destinationPath . '/' . $fileName);

        if ($img) {
            $upload = new Fileupload;
            $upload->nama_file = $fileName;
            $upload->mime = $mime;
            $upload->ext = $extension;
            $upload->folder = $destinationPath;
            $upload->save();
            $link = route('download',['id' => $upload->id ]);
            
            return $this->SendData(['link' => $link], 201);
        }

        return false;
    }
    
    public function download(Request $request,$id) {
        $file = Fileupload::find($id);
        $nama_file = $file->nama_file;
        $path = $file->folder.'/';
        $mime = $file->mime;
        $contents = Storage::get($path.$nama_file);
        
        
        if($request->jenis_gambar){
            $img = Image::make($contents);    
            if($request->jenis_gambar == 'list'){
//                $img->resize(100, 100);
                $img->fit(100, 100)->encode('jpg', 20);
            }else if($request->jenis_gambar == 'detil'){
                $img->resize(300, 300);
            }
            return $img->response('jpg');
        }
        
        $response = Response($contents, 200);
        return $response->header('Content-Type', $mime);
    }
    
    public function index(Request $request) {
        abort(404);
    }

    public function create() {
        abort(404);
    }

    public function store(Request $request) {
        abort(404);
    }

    public function show($id) {
        abort(404);
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
