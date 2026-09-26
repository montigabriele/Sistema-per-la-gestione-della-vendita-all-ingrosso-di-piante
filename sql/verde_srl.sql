CREATE DATABASE  IF NOT EXISTS `verde_srl` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `verde_srl`;
-- MySQL dump 10.13  Distrib 8.0.46, for macos15 (arm64)
--
-- Host: localhost    Database: verde_srl
-- ------------------------------------------------------
-- Server version	9.7.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '7b408fc8-1512-11f0-bc6b-7610f082bc27:1-11559';

--
-- Table structure for table `AziendeRivenditrici`
--

DROP TABLE IF EXISTS `AziendeRivenditrici`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AziendeRivenditrici` (
  `PartitaIVA` char(11) NOT NULL,
  `NomeAzienda` varchar(100) NOT NULL,
  `IdIndirizzoLegale` int NOT NULL,
  `IdIndirizzoFatturazione` int NOT NULL,
  `IdReferente` int NOT NULL,
  PRIMARY KEY (`PartitaIVA`),
  KEY `FK_Azienda_IndirizzoLegale` (`IdIndirizzoLegale`),
  KEY `FK_Azienda_IndirizzoFatturazione` (`IdIndirizzoFatturazione`),
  KEY `FK_Azienda_Referente` (`IdReferente`),
  CONSTRAINT `FK_Azienda_IndirizzoFatturazione` FOREIGN KEY (`IdIndirizzoFatturazione`) REFERENCES `Indirizzi` (`IdIndirizzo`),
  CONSTRAINT `FK_Azienda_IndirizzoLegale` FOREIGN KEY (`IdIndirizzoLegale`) REFERENCES `Indirizzi` (`IdIndirizzo`),
  CONSTRAINT `FK_Azienda_Referente` FOREIGN KEY (`IdReferente`) REFERENCES `ReferentiAziendali` (`IdReferente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AziendeRivenditrici`
--

LOCK TABLES `AziendeRivenditrici` WRITE;
/*!40000 ALTER TABLE `AziendeRivenditrici` DISABLE KEYS */;
INSERT INTO `AziendeRivenditrici` VALUES ('12345678901','VIVAIO SRL',1,2,1),('22345678902','GARDEN NAPOLI SRL',3,3,2),('32345678903','FLORA MILANO SPA',2,5,3),('42345678904','TORINO GARDEN SNC',4,4,4),('52345678905','FIORITALIA SRL',5,1,5);
/*!40000 ALTER TABLE `AziendeRivenditrici` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CodiceSpecieProgressivo`
--

DROP TABLE IF EXISTS `CodiceSpecieProgressivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CodiceSpecieProgressivo` (
  `Categoria` char(3) NOT NULL,
  `UltimoNumero` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`Categoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CodiceSpecieProgressivo`
--

LOCK TABLES `CodiceSpecieProgressivo` WRITE;
/*!40000 ALTER TABLE `CodiceSpecieProgressivo` DISABLE KEYS */;
INSERT INTO `CodiceSpecieProgressivo` VALUES ('FES',6),('FIN',6),('NES',5),('NIN',5);
/*!40000 ALTER TABLE `CodiceSpecieProgressivo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Contatti`
--

DROP TABLE IF EXISTS `Contatti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Contatti` (
  `IdContatto` int NOT NULL AUTO_INCREMENT,
  `TipoContatto` enum('TELEFONO','CELLULARE','EMAIL') NOT NULL,
  `ValoreContatto` varchar(100) NOT NULL,
  PRIMARY KEY (`IdContatto`),
  UNIQUE KEY `Contatto` (`TipoContatto`,`ValoreContatto`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Contatti`
--

LOCK TABLES `Contatti` WRITE;
/*!40000 ALTER TABLE `Contatti` DISABLE KEYS */;
INSERT INTO `Contatti` VALUES (4,'TELEFONO','011-5678901'),(2,'TELEFONO','02-98765432'),(5,'TELEFONO','055-4321098'),(1,'TELEFONO','06-12345678'),(3,'TELEFONO','081-3456789'),(21,'TELEFONO','33310101011'),(6,'CELLULARE','333-1234567'),(7,'CELLULARE','335-7654321'),(8,'CELLULARE','347-9876543'),(9,'CELLULARE','348-1357924'),(10,'CELLULARE','349-2468013'),(18,'EMAIL','acquisti@floramilano.it'),(20,'EMAIL','amministrazione@fioritalia.it'),(17,'EMAIL','commerciale@garden.it'),(13,'EMAIL','contatti@floramilano.it'),(12,'EMAIL','info@garden.it'),(14,'EMAIL','info@torinogarden.com'),(11,'EMAIL','info@vivaiosrl.it'),(16,'EMAIL','ordini@vivaiosrl.it'),(15,'EMAIL','ufficio@fioritalia.it'),(19,'EMAIL','vendite@torinogarden.com');
/*!40000 ALTER TABLE `Contatti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DettaglioOrdineRifornimento`
--

DROP TABLE IF EXISTS `DettaglioOrdineRifornimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DettaglioOrdineRifornimento` (
  `IdOrdine` int NOT NULL,
  `CodiceSpecie` char(10) NOT NULL,
  `Quantita` int NOT NULL,
  PRIMARY KEY (`IdOrdine`,`CodiceSpecie`),
  KEY `idx_ordine_dettaglio` (`IdOrdine`),
  KEY `idx_specie_dettaglio` (`CodiceSpecie`),
  CONSTRAINT `FK_DettaglioOrdine_Ordine` FOREIGN KEY (`IdOrdine`) REFERENCES `OrdineRifornimento` (`IdOrdine`) ON DELETE CASCADE,
  CONSTRAINT `FK_DettaglioOrdine_Specie` FOREIGN KEY (`CodiceSpecie`) REFERENCES `SpecieDiPiante` (`CodiceSpecie`) ON DELETE RESTRICT,
  CONSTRAINT `dettaglioordinerifornimento_chk_1` CHECK ((`Quantita` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DettaglioOrdineRifornimento`
--

LOCK TABLES `DettaglioOrdineRifornimento` WRITE;
/*!40000 ALTER TABLE `DettaglioOrdineRifornimento` DISABLE KEYS */;
INSERT INTO `DettaglioOrdineRifornimento` VALUES (1,'SP-F001-IN',30),(1,'SP-F002-IN',25),(1,'SP-N001-IN',20),(2,'SP-F001-ES',40),(2,'SP-F004-ES',50),(2,'SP-N001-ES',15),(3,'SP-F002-ES',20),(3,'SP-N003-IN',15),(3,'SP-N005-IN',10),(4,'SP-F005-IN',35),(4,'SP-N001-IN',25),(5,'SP-F002-ES',30),(5,'SP-N005-ES',20),(6,'SP-F001-ES',23);
/*!40000 ALTER TABLE `DettaglioOrdineRifornimento` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_dettaglio_rifornimento_insert` BEFORE INSERT ON `dettaglioordinerifornimento` FOR EACH ROW BEGIN
  DECLARE v_stato ENUM('APERTO','CONFERMATO','SPEDITO','CONSEGNATO','ANNULLATO');
  DECLARE v_fornitore_disponibile INT DEFAULT 0;
  DECLARE v_codice_fornitore INT;
  DECLARE msg VARCHAR(255);
  
  SELECT Stato, CodiceFornitore INTO v_stato, v_codice_fornitore
  FROM OrdineRifornimento
  WHERE IdOrdine = NEW.IdOrdine;
  
  IF v_stato IS NULL THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Ordine di rifornimento non trovato';
  END IF;
  
  IF NEW.Quantita <= 0 THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'La quantità deve essere maggiore di zero';
  END IF;
  
  SELECT COUNT(*) INTO v_fornitore_disponibile
  FROM FornituraSpecie
  WHERE CodiceFornitore = v_codice_fornitore 
    AND CodiceSpecie = NEW.CodiceSpecie 
    AND Attiva = TRUE;
  
  IF v_fornitore_disponibile = 0 THEN
    SET msg = CONCAT('Il fornitore non può fornire la specie: ', NEW.CodiceSpecie);
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = msg;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `DettagliOrdine`
--

DROP TABLE IF EXISTS `DettagliOrdine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DettagliOrdine` (
  `IdOrdine` int NOT NULL,
  `CodiceSpecie` char(10) NOT NULL,
  `Quantita` int NOT NULL,
  `PrezzoUnitario` decimal(10,2) NOT NULL,
  PRIMARY KEY (`IdOrdine`,`CodiceSpecie`),
  KEY `idx_DettagliOrdine_CodiceSpecie` (`CodiceSpecie`),
  CONSTRAINT `FK_Dettaglio_Ordine` FOREIGN KEY (`IdOrdine`) REFERENCES `OrdiniDiVendita` (`IdOrdine`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_Dettaglio_Specie` FOREIGN KEY (`CodiceSpecie`) REFERENCES `SpecieDiPiante` (`CodiceSpecie`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_dettagliordine_prezzo` CHECK ((`PrezzoUnitario` >= 0)),
  CONSTRAINT `chk_dettagliordine_quantita` CHECK ((`Quantita` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DettagliOrdine`
--

LOCK TABLES `DettagliOrdine` WRITE;
/*!40000 ALTER TABLE `DettagliOrdine` DISABLE KEYS */;
INSERT INTO `DettagliOrdine` VALUES (1,'SP-F001-IN',5,25.99),(1,'SP-F003-IN',10,12.75),(1,'SP-N002-IN',3,15.75),(2,'SP-F002-ES',4,28.90),(2,'SP-F004-ES',12,10.50),(2,'SP-N003-ES',1,140.00),(3,'SP-F001-IN',8,27.50),(3,'SP-F003-ES',15,8.75),(3,'SP-N001-IN',5,39.99),(3,'SP-N005-IN',2,45.00),(4,'SP-F004-IN',10,15.25),(4,'SP-F005-IN',20,9.99),(4,'SP-N004-ES',8,19.75),(5,'SP-N002-ES',2,55.00),(5,'SP-N005-ES',3,65.50),(6,'SP-F001-ES',60,24.99);
/*!40000 ALTER TABLE `DettagliOrdine` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_dettaglio_ordine_insert` AFTER INSERT ON `dettagliordine` FOR EACH ROW BEGIN
  DECLARE v_stato ENUM('APERTO','CONFERMATO','SPEDITO','CONSEGNATO','ANNULLATO');
  
  SELECT Stato INTO v_stato
  FROM OrdiniDiVendita
  WHERE IdOrdine = NEW.IdOrdine;
  
  IF v_stato = 'APERTO' THEN
    UPDATE `Magazzino`
    SET Quantita = Quantita - NEW.Quantita
    WHERE CodiceSpecie = NEW.CodiceSpecie;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_dettaglio_ordine_delete` AFTER DELETE ON `dettagliordine` FOR EACH ROW BEGIN
  DECLARE v_stato ENUM('APERTO','CONFERMATO','SPEDITO','CONSEGNATO','ANNULLATO');
  
  SELECT Stato INTO v_stato
  FROM OrdiniDiVendita
  WHERE IdOrdine = OLD.IdOrdine;

    IF v_stato = 'CONFERMATO' OR v_stato = 'APERTO' THEN
    UPDATE `Magazzino`
    SET Quantita = Quantita + OLD.Quantita
    WHERE CodiceSpecie = OLD.CodiceSpecie;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `Fornitore`
--

DROP TABLE IF EXISTS `Fornitore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Fornitore` (
  `CodiceFornitore` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(100) NOT NULL,
  `CodiceFiscale` varchar(16) NOT NULL,
  PRIMARY KEY (`CodiceFornitore`),
  UNIQUE KEY `CodiceFiscale` (`CodiceFiscale`),
  KEY `idx_nome` (`Nome`),
  KEY `idx_codicefiscale` (`CodiceFiscale`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Fornitore`
--

LOCK TABLES `Fornitore` WRITE;
/*!40000 ALTER TABLE `Fornitore` DISABLE KEYS */;
INSERT INTO `Fornitore` VALUES (1,'FIORI SPA','03456789012'),(2,'VIVAI  SPA','05678901234'),(3,'PIANTE ESOTICHE','07890123456'),(4,'FLORA IMPORT','09123456789'),(5,'MULTISPECIE SRL','02345678901');
/*!40000 ALTER TABLE `Fornitore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FornituraSpecie`
--

DROP TABLE IF EXISTS `FornituraSpecie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FornituraSpecie` (
  `CodiceFornitore` int NOT NULL,
  `CodiceSpecie` char(10) NOT NULL,
  `Quantita` int NOT NULL DEFAULT '0',
  `Attiva` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`CodiceFornitore`,`CodiceSpecie`),
  KEY `idx_fornitore_specie` (`CodiceFornitore`),
  KEY `idx_specie_fornitore` (`CodiceSpecie`),
  KEY `idx_attiva` (`Attiva`),
  CONSTRAINT `FK_FornituraSpecie_Fornitore` FOREIGN KEY (`CodiceFornitore`) REFERENCES `Fornitore` (`CodiceFornitore`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_FornituraSpecie_Specie` FOREIGN KEY (`CodiceSpecie`) REFERENCES `SpecieDiPiante` (`CodiceSpecie`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chk_fornituraspecie_quantita` CHECK ((`Quantita` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FornituraSpecie`
--

LOCK TABLES `FornituraSpecie` WRITE;
/*!40000 ALTER TABLE `FornituraSpecie` DISABLE KEYS */;
INSERT INTO `FornituraSpecie` VALUES (1,'SP-F001-IN',150,1),(1,'SP-F002-IN',120,1),(1,'SP-F003-IN',200,1),(1,'SP-F004-IN',80,1),(1,'SP-N001-IN',100,1),(1,'SP-N002-IN',90,1),(2,'SP-F001-ES',180,1),(2,'SP-F003-ES',140,1),(2,'SP-F004-ES',160,1),(2,'SP-N001-ES',60,1),(2,'SP-N002-ES',45,1),(2,'SP-N004-ES',75,1),(2,'SP-N005-ES',85,1),(3,'SP-F001-IN',70,1),(3,'SP-F002-ES',50,1),(3,'SP-F002-IN',65,1),(3,'SP-F005-ES',40,1),(3,'SP-N002-IN',85,1),(3,'SP-N003-ES',25,1),(3,'SP-N003-IN',60,1),(3,'SP-N004-IN',75,1),(3,'SP-N005-IN',55,1),(4,'SP-F001-ES',0,1),(4,'SP-F004-ES',0,1),(4,'SP-F005-IN',0,1),(4,'SP-N001-IN',0,1),(4,'SP-N003-IN',0,1),(4,'SP-N004-ES',0,1),(5,'SP-F001-ES',0,1),(5,'SP-F002-ES',0,1),(5,'SP-F005-ES',0,1),(5,'SP-N002-ES',0,1),(5,'SP-N005-ES',0,1);
/*!40000 ALTER TABLE `FornituraSpecie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Indirizzi`
--

DROP TABLE IF EXISTS `Indirizzi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Indirizzi` (
  `IdIndirizzo` int NOT NULL AUTO_INCREMENT,
  `TipoIndirizzo` enum('LEGALE','FATTURAZIONE') NOT NULL,
  `Via` varchar(100) NOT NULL,
  `CAP` char(5) NOT NULL,
  `Citta` varchar(50) NOT NULL,
  PRIMARY KEY (`IdIndirizzo`),
  KEY `idx_Indirizzi_Citta` (`Citta`,`CAP`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Indirizzi`
--

LOCK TABLES `Indirizzi` WRITE;
/*!40000 ALTER TABLE `Indirizzi` DISABLE KEYS */;
INSERT INTO `Indirizzi` VALUES (1,'LEGALE','VIA ROMA 12','00100','ROMA'),(2,'LEGALE','VIA MILANO 15','20100','MILANO'),(3,'LEGALE','VIA NAPOLI 8','80100','NAPOLI'),(4,'LEGALE','VIA TORINO 22','10100','TORINO'),(5,'LEGALE','VIA FIRENZE 45','50100','FIRENZE'),(6,'LEGALE','VIA DEI VIVAI 45','51100','PISTOIA'),(7,'FATTURAZIONE','VIA COMMERCIALE 12','50100','FIRENZE'),(8,'LEGALE','CORSO EUROPA 78','10100','TORINO'),(9,'FATTURAZIONE','VIA FATTURE 23','10120','TORINO'),(10,'LEGALE','VIA VESUVIO 156','80100','NAPOLI'),(11,'FATTURAZIONE','CORSO UMBERTO 89','80138','NAPOLI'),(12,'LEGALE','VIA BRERA 34','20100','MILANO'),(13,'FATTURAZIONE','VIA DELLA SPIGA 67','20121','MILANO'),(14,'LEGALE','VIA MEDITERRANEA 90','90100','PALERMO'),(15,'FATTURAZIONE','VIA SICILIA 45','90141','PALERMO');
/*!40000 ALTER TABLE `Indirizzi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IndirizzoFornitore`
--

DROP TABLE IF EXISTS `IndirizzoFornitore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IndirizzoFornitore` (
  `CodiceFornitore` int NOT NULL,
  `IdIndirizzo` int NOT NULL,
  `Tipo` enum('LEGALE','FATTURAZIONE') NOT NULL DEFAULT 'LEGALE',
  PRIMARY KEY (`CodiceFornitore`,`IdIndirizzo`),
  KEY `FK_IndirizzoFornitore_Indirizzo` (`IdIndirizzo`),
  CONSTRAINT `FK_IndirizzoFornitore_Fornitore` FOREIGN KEY (`CodiceFornitore`) REFERENCES `Fornitore` (`CodiceFornitore`) ON DELETE CASCADE,
  CONSTRAINT `FK_IndirizzoFornitore_Indirizzo` FOREIGN KEY (`IdIndirizzo`) REFERENCES `Indirizzi` (`IdIndirizzo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IndirizzoFornitore`
--

LOCK TABLES `IndirizzoFornitore` WRITE;
/*!40000 ALTER TABLE `IndirizzoFornitore` DISABLE KEYS */;
INSERT INTO `IndirizzoFornitore` VALUES (1,6,'LEGALE'),(1,7,'FATTURAZIONE'),(2,8,'LEGALE'),(2,9,'FATTURAZIONE'),(3,10,'LEGALE'),(3,11,'FATTURAZIONE'),(4,12,'LEGALE'),(4,13,'FATTURAZIONE'),(5,14,'LEGALE'),(5,15,'FATTURAZIONE');
/*!40000 ALTER TABLE `IndirizzoFornitore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Magazzino`
--

DROP TABLE IF EXISTS `Magazzino`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Magazzino` (
  `CodiceSpecie` char(10) NOT NULL,
  `Quantita` int NOT NULL,
  PRIMARY KEY (`CodiceSpecie`),
  CONSTRAINT `FK_Giacenza_Specie` FOREIGN KEY (`CodiceSpecie`) REFERENCES `SpecieDiPiante` (`CodiceSpecie`) ON DELETE CASCADE,
  CONSTRAINT `chk_magazzino_quantita` CHECK ((`Quantita` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Magazzino`
--

LOCK TABLES `Magazzino` WRITE;
/*!40000 ALTER TABLE `Magazzino` DISABLE KEYS */;
INSERT INTO `Magazzino` VALUES ('SP-F001-ES',40),('SP-F001-IN',45),('SP-F002-ES',38),('SP-F002-IN',32),('SP-F003-ES',120),('SP-F003-IN',78),('SP-F004-ES',80),('SP-F004-IN',15),('SP-F005-ES',43),('SP-F005-IN',50),('SP-N001-ES',24),('SP-N001-IN',28),('SP-N002-ES',9),('SP-N002-IN',42),('SP-N003-ES',12),('SP-N003-IN',2),('SP-N004-ES',27),('SP-N004-IN',56),('SP-N005-ES',22),('SP-N005-IN',18);
/*!40000 ALTER TABLE `Magazzino` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OrdineRifornimento`
--

DROP TABLE IF EXISTS `OrdineRifornimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OrdineRifornimento` (
  `IdOrdine` int NOT NULL AUTO_INCREMENT,
  `CodiceFornitore` int NOT NULL,
  `DataOrdine` date NOT NULL,
  `Stato` enum('APERTO','CONFERMATO','SPEDITO','CONSEGNATO','ANNULLATO') NOT NULL DEFAULT 'APERTO',
  `EmailContatto` varchar(100) NOT NULL DEFAULT 'magazzino@verdesrl.it',
  `ViaConsegna` varchar(100) NOT NULL DEFAULT 'Via delle Piante 10',
  `CittaConsegna` varchar(50) NOT NULL DEFAULT 'Roma',
  `CapConsegna` char(5) NOT NULL DEFAULT '00100',
  `Referente` varchar(100) DEFAULT NULL,
  `RecapitoCorriere` varchar(50) DEFAULT NULL,
  `DataCreazione` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `DataModifica` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`IdOrdine`),
  KEY `idx_fornitore_ordine` (`CodiceFornitore`),
  KEY `idx_stato_ordine` (`Stato`),
  KEY `idx_data_ordine` (`DataOrdine`),
  CONSTRAINT `FK_OrdineRifornimento_Fornitore` FOREIGN KEY (`CodiceFornitore`) REFERENCES `Fornitore` (`CodiceFornitore`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OrdineRifornimento`
--

LOCK TABLES `OrdineRifornimento` WRITE;
/*!40000 ALTER TABLE `OrdineRifornimento` DISABLE KEYS */;
INSERT INTO `OrdineRifornimento` VALUES (1,1,'2025-07-25','CONSEGNATO','magazzino@verdesrl.it','Via delle Piante 10','Roma','00100','Luigi Verdi','334-7890123','2025-09-08 19:15:01','2025-09-08 19:15:01'),(2,2,'2025-09-01','SPEDITO','magazzino@verdesrl.it','Via delle Piante 10','Roma','00100','Luigi Verdi','334-7890123','2025-09-08 19:15:01','2025-09-08 19:15:01'),(3,3,'2025-09-05','CONFERMATO','magazzino@verdesrl.it','Via delle Piante 10','Roma','00100','Luigi Verdi','334-7890123','2025-09-08 19:15:01','2025-09-08 19:15:01'),(4,4,'2025-09-08','APERTO','magazzino@verdesrl.it','Via delle Piante 10','Roma','00100','Luigi Verdi','334-7890123','2025-09-08 19:15:01','2025-09-08 19:15:01'),(5,5,'2025-08-25','ANNULLATO','magazzino@verdesrl.it','Via delle Piante 10','Roma','00100','Luigi Verdi','051-7890123','2025-09-08 19:15:01','2025-09-08 19:15:01'),(6,2,'2026-09-26','CONSEGNATO','magazzino@verdesrl.it','Via delle Piante 10','Roma','00100','MARCO GIALLI','3331234567','2026-09-26 08:15:42','2026-09-26 08:16:37');
/*!40000 ALTER TABLE `OrdineRifornimento` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_ordine_rifornimento_stato_update` BEFORE UPDATE ON `ordinerifornimento` FOR EACH ROW BEGIN
  IF OLD.Stato <> NEW.Stato THEN
    IF (OLD.Stato = 'CONSEGNATO' OR OLD.Stato = 'ANNULLATO') THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Non è possibile modificare un ordine già completato o annullato';
    END IF;
    
    IF NEW.Stato = 'CONFERMATO' AND OLD.Stato NOT IN ('APERTO') THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Puoi confermare solo un ordine APERTO';
    END IF;
    
    IF NEW.Stato = 'SPEDITO' AND OLD.Stato NOT IN ('CONFERMATO') THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Puoi spedire solo un ordine CONFERMATO';
    END IF;
    
    IF NEW.Stato = 'CONSEGNATO' AND OLD.Stato NOT IN ('SPEDITO') THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Puoi consegnare solo un ordine SPEDITO';
    END IF;
    
    IF NEW.Stato = 'CONFERMATO' THEN
      IF NOT EXISTS (SELECT 1 FROM DettaglioOrdineRifornimento WHERE IdOrdine = NEW.IdOrdine) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Non è possibile confermare un ordine senza dettagli';
      END IF;
    END IF;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_ordine_rifornimento_stato_update` AFTER UPDATE ON `ordinerifornimento` FOR EACH ROW BEGIN
  IF OLD.Stato != 'CONSEGNATO' AND NEW.Stato = 'CONSEGNATO' THEN
    INSERT INTO `Magazzino` (CodiceSpecie, Quantita)
    SELECT CodiceSpecie, Quantita
    FROM DettaglioOrdineRifornimento
    WHERE IdOrdine = NEW.IdOrdine
    ON DUPLICATE KEY UPDATE
    Quantita = `Magazzino`.Quantita + VALUES(Quantita);
  END IF;
  
  IF OLD.Stato = 'CONSEGNATO' AND NEW.Stato != 'CONSEGNATO' THEN
    UPDATE `Magazzino` g
    JOIN DettaglioOrdineRifornimento d ON g.CodiceSpecie = d.CodiceSpecie
    SET g.Quantita = GREATEST(0, g.Quantita - d.Quantita)
    WHERE d.IdOrdine = NEW.IdOrdine;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `OrdiniDiVendita`
--

DROP TABLE IF EXISTS `OrdiniDiVendita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OrdiniDiVendita` (
  `IdOrdine` int NOT NULL AUTO_INCREMENT,
  `PartitaIVA` char(11) NOT NULL,
  `DataOrdine` date NOT NULL,
  `Stato` enum('APERTO','CONFERMATO','SPEDITO','CONSEGNATO','ANNULLATO') NOT NULL DEFAULT 'APERTO',
  `ViaConsegna` varchar(100) NOT NULL,
  `CittaConsegna` varchar(50) NOT NULL,
  `CAPConsegna` char(5) NOT NULL,
  `Referente` varchar(100) NOT NULL,
  `RecapitoCorriere` varchar(50) NOT NULL,
  PRIMARY KEY (`IdOrdine`),
  KEY `idx_OrdiniDiVendita_Stato` (`Stato`),
  KEY `idx_OrdiniDiVendita_PartitaIVA` (`PartitaIVA`),
  CONSTRAINT `FK_Ordini_Azienda` FOREIGN KEY (`PartitaIVA`) REFERENCES `AziendeRivenditrici` (`PartitaIVA`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OrdiniDiVendita`
--

LOCK TABLES `OrdiniDiVendita` WRITE;
/*!40000 ALTER TABLE `OrdiniDiVendita` DISABLE KEYS */;
INSERT INTO `OrdiniDiVendita` VALUES (1,'12345678901','2025-07-08','CONSEGNATO','Via Roma 12','Roma','00100','Mario Rossi','333-1234567'),(2,'22345678902','2025-09-03','SPEDITO','Via Napoli 8','Napoli','80100','Lucia Bianchi','347-9876543'),(3,'32345678903','2025-09-06','CONFERMATO','Via Milano 15','Milano','20100','Giovanni Verdi','335-7654321'),(4,'42345678904','2025-09-08','APERTO','Corso Torino 22','Torino','10100','Anna Russo','348-1357924'),(5,'52345678905','2025-08-08','ANNULLATO','Via Firenze 45','Firenze','50100','Paolo Gialli','349-2468013'),(6,'12345678901','2025-09-18','APERTO','VIA DELLE PIANTE 10','ROMA','00100','Nessun referente associato all\'ordine','333778899');
/*!40000 ALTER TABLE `OrdiniDiVendita` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_ordine_stato_update` BEFORE UPDATE ON `ordinidivendita` FOR EACH ROW BEGIN
    IF OLD.Stato <> NEW.Stato THEN

        IF OLD.Stato IN ('CONSEGNATO', 'ANNULLATO') THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT =
                'Non è possibile modificare un ordine già completato o annullato';
        END IF;

        IF NEW.Stato = 'CONFERMATO'
           AND OLD.Stato <> 'APERTO' THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT =
                'Puoi confermare solo un ordine APERTO';
        END IF;

        IF NEW.Stato = 'SPEDITO'
           AND OLD.Stato <> 'CONFERMATO' THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT =
                'Puoi spedire solo un ordine confermato';
        END IF;

        IF NEW.Stato = 'CONSEGNATO'
           AND OLD.Stato <> 'SPEDITO' THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT =
                'Puoi consegnare solo un ordine spedito';
        END IF;

    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_ordine_stato_update` AFTER UPDATE ON `ordinidivendita` FOR EACH ROW BEGIN
    IF NEW.Stato = 'ANNULLATO'
       AND OLD.Stato IN ('APERTO', 'CONFERMATO') THEN

        UPDATE Magazzino g
        JOIN DettagliOrdine d
          ON g.CodiceSpecie = d.CodiceSpecie
        SET g.Quantita = g.Quantita + d.Quantita
        WHERE d.IdOrdine = NEW.IdOrdine;

    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_ordine_vendita_delete` BEFORE DELETE ON `ordinidivendita` FOR EACH ROW BEGIN
  IF OLD.`Stato` = 'APERTO' THEN
    INSERT INTO `Magazzino` (`CodiceSpecie`, `Quantita`)
    SELECT d.`CodiceSpecie`, SUM(d.`Quantita`) AS qty
    FROM `DettagliOrdine` d
    WHERE d.`IdOrdine` = OLD.`IdOrdine`
    GROUP BY d.`CodiceSpecie`
    ON DUPLICATE KEY UPDATE
      `Quantita` = `Magazzino`.`Quantita` + VALUES(`Quantita`);
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `PossiedeRecapito`
--

DROP TABLE IF EXISTS `PossiedeRecapito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PossiedeRecapito` (
  `PartitaIVA` char(11) NOT NULL,
  `IdContatto` int NOT NULL,
  PRIMARY KEY (`PartitaIVA`,`IdContatto`),
  KEY `IdContatto` (`IdContatto`),
  CONSTRAINT `possiederecapito_ibfk_1` FOREIGN KEY (`PartitaIVA`) REFERENCES `AziendeRivenditrici` (`PartitaIVA`) ON DELETE CASCADE,
  CONSTRAINT `possiederecapito_ibfk_2` FOREIGN KEY (`IdContatto`) REFERENCES `Contatti` (`IdContatto`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PossiedeRecapito`
--

LOCK TABLES `PossiedeRecapito` WRITE;
/*!40000 ALTER TABLE `PossiedeRecapito` DISABLE KEYS */;
INSERT INTO `PossiedeRecapito` VALUES ('12345678901',1),('32345678903',2),('22345678902',3),('42345678904',4),('52345678905',5),('12345678901',6),('32345678903',7),('22345678902',8),('42345678904',9),('52345678905',10),('12345678901',11),('22345678902',12),('32345678903',13),('42345678904',14),('52345678905',15),('12345678901',16),('22345678902',17),('32345678903',18),('42345678904',19),('52345678905',20);
/*!40000 ALTER TABLE `PossiedeRecapito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ReferentiAziendali`
--

DROP TABLE IF EXISTS `ReferentiAziendali`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ReferentiAziendali` (
  `IdReferente` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(50) NOT NULL,
  `Cognome` varchar(50) NOT NULL,
  PRIMARY KEY (`IdReferente`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ReferentiAziendali`
--

LOCK TABLES `ReferentiAziendali` WRITE;
/*!40000 ALTER TABLE `ReferentiAziendali` DISABLE KEYS */;
INSERT INTO `ReferentiAziendali` VALUES (1,'MARIO','ROSSI'),(2,'LUCIA','BIANCHI'),(3,'GIOVANNI','VERDI'),(4,'ANNA','RUSSO'),(5,'PAOLO','GIALLI');
/*!40000 ALTER TABLE `ReferentiAziendali` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SpecieDiPiante`
--

DROP TABLE IF EXISTS `SpecieDiPiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SpecieDiPiante` (
  `CodiceSpecie` char(10) NOT NULL,
  `NomeComune` varchar(45) NOT NULL,
  `NomeLatino` varchar(45) NOT NULL,
  `Tipologia` enum('APPARTAMENTO','GIARDINO') NOT NULL,
  `Esotica` tinyint NOT NULL DEFAULT '0',
  `Fiorita` tinyint NOT NULL,
  PRIMARY KEY (`CodiceSpecie`),
  KEY `idx_SpecieDiPiante_Tipologia_Fiorita` (`Tipologia`,`Fiorita`),
  KEY `idx_SpecieDiPiante_Esotica` (`Esotica`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SpecieDiPiante`
--

LOCK TABLES `SpecieDiPiante` WRITE;
/*!40000 ALTER TABLE `SpecieDiPiante` DISABLE KEYS */;
INSERT INTO `SpecieDiPiante` VALUES ('SP-F001-ES','GELSOMINO','JASMINUM OFFICINALE','GIARDINO',0,1),('SP-F001-IN','ORCHIDEA PHALAENOPSIS','PHALAENOPSIS AMABILIS','APPARTAMENTO',1,1),('SP-F002-ES','BOUGAINVILLEA','BOUGAINVILLEA SPECTABILIS','GIARDINO',1,1),('SP-F002-IN','ANTHURIUM','ANTHURIUM ANDRAEANUM','APPARTAMENTO',1,1),('SP-F003-ES','SURFINIA','PETUNIA HYBRIDA','GIARDINO',0,1),('SP-F003-IN','KALANCHOE','KALANCHOE BLOSSFELDIANA','APPARTAMENTO',0,1),('SP-F004-ES','GERANIO','PELARGONIUM ZONALE','GIARDINO',0,1),('SP-F004-IN','AZALEA','RHODODENDRON SIMSII','APPARTAMENTO',0,1),('SP-F005-ES','GERBERA','GERBERA JAMESONII','GIARDINO',1,1),('SP-F005-IN','CICLAMINO','CYCLAMEN PERSICUM','APPARTAMENTO',0,1),('SP-N001-ES','PINO MUGO NANO','PINUS MUGO VAR. PUMILIO','GIARDINO',0,0),('SP-N001-IN','FICUS ELASTICA','FICUS ELASTICA','APPARTAMENTO',0,0),('SP-N002-ES','BONSAI DI OLIVO','OLEA EUROPAEA','GIARDINO',0,0),('SP-N002-IN','POTHOS','EPIPREMNUM AUREUM','APPARTAMENTO',1,0),('SP-N003-ES','PALMA DI PHOENIX','PHOENIX CANARIENSIS','GIARDINO',1,0),('SP-N003-IN','DRACENA','DRACAENA MARGINATA','APPARTAMENTO',1,0),('SP-N004-ES','PITOSFORO NANO','PITTOSPORUM TOBIRA NANA','GIARDINO',0,0),('SP-N004-IN','SANSEVIERIA','SANSEVIERIA TRIFASCIATA','APPARTAMENTO',1,0),('SP-N005-ES','CIPRESSO TOSCANO','CUPRESSUS SEMPERVIRENS','GIARDINO',0,0),('SP-N005-IN','PALMA DI ARECA','DYPSIS LUTESCENS','APPARTAMENTO',1,0);
/*!40000 ALTER TABLE `SpecieDiPiante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SpecieFiorita`
--

DROP TABLE IF EXISTS `SpecieFiorita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SpecieFiorita` (
  `CodiceSpecie` char(10) NOT NULL,
  `Colorazioni` enum('ROSSA','GIALLA','ARANCIONE','BLU','VIOLA','BIANCA','MULTICOLORE') NOT NULL,
  PRIMARY KEY (`CodiceSpecie`),
  KEY `idx_SpecieFiorita_Colorazioni` (`Colorazioni`),
  CONSTRAINT `FK_SpecieFiorita_Specie` FOREIGN KEY (`CodiceSpecie`) REFERENCES `SpecieDiPiante` (`CodiceSpecie`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SpecieFiorita`
--

LOCK TABLES `SpecieFiorita` WRITE;
/*!40000 ALTER TABLE `SpecieFiorita` DISABLE KEYS */;
INSERT INTO `SpecieFiorita` VALUES ('SP-F002-IN','ROSSA'),('SP-F003-IN','ARANCIONE'),('SP-F005-ES','ARANCIONE'),('SP-F002-ES','VIOLA'),('SP-F003-ES','VIOLA'),('SP-F004-IN','VIOLA'),('SP-F005-IN','VIOLA'),('SP-F001-ES','BIANCA'),('SP-F001-IN','BIANCA'),('SP-F004-ES','MULTICOLORE');
/*!40000 ALTER TABLE `SpecieFiorita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `StoricoPrezzi`
--

DROP TABLE IF EXISTS `StoricoPrezzi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `StoricoPrezzi` (
  `DataVariazione` datetime(6) NOT NULL,
  `PrezzoPrecedente` decimal(10,2) DEFAULT NULL,
  `PrezzoAttuale` decimal(10,2) NOT NULL,
  `SpeciePianta` char(10) NOT NULL,
  PRIMARY KEY (`DataVariazione`,`SpeciePianta`),
  KEY `idx_StoricoPrezzi_Specie_Data` (`SpeciePianta`,`DataVariazione`),
  CONSTRAINT `FK_StoricoPrezzi_Specie` FOREIGN KEY (`SpeciePianta`) REFERENCES `SpecieDiPiante` (`CodiceSpecie`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `StoricoPrezzi`
--

LOCK TABLES `StoricoPrezzi` WRITE;
/*!40000 ALTER TABLE `StoricoPrezzi` DISABLE KEYS */;
INSERT INTO `StoricoPrezzi` VALUES ('2025-08-08 00:00:00.000000',22.50,24.99,'SP-F001-ES'),('2025-08-08 00:00:00.000000',25.99,27.50,'SP-F001-IN'),('2025-08-08 00:00:00.000000',18.50,19.90,'SP-F002-IN'),('2025-08-08 00:00:00.000000',38.50,42.75,'SP-N001-ES'),('2025-08-08 00:00:00.000000',35.00,39.99,'SP-N001-IN'),('2025-08-08 00:00:00.000000',125.00,140.00,'SP-N003-ES'),('2025-09-08 00:00:00.000000',22.50,24.99,'SP-F001-ES'),('2025-09-08 00:00:00.000000',25.99,27.50,'SP-F001-IN'),('2025-09-08 00:00:00.000000',NULL,28.90,'SP-F002-ES'),('2025-09-08 00:00:00.000000',18.50,19.90,'SP-F002-IN'),('2025-09-08 00:00:00.000000',NULL,8.75,'SP-F003-ES'),('2025-09-08 00:00:00.000000',NULL,12.75,'SP-F003-IN'),('2025-09-08 00:00:00.000000',NULL,10.50,'SP-F004-ES'),('2025-09-08 00:00:00.000000',NULL,15.25,'SP-F004-IN'),('2025-09-08 00:00:00.000000',NULL,12.30,'SP-F005-ES'),('2025-09-08 00:00:00.000000',NULL,9.99,'SP-F005-IN'),('2025-09-08 00:00:00.000000',38.50,42.75,'SP-N001-ES'),('2025-09-08 00:00:00.000000',35.00,39.99,'SP-N001-IN'),('2025-09-08 00:00:00.000000',NULL,55.00,'SP-N002-ES'),('2025-09-08 00:00:00.000000',NULL,15.75,'SP-N002-IN'),('2025-09-08 00:00:00.000000',125.00,140.00,'SP-N003-ES'),('2025-09-08 00:00:00.000000',NULL,22.99,'SP-N003-IN'),('2025-09-08 00:00:00.000000',NULL,19.75,'SP-N004-ES'),('2025-09-08 00:00:00.000000',NULL,18.50,'SP-N004-IN'),('2025-09-08 00:00:00.000000',NULL,65.50,'SP-N005-ES'),('2025-09-08 00:00:00.000000',NULL,45.00,'SP-N005-IN');
/*!40000 ALTER TABLE `StoricoPrezzi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Utenti`
--

DROP TABLE IF EXISTS `Utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Utenti` (
  `Username` varchar(45) NOT NULL,
  `Password` varchar(64) NOT NULL,
  `Ruolo` enum('amministratore','responsabileCommerciale','responsabileLogistico') NOT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Utenti`
--

LOCK TABLES `Utenti` WRITE;
/*!40000 ALTER TABLE `Utenti` DISABLE KEYS */;
INSERT INTO `Utenti` VALUES ('admin','0d38a29a727a8f0c87cd8918585260f59a34da92003810484a3a2105214b2c0e','amministratore'),('luigiverdi','980bffe3395f018ade823bc35affe006b0b662becbcaeaea2dbc0ed7e820e2fa','responsabileLogistico'),('mariorossi','2b77c24cf7a861811c81724fc5c481be11de54edba66aecb0d0a729f758158f8','responsabileCommerciale');
/*!40000 ALTER TABLE `Utenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_giacenzecritiche`
--

DROP TABLE IF EXISTS `v_giacenzecritiche`;
/*!50001 DROP VIEW IF EXISTS `v_giacenzecritiche`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_giacenzecritiche` AS SELECT 
 1 AS `CodiceSpecie`,
 1 AS `NomeComune`,
 1 AS `NomeLatino`,
 1 AS `Quantita`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_infopiante`
--

DROP TABLE IF EXISTS `v_infopiante`;
/*!50001 DROP VIEW IF EXISTS `v_infopiante`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_infopiante` AS SELECT 
 1 AS `CodiceSpecie`,
 1 AS `NomeComune`,
 1 AS `NomeLatino`,
 1 AS `Tipologia`,
 1 AS `Esotica`,
 1 AS `Fiorita`,
 1 AS `Colorazioni`,
 1 AS `PrezzoAttuale`,
 1 AS `Giacenza`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_reportvenditespecie`
--

DROP TABLE IF EXISTS `v_reportvenditespecie`;
/*!50001 DROP VIEW IF EXISTS `v_reportvenditespecie`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_reportvenditespecie` AS SELECT 
 1 AS `CodiceSpecie`,
 1 AS `NomeComune`,
 1 AS `Tipologia`,
 1 AS `Fiorita`,
 1 AS `QuantitaVenduta`,
 1 AS `ValoreTotaleVendite`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping events for database 'verde_srl'
--

--
-- Dumping routines for database 'verde_srl'
--
/*!50003 DROP FUNCTION IF EXISTS `CalcolaValoreTotaleOrdine` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `CalcolaValoreTotaleOrdine`(p_idOrdine INT) RETURNS decimal(12,2)
    READS SQL DATA
BEGIN
    DECLARE totale DECIMAL(12,2);

    SELECT COALESCE(SUM(Quantita * PrezzoUnitario), 0)
    INTO totale
    FROM DettagliOrdine
    WHERE IdOrdine = p_idOrdine;

    RETURN totale;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `VerificaGiacenzeSufficienti` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `VerificaGiacenzeSufficienti`(
    p_codiceSpecie CHAR(10),
    p_quantita INT
) RETURNS tinyint(1)
    READS SQL DATA
BEGIN
    DECLARE v_giacenzaDisponibile INT;

    SELECT Quantita
    INTO v_giacenzaDisponibile
    FROM Magazzino
    WHERE CodiceSpecie = p_codiceSpecie;

    IF v_giacenzaDisponibile IS NULL THEN
        RETURN FALSE;
    END IF;

    RETURN v_giacenzaDisponibile >= p_quantita;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `EliminaAziendaRivenditrice` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `EliminaAziendaRivenditrice`(
    IN p_partitaIVA CHAR(11)
)
BEGIN
    DECLARE idLegale INT;
    DECLARE idFatt INT;
    DECLARE idRef INT;
    DECLARE ordiniAssociati INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    SELECT IdIndirizzoLegale, IdIndirizzoFatturazione, IdReferente
    INTO idLegale, idFatt, idRef
    FROM AziendeRivenditrici
    WHERE PartitaIVA = p_partitaIVA
    FOR UPDATE;

    IF idLegale IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Azienda non trovata.';
    END IF;

    SELECT COUNT(*) INTO ordiniAssociati
    FROM OrdiniDiVendita
    WHERE PartitaIVA = p_partitaIVA
    FOR UPDATE;

    IF ordiniAssociati > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Impossibile eliminare l azienda: esistono ordini di vendita associati.';
    END IF;

    DELETE FROM PossiedeRecapito WHERE PartitaIVA = p_partitaIVA;
    DELETE FROM AziendeRivenditrici WHERE PartitaIVA = p_partitaIVA;

    DELETE FROM Indirizzi
    WHERE IdIndirizzo = idLegale
      AND NOT EXISTS (SELECT 1 FROM AziendeRivenditrici a WHERE a.IdIndirizzoLegale = idLegale OR a.IdIndirizzoFatturazione = idLegale)
      AND NOT EXISTS (SELECT 1 FROM IndirizzoFornitore f WHERE f.IdIndirizzo = idLegale);

    IF idFatt IS NOT NULL AND idFatt <> idLegale THEN
        DELETE FROM Indirizzi
        WHERE IdIndirizzo = idFatt
          AND NOT EXISTS (SELECT 1 FROM AziendeRivenditrici a WHERE a.IdIndirizzoLegale = idFatt OR a.IdIndirizzoFatturazione = idFatt)
          AND NOT EXISTS (SELECT 1 FROM IndirizzoFornitore f WHERE f.IdIndirizzo = idFatt);
    END IF;

    DELETE FROM ReferentiAziendali
    WHERE IdReferente = idRef
      AND NOT EXISTS (SELECT 1 FROM AziendeRivenditrici a WHERE a.IdReferente = idRef);

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `EliminaFornitore` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `EliminaFornitore`(
    IN p_CodiceFornitore INT
)
BEGIN
    DECLARE ordiniAssociati INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    IF NOT EXISTS (
        SELECT 1 FROM Fornitore
        WHERE CodiceFornitore = p_CodiceFornitore
        FOR UPDATE
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Fornitore non trovato.';
    END IF;

    SELECT COUNT(*) 
      INTO ordiniAssociati
    FROM OrdineRifornimento 
    WHERE CodiceFornitore = p_CodiceFornitore
    FOR UPDATE;

    IF ordiniAssociati > 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Impossibile eliminare il fornitore ha ordini di rifornimento associati.';
    END IF;

    DELETE i
    FROM Indirizzi i
    INNER JOIN IndirizzoFornitore target
        ON target.IdIndirizzo = i.IdIndirizzo
       AND target.CodiceFornitore = p_CodiceFornitore
    WHERE NOT EXISTS (
        SELECT 1
        FROM IndirizzoFornitore other
        WHERE other.IdIndirizzo = i.IdIndirizzo
          AND other.CodiceFornitore <> p_CodiceFornitore
    )
      AND NOT EXISTS (
        SELECT 1
        FROM AziendeRivenditrici a
        WHERE a.IdIndirizzoLegale = i.IdIndirizzo
           OR a.IdIndirizzoFatturazione = i.IdIndirizzo
    );

    DELETE FROM IndirizzoFornitore
    WHERE CodiceFornitore = p_CodiceFornitore;

    DELETE FROM Fornitore WHERE CodiceFornitore = p_CodiceFornitore;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `EliminaFornituraSpecie` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `EliminaFornituraSpecie`(
    IN p_CodiceFornitore INT,
    IN p_CodiceSpecie CHAR(10)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    UPDATE FornituraSpecie 
       SET Attiva = FALSE 
     WHERE CodiceFornitore = p_CodiceFornitore 
       AND CodiceSpecie = p_CodiceSpecie;

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Nessuna fornitura trovata.';
    END IF;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `EliminaOrdineRifornimento` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `EliminaOrdineRifornimento`(
    IN p_idOrdine INT
)
BEGIN
    DECLARE v_stato VARCHAR(20);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    SET v_stato = NULL;

    SELECT Stato INTO v_stato
    FROM OrdineRifornimento
    WHERE IdOrdine = p_idOrdine
    FOR UPDATE;

    IF v_stato IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Ordine di rifornimento non trovato';
    END IF;

    IF v_stato <> 'APERTO' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Si possono eliminare solamente ordini aperti!';
    END IF;

    DELETE FROM OrdineRifornimento
    WHERE IdOrdine = p_idOrdine;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `EliminaOrdineVendita` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `EliminaOrdineVendita`(IN p_idOrdine INT)
BEGIN
    DECLARE v_stato VARCHAR(20);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    SELECT Stato INTO v_stato
    FROM OrdiniDiVendita
    WHERE IdOrdine = p_idOrdine
    FOR UPDATE;

    IF v_stato IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Ordine non trovato';
    END IF;

    IF v_stato <> 'APERTO' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Si possono eliminare solamente ordini aperti!';
    END IF;

    DELETE FROM OrdiniDiVendita
    WHERE IdOrdine = p_idOrdine;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `EliminaSpecie` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `EliminaSpecie`(IN pCodiceSpecie CHAR(10))
BEGIN
    DECLARE v_ordini_vendita_count INT DEFAULT 0;
    DECLARE v_ordini_rifornimento_count INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    IF NOT EXISTS (
        SELECT 1
        FROM SpecieDiPiante
        WHERE CodiceSpecie = pCodiceSpecie
        FOR UPDATE
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Nessuna specie trovata da rimuovere.';
    END IF;

    SELECT COUNT(*)
    INTO v_ordini_vendita_count
    FROM DettagliOrdine
    WHERE CodiceSpecie = pCodiceSpecie
    FOR UPDATE;

    SELECT COUNT(*)
    INTO v_ordini_rifornimento_count
    FROM DettaglioOrdineRifornimento
    WHERE CodiceSpecie = pCodiceSpecie
    FOR UPDATE;

    IF v_ordini_vendita_count > 0 OR v_ordini_rifornimento_count > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Impossibile eliminare la specie presente in ordini di vendita o rifornimento.';
    END IF;

    DELETE FROM SpecieDiPiante
    WHERE CodiceSpecie = pCodiceSpecie;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `EliminaUtente` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `EliminaUtente`(
    IN var_Username VARCHAR(45)
)
BEGIN
    DECLARE v_rows INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    DELETE FROM Utenti
    WHERE Username = var_Username;

    SET v_rows = ROW_COUNT();

    IF v_rows = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Utente non trovato.';
    END IF;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InserisciAziendaRivenditrice` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InserisciAziendaRivenditrice`(
    IN pPartitaIVA CHAR(11),
    IN pNomeAzienda VARCHAR(100),
    IN pNomeReferente VARCHAR(50),
    IN pCognomeReferente VARCHAR(50),
    IN pViaLegale VARCHAR(100),
    IN pCapLegale CHAR(5),
    IN pCittaLegale VARCHAR(50),
    IN pViaFatturazione VARCHAR(100),
    IN pCapFatturazione CHAR(5),
    IN pCittaFatturazione VARCHAR(50),
    IN pContatti JSON
)
BEGIN
    DECLARE vIdReferente INT;
    DECLARE vIdIndirizzoLegale INT;
    DECLARE vIdIndirizzoFatturazione INT;
    DECLARE vIndex INT DEFAULT 0;
    DECLARE vCount INT DEFAULT 0;
    DECLARE vTipoContatto VARCHAR(20);
    DECLARE vValoreContatto VARCHAR(100);
    DECLARE vIdContatto INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    IF pPartitaIVA IS NULL OR pPartitaIVA NOT REGEXP '^[0-9]{11}$' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Partita IVA non valida.';
    END IF;

    IF pNomeAzienda IS NULL OR CHAR_LENGTH(TRIM(pNomeAzienda)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nome azienda obbligatorio.';
    END IF;

    IF pNomeReferente IS NULL OR CHAR_LENGTH(TRIM(pNomeReferente)) = 0
       OR pCognomeReferente IS NULL OR CHAR_LENGTH(TRIM(pCognomeReferente)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Referente aziendale obbligatorio.';
    END IF;

    IF pViaLegale IS NULL OR CHAR_LENGTH(TRIM(pViaLegale)) = 0
       OR pCapLegale IS NULL OR pCapLegale NOT REGEXP '^[0-9]{5}$'
       OR pCittaLegale IS NULL OR CHAR_LENGTH(TRIM(pCittaLegale)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Indirizzo legale non valido.';
    END IF;

    IF pViaFatturazione IS NULL OR CHAR_LENGTH(TRIM(pViaFatturazione)) = 0
       OR pCapFatturazione IS NULL OR pCapFatturazione NOT REGEXP '^[0-9]{5}$'
       OR pCittaFatturazione IS NULL OR CHAR_LENGTH(TRIM(pCittaFatturazione)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Indirizzo di fatturazione non valido.';
    END IF;

    IF pContatti IS NULL OR JSON_TYPE(pContatti) <> 'ARRAY' OR JSON_LENGTH(pContatti) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aggiungere almeno un contatto aziendale.';
    END IF;

    IF EXISTS (SELECT 1 FROM AziendeRivenditrici WHERE PartitaIVA = pPartitaIVA) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Partita IVA già presente.';
    END IF;

    INSERT INTO ReferentiAziendali (Nome, Cognome)
    VALUES (TRIM(pNomeReferente), TRIM(pCognomeReferente));
    SET vIdReferente = LAST_INSERT_ID();

    INSERT INTO Indirizzi (TipoIndirizzo, Via, CAP, Citta)
    VALUES ('LEGALE', TRIM(pViaLegale), pCapLegale, TRIM(pCittaLegale));
    SET vIdIndirizzoLegale = LAST_INSERT_ID();

    INSERT INTO Indirizzi (TipoIndirizzo, Via, CAP, Citta)
    VALUES ('FATTURAZIONE', TRIM(pViaFatturazione), pCapFatturazione, TRIM(pCittaFatturazione));
    SET vIdIndirizzoFatturazione = LAST_INSERT_ID();

    INSERT INTO AziendeRivenditrici (PartitaIVA, NomeAzienda, IdIndirizzoLegale, IdIndirizzoFatturazione, IdReferente)
    VALUES (pPartitaIVA, TRIM(pNomeAzienda), vIdIndirizzoLegale, vIdIndirizzoFatturazione, vIdReferente);

    SET vCount = JSON_LENGTH(pContatti);
    WHILE vIndex < vCount DO
        SET vTipoContatto = UPPER(TRIM(JSON_UNQUOTE(JSON_EXTRACT(pContatti, CONCAT('$[', vIndex, '].type')))));
        SET vValoreContatto = TRIM(JSON_UNQUOTE(JSON_EXTRACT(pContatti, CONCAT('$[', vIndex, '].value'))));

        IF vTipoContatto IS NULL OR vTipoContatto NOT IN ('TELEFONO', 'CELLULARE', 'EMAIL') THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo contatto non valido.';
        END IF;
        IF vValoreContatto IS NULL OR CHAR_LENGTH(vValoreContatto) = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Valore contatto obbligatorio.';
        END IF;

        INSERT IGNORE INTO Contatti (TipoContatto, ValoreContatto) VALUES (vTipoContatto, vValoreContatto);
        SELECT IdContatto INTO vIdContatto FROM Contatti
        WHERE TipoContatto = vTipoContatto AND ValoreContatto = vValoreContatto LIMIT 1;
        INSERT INTO PossiedeRecapito (PartitaIVA, IdContatto) VALUES (pPartitaIVA, vIdContatto);
        SET vIndex = vIndex + 1;
    END WHILE;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InserisciFornitoreCompleto` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InserisciFornitoreCompleto`(
    IN p_Nome VARCHAR(100),
    IN p_CodiceFiscale VARCHAR(16),
    IN p_Indirizzi JSON,
    OUT p_CodiceFornitore INT
)
BEGIN
    DECLARE v_index INT DEFAULT 0;
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_tipo VARCHAR(20);
    DECLARE v_via VARCHAR(100);
    DECLARE v_cap VARCHAR(5);
    DECLARE v_citta VARCHAR(50);
    DECLARE v_idIndirizzo INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    IF p_Nome IS NULL OR CHAR_LENGTH(TRIM(p_Nome)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nome fornitore obbligatorio.';
    END IF;

    IF p_CodiceFiscale IS NULL OR CHAR_LENGTH(TRIM(p_CodiceFiscale)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Codice fiscale obbligatorio.';
    END IF;

    IF p_Indirizzi IS NULL OR JSON_TYPE(p_Indirizzi) <> 'ARRAY' OR JSON_LENGTH(p_Indirizzi) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Un fornitore deve avere almeno un indirizzo.';
    END IF;

    IF EXISTS (SELECT 1 FROM Fornitore WHERE CodiceFiscale = p_CodiceFiscale) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Codice fiscale già presente.';
    END IF;

    INSERT INTO Fornitore(Nome, CodiceFiscale)
    VALUES (TRIM(p_Nome), UPPER(TRIM(p_CodiceFiscale)));

    SET p_CodiceFornitore = LAST_INSERT_ID();
    SET v_count = JSON_LENGTH(p_Indirizzi);

    WHILE v_index < v_count DO
        SET v_tipo = JSON_UNQUOTE(JSON_EXTRACT(p_Indirizzi, CONCAT('$[', v_index, '].type')));
        SET v_via = JSON_UNQUOTE(JSON_EXTRACT(p_Indirizzi, CONCAT('$[', v_index, '].street')));
        SET v_cap = JSON_UNQUOTE(JSON_EXTRACT(p_Indirizzi, CONCAT('$[', v_index, '].postalCode')));
        SET v_citta = JSON_UNQUOTE(JSON_EXTRACT(p_Indirizzi, CONCAT('$[', v_index, '].city')));

        IF v_tipo IS NULL OR v_tipo NOT IN ('LEGALE', 'FATTURAZIONE') THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo indirizzo non valido.';
        END IF;

        IF v_via IS NULL OR CHAR_LENGTH(TRIM(v_via)) = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Via indirizzo obbligatoria.';
        END IF;

        IF v_cap IS NULL OR v_cap NOT REGEXP '^[0-9]{5}$' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'CAP indirizzo non valido.';
        END IF;

        IF v_citta IS NULL OR CHAR_LENGTH(TRIM(v_citta)) = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Città indirizzo obbligatoria.';
        END IF;

        INSERT INTO Indirizzi(TipoIndirizzo, Via, CAP, Citta)
        VALUES (v_tipo, TRIM(v_via), v_cap, TRIM(v_citta));

        SET v_idIndirizzo = LAST_INSERT_ID();

        INSERT INTO IndirizzoFornitore(CodiceFornitore, IdIndirizzo, Tipo)
        VALUES (p_CodiceFornitore, v_idIndirizzo, v_tipo);

        SET v_index = v_index + 1;
    END WHILE;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InserisciFornituraSpecie` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InserisciFornituraSpecie`(
    IN p_CodiceFornitore INT,
    IN p_CodiceSpecie CHAR(10)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;
    
    IF NOT EXISTS (
        SELECT 1 FROM Fornitore 
        WHERE CodiceFornitore = p_CodiceFornitore
        FOR UPDATE
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Fornitore non trovato.';
    END IF;
    
    IF NOT EXISTS (
        SELECT 1 FROM SpecieDiPiante 
        WHERE CodiceSpecie = p_CodiceSpecie
        FOR UPDATE
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Specie non trovata.';
    END IF;
    
    INSERT INTO FornituraSpecie (CodiceFornitore, CodiceSpecie, Quantita, Attiva)
    VALUES (p_CodiceFornitore, p_CodiceSpecie, 0, TRUE)
    ON DUPLICATE KEY UPDATE Attiva = TRUE;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InserisciGiacenza` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InserisciGiacenza`(
    IN var_CodiceSpecie CHAR(10),
    IN var_Quantita INT
)
BEGIN
    DECLARE EXIT HANDLER FOR 1062
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Questa specie ha gia una giacenza, può essere solo modificata.';
    END;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    IF var_Quantita < 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La quantità di magazzino non può essere negativa.';
    END IF;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    INSERT INTO Magazzino (CodiceSpecie, Quantita)
    VALUES (var_CodiceSpecie, var_Quantita);

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InserisciOrdineRifornimentoCompleto` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InserisciOrdineRifornimentoCompleto`(
    IN p_codiceFornitore INT,
    IN p_dataOrdine DATE,
    IN p_emailContatto VARCHAR(100),
    IN p_viaConsegna VARCHAR(100),
    IN p_cittaConsegna VARCHAR(50),
    IN p_capConsegna CHAR(5),
    IN p_referente VARCHAR(100),
    IN p_recapitoCorriere VARCHAR(50),
    IN p_dettagli JSON,
    OUT p_idOrdine INT
)
BEGIN
    DECLARE v_index INT DEFAULT 0;
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_codiceSpecie CHAR(10);
    DECLARE v_quantita INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    IF p_codiceFornitore IS NULL OR p_codiceFornitore <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Codice fornitore non valido';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM Fornitore
        WHERE CodiceFornitore = p_codiceFornitore
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Fornitore non trovato';
    END IF;

    IF p_dettagli IS NULL OR JSON_TYPE(p_dettagli) <> 'ARRAY' OR JSON_LENGTH(p_dettagli) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Un ordine di rifornimento deve contenere almeno un dettaglio';
    END IF;

    INSERT INTO OrdineRifornimento (
        CodiceFornitore, DataOrdine, Stato, EmailContatto,
        ViaConsegna, CittaConsegna, CapConsegna, Referente, RecapitoCorriere
    ) VALUES (
        p_codiceFornitore, p_dataOrdine, 'APERTO', p_emailContatto,
        p_viaConsegna, p_cittaConsegna, p_capConsegna, p_referente, p_recapitoCorriere
    );

    SET p_idOrdine = LAST_INSERT_ID();
    SET v_count = JSON_LENGTH(p_dettagli);

    WHILE v_index < v_count DO
        SET v_codiceSpecie = JSON_UNQUOTE(JSON_EXTRACT(p_dettagli, CONCAT('$[', v_index, '].speciesCode')));
        SET v_quantita = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_dettagli, CONCAT('$[', v_index, '].quantity'))) AS UNSIGNED);

        IF v_codiceSpecie IS NULL OR CHAR_LENGTH(TRIM(v_codiceSpecie)) = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Codice specie non valido';
        END IF;

        IF v_quantita IS NULL OR v_quantita <= 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La quantità deve essere maggiore di zero';
        END IF;

        IF NOT EXISTS (
            SELECT 1
            FROM FornituraSpecie
            WHERE CodiceFornitore = p_codiceFornitore
              AND CodiceSpecie = v_codiceSpecie
              AND Attiva = TRUE
        ) THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il fornitore non può fornire una delle specie richieste';
        END IF;

        INSERT INTO DettaglioOrdineRifornimento (IdOrdine, CodiceSpecie, Quantita)
        VALUES (p_idOrdine, v_codiceSpecie, v_quantita);

        SET v_index = v_index + 1;
    END WHILE;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InserisciOrdineVenditaCompleto` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InserisciOrdineVenditaCompleto`(
    IN p_partitaIVA CHAR(11),
    IN p_dataOrdine DATE,
    IN p_viaConsegna VARCHAR(100),
    IN p_cittaConsegna VARCHAR(50),
    IN p_capConsegna CHAR(5),
    IN p_referente VARCHAR(100),
    IN p_recapitoCorriere VARCHAR(50),
    IN p_dettagli JSON,
    OUT p_idOrdine INT
)
BEGIN
    DECLARE v_index INT DEFAULT 0;
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_codiceSpecie CHAR(10);
    DECLARE v_quantita INT;
    DECLARE v_prezzoUnitario DECIMAL(10,2);
    DECLARE v_giacenza INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    IF p_partitaIVA IS NULL OR CHAR_LENGTH(TRIM(p_partitaIVA)) <> 11 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Partita IVA non valida';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM AziendeRivenditrici
        WHERE PartitaIVA = p_partitaIVA
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Azienda non trovata';
    END IF;

    IF p_dettagli IS NULL OR JSON_TYPE(p_dettagli) <> 'ARRAY' OR JSON_LENGTH(p_dettagli) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Un ordine di vendita deve contenere almeno un dettaglio';
    END IF;

    INSERT INTO OrdiniDiVendita (
        PartitaIVA, DataOrdine, Stato, ViaConsegna,
        CittaConsegna, CAPConsegna, Referente, RecapitoCorriere
    ) VALUES (
        p_partitaIVA, p_dataOrdine, 'APERTO', p_viaConsegna,
        p_cittaConsegna, p_capConsegna, p_referente, p_recapitoCorriere
    );

    SET p_idOrdine = LAST_INSERT_ID();
    SET v_count = JSON_LENGTH(p_dettagli);

    WHILE v_index < v_count DO
        SET v_codiceSpecie = JSON_UNQUOTE(JSON_EXTRACT(p_dettagli, CONCAT('$[', v_index, '].speciesCode')));
        SET v_quantita = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_dettagli, CONCAT('$[', v_index, '].quantity'))) AS UNSIGNED);
        SET v_prezzoUnitario = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_dettagli, CONCAT('$[', v_index, '].unitPrice'))) AS DECIMAL(10,2));

        IF v_codiceSpecie IS NULL OR CHAR_LENGTH(TRIM(v_codiceSpecie)) = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Codice specie non valido';
        END IF;

        IF v_quantita IS NULL OR v_quantita <= 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La quantità deve essere maggiore di zero';
        END IF;

        IF v_prezzoUnitario IS NULL OR v_prezzoUnitario <= 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il prezzo unitario deve essere maggiore di zero';
        END IF;

        SET v_giacenza = NULL;
        SELECT Quantita INTO v_giacenza
        FROM Magazzino
        WHERE CodiceSpecie = v_codiceSpecie
        FOR UPDATE;

        IF v_giacenza IS NULL THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Specie non presente in magazzino';
        END IF;

        IF v_giacenza < v_quantita THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Giacenza insufficiente per soddisfare l ordine';
        END IF;

        INSERT INTO DettagliOrdine (IdOrdine, CodiceSpecie, Quantita, PrezzoUnitario)
        VALUES (p_idOrdine, v_codiceSpecie, v_quantita, v_prezzoUnitario);

        SET v_index = v_index + 1;
    END WHILE;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InserisciSpecieConPrezzo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InserisciSpecieConPrezzo`(
    IN p_nome_comune VARCHAR(45),
    IN p_nome_latino VARCHAR(45),
    IN p_tipologia ENUM('APPARTAMENTO', 'GIARDINO'),
    IN p_esotica TINYINT,
    IN p_fiorita TINYINT,
    IN p_colorazione ENUM('ROSSA', 'GIALLA', 'ARANCIONE', 'BLU', 'VIOLA', 'BIANCA', 'MULTICOLORE'),
    IN p_prezzo_iniziale DECIMAL(10,2),
    OUT p_codice_specie CHAR(10)
)
BEGIN
    DECLARE v_tipo_fioritura CHAR(1);
    DECLARE v_tipo_ambiente CHAR(2);
    DECLARE v_categoria CHAR(3);
    DECLARE v_nuovo_numero INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_codice_specie = NULL;
        RESIGNAL;
    END;

    IF p_prezzo_iniziale IS NULL OR p_prezzo_iniziale <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Il prezzo iniziale deve essere maggiore di zero.';
    END IF;

    IF p_fiorita = 1 AND p_colorazione IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Colorazione obbligatoria per specie fiorita.';
    END IF;

    SET v_tipo_fioritura = IF(p_fiorita = 1, 'F', 'N');
    SET v_tipo_ambiente = IF(p_tipologia = 'APPARTAMENTO', 'IN', 'ES');
    SET v_categoria = CONCAT(v_tipo_fioritura, v_tipo_ambiente);

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    INSERT INTO CodiceSpecieProgressivo (Categoria, UltimoNumero)
    VALUES (v_categoria, 0)
    ON DUPLICATE KEY UPDATE Categoria = VALUES(Categoria);

    SELECT UltimoNumero + 1
    INTO v_nuovo_numero
    FROM CodiceSpecieProgressivo
    WHERE Categoria = v_categoria
    FOR UPDATE;

    UPDATE CodiceSpecieProgressivo
    SET UltimoNumero = v_nuovo_numero
    WHERE Categoria = v_categoria;

    SET p_codice_specie = CONCAT(
        'SP-', v_tipo_fioritura, LPAD(v_nuovo_numero, 3, '0'), '-', v_tipo_ambiente
    );

    INSERT INTO SpecieDiPiante (
        CodiceSpecie, NomeComune, NomeLatino, Tipologia, Esotica, Fiorita
    ) VALUES (
        p_codice_specie, p_nome_comune, p_nome_latino, p_tipologia, p_esotica, p_fiorita
    );

    IF p_fiorita = 1 THEN
        INSERT INTO SpecieFiorita (CodiceSpecie, Colorazioni)
        VALUES (p_codice_specie, p_colorazione);
    END IF;

    INSERT INTO StoricoPrezzi (
        DataVariazione, PrezzoPrecedente, PrezzoAttuale, SpeciePianta
    ) VALUES (
        NOW(6), NULL, p_prezzo_iniziale, p_codice_specie
    );

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InserisciUtente` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InserisciUtente`(
    IN var_Username VARCHAR(45),
    IN var_Password VARCHAR(64),
    IN var_Ruolo ENUM('amministratore', 'responsabileCommerciale', 'responsabileLogistico')
)
BEGIN
    DECLARE EXIT HANDLER FOR 1062
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Username già esistente.';
    END;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    INSERT INTO Utenti (Username, Password, Ruolo)
    VALUES (var_Username, SHA2(var_Password, 256), var_Ruolo);

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InserisciVariazionePrezzo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InserisciVariazionePrezzo`(
    IN var_SpeciePianta CHAR(10),
    IN var_NuovoPrezzo DECIMAL(10,2)
)
BEGIN
    DECLARE var_PrezzoCorrente DECIMAL(10,2) DEFAULT NULL;
    DECLARE var_DataAttuale DATETIME(6);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    IF var_SpeciePianta IS NULL OR CHAR_LENGTH(TRIM(var_SpeciePianta)) = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Il codice della specie è obbligatorio.';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM SpecieDiPiante
        WHERE CodiceSpecie = var_SpeciePianta
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Specie non trovata.';
    END IF;

    IF var_NuovoPrezzo IS NULL OR var_NuovoPrezzo <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Il prezzo deve essere maggiore di zero.';
    END IF;

    SELECT PrezzoAttuale
    INTO var_PrezzoCorrente
    FROM StoricoPrezzi
    WHERE SpeciePianta = var_SpeciePianta
    ORDER BY DataVariazione DESC
    LIMIT 1
    FOR UPDATE;

    IF var_PrezzoCorrente IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Nessun prezzo corrente trovato per la specie.';
    END IF;

    SET var_DataAttuale = NOW(6);

    INSERT INTO StoricoPrezzi (
        DataVariazione, PrezzoPrecedente, PrezzoAttuale, SpeciePianta
    ) VALUES (
        var_DataAttuale, var_PrezzoCorrente, var_NuovoPrezzo, var_SpeciePianta
    );

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `login` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `login`(
    IN  var_Username VARCHAR(45), 
    IN  var_pass VARCHAR(64), 
    OUT var_role INT,
    OUT var_error_code INT,
    OUT var_error_message VARCHAR(100)
)
BEGIN
    DECLARE var_user_role ENUM('amministratore', 'responsabileCommerciale', 'responsabileLogistico');
    DECLARE var_user_exists INT DEFAULT 0;

    SET var_role = -1;
    SET var_error_code = NULL;
    SET var_error_message = NULL;

    SELECT COUNT(*) INTO var_user_exists
    FROM Utenti
    WHERE Username = var_Username;

    IF var_user_exists = 0 THEN
        SET var_role = -1;
        SET var_error_code = 1;
        SET var_error_message = 'Username non trovato';
    ELSE
        SELECT Ruolo INTO var_user_role
        FROM Utenti
        WHERE Username = var_Username
          AND Password = SHA2(var_pass, 256)
        LIMIT 1;

        IF var_user_role IS NULL THEN
            SET var_role = -1;
            SET var_error_code = 2;
            SET var_error_message = 'Password errata';
        ELSE
            IF var_user_role = 'amministratore' THEN
                SET var_role = 1;
            ELSEIF var_user_role = 'responsabileCommerciale' THEN
                SET var_role = 2;
            ELSEIF var_user_role = 'responsabileLogistico' THEN
                SET var_role = 3;
            ELSE
                SET var_role = -1;
                SET var_error_code = 3;
                SET var_error_message = 'Ruolo non valido';
            END IF;

            IF var_role > 0 THEN
                SET var_error_code = 0;
                SET var_error_message = 'Login riuscito';
            END IF;
        END IF;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ModificaAziendaRivenditrice` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ModificaAziendaRivenditrice`(
    IN p_partitaIVA CHAR(11),
    IN p_nomeAzienda VARCHAR(100),
    IN p_nomeReferente VARCHAR(50),
    IN p_cognomeReferente VARCHAR(50),
    IN p_viaLegale VARCHAR(100),
    IN p_capLegale CHAR(5),
    IN p_cittaLegale VARCHAR(50),
    IN p_viaFatturazione VARCHAR(100),
    IN p_capFatturazione CHAR(5),
    IN p_cittaFatturazione VARCHAR(50),
    IN p_contatti JSON
)
BEGIN
    DECLARE idLegale INT;
    DECLARE idFatt INT;
    DECLARE idRef INT;
    DECLARE nuovoIdLegale INT;
    DECLARE nuovoIdFatt INT;
    DECLARE nuovoIdRef INT;
    DECLARE vIndex INT DEFAULT 0;
    DECLARE vCount INT DEFAULT 0;
    DECLARE vTipoContatto VARCHAR(20);
    DECLARE vValoreContatto VARCHAR(100);
    DECLARE vIdContatto INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    SELECT IdIndirizzoLegale, IdIndirizzoFatturazione, IdReferente
    INTO idLegale, idFatt, idRef
    FROM AziendeRivenditrici
    WHERE PartitaIVA = p_partitaIVA
    FOR UPDATE;

    IF idLegale IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Azienda non trovata.';
    END IF;

    IF p_nomeAzienda IS NULL OR CHAR_LENGTH(TRIM(p_nomeAzienda)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nome azienda obbligatorio.';
    END IF;
    IF p_nomeReferente IS NULL OR CHAR_LENGTH(TRIM(p_nomeReferente)) = 0
       OR p_cognomeReferente IS NULL OR CHAR_LENGTH(TRIM(p_cognomeReferente)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Referente aziendale obbligatorio.';
    END IF;
    IF p_viaLegale IS NULL OR CHAR_LENGTH(TRIM(p_viaLegale)) = 0
       OR p_capLegale IS NULL OR p_capLegale NOT REGEXP '^[0-9]{5}$'
       OR p_cittaLegale IS NULL OR CHAR_LENGTH(TRIM(p_cittaLegale)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Indirizzo legale non valido.';
    END IF;
    IF p_viaFatturazione IS NULL OR CHAR_LENGTH(TRIM(p_viaFatturazione)) = 0
       OR p_capFatturazione IS NULL OR p_capFatturazione NOT REGEXP '^[0-9]{5}$'
       OR p_cittaFatturazione IS NULL OR CHAR_LENGTH(TRIM(p_cittaFatturazione)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Indirizzo di fatturazione non valido.';
    END IF;
    IF p_contatti IS NULL OR JSON_TYPE(p_contatti) <> 'ARRAY' OR JSON_LENGTH(p_contatti) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aggiungere almeno un contatto aziendale.';
    END IF;

    INSERT INTO ReferentiAziendali (Nome, Cognome)
    VALUES (TRIM(p_nomeReferente), TRIM(p_cognomeReferente));
    SET nuovoIdRef = LAST_INSERT_ID();

    INSERT INTO Indirizzi (TipoIndirizzo, Via, CAP, Citta)
    VALUES ('LEGALE', TRIM(p_viaLegale), p_capLegale, TRIM(p_cittaLegale));
    SET nuovoIdLegale = LAST_INSERT_ID();

    INSERT INTO Indirizzi (TipoIndirizzo, Via, CAP, Citta)
    VALUES ('FATTURAZIONE', TRIM(p_viaFatturazione), p_capFatturazione, TRIM(p_cittaFatturazione));
    SET nuovoIdFatt = LAST_INSERT_ID();

    UPDATE AziendeRivenditrici
    SET NomeAzienda = TRIM(p_nomeAzienda),
        IdIndirizzoLegale = nuovoIdLegale,
        IdIndirizzoFatturazione = nuovoIdFatt,
        IdReferente = nuovoIdRef
    WHERE PartitaIVA = p_partitaIVA;

    DELETE FROM PossiedeRecapito WHERE PartitaIVA = p_partitaIVA;

    SET vCount = JSON_LENGTH(p_contatti);
    WHILE vIndex < vCount DO
        SET vTipoContatto = UPPER(TRIM(JSON_UNQUOTE(JSON_EXTRACT(p_contatti, CONCAT('$[', vIndex, '].type')))));
        SET vValoreContatto = TRIM(JSON_UNQUOTE(JSON_EXTRACT(p_contatti, CONCAT('$[', vIndex, '].value'))));
        IF vTipoContatto IS NULL OR vTipoContatto NOT IN ('TELEFONO', 'CELLULARE', 'EMAIL') THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo contatto non valido.';
        END IF;
        IF vValoreContatto IS NULL OR CHAR_LENGTH(vValoreContatto) = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Valore contatto obbligatorio.';
        END IF;
        INSERT IGNORE INTO Contatti (TipoContatto, ValoreContatto) VALUES (vTipoContatto, vValoreContatto);
        SELECT IdContatto INTO vIdContatto FROM Contatti
        WHERE TipoContatto = vTipoContatto AND ValoreContatto = vValoreContatto LIMIT 1;
        INSERT INTO PossiedeRecapito (PartitaIVA, IdContatto) VALUES (p_partitaIVA, vIdContatto);
        SET vIndex = vIndex + 1;
    END WHILE;

    DELETE FROM Indirizzi
    WHERE IdIndirizzo = idLegale
      AND NOT EXISTS (SELECT 1 FROM AziendeRivenditrici a WHERE a.IdIndirizzoLegale = idLegale OR a.IdIndirizzoFatturazione = idLegale)
      AND NOT EXISTS (SELECT 1 FROM IndirizzoFornitore f WHERE f.IdIndirizzo = idLegale);

    IF idFatt IS NOT NULL AND idFatt <> idLegale THEN
        DELETE FROM Indirizzi
        WHERE IdIndirizzo = idFatt
          AND NOT EXISTS (SELECT 1 FROM AziendeRivenditrici a WHERE a.IdIndirizzoLegale = idFatt OR a.IdIndirizzoFatturazione = idFatt)
          AND NOT EXISTS (SELECT 1 FROM IndirizzoFornitore f WHERE f.IdIndirizzo = idFatt);
    END IF;

    DELETE FROM ReferentiAziendali
    WHERE IdReferente = idRef
      AND NOT EXISTS (SELECT 1 FROM AziendeRivenditrici a WHERE a.IdReferente = idRef);

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ModificaFornitoreCompleto` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ModificaFornitoreCompleto`(
    IN p_CodiceFornitore INT,
    IN p_Nome VARCHAR(100),
    IN p_CodiceFiscale VARCHAR(16),
    IN p_Indirizzi JSON
)
BEGIN
    DECLARE v_index INT DEFAULT 0;
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_tipo VARCHAR(20);
    DECLARE v_via VARCHAR(100);
    DECLARE v_cap VARCHAR(5);
    DECLARE v_citta VARCHAR(50);
    DECLARE v_idIndirizzo INT;
    DECLARE v_ordiniAssociati INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    IF p_CodiceFornitore IS NULL OR p_CodiceFornitore <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Codice fornitore non valido.';
    END IF;

    IF p_Nome IS NULL OR CHAR_LENGTH(TRIM(p_Nome)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nome fornitore obbligatorio.';
    END IF;

    IF p_CodiceFiscale IS NULL OR CHAR_LENGTH(TRIM(p_CodiceFiscale)) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Codice fiscale obbligatorio.';
    END IF;

    IF p_Indirizzi IS NULL OR JSON_TYPE(p_Indirizzi) <> 'ARRAY' OR JSON_LENGTH(p_Indirizzi) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Un fornitore deve avere almeno un indirizzo.';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM Fornitore
        WHERE CodiceFornitore = p_CodiceFornitore
        FOR UPDATE
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Fornitore non trovato.';
    END IF;

    SELECT COUNT(*)
      INTO v_ordiniAssociati
    FROM OrdineRifornimento
    WHERE CodiceFornitore = p_CodiceFornitore
    FOR UPDATE;

    IF v_ordiniAssociati > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Impossibile modificare il fornitore ha ordini di rifornimento associati.';
    END IF;

    IF EXISTS (
        SELECT 1 FROM Fornitore
        WHERE CodiceFiscale = p_CodiceFiscale
          AND CodiceFornitore <> p_CodiceFornitore
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Codice fiscale già esistente per un altro fornitore.';
    END IF;

    UPDATE Fornitore
       SET Nome = TRIM(p_Nome), CodiceFiscale = UPPER(TRIM(p_CodiceFiscale))
     WHERE CodiceFornitore = p_CodiceFornitore;

    DELETE i
    FROM Indirizzi i
    INNER JOIN IndirizzoFornitore target
        ON target.IdIndirizzo = i.IdIndirizzo
       AND target.CodiceFornitore = p_CodiceFornitore
    WHERE NOT EXISTS (
        SELECT 1
        FROM IndirizzoFornitore other
        WHERE other.IdIndirizzo = i.IdIndirizzo
          AND other.CodiceFornitore <> p_CodiceFornitore
    )
      AND NOT EXISTS (
        SELECT 1
        FROM AziendeRivenditrici a
        WHERE a.IdIndirizzoLegale = i.IdIndirizzo
           OR a.IdIndirizzoFatturazione = i.IdIndirizzo
    );

    DELETE FROM IndirizzoFornitore
    WHERE CodiceFornitore = p_CodiceFornitore;

    SET v_count = JSON_LENGTH(p_Indirizzi);

    WHILE v_index < v_count DO
        SET v_tipo = JSON_UNQUOTE(JSON_EXTRACT(p_Indirizzi, CONCAT('$[', v_index, '].type')));
        SET v_via = JSON_UNQUOTE(JSON_EXTRACT(p_Indirizzi, CONCAT('$[', v_index, '].street')));
        SET v_cap = JSON_UNQUOTE(JSON_EXTRACT(p_Indirizzi, CONCAT('$[', v_index, '].postalCode')));
        SET v_citta = JSON_UNQUOTE(JSON_EXTRACT(p_Indirizzi, CONCAT('$[', v_index, '].city')));

        IF v_tipo IS NULL OR v_tipo NOT IN ('LEGALE', 'FATTURAZIONE') THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo indirizzo non valido.';
        END IF;

        IF v_via IS NULL OR CHAR_LENGTH(TRIM(v_via)) = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Via indirizzo obbligatoria.';
        END IF;

        IF v_cap IS NULL OR v_cap NOT REGEXP '^[0-9]{5}$' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'CAP indirizzo non valido.';
        END IF;

        IF v_citta IS NULL OR CHAR_LENGTH(TRIM(v_citta)) = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Città indirizzo obbligatoria.';
        END IF;

        INSERT INTO Indirizzi(TipoIndirizzo, Via, CAP, Citta)
        VALUES (v_tipo, TRIM(v_via), v_cap, TRIM(v_citta));

        SET v_idIndirizzo = LAST_INSERT_ID();

        INSERT INTO IndirizzoFornitore(CodiceFornitore, IdIndirizzo, Tipo)
        VALUES (p_CodiceFornitore, v_idIndirizzo, v_tipo);

        SET v_index = v_index + 1;
    END WHILE;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ModificaGiacenza` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ModificaGiacenza`(
    IN var_CodiceSpecie CHAR(10),
    IN var_NuovaQuantita INT
)
BEGIN
    DECLARE v_exist INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    IF var_NuovaQuantita < 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La quantità di magazzino non può essere negativa.';
    END IF;

    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    SELECT COUNT(*)
    INTO v_exist
    FROM Magazzino
    WHERE CodiceSpecie = var_CodiceSpecie
    FOR UPDATE;

    IF v_exist = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Specie non trovata nelle giacenze.';
    END IF;

    UPDATE Magazzino
    SET Quantita = var_NuovaQuantita
    WHERE CodiceSpecie = var_CodiceSpecie;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ModificaOrdineRifornimentoAperto` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ModificaOrdineRifornimentoAperto`(
    IN p_idOrdine INT,
    IN p_emailContatto VARCHAR(100),
    IN p_viaConsegna VARCHAR(100),
    IN p_cittaConsegna VARCHAR(50),
    IN p_capConsegna CHAR(5),
    IN p_referente VARCHAR(100),
    IN p_recapitoCorriere VARCHAR(50)
)
BEGIN
    DECLARE v_stato VARCHAR(20);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    SET v_stato = NULL;
    SELECT Stato INTO v_stato
    FROM OrdineRifornimento
    WHERE IdOrdine = p_idOrdine
    FOR UPDATE;

    IF v_stato IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ordine di rifornimento non trovato';
    END IF;

    IF v_stato <> 'APERTO' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Si possono modificare solamente ordini di rifornimento aperti';
    END IF;

    UPDATE OrdineRifornimento
    SET EmailContatto = p_emailContatto,
        ViaConsegna = p_viaConsegna,
        CittaConsegna = p_cittaConsegna,
        CapConsegna = p_capConsegna,
        Referente = p_referente,
        RecapitoCorriere = p_recapitoCorriere
    WHERE IdOrdine = p_idOrdine;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ModificaOrdineVenditaAperto` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ModificaOrdineVenditaAperto`(
    IN p_idOrdine INT,
    IN p_viaConsegna VARCHAR(100),
    IN p_cittaConsegna VARCHAR(50),
    IN p_capConsegna CHAR(5),
    IN p_referente VARCHAR(100),
    IN p_recapitoCorriere VARCHAR(50)
)
BEGIN
    DECLARE v_stato VARCHAR(20);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    SET v_stato = NULL;
    SELECT Stato INTO v_stato
    FROM OrdiniDiVendita
    WHERE IdOrdine = p_idOrdine
    FOR UPDATE;

    IF v_stato IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ordine non trovato';
    END IF;

    IF v_stato <> 'APERTO' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Si possono modificare solamente ordini aperti';
    END IF;

    UPDATE OrdiniDiVendita
    SET ViaConsegna = p_viaConsegna,
        CittaConsegna = p_cittaConsegna,
        CAPConsegna = p_capConsegna,
        Referente = p_referente,
        RecapitoCorriere = p_recapitoCorriere
    WHERE IdOrdine = p_idOrdine;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ModificaSpecie` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ModificaSpecie`(
    IN p_codice_specie CHAR(10),
    IN p_nome_comune VARCHAR(45),
    IN p_nome_latino VARCHAR(45),
    IN p_tipologia ENUM('APPARTAMENTO', 'GIARDINO'),
    IN p_esotica TINYINT,
    IN p_fiorita TINYINT,
    IN p_colorazione ENUM('ROSSA', 'GIALLA', 'ARANCIONE', 'BLU', 'VIOLA', 'BIANCA', 'MULTICOLORE')
)
BEGIN
    DECLARE ordiniVendita INT DEFAULT 0;
    DECLARE ordiniRifornimento INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    IF NOT EXISTS (
        SELECT 1
        FROM SpecieDiPiante
        WHERE CodiceSpecie = p_codice_specie
        FOR UPDATE
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Nessuna specie trovata da modificare.';
    END IF;

    IF p_fiorita = 1 AND p_colorazione IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Colorazione obbligatoria per specie fiorita.';
    END IF;

    SELECT COUNT(*)
    INTO ordiniVendita
    FROM DettagliOrdine
    WHERE CodiceSpecie = p_codice_specie
    FOR UPDATE;

    SELECT COUNT(*)
    INTO ordiniRifornimento
    FROM DettaglioOrdineRifornimento
    WHERE CodiceSpecie = p_codice_specie
    FOR UPDATE;

    IF ordiniVendita > 0 OR ordiniRifornimento > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Impossibile modificare la specie presente in ordini di vendita o rifornimento esistenti.';
    END IF;

    UPDATE SpecieDiPiante
    SET NomeComune = p_nome_comune,
        NomeLatino = p_nome_latino,
        Tipologia = p_tipologia,
        Esotica = p_esotica,
        Fiorita = p_fiorita
    WHERE CodiceSpecie = p_codice_specie;

    IF p_fiorita = 1 THEN
        IF EXISTS (
            SELECT 1
            FROM SpecieFiorita
            WHERE CodiceSpecie = p_codice_specie
        ) THEN
            UPDATE SpecieFiorita
            SET Colorazioni = p_colorazione
            WHERE CodiceSpecie = p_codice_specie;
        ELSE
            INSERT INTO SpecieFiorita (CodiceSpecie, Colorazioni)
            VALUES (p_codice_specie, p_colorazione);
        END IF;
    ELSE
        DELETE FROM SpecieFiorita
        WHERE CodiceSpecie = p_codice_specie;
    END IF;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ModificaStatoOrdine` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ModificaStatoOrdine`(
    IN p_idOrdine INT,
    IN p_nuovoStato ENUM(
        'APERTO',
        'CONFERMATO',
        'SPEDITO',
        'CONSEGNATO',
        'ANNULLATO'
    )
)
BEGIN
    DECLARE stato_corrente ENUM(
        'APERTO',
        'CONFERMATO',
        'SPEDITO',
        'CONSEGNATO',
        'ANNULLATO'
    );

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    SELECT Stato INTO stato_corrente
    FROM OrdiniDiVendita
    WHERE IdOrdine = p_idOrdine
    FOR UPDATE;

    IF stato_corrente IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Ordine non trovato';
    END IF;

    UPDATE OrdiniDiVendita
    SET Stato = p_nuovoStato
    WHERE IdOrdine = p_idOrdine;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ModificaStatoOrdineRifornimento` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ModificaStatoOrdineRifornimento`(
    IN p_idOrdine INT,
    IN p_nuovoStato ENUM(
        'APERTO',
        'CONFERMATO',
        'SPEDITO',
        'CONSEGNATO',
        'ANNULLATO'
    )
)
BEGIN
    DECLARE order_exists INT DEFAULT 0;
    DECLARE msg TEXT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    SELECT COUNT(*) INTO order_exists
    FROM OrdineRifornimento
    WHERE IdOrdine = p_idOrdine
    FOR UPDATE;

    IF order_exists = 0 THEN
        SET msg = CONCAT(
            'Ordine di rifornimento con ID ',
            p_idOrdine,
            ' non trovato'
        );

        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = msg;
    END IF;

    UPDATE OrdineRifornimento
    SET Stato = p_nuovoStato
    WHERE IdOrdine = p_idOrdine;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `OttieniPrezzoAttuale` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `OttieniPrezzoAttuale`(
    IN p_CodiceSpecie CHAR(10)
)
BEGIN
    SELECT PrezzoAttuale
    FROM StoricoPrezzi
    WHERE SpeciePianta = p_CodiceSpecie
    ORDER BY DataVariazione DESC
    LIMIT 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `OttieniQuantitaGiacenza` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `OttieniQuantitaGiacenza`(IN p_CodiceSpecie CHAR(10))
BEGIN
    SELECT COALESCE((
        SELECT Quantita
        FROM Magazzino
        WHERE CodiceSpecie = p_CodiceSpecie
        LIMIT 1
    ), 0) AS Quantita;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RicercaAziendaPerPartitaIVA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RicercaAziendaPerPartitaIVA`(
    IN p_partitaIVA CHAR(11)
)
BEGIN
    SELECT 
        ar.PartitaIVA,
        ar.NomeAzienda,
        r.Nome AS NomeReferente,
        r.Cognome AS CognomeReferente,
        il.Via AS ViaLegale,
        il.CAP AS CapLegale,
        il.Citta AS CittaLegale,
        ifat.Via AS ViaFatturazione,
        ifat.CAP AS CapFatturazione,
        ifat.Citta AS CittaFatturazione
    FROM AziendeRivenditrici ar
    JOIN ReferentiAziendali r ON ar.IdReferente = r.IdReferente
    JOIN Indirizzi il ON ar.IdIndirizzoLegale = il.IdIndirizzo
    JOIN Indirizzi ifat ON ar.IdIndirizzoFatturazione = ifat.IdIndirizzo
    WHERE ar.PartitaIVA = p_partitaIVA;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RicercaFornitorePerCodice` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RicercaFornitorePerCodice`(IN p_CodiceFornitore INT)
BEGIN
    SELECT CodiceFornitore, Nome, CodiceFiscale
    FROM Fornitore
    WHERE CodiceFornitore = p_CodiceFornitore;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RicercaOrdineRifornimento` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RicercaOrdineRifornimento`(
    IN p_idOrdine INT
)
BEGIN
    SELECT 
        o.IdOrdine,
        o.CodiceFornitore,
        f.Nome AS NomeFornitore,
        f.CodiceFiscale,
        o.DataOrdine,
        o.Stato,
        o.EmailContatto,
        o.ViaConsegna,
        o.CittaConsegna,
        o.CapConsegna,
        o.Referente,
        o.RecapitoCorriere,
        o.DataCreazione,
        o.DataModifica
    FROM 
        OrdineRifornimento o
    JOIN 
        Fornitore f ON o.CodiceFornitore = f.CodiceFornitore
    WHERE 
        o.IdOrdine = p_idOrdine;
        
    SELECT 
        d.CodiceSpecie,
        s.NomeComune,
        s.NomeLatino,
        d.Quantita
    FROM 
        DettaglioOrdineRifornimento d
    JOIN 
        SpecieDiPiante s ON d.CodiceSpecie = s.CodiceSpecie
    WHERE 
        d.IdOrdine = p_idOrdine
    ORDER BY 
        s.NomeComune;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RicercaOrdineVendita` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RicercaOrdineVendita`(IN p_idOrdine INT)
BEGIN
    SELECT IdOrdine, PartitaIVA, DataOrdine, Stato, ViaConsegna,
           CittaConsegna, CAPConsegna, Referente, RecapitoCorriere
    FROM OrdiniDiVendita
    WHERE IdOrdine = p_idOrdine;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RicercaOrdiniPerAzienda` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RicercaOrdiniPerAzienda`(
    IN p_partita_iva CHAR(11)
)
BEGIN
    SELECT 
        o.IdOrdine,
        o.PartitaIVA,
        a.NomeAzienda,
        o.DataOrdine,
        o.Stato,
        o.ViaConsegna,
        o.CittaConsegna,
        o.CAPConsegna,
        o.Referente,
        o.RecapitoCorriere,
        (SELECT SUM(d.Quantita * d.PrezzoUnitario) FROM DettagliOrdine d WHERE d.IdOrdine = o.IdOrdine) AS ValoreTotale
    FROM 
        OrdiniDiVendita o
    JOIN 
        AziendeRivenditrici a ON o.PartitaIVA = a.PartitaIVA
    WHERE 
        o.PartitaIVA = p_partita_iva
    ORDER BY 
        o.DataOrdine DESC;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RicercaOrdiniPerStato` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RicercaOrdiniPerStato`(
    IN p_stato ENUM('APERTO','CONFERMATO','SPEDITO','CONSEGNATO','ANNULLATO')
)
BEGIN
    SELECT 
        o.IdOrdine,
        o.PartitaIVA,
        a.NomeAzienda,
        o.DataOrdine,
        o.Stato,
        o.ViaConsegna,
        o.CittaConsegna,
        o.CAPConsegna,
        o.Referente,
        o.RecapitoCorriere,
        (SELECT SUM(d.Quantita * d.PrezzoUnitario) FROM DettagliOrdine d WHERE d.IdOrdine = o.IdOrdine) AS ValoreTotale
    FROM 
        OrdiniDiVendita o
    JOIN 
        AziendeRivenditrici a ON o.PartitaIVA = a.PartitaIVA
    WHERE 
        o.Stato = p_stato
    ORDER BY 
        o.DataOrdine DESC;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RicercaOrdiniRifornimentoPerFornitore` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RicercaOrdiniRifornimentoPerFornitore`(IN p_codiceFornitore INT)
BEGIN
    SELECT IdOrdine, CodiceFornitore, DataOrdine, Stato, EmailContatto,
           ViaConsegna, CittaConsegna, CapConsegna, Referente, RecapitoCorriere
    FROM OrdineRifornimento
    WHERE CodiceFornitore = p_codiceFornitore
    ORDER BY DataOrdine DESC, IdOrdine DESC;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RicercaOrdiniRifornimentoPerStato` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RicercaOrdiniRifornimentoPerStato`(
    IN p_stato ENUM('APERTO','CONFERMATO','SPEDITO','CONSEGNATO','ANNULLATO')
)
BEGIN
    SELECT 
        o.IdOrdine,
        o.CodiceFornitore,
        f.Nome AS NomeFornitore,
        o.DataOrdine,
        o.Stato,
        o.ViaConsegna,
        o.CittaConsegna,
        o.CapConsegna,
        o.Referente,
        o.RecapitoCorriere,
        o.EmailContatto,
        SUM(d.Quantita) AS QuantitaTotale
    FROM 
        OrdineRifornimento o
    JOIN 
        Fornitore f ON o.CodiceFornitore = f.CodiceFornitore
    LEFT JOIN 
        DettaglioOrdineRifornimento d ON o.IdOrdine = d.IdOrdine
    WHERE 
        o.Stato = p_stato
    GROUP BY
        o.IdOrdine
    ORDER BY 
        o.DataOrdine DESC;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RicercaSpeciePerColorazione` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RicercaSpeciePerColorazione`(IN colorazioneRicerca VARCHAR(45))
BEGIN
    SELECT 
        sp.CodiceSpecie, 
        sp.NomeComune, 
        sp.NomeLatino, 
        sp.Tipologia, 
        sp.Esotica, 
        sp.Fiorita,
        sf.Colorazioni
    FROM 
        SpecieDiPiante sp
    INNER JOIN 
        SpecieFiorita sf ON sp.CodiceSpecie = sf.CodiceSpecie
    WHERE 
        sf.Colorazioni = colorazioneRicerca;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RicercaSpeciePerNome` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RicercaSpeciePerNome`(IN termineRicerca VARCHAR(45))
BEGIN    
    SELECT 
        sp.CodiceSpecie, 
        sp.NomeComune, 
        sp.NomeLatino, 
        sp.Tipologia, 
        sp.Esotica, 
        sp.Fiorita,
        sf.Colorazioni
    FROM 
        SpecieDiPiante sp
    LEFT JOIN 
        SpecieFiorita sf ON sp.CodiceSpecie = sf.CodiceSpecie
    WHERE 
        sp.NomeComune LIKE CONCAT('%', termineRicerca, '%') OR 
        sp.NomeLatino LIKE CONCAT('%', termineRicerca, '%');        
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `RicercaSpeciePerTipologia` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `RicercaSpeciePerTipologia`(IN tipologiaRicerca VARCHAR(45))
BEGIN  
    SELECT 
        sp.CodiceSpecie, 
        sp.NomeComune, 
        sp.NomeLatino, 
        sp.Tipologia, 
        sp.Esotica, 
        sp.Fiorita,
        sf.Colorazioni
    FROM 
        SpecieDiPiante sp
    LEFT JOIN 
        SpecieFiorita sf ON sp.CodiceSpecie = sf.CodiceSpecie
    WHERE 
        sp.Tipologia = tipologiaRicerca;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `SpecieForniteDaFornitore` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SpecieForniteDaFornitore`(
    IN p_CodiceFornitore INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        RESIGNAL;
    END;

    IF p_CodiceFornitore IS NULL OR p_CodiceFornitore <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Codice fornitore non valido';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM Fornitore WHERE CodiceFornitore = p_CodiceFornitore) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Fornitore non trovato';
    END IF;

    SELECT 
        s.CodiceSpecie,
        s.NomeLatino,
        s.NomeComune,
        s.Tipologia,
        s.Esotica,
        sf.Colorazioni AS Colorazione
    FROM FornituraSpecie fs
    INNER JOIN SpecieDiPiante s ON fs.CodiceSpecie = s.CodiceSpecie
    LEFT JOIN SpecieFiorita sf ON s.CodiceSpecie = sf.CodiceSpecie
    WHERE fs.CodiceFornitore = p_CodiceFornitore 
      AND fs.Attiva = 1
    ORDER BY s.NomeComune, s.CodiceSpecie;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VerificaAziendaRivenditriceEsistente` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VerificaAziendaRivenditriceEsistente`(IN p_PartitaIVA CHAR(11))
BEGIN
    SELECT EXISTS(
        SELECT 1
        FROM AziendeRivenditrici
        WHERE PartitaIVA = p_PartitaIVA
    ) AS Esiste;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VerificaFornitoreEsistente` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VerificaFornitoreEsistente`(IN p_CodiceFornitore INT)
BEGIN
    SELECT EXISTS(
        SELECT 1
        FROM Fornitore
        WHERE CodiceFornitore = p_CodiceFornitore
    ) AS Esiste;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VerificaFornituraSpecie` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VerificaFornituraSpecie`(
    IN p_CodiceFornitore INT,
    IN p_CodiceSpecie CHAR(10)
)
BEGIN
    SELECT EXISTS(
        SELECT 1
        FROM FornituraSpecie
        WHERE CodiceFornitore = p_CodiceFornitore
          AND CodiceSpecie = p_CodiceSpecie
          AND Attiva = 1
    ) AS Esiste;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VerificaOrdineRifornimentoEsistente` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VerificaOrdineRifornimentoEsistente`(IN p_idOrdine INT)
BEGIN
    SELECT EXISTS(
        SELECT 1
        FROM OrdineRifornimento
        WHERE IdOrdine = p_idOrdine
    ) AS Esiste;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VerificaOrdineVenditaEsistente` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VerificaOrdineVenditaEsistente`(IN p_idOrdine INT)
BEGIN
    SELECT EXISTS(
        SELECT 1
        FROM OrdiniDiVendita
        WHERE IdOrdine = p_idOrdine
    ) AS Esiste;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VerificaSpecieEsistente` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VerificaSpecieEsistente`(IN p_CodiceSpecie CHAR(10))
BEGIN
    SELECT EXISTS(
        SELECT 1
        FROM SpecieDiPiante
        WHERE CodiceSpecie = p_CodiceSpecie
    ) AS Esiste;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VerificaUsernameEsistente` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VerificaUsernameEsistente`(IN p_Username VARCHAR(45))
BEGIN
    SELECT EXISTS(
        SELECT 1
        FROM Utenti
        WHERE Username = p_Username
    ) AS Esiste;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaAziendeClienti` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaAziendeClienti`()
BEGIN
  SELECT 
    a.PartitaIVA,
    a.NomeAzienda,
    il.Via AS ViaLegale,
    il.CAP AS CAPLegale,
    il.Citta AS CittaLegale,
    ifa.Via AS ViaFatturazione,
    ifa.CAP AS CAPFatturazione,
    ifa.Citta AS CittaFatturazione,
    r.Nome AS NomeReferente,
    r.Cognome AS CognomeReferente
  FROM AziendeRivenditrici a
  JOIN Indirizzi il ON a.IdIndirizzoLegale = il.IdIndirizzo
  JOIN Indirizzi ifa ON a.IdIndirizzoFatturazione = ifa.IdIndirizzo
  JOIN ReferentiAziendali r ON a.IdReferente = r.IdReferente;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaCatalogoPiante` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaCatalogoPiante`()
BEGIN
    SELECT CodiceSpecie, NomeComune, NomeLatino, Tipologia, Esotica, Fiorita,
           Colorazioni, PrezzoAttuale, Giacenza
    FROM v_infopiante
    ORDER BY CodiceSpecie;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaContattiAzienda` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaContattiAzienda`(IN p_PartitaIVA CHAR(11))
BEGIN
    SELECT c.TipoContatto, c.ValoreContatto
    FROM Contatti c
    INNER JOIN PossiedeRecapito pr ON c.IdContatto = pr.IdContatto
    WHERE pr.PartitaIVA = p_PartitaIVA
    ORDER BY c.IdContatto;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaDettagliOrdineRifornimento` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaDettagliOrdineRifornimento`(IN p_idOrdine INT)
BEGIN
    SELECT IdOrdine, CodiceSpecie, Quantita
    FROM DettaglioOrdineRifornimento
    WHERE IdOrdine = p_idOrdine
    ORDER BY CodiceSpecie;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaDettagliOrdineVendita` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaDettagliOrdineVendita`(IN p_idOrdine INT)
BEGIN
    SELECT IdOrdine, CodiceSpecie, Quantita, PrezzoUnitario
    FROM DettagliOrdine
    WHERE IdOrdine = p_idOrdine
    ORDER BY CodiceSpecie;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaFornitori` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaFornitori`()
BEGIN
    SELECT CodiceFornitore, Nome, CodiceFiscale
    FROM Fornitore
    ORDER BY Nome;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaGiacenzeCritiche` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaGiacenzeCritiche`()
BEGIN
    SELECT CodiceSpecie, NomeComune, NomeLatino, Quantita
    FROM v_giacenzecritiche
    ORDER BY CodiceSpecie;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaIndirizziFornitore` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaIndirizziFornitore`(IN p_CodiceFornitore INT)
BEGIN
    SELECT i.IdIndirizzo, i.Via, i.Citta, i.CAP, inf.Tipo
    FROM IndirizzoFornitore inf
    INNER JOIN Indirizzi i ON inf.IdIndirizzo = i.IdIndirizzo
    WHERE inf.CodiceFornitore = p_CodiceFornitore
    ORDER BY CASE inf.Tipo
        WHEN 'LEGALE' THEN 1
        WHEN 'FATTURAZIONE' THEN 2
        ELSE 3
    END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaOrdiniRifornimento` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaOrdiniRifornimento`()
BEGIN
    SELECT IdOrdine, CodiceFornitore, DataOrdine, Stato, EmailContatto,
           ViaConsegna, CittaConsegna, CapConsegna, Referente, RecapitoCorriere
    FROM OrdineRifornimento
    ORDER BY DataOrdine DESC, IdOrdine DESC;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaOrdiniVendita` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaOrdiniVendita`()
BEGIN
    SELECT IdOrdine, PartitaIVA, DataOrdine, Stato, ViaConsegna,
           CittaConsegna, CAPConsegna, Referente, RecapitoCorriere
    FROM OrdiniDiVendita
    ORDER BY DataOrdine DESC, IdOrdine DESC;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaReportVenditeSpecie` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaReportVenditeSpecie`()
BEGIN
    SELECT CodiceSpecie, NomeComune, Tipologia, Fiorita,
           QuantitaVenduta, ValoreTotaleVendite
    FROM v_reportvenditespecie
    ORDER BY CodiceSpecie;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaSpecie` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaSpecie`(
  IN codice_in CHAR(10)
)
BEGIN
  SELECT
    s.CodiceSpecie,
    s.NomeComune,
    s.NomeLatino,
    s.Tipologia,
    s.Esotica,
    s.Fiorita,
    sf.Colorazioni
  FROM SpecieDiPiante s
  LEFT JOIN SpecieFiorita sf ON s.CodiceSpecie = sf.CodiceSpecie
  WHERE s.CodiceSpecie = codice_in;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaSpecieTotali` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaSpecieTotali`()
BEGIN
  SELECT
    s.CodiceSpecie,
    s.NomeComune,
    s.NomeLatino,
    s.Tipologia,
    s.Esotica,
    s.Fiorita,
    sf.Colorazioni
  FROM SpecieDiPiante s
  LEFT JOIN SpecieFiorita sf ON s.CodiceSpecie = sf.CodiceSpecie
  ORDER BY s.CodiceSpecie;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaStoricoPrezzi` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaStoricoPrezzi`(
    IN codiceSpecie CHAR(10)
)
BEGIN
    SELECT DataVariazione, PrezzoPrecedente, PrezzoAttuale
    FROM StoricoPrezzi
    WHERE SpeciePianta = codiceSpecie
    ORDER BY DataVariazione DESC;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VisualizzaUtenti` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VisualizzaUtenti`()
BEGIN
  SELECT Username, Ruolo
  FROM Utenti
  ORDER BY Ruolo, Username;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `v_giacenzecritiche`
--

/*!50001 DROP VIEW IF EXISTS `v_giacenzecritiche`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_giacenzecritiche` AS select `p`.`CodiceSpecie` AS `CodiceSpecie`,`p`.`NomeComune` AS `NomeComune`,`p`.`NomeLatino` AS `NomeLatino`,`g`.`Quantita` AS `Quantita` from (`magazzino` `g` join `speciedipiante` `p` on((`g`.`CodiceSpecie` = `p`.`CodiceSpecie`))) where (`g`.`Quantita` < 10) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_infopiante`
--

/*!50001 DROP VIEW IF EXISTS `v_infopiante`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_infopiante` AS select `s`.`CodiceSpecie` AS `CodiceSpecie`,`s`.`NomeComune` AS `NomeComune`,`s`.`NomeLatino` AS `NomeLatino`,`s`.`Tipologia` AS `Tipologia`,`s`.`Esotica` AS `Esotica`,`s`.`Fiorita` AS `Fiorita`,`sf`.`Colorazioni` AS `Colorazioni`,`p`.`PrezzoAttuale` AS `PrezzoAttuale`,ifnull(`g`.`Quantita`,0) AS `Giacenza` from (((`speciedipiante` `s` left join `speciefiorita` `sf` on((`s`.`CodiceSpecie` = `sf`.`CodiceSpecie`))) left join (select `sp`.`SpeciePianta` AS `SpeciePianta`,`sp`.`PrezzoAttuale` AS `PrezzoAttuale` from (`storicoprezzi` `sp` join (select `storicoprezzi`.`SpeciePianta` AS `SpeciePianta`,max(`storicoprezzi`.`DataVariazione`) AS `MaxData` from `storicoprezzi` group by `storicoprezzi`.`SpeciePianta`) `last_sp` on(((`sp`.`SpeciePianta` = `last_sp`.`SpeciePianta`) and (`sp`.`DataVariazione` = `last_sp`.`MaxData`))))) `p` on((`s`.`CodiceSpecie` = `p`.`SpeciePianta`))) left join `magazzino` `g` on((`s`.`CodiceSpecie` = `g`.`CodiceSpecie`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_reportvenditespecie`
--

/*!50001 DROP VIEW IF EXISTS `v_reportvenditespecie`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_reportvenditespecie` AS select `s`.`CodiceSpecie` AS `CodiceSpecie`,`s`.`NomeComune` AS `NomeComune`,`s`.`Tipologia` AS `Tipologia`,`s`.`Fiorita` AS `Fiorita`,sum(`d`.`Quantita`) AS `QuantitaVenduta`,sum((`d`.`Quantita` * `d`.`PrezzoUnitario`)) AS `ValoreTotaleVendite` from ((`speciedipiante` `s` join `dettagliordine` `d` on((`s`.`CodiceSpecie` = `d`.`CodiceSpecie`))) join `ordinidivendita` `o` on((`d`.`IdOrdine` = `o`.`IdOrdine`))) where (`o`.`Stato` in ('CONSEGNATO','SPEDITO')) group by `s`.`CodiceSpecie`,`s`.`NomeComune`,`s`.`Tipologia`,`s`.`Fiorita` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-26 10:28:46
