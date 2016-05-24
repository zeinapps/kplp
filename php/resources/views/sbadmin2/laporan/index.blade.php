@extends('sbadmin2.master')
@section('content')
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Laporan</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Rekap Laporan
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th>No</th>
                                    <th>Nama Pelapor</th>
                                    <th>Tanggal</th>
                                    <th>Jenis</th>
                                    <th>Tempat</th>
                                    <th>Aksi</th>
                                </tr>
                            </thead>
                            <tbody>
                                
                                @foreach ($data as $item)
                                <tr>
                                    <td>{{ $no++ }}</td>
                                    <td>{{ $item->nama }}</td>
                                    <td>{{ $item->waktu }}</td>
                                    <td>{{ $item->jenis }}</td>
                                    <td>{{ $item->lokasi_kejadian }}</td>
                                    <td><a href="/laporan/{{ $item->id }}" class="btn btn-primary btn-xs">Detil</a></td>
                                </tr>
                                @endforeach
                                
                                
                            </tbody>
                        </table>
                        <div class="col-md-12">
                            {!! $pagination->render() !!}
                        </div> 
                        <div class="col-md-4">
                    <form role="form" method="GET" action="{{ url('/downloadlaporan') }}" >
                        <div class="form-group">
                            <input class="form-control" placeholder="jumlah data" name="limit" type="" value="" autofocus>
                        </div>
                        <button type="submit" class="btn btn-lg btn-success btn-block"><i class="fa fa-btn fa-sign-in"></i>Export</button>
                    </form>
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