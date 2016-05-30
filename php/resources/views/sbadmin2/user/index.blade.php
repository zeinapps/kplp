@extends('sbadmin2.master')
@section('content')
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">User</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <a href="/user/create" class="btn btn-primary">Tambah User</a><br><br>
            <div class="panel panel-default">
                <div class="panel-heading">
                    Rekap User
                </div>
                <!-- /.panel-heading -->
                
                <div class="panel-body">
                    
                    <div class="table-responsive">
                        <table class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th>No</th>
                                    <th>Nama</th>
                                    <th>HP</th>
                                    <th>Jabatan</th>
                                    <th>Role</th>
                                    <th>Aksi</th>
                                </tr>
                            </thead>
                            <tbody>
                                
                                @foreach ($data as $item)
                                <tr>
                                    <td>{{ $no++ }}</td>
                                    <td>{{ $item['name'] }}</td>
                                    <td>{{ $item['hp'] }}</td>
                                    <td>{{ $item['jabatan'] }}</td>
                                    <td>
                                        <?php $roles = $item['roles'];?>
                                        @foreach ( $roles as $ite)
                                        <div class="@if( $ite['slug'] == 'administrator' ) {{ 'alert alert-info' }} @else {{ 'alert alert-danger' }} @endif" style="padding: 5px; margin-bottom: 5px">{{ $ite['name'] }}</div>
                                        @endforeach
                                    </td>
                                    <td>
                                        <a href="/user/{{ $item['id'] }}" class="btn btn-primary btn-xs">Detil</a>
                                        <a href="/user/edit/{{ $item['id'] }}" class="btn btn-warning btn-xs">Edit</a>
                                        <a href="/user/edit/{{ $item['id'] }}/role" class="btn btn-warning btn-xs">Atur Role </a>
                                    </td>
                                </tr>
                                @endforeach
                                
                                
                            </tbody>
                        </table>
                        <div class="col-md-12">
                            {!! $pagination->render() !!}
                        </div> 
                        <div class="col-md-4">
                    
                </div>
                    </div>
                    <!-- /.table-responsive -->
                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
        </div>
        <!-- /.col-lg-6 -->
    </div>
</div>
@endsection