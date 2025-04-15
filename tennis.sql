-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tennis
-- ------------------------------------------------------
-- Server version	8.0.37

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
-- Table structure for table `match_table`
--

DROP TABLE IF EXISTS `match_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `match_table` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `match_date` datetime(6) NOT NULL,
  `score` varchar(255) DEFAULT NULL,
  `player1_id` bigint DEFAULT NULL,
  `player2_id` bigint DEFAULT NULL,
  `referee_id` bigint DEFAULT NULL,
  `tournament_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5cfw2ybmwynforfvsy8rw7v5y` (`player1_id`),
  KEY `FK8nsehcuxghqkj9e1arcrhhiew` (`player2_id`),
  KEY `FKbar4s8peatmrn9r8rykonwokx` (`referee_id`),
  KEY `FKpthau8b3dieneb79so8fwmjj6` (`tournament_id`),
  CONSTRAINT `FK5cfw2ybmwynforfvsy8rw7v5y` FOREIGN KEY (`player1_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK8nsehcuxghqkj9e1arcrhhiew` FOREIGN KEY (`player2_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKbar4s8peatmrn9r8rykonwokx` FOREIGN KEY (`referee_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKpthau8b3dieneb79so8fwmjj6` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_table`
--

LOCK TABLES `match_table` WRITE;
/*!40000 ALTER TABLE `match_table` DISABLE KEYS */;
INSERT INTO `match_table` VALUES (6,'2025-05-20 10:00:00.000000','6-4 6-2',10,15,8,1),(7,'2025-05-20 13:00:00.000000','6-7 7-6 6-2',10,15,18,1),(8,'2025-05-21 09:30:00.000000',NULL,16,15,8,2),(9,'2025-05-21 14:00:00.000000',NULL,16,17,18,2),(10,'2025-05-22 11:00:00.000000','7-6 6-7 7-5',10,17,8,2);
/*!40000 ALTER TABLE `match_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tournament`
--

DROP TABLE IF EXISTS `tournament`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tournament` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `end_date` datetime(6) NOT NULL,
  `name` varchar(255) NOT NULL,
  `start_date` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tournament`
--

LOCK TABLES `tournament` WRITE;
/*!40000 ALTER TABLE `tournament` DISABLE KEYS */;
INSERT INTO `tournament` VALUES (1,'2025-01-27 00:00:00.000000','Australian Open','2025-01-13 00:00:00.000000'),(2,'2025-06-08 00:00:00.000000','French Open','2025-05-25 00:00:00.000000'),(3,'2025-07-21 00:00:00.000000','Wimbledon','2025-07-07 00:00:00.000000'),(4,'2025-09-08 00:00:00.000000','US Open','2025-08-25 00:00:00.000000'),(5,'2025-05-18 00:00:00.000000','Rome Masters','2025-05-12 00:00:00.000000'),(6,'2025-05-05 00:00:00.000000','Madrid Open','2025-04-29 00:00:00.000000'),(7,'2025-03-30 00:00:00.000000','Miami Open','2025-03-17 00:00:00.000000'),(8,'2025-03-16 00:00:00.000000','Indian Wells','2025-03-03 00:00:00.000000'),(9,'2025-04-14 00:00:00.000000','Monte Carlo Masters','2025-04-07 00:00:00.000000'),(10,'2025-08-17 00:00:00.000000','Cincinnati Masters','2025-08-11 00:00:00.000000');
/*!40000 ALTER TABLE `tournament` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (4,'marcel@yahoo.com','Marcel','Pop','$2a$10$Gz/R/cHUvqyl1YAV0zUsDuCF8lDF8m28bfGW2syh1kxttk/e0Zbzq','admin','marcelp'),(8,'marao@yahoo.com','Mara','Oprisor','$2a$10$MUwDxcqj.pb2tDJulMMNneLVBtWew9r4lLcTTZ0KYEqYA3egdcwpi','referee','maraoprisor'),(10,'maria@yahoo.com','Maria','Gozman','$2a$10$U5PqUDv2sjv/.fQumglyke1/dOZ8mIlkLgwGQ5qvO8aGrLBtLq2jO','player','maria'),(12,'calina2@yahoo.com','Calina','Borzan','$2a$10$3dbuBBkbi1S0Et7fSO3cRe3SlxAWL9B0WHkkKanOmeUmwR8NGDRsS','admin','calina2'),(13,'calina.borzan18@yahoo.com','Calina','Borzan','$2a$10$qDIkOd65ncYEA1IhgxdVX.gfTjNlJy0Gj5/qf5A78joeXwaC55vx2','admin','calinaborzan'),(15,'gigi2@yahoo.com','Gigi','Pop','$2a$10$fVB8hD59.98crjSHmo/6J.x/5Eau9qDsivB8tDw587DCzJE8xOKLq','player','gigi'),(16,'simohalep@gmail.com','Simona','Halep','$2a$10$fC0J1brv41DBSXgw6SyJ4eNmnrNQMpBAfX3O0Pho2kmokScWGWGHy','player','simoh'),(17,'carmen@yahoo.com','Carmen','Borzan','$2a$10$H8nc16ecPZa6Hu8vDsmznOhiXyIZkdLMtf1Olg7Tav00xekwfGmGC','player','carmenborzan'),(18,'rafa@yahoo.com','Rafael','Butas','$2a$10$zXnBub.Srh26zOYOcRWpoOo9Ci.x79O.GBwCreoHUCWnMfe3OcVoa','referee','rafa');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_tournaments`
--

DROP TABLE IF EXISTS `user_tournaments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_tournaments` (
  `user_id` bigint NOT NULL,
  `tournament_id` bigint NOT NULL,
  KEY `FKa0x9bmn36n48hxh5fsemv2j15` (`tournament_id`),
  KEY `FKpcbnkqjs8tftc5fox9u881fsm` (`user_id`),
  CONSTRAINT `FKa0x9bmn36n48hxh5fsemv2j15` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`),
  CONSTRAINT `FKpcbnkqjs8tftc5fox9u881fsm` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_tournaments`
--

LOCK TABLES `user_tournaments` WRITE;
/*!40000 ALTER TABLE `user_tournaments` DISABLE KEYS */;
INSERT INTO `user_tournaments` VALUES (17,2),(17,4),(15,3),(15,2);
/*!40000 ALTER TABLE `user_tournaments` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-15 16:20:10
