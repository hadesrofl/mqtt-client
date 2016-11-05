-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 05. Nov 2016 um 17:51
-- Server Version: 5.5.52-0+deb8u1
-- PHP-Version: 5.6.27-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `NodeMCUTest`
--
CREATE DATABASE IF NOT EXISTS `NodeMCUTest` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `NodeMCUTest`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `humidity`
--

CREATE TABLE IF NOT EXISTS `humidity` (
`Id` int(11) NOT NULL,
  `Room` text NOT NULL,
  `Value` float NOT NULL,
  `Date` datetime NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=945 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `temperature`
--

CREATE TABLE IF NOT EXISTS `temperature` (
`Id` int(11) NOT NULL,
  `Room` text NOT NULL,
  `Value` float NOT NULL,
  `Date` datetime NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=945 DEFAULT CHARSET=latin1;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `humidity`
--
ALTER TABLE `humidity`
 ADD PRIMARY KEY (`Id`);

--
-- Indizes für die Tabelle `temperature`
--
ALTER TABLE `temperature`
 ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `humidity`
--
ALTER TABLE `humidity`
MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=945;
--
-- AUTO_INCREMENT für Tabelle `temperature`
--
ALTER TABLE `temperature`
MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=945;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
