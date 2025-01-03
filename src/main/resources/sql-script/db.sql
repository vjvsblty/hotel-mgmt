CREATE DATABASE hotel_management;

USE hotel_management;

CREATE TABLE `menu_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
)

CREATE TABLE `hotel_table` (
  `id` int NOT NULL AUTO_INCREMENT,
  `table_name` varchar(255) NOT NULL,
  `table_size` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
)

CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `table_id` decimal(10,2) NOT NULL,
  `menu_item_id` decimal(10,2) NOT NULL,
  `quantity` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
)

CREATE TABLE `final_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `table_id` int NOT NULL,
  `items` json DEFAULT NULL,
  `total_cost` double NOT NULL,
  `order_date` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
)

CREATE TABLE `owner` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
)

CREATE TABLE `expense` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `cost` double NOT NULL,
  `owner_id` int NOT NULL,
  `date` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);


INSERT INTO `menu_items` VALUES (1,'Chicken Manchow Soup',60.00),(2,'Chicken Ukkad Soup',130.00),(3,'Mutton Soup',80.00),(4,'Mutton Ukkad Soup',170.00),(5,'Chicken Fry/Roast',180.00),(6,'Mutton Fry',230.00),(7,'Chicken Chilly',220.00),(8,'Chicken Chilly (Grevy)',230.00),(9,'Chicken 65',240.00),(10,'Chicken Lollypop',220.00),(11,'Chicken Fried Rice',200.00),(12,'Chicken Triple Fried Rice',250.00),(13,'Egg Fried Rice',180.00),(14,'Chicken Tandoori(Full)',500.00),(15,'Chicken Tandoori(Half)',250.00),(16,'Chicken Tikka',260.00),(17,'Chicken Tangadi Kabab',380.00),(18,'Chicken Handi(Full)',600.00),(19,'Chicken Handi(Half)',300.00),(20,'Chicken Masala',180.00),(21,'Chicken Tikka Masala',250.00),(22,'Chicken Roast',200.00),(23,'Butter Chicken Handi(Full)',800.00),(24,'Butter Checken Handi(Half)',420.00),(25,'Chicken Hyderabadi',320.00),(26,'Chicken Thali',250.00),(27,'Spl. Chicken Thali',300.00),(28,'Mutton Thali',350.00),(29,'Spl. Mutton Thali',400.00),(30,'Mutton Handi(Full)',800.00),(31,'Mutton Handi(Half)',400.00),(32,'Mutton Masala',300.00),(33,'Boil Eggs(2 Pcs.)',30.00),(34,'Boil Eggs Fry(2 Pcs.)',40.00),(35,'Eggs Bhurji',80.00),(36,'Eggs Omlet',60.00),(37,'Egg Fry Masala',80.00),(38,'Egg Masala/Curry',120.00),(39,'Chicken Biryani',220.00),(40,'Mutton Biryani',300.00),(41,'Fish Biryani',170.00),(42,'Anda Biryani',180.00),(43,'Chicken Dum Biryani(Half)',300.00),(44,'Chicken Dum Biryani(Full)',600.00),(45,'Mutton Dum Biryani(Half)',350.00),(46,'Mutton Dum Biryani(Full)',700.00),(47,'Fish Fry',200.00),(48,'Fish Curry',200.00),(49,'Fish Roast',180.00),(50,'Chicken 1KG',300.00),(51,'Mutton 1KG',400.00),(52,'Green Salad',50.00),(53,'Kakdi/Tomato Salad',40.00),(54,'Roasted Papad',20.00),(55,'Fry Papad',30.00),(56,'Masala Papad',40.00),(57,'Nagali Fry Papad',30.00),(58,'Nagali Masala Papad',40.00),(59,'Veg. Manchurian',100.00),(60,'Hakka Noodles',100.00),(61,'Schezwan Noodles',120.00),(62,'Soyabean Chilly',100.00),(63,'Paneer Chilly',200.00),(64,'Manchow Soup',60.00),(65,'Fried Rice',100.00),(66,'Schezwan Fried Rice',120.00),(67,'Manchurian Rice',120.00),(68,'Tandoori Roti',15.00),(69,'Butter Tandoori Roti',25.00),(70,'Chapati',15.00),(71,'Butter Chapati',20.00),(72,'Bhakri',20.00),(73,'Plain Nan',40.00),(74,'Butter Nan',50.00),(75,'Garlic Nan',60.00),(76,'Dal Fry',110.00),(77,'Dal Tadka',130.00),(78,'Shev Bhaji',90.00),(79,'Chana Masala',100.00),(80,'Pithal Bhakri',150.00),(81,'Lasuni Methi Fry/Masala',150.00),(82,'Thecha',80.00),(83,'Kaju Curry',190.00),(84,'Kaju Masala',200.00),(85,'Palak Paneer',150.00),(86,'Paneer Tikka Masala',220.00),(87,'Paneer Button Masala',180.00),(88,'Paneer Masala',150.00),(89,'Paneer Kadai',240.00),(90,'Paneer Handi',440.00),(91,'Paneer Kolhapuri',180.00),(92,'Paneer Patiyala',220.00),(93,'Veg. Kolhapuri',140.00),(94,'Mix Veg',120.00),(95,'Veg Hyderabadi',140.00),(96,'Plain Palak',100.00),(97,'Baingan Masala',120.00),(98,'Shevga Masala',130.00),(99,'Malai Kofta',180.00),(100,'Veg Kofta',170.00),(101,'Veg Patiyala',180.00),(102,'Plain Rice',70.00),(103,'Jeera Rice',80.00),(104,'Dal Khichadi',150.00),(105,'Masala Rice',120.00),(106,'Veg. Biryani',150.00),(107,'Veg. Pulav',150.00),(108,'Curd Rice',130.00);
INSERT INTO `owner` VALUES(1, 'SAGAR'), (2, 'VIJAY');