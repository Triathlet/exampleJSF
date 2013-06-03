DROP TABLE IF EXISTS `propertystore`.`properties`;
CREATE TABLE  `propertystore`.`properties` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `property_key` VARCHAR(255) NOT NULL,
  `property_value` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
); 
--ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
 
--INSERT INTO propertystore.properties(customer_id, name, address, created_date)
--VALUES(1, 'mkyong1', 'address1', now());
--INSERT INTO propertystore.properties(customer_id, name, address, created_date)
--VALUES(2, 'mkyong2', 'address2', now());