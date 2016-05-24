<?php

namespace App\Http\Controllers\api\v1;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\User;
use Auth;


class UserController extends Controller {

    public function index(Request $request) {
        abort(404);
    }

    public function create() {
        abort(404);
    }

    public function store(Request $request) {
        abort(404);
    }
    
    public function login(Request $request) {
        
        $credentials = [
            'email' => $request->email,
            'password' => $request->password,
        ];
        if ($user = Auth::once($credentials)) {
            return $this->SendData(Auth::user());
        }else{
            return $this->SendError(['Login Gagal']);
        }
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
