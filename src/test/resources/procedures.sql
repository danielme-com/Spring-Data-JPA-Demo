DROP PROCEDURE IF EXISTS countries_by_confederation_id^;
DROP PROCEDURE IF EXISTS countries_id_name_by_confederation_id^;

CREATE PROCEDURE countries_by_confederation_id(IN param_conf_id INT)
BEGIN
    SELECT * FROM countries WHERE confederation_id = param_conf_id ORDER BY name;
END^;

CREATE PROCEDURE countries_id_name_by_confederation_id(IN param_conf_id INT)
BEGIN
    SELECT id, name FROM countries WHERE confederation_id = param_conf_id ORDER BY name;
END^;