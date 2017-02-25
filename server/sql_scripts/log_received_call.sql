USE cobenesoka;
DROP FUNCTION IF EXISTS log_received_call;

DELIMITER $$
CREATE FUNCTION log_received_call (phone_number VARCHAR(15), latitude VARCHAR(20), longitude VARCHAR(20))
	RETURNS BOOLEAN
BEGIN
  INSERT INTO calls (phone_number, time_received, latitude, longitude)
    VALUES (phone_number, NOW(), latitude, longitude);
  RETURN TRUE;
END$$
DELIMITER ;
