USE cobenesoka;
DROP PROCEDURE IF EXISTS get_recent_log;

DELIMITER $$
CREATE PROCEDURE get_recent_log(in_phone_number VARCHAR(15))
  BEGIN
    SELECT time_received, latitude, longitude
    FROM calls c
    WHERE DATEDIFF(NOW(), c.time_received) < 2
      AND c.phone_number = in_phone_number
      AND time_received = (SELECT MAX(c2.time_received) 
                           FROM calls c2
                           WHERE c2.phone_number = in_phone_number);
      
  END$$
DELIMITER ;