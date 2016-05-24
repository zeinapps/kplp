@extends('sbadmin2.master')
@section('content')
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Detil User</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-body">
                    <h3>User ID {{ $id }}</h3>
                    
                    <div class="table-responsive">
                        <table class="table table-bordered table-striped">
                            
                            <tbody>
                                
                                <tr>
                                    <th>Nama</th>
                                    <td colspan="4">{{ $name }}</td>
                                </tr>
                                <tr>
                                    <th>Jenis</th>
                                    <td colspan="4">{{ $email }}</td>
                                </tr>
                                <tr>
                                    <th>HP</th>
                                    <td colspan="4">{{ $hp }}</td>
                                </tr>
                                <tr>
                                    <th>Jabatan</th>
                                    <td colspan="4">{{ $jabatan }}</td>
                                </tr>
                               
                            </tbody>
                        </table>
                    </div>
                    
                </div>
            </div>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
</div>
@endsection