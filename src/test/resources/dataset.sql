DELETE FROM  `countries`;
DELETE FROM  `confederations`;

INSERT INTO `confederations` (`id`, `name`) VALUES (1, 'UEFA');
INSERT INTO `confederations` (`id`, `name`) VALUES (2, 'CONCACAF');
INSERT INTO `confederations` (`id`, `name`) VALUES (3, 'CONMEBOL');

INSERT INTO `countries` (`id`, `creation`, `name`, `population`, `confederation_id`) VALUES (1, '2018-10-06 18:10:48', 'Norway', 5136700, 1);
INSERT INTO `countries` (`id`, `creation`, `name`, `population`, `confederation_id`) VALUES (2, '2018-10-06 18:15:00', 'Spain', 47265321, 1);
INSERT INTO `countries` (`id`, `creation`, `name`, `population`, `confederation_id`) VALUES (3, '2018-10-06 18:10:48', 'Mexico', 115296767, 2);
INSERT INTO `countries` (`id`, `creation`, `name`, `population`, `confederation_id`) VALUES (4, '2018-10-06 18:10:48', 'Colombia', 47846160, 3);
INSERT INTO `countries` (`id`, `creation`, `name`, `population`, `confederation_id`) VALUES (5, '2018-10-06 18:10:48', 'Costa Rica', 4586353, 2);
