/*
 * Пример использование скрипта, состоящего из нескольких операторов
 */
INSERT INTO TEST(ID, STR, DT)
SELECT ID*10, 'QQQ ' || STR, DT
FROM TEST
/
/*
 * Второй оператор
 */
INSERT INTO TEST(ID, STR, DT)
SELECT ID*100, 'TTT ' || STR, DT
FROM TEST
/
/*
 * Третий оператор
 */
DELETE FROM TEST WHERE ID = :id
