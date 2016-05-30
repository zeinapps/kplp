<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\User;
use PDF;
use App\LihatLaporan;
use Excel;
use DB;
use Validator;

class UserController extends Controller {

    public function index(Request $request) {
        $limit = $request->limit ? $request->limit : 10;
        $query = User::with(['roles' => function($q){
                            $q->select('roles.id','name','slug');
                        }])->paginate($limit);
        $page = $request->page ? $request->page : 1;
        $no = ($page-1) * $limit + 1;
        $Data = $query->toArray();
//        dd($Data['data']);
        $data = [
            'data' => $Data['data'],
            'pagination' => $query,
            'no' => $no,
        ];
        return view('sbadmin2.user.index', $data);
        
    }
    
    public function show(Request $request, $id) {
        $user_login = $request->user();
        $user = User::find($id);
        
        
        return view('sbadmin2.user.show', $user);
    }
    
    public function create() {
        return view('sbadmin2.user.formcreate');
    }
    
    public function edit($id) {
        $user = User::find($id);
        return view('sbadmin2.user.formedit', $user);
    }
    
    public function store(Request $request) {
        $validator = Validator::make($request->all(), [
            'name' => 'required',
            'email' => 'required|email',
            'hp' => 'required',
            'password' => 'required|confirmed',
        ]);
        
        if ($validator->fails()) {
            return redirect('user/create')
                        ->withErrors($validator)
                        ->withInput();
        }
        
        $user = new User;
        $user->name = $request->name;
        $user->email = $request->email;
        $user->hp = $request->hp;
        $user->jabatan = $request->jabatan ? $request->jabatan : '';
        $user->password = bcrypt($request->password);
        $user->save();
        $user->assignRole('admin');
        return redirect('user');
    }
    
    public function editrole($id) {
        $user = User::find($id);
        $user->administrator = $user->is('administrator');
        $user->admin = $user->is('admin') ;
//        dd($user->administrator);
        return view('sbadmin2.user.formeditrole', $user);
    }
    
    public function update(Request $request, $id) {
        $validator = Validator::make($request->all(), [
            'email' => 'email',
            'password' => 'confirmed',
        ]);
        
        if ($validator->fails()) {
            return redirect('user/edit/'.$id)
                        ->withErrors($validator)
                        ->withInput();
        }
        $reqs = $request->all();
        try {      
        $query = User::find($id);
        foreach ($reqs as $key => $value) {
            if($key != 'password_confirmation' && $value){
                if($key == 'password'){
                    $query->{$key} = bcrypt($value);
                }else{
                    $query->{$key} = $value;
                }
            }
                
        }
        $query->save();
        } catch (\Illuminate\Database\Eloquent\ModelNotFoundException $exc) {
            return $this->SendError(['Gagal Menyimpan']);
        } 
        
        return redirect('user');
    }
    
    public function updaterole(Request $request, $id) {
        $userlogin = $request->user();
        $user = User::find($id);
        
        
        
        if($request->administrator){
            $user->assignRole('administrator');
        }else{
            if($userlogin->id == $id){
                return redirect('user/edit/'.$id.'/role')
                            ->withErrors(['Maaf, Anda Tidak bisa menghapus role administrator anda'])
                            ->withInput();
            }else{
                $user->revokeRole('administrator');
            }
            
        }
        
        if($request->admin){
            $user->assignRole('admin');
        }else{
            $user->revokeRole('admin');
        }
        
        return redirect('user');
    }
}
