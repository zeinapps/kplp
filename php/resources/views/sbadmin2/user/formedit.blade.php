@extends('sbadmin2.master')
@section('content')
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Tambah User</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-body">
                    @if (count($errors) > 0)
                        @foreach ($errors->all() as $error)
                            <div class="alert alert-danger">
                                <button type="button" class="close" data-dismiss="alert">×</button>
                                <i class="fa fa-exclamation-triangle"></i>
                                {{ $error }}
                            </div>
                        @endforeach
                    @endif
                    <form role="form" method="post" action="/user/update/{{ $id }}">
                        <div class="form-group">
                            <label class="control-label" for="inputSuccess">Nama</label>
                            <input class="form-control" id="inputSuccess" type="text" name="name" value="{{ $name }}">
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="inputWarning">Email</label>
                            <input class="form-control" id="inputWarning" type="email" name="email" value="{{ $email }}">
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="inputError">Hp</label>
                            <input class="form-control" id="inputError" type="text" name="hp" value="{{ $hp }}">
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="inputError">Jabatan</label>
                            <input class="form-control" id="inputError" type="text" name="jabatan" value="{{ $jabatan }}">
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="inputError">Password</label>
                            <input class="form-control" id="inputError" type="password" name="password" >
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="inputError">Konfirmasi Password</label>
                            <input class="form-control" id="inputError" type="password" name="password_confirmation">
                        </div>
                        <div class="row">
                            <div class="col-sm-3"> 
                                <button type="submit" class="btn btn-primary">Simpan</button>
                                <a href="/user" class="btn btn-warning">Batal</a>
                            </div>
                        </div>
                    </form>
                    
                </div>
            </div>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
</div>
@endsection