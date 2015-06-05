package com.rizal.tempatwifimalang;

/**
 * Created by Arizal on 5/29/2015.
 */


public class TempatWifi
{
    private int id;
    private String	nama;
    private String	alamat;
    private double	lat;
    private double	lng;

    public TempatWifi()
    {
        // TODO Auto-generated constructor stub
    }

    public TempatWifi(int id, String nama, String alamat, double lat, double lng)
    {
        super();
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.lat = lat;
        this.lng = lng;
    }

    public String getNama()
    {
        return nama;
    }

    public void setNama(String nama)
    {
        this.nama = nama;
    }

    public String getAlamat()
    {
        return alamat;
    }

    public void setAlamat(String alamat)
    {
        this.alamat = alamat;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public double getLng()
    {
        return lng;
    }

    public void setLng(double lng)
    {
        this.lng = lng;
    }

}

