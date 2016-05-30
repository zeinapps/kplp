<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the controller to call when that URI is requested.
|
*/

Route::get('/', function () {
    return redirect('dashboard');
});

Route::get('api/v1/download/{id}', [
            'as' => 'download',
            'uses' => 'api\v1\UploadController@download'
        ]);

Route::group(['prefix' => 'api/v1', 'namespace' => 'api\v1'], function() {
    Route::post('laporan', 'LaporanController@store');
    Route::get('laporan/{id}/{user_id}', 'LaporanController@show');
    Route::get('ceklaporan/{user_id}', 'LaporanController@ceklaporan');
    Route::post('login', 'UserController@login');
    Route::get('laporan', 'LaporanController@index');
    Route::get('laporan/{id}/assign/{user_id}', 'LaporanController@assign');
    Route::get('laporan/{id}/unassign/{user_id}', 'LaporanController@unassign');
    Route::get('laporan/{id}/finish/{user_id}', 'LaporanController@finish');
}); 
Route::auth();

Route::group(['middleware' => ['auth','acl'],'is' => 'administrator'], function () {
    Route::get('/dashboard', 'DashboardController@index');
    Route::get('/laporan', 'LaporanController@index');
    Route::get('/laporan/{id}', 'LaporanController@show');
    Route::get('/downloadlaporan', 'LaporanController@downloadlist');
    
    Route::post('/user', 'UserController@store');
    Route::post('/user/update/{id}', 'UserController@update');
    Route::get('/user', 'UserController@index');
    Route::get('/user/create', 'UserController@create');
    Route::get('/user/edit/{id}', 'UserController@edit');
    Route::get('/user/{id}', 'UserController@show');
    
    
    Route::get('/user/edit/{id}/role', 'UserController@editrole');
    Route::post('/user/update/{id}/role', 'UserController@updaterole');
});

Route::get('503', function () {
    return  view('errors.503');
});

