CREATE DATABASE  IF NOT EXISTS `alumnos` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish2_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `alumnos`;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: alumnos
-- ------------------------------------------------------
-- Server version	8.0.19

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

--
-- Table structure for table `curso`
--

DROP TABLE IF EXISTS `curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `curso` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) COLLATE utf8_spanish2_ci NOT NULL,
  `imagen` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `precio` decimal(6,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `titulo_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curso`
--

LOCK TABLES `curso` WRITE;
/*!40000 ALTER TABLE `curso` DISABLE KEYS */;
INSERT INTO `curso` VALUES (1,'HTML','html.png',1.10),(2,'Java','java.png',2.20),(3,'Javascript','js.png',3.30),(4,'PHP','php.png',4.40),(5,'MySQL','mysql.png',5.50),(6,'CSS','css.png',1.10),(11,'Cerveza Artesana','cerveza.png',10.10),(12,'Enologia','enologia.png',10.10),(13,'reposteria','reposteria.png',6.60),(14,'Huerto urbano','huertourbano.png',7.60),(15,'Python','python.png',5.50);
/*!40000 ALTER TABLE `curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `noticia`
--

DROP TABLE IF EXISTS `noticia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `noticia` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(250) COLLATE utf8_spanish2_ci NOT NULL,
  `fecha` datetime NOT NULL,
  `contenido` text COLLATE utf8_spanish2_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `noticia`
--

LOCK TABLES `noticia` WRITE;
/*!40000 ALTER TABLE `noticia` DISABLE KEYS */;
INSERT INTO `noticia` VALUES (1,'Titulo 1','2020-04-23 12:12:12','En un lugar de la mancha <img src=\"img/enologia.png\" alt=\"imagen\"> mira que hay cosas bonitas y todos'),(2,'Titulo 2','2020-04-23 12:12:12','En un lugar de la mancha <img src=\"img/java.png\" alt=\"imagen\"> mira que hay cosas bonitas y todos'),(3,'Titulo 3','2020-04-23 12:12:12','En un lugar de la mancha <img src=\"img/html.png\" alt=\"imagen\"> mira que hay cosas bonitas y todos'),(4,'Titulo 4','2020-04-23 12:12:12','En un lugar de la mancha <img src=\"img/php.png\" alt=\"imagen\"> mira que hay cosas bonitas y todos'),(5,'Titulo 5','2020-04-23 12:12:12','En un lugar de la mancha <img src=\"cerveza.png\" alt=\"imagen\"> mira que hay cosas bonitas y todos');
/*!40000 ALTER TABLE `noticia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persona` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `avatar` varchar(250) COLLATE utf8_spanish2_ci NOT NULL,
  `sexo` enum('H','M') COLLATE utf8_spanish2_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
INSERT INTO `persona` VALUES (1,'Arantxaaaaaaa','avatar1.png','M'),(2,'Idoia','avatar2.png','M'),(3,'Iker','avatar3.png','H'),(4,'Hodei','avatar4.png','H'),(14,'Matias','avatar5.png','H');
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personacurso`
--

DROP TABLE IF EXISTS `personacurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personacurso` (
  `persona_id` int NOT NULL,
  `curso_id` int NOT NULL,
  `precio_pagado` decimal(6,2) NOT NULL,
  PRIMARY KEY (`persona_id`,`curso_id`),
  KEY `fk_persona_has_curso_curso1_idx` (`curso_id`),
  KEY `fk_persona_has_curso_persona_idx` (`persona_id`),
  CONSTRAINT `fk_persona_has_curso_curso1` FOREIGN KEY (`curso_id`) REFERENCES `curso` (`id`),
  CONSTRAINT `fk_persona_has_curso_persona` FOREIGN KEY (`persona_id`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personacurso`
--

LOCK TABLES `personacurso` WRITE;
/*!40000 ALTER TABLE `personacurso` DISABLE KEYS */;
INSERT INTO `personacurso` VALUES (1,1,1.10),(1,4,4.40),(1,5,1.10),(1,6,1.10),(1,11,10.10),(1,15,5.50),(2,4,4.40),(2,11,10.10),(2,12,10.10),(2,13,6.60),(2,14,7.60),(2,15,5.50),(3,1,1.10),(3,2,2.20),(3,4,0.00),(3,11,10.10),(3,13,6.60),(3,14,7.60),(4,1,1.10),(4,4,4.40),(4,5,5.50),(4,6,1.10),(4,11,10.10),(4,12,10.10),(4,13,6.60),(4,14,7.60),(4,15,5.50),(14,5,5.50),(14,6,1.10),(14,11,10.10),(14,12,10.10),(14,13,6.60),(14,14,7.60);
/*!40000 ALTER TABLE `personacurso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'alumnos'
--

--
-- Dumping routines for database 'alumnos'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-23 14:54:55
