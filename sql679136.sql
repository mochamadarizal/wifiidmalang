-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 30, 2015 at 05:21 PM
-- Server version: 5.6.16
-- PHP Version: 5.5.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `sql679136`
--

DELIMITER $$
--
-- Procedures
--


CREATE TABLE IF NOT EXISTS `barang_keluar` (
  `id_keluar` int(20) NOT NULL AUTO_INCREMENT,
  `tgl` date NOT NULL,
  `kode_barang` int(20) NOT NULL,
  `jumlah` int(10) NOT NULL,
  `keterangan` varchar(255) NOT NULL,
  PRIMARY KEY (`id_keluar`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `barang_keluar`
--

INSERT INTO `barang_keluar` (`id_keluar`, `tgl`, `kode_barang`, `jumlah`, `keterangan`) VALUES
(1, '2015-05-08', 1, 10, 'Pemesanan PT Cakra Adinata'),
(2, '2015-05-18', 1, 1, 'Penjual dengan id Transaksi 1431599944'),
(3, '2015-05-30', 2, 20, 'Pembelian dari PT Satria');

--
-- Triggers `barang_keluar`
--
DROP TRIGGER IF EXISTS `kurang_stok`;
DELIMITER //
CREATE TRIGGER `kurang_stok` AFTER INSERT ON `barang_keluar`
 FOR EACH ROW begin
	Update data_persediaan SET keluar=keluar + NEW.jumlah ,stok_tersedia=stok_tersedia - NEW.jumlah Where kode_barang=NEW.kode_barang;
end
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `barang_masuk`
--

CREATE TABLE IF NOT EXISTS `barang_masuk` (
  `id_masuk` int(20) NOT NULL AUTO_INCREMENT,
  `tgl` date NOT NULL,
  `kode_barang` int(20) NOT NULL,
  `jumlah` int(20) NOT NULL,
  `keterangan` varchar(255) NOT NULL,
  PRIMARY KEY (`id_masuk`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `barang_masuk`
--

INSERT INTO `barang_masuk` (`id_masuk`, `tgl`, `kode_barang`, `jumlah`, `keterangan`) VALUES
(1, '2015-05-01', 1, 150, 'Pembelian dari PT Satria'),
(2, '2015-05-07', 2, 30, 'pembelian terakhir');

--
-- Triggers `barang_masuk`
--
DROP TRIGGER IF EXISTS `tambah_stok`;
DELIMITER //
CREATE TRIGGER `tambah_stok` AFTER INSERT ON `barang_masuk`
 FOR EACH ROW begin

declare jml int;
Select count(*) into jml from data_persediaan where kode_barang=NEW.kode_barang;

if jml = 1
THEN
	Update data_persediaan SET masuk = masuk + NEW.jumlah , stok_tersedia = stok_tersedia + new.jumlah Where kode_barang = new.kode_barang;
else
	Insert into data_persediaan(kode_barang , stok_awal , masuk , stok_tersedia) values( new.kode_barang, new.jumlah , new.jumlah , new.jumlah);
end if;
end
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE IF NOT EXISTS `customer` (
  `id_customer` int(11) NOT NULL AUTO_INCREMENT,
  `nama_customer` varchar(255) NOT NULL,
  `no_hp` varchar(255) NOT NULL,
  `alamat` varchar(255) NOT NULL,
  PRIMARY KEY (`id_customer`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id_customer`, `nama_customer`, `no_hp`, `alamat`) VALUES
(1, 'PT cakra Adinta', '085746748089', 'Jln Bandung No 18 Jakarta Pusat');

-- --------------------------------------------------------

--
-- Table structure for table `data_barang`
--

CREATE TABLE IF NOT EXISTS `data_barang` (
  `kode_barang` int(20) NOT NULL AUTO_INCREMENT,
  `nama_barang` varchar(40) NOT NULL,
  `jenis_barang` varchar(10) NOT NULL,
  `harga` int(11) NOT NULL,
  PRIMARY KEY (`kode_barang`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `data_barang`
--

INSERT INTO `data_barang` (`kode_barang`, `nama_barang`, `jenis_barang`, `harga`) VALUES
(1, 'Baju', 'Unggulan', 200000),
(2, 'Celana', 'Unggulan', 100000);

-- --------------------------------------------------------

--
-- Table structure for table `data_persediaan`
--

CREATE TABLE IF NOT EXISTS `data_persediaan` (
  `kode_barang` int(20) NOT NULL,
  `stok_awal` int(10) NOT NULL DEFAULT '0',
  `masuk` int(10) NOT NULL DEFAULT '0',
  `keluar` int(10) NOT NULL DEFAULT '0',
  `stok_akhir` int(10) NOT NULL DEFAULT '0',
  `rata_keluar` int(10) NOT NULL DEFAULT '0',
  `safety_stok` int(10) NOT NULL DEFAULT '0',
  `stok_tersedia` int(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`kode_barang`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `data_persediaan`
--

INSERT INTO `data_persediaan` (`kode_barang`, `stok_awal`, `masuk`, `keluar`, `stok_akhir`, `rata_keluar`, `safety_stok`, `stok_tersedia`) VALUES
(1, 150, 150, 31, 0, 0, 0, 119),
(2, 30, 30, 20, 0, 0, 0, 10);

-- --------------------------------------------------------

--
-- Table structure for table `pemasukan`
--

CREATE TABLE IF NOT EXISTS `pemasukan` (
  `kode_pemasukan` int(11) NOT NULL AUTO_INCREMENT,
  `tujuan_asal` varchar(30) NOT NULL,
  `tgl` date NOT NULL,
  `jumlah` int(11) NOT NULL,
  `keterangan` varchar(255) NOT NULL,
  PRIMARY KEY (`kode_pemasukan`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `pemasukan`
--

INSERT INTO `pemasukan` (`kode_pemasukan`, `tujuan_asal`, `tgl`, `jumlah`, `keterangan`) VALUES
(1, 'PT cakra Adinta', '2015-05-16', 2000000, 'Dari proses penjual dengan id Transaksi 1431768497'),
(2, 'PT cakra Adinta', '2015-05-18', 200000, 'Dari proses penjual dengan id Transaksi 1431599944');

-- --------------------------------------------------------

--
-- Table structure for table `pengeluaran`
--

CREATE TABLE IF NOT EXISTS `pengeluaran` (
  `kode_pengeluaran` int(11) NOT NULL AUTO_INCREMENT,
  `tujuan_asal` varchar(255) NOT NULL,
  `tgl` date NOT NULL,
  `jumlah` double NOT NULL,
  `keterangan` varchar(255) NOT NULL,
  PRIMARY KEY (`kode_pengeluaran`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `pesanan_fax`
--

CREATE TABLE IF NOT EXISTS `pesanan_fax` (
  `id_pesanan` int(11) NOT NULL AUTO_INCREMENT,
  `id_transaksi` varchar(255) NOT NULL,
  `tgl` date NOT NULL,
  `id_customer` int(30) NOT NULL,
  `kode_barang` int(20) NOT NULL,
  `jumlah` int(10) NOT NULL,
  `status` varchar(10) NOT NULL,
  PRIMARY KEY (`id_pesanan`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `pesanan_fax`
--

INSERT INTO `pesanan_fax` (`id_pesanan`, `id_transaksi`, `tgl`, `id_customer`, `kode_barang`, `jumlah`, `status`) VALUES
(9, '1431599944', '2015-05-14', 1, 1, 1, 'tersedia'),
(10, '1431768497', '2015-05-16', 1, 1, 10, 'tersedia'),
(11, '1432730745', '2015-05-27', 1, 1, 3, 'menunggu');

-- --------------------------------------------------------

--
-- Table structure for table `proses`
--

CREATE TABLE IF NOT EXISTS `proses` (
  `id_proses` int(11) NOT NULL AUTO_INCREMENT,
  `id_transaksi` varchar(255) NOT NULL,
  `status_proses` varchar(20) NOT NULL,
  `status_baca` varchar(1) NOT NULL,
  `tgl` date NOT NULL,
  `pesan` varchar(255) NOT NULL,
  PRIMARY KEY (`id_proses`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `proses`
--

INSERT INTO `proses` (`id_proses`, `id_transaksi`, `status_proses`, `status_baca`, `tgl`, `pesan`) VALUES
(5, '1431599944', '6', 'S', '2015-05-14', ''),
(6, '1431768497', '5', 'S', '2015-05-16', ''),
(7, '1432730745', '2', 'S', '2015-05-27', ''),
(8, '1432730779', '2', 'S', '2015-05-27', '');

-- --------------------------------------------------------

--
-- Table structure for table `struk`
--

CREATE TABLE IF NOT EXISTS `struk` (
  `id_struk` int(11) NOT NULL AUTO_INCREMENT,
  `id_transaksi` varchar(255) NOT NULL,
  `tgl` date NOT NULL,
  PRIMARY KEY (`id_struk`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `struk`
--

INSERT INTO `struk` (`id_struk`, `id_transaksi`, `tgl`) VALUES
(1, '1431768497', '2015-05-16'),
(2, '1431599944', '2015-05-18');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE IF NOT EXISTS `transaksi` (
  `id_transaksi` varchar(255) NOT NULL,
  `tgl` date NOT NULL,
  `id_customer` int(30) NOT NULL,
  `status` varchar(255) NOT NULL,
  `metode_pengiriman` varchar(255) NOT NULL,
  `alamat_pengiriman` varchar(255) NOT NULL,
  `total` double NOT NULL,
  PRIMARY KEY (`id_transaksi`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id_transaksi`, `tgl`, `id_customer`, `status`, `metode_pengiriman`, `alamat_pengiriman`, `total`) VALUES
('1431599944', '2015-05-14', 1, 'done', 'diambil', 'lalal', 200000),
('1431768497', '2015-05-16', 1, 'done', 'diambil', '', 2000000),
('1432730745', '2015-05-27', 1, 'menunggu', '', '', 0),
('1432730779', '2015-05-27', 1, 'menunggu', '', '', 0);

-- --------------------------------------------------------

--
-- Table structure for table `user_login`
--

CREATE TABLE IF NOT EXISTS `user_login` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `login_hash` varchar(255) NOT NULL,
  `foto` varchar(255) NOT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `user_login`
--

INSERT INTO `user_login` (`id_user`, `username`, `password`, `login_hash`, `foto`) VALUES
(7, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'administrator', 'admin.jpg'),
(8, 'rizal', '150fb021c56c33f82eef99253eb36ee1', 'gudang', 'rizal1.jpg'),
(9, 'dinda', '594280c6ddc94399a392934cac9d80d5', 'sales', 'dinda.jpg'),
(10, 'marsa', 'ad04f7d73d1a9140f0d67387810eef04', 'keuangan', 'marsa.jpg');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
