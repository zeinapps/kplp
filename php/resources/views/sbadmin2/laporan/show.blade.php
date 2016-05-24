@extends('sbadmin2.master')
@section('content')
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Detil Laporan</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-body">
                    <h3>Laporan ID {{ $id }}</h3>
                    
                    <div class="table-responsive">
                        <table class="table table-bordered table-striped">
                            
                            <tbody>
                                
                                <tr>
                                    <th>Nama Pelapor</th>
                                    <td colspan="4">{{ $nama }}</td>
                                </tr>
                                <tr>
                                    <th>Jenis</th>
                                    <td colspan="4">{{ $jenis }}</td>
                                </tr>
                                <tr>
                                    <th>Tanggal</th>
                                    <td colspan="4">{{ $waktu }}</td>
                                </tr>
                                <tr>
                                    <th>HP</th>
                                    <td colspan="4">{{ $hp }}</td>
                                </tr>
                                <tr>
                                    <th>Email</th>
                                    <td colspan="4">{{ $email_pelapor }}</td>
                                </tr>
                                <tr>
                                    <th>Lokasi pelapor</th>
                                    <td colspan="4">{{ $lokasi_pelapor }}</td>
                                </tr>
                                <tr>
                                    <th>Lokasi Kejadian</th>
                                    <td colspan="4">{{ $lokasi_kejadian }}</td>
                                </tr>
                                <tr>
                                    <th>Kronologi</th>
                                    <td colspan="4">{{ $deskripsi }}</td>
                                </tr>
                                <tr>
                                    <th>Status</th>
                                    <td colspan="4">{{ $status }}</td>
                                </tr>
                                <tr>
                                    <th>Lampiran</th>
                                    <td colspan="4"><img src="{{ $foto }}" /></td>
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