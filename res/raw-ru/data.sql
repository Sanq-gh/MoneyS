PRAGMA foreign_keys=ON;;;
--Валюта
--INSERT INTO currency(name) VALUES('uah');;;
--INSERT INTO currency(name) VALUES('rub');;;
--INSERT INTO currency(name) VALUES('usd');;;
--INSERT INTO currency(name) VALUES('eur');;;

--Трансфер
INSERT INTO category(active, name,id_parent,isgroup) VALUES(0,'Перевод между счетами',0,1);;;

--Без  категории
INSERT INTO category(name,id_parent,isgroup) VALUES('Без категории',0,1);;;

--Доходы
INSERT INTO category(name,id_parent,isgroup) VALUES('Доходы',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Депозиты',0);;;
INSERT INTO category(name,id_parent) VALUES('Пенсионные накопления',0);;;
INSERT INTO category(name,id_parent) VALUES('Дивиденды',0);;;
INSERT INTO category(name,id_parent) VALUES('Зарплата',0);;;
INSERT INTO category(name,id_parent) VALUES('Подарок в денежной форме',0);;;
INSERT INTO category(name,id_parent) VALUES('Пособие на ребёнка',0);;;
INSERT INTO category(name,id_parent) VALUES('Доходы аренды',0);;;
INSERT INTO category(name,id_parent) VALUES('Дополнительная работа',0);;;
INSERT INTO category(name,id_parent) VALUES('Пенсия',0);;;
INSERT INTO category(name,id_parent) VALUES('Возврат налогов',0);;;
INSERT INTO category(name,id_parent) VALUES('Проценты',0);;;
INSERT INTO category(name,id_parent) VALUES('Пособие по безработице',0);;;
INSERT INTO category(name,id_parent) VALUES('Временная работа',0);;;
INSERT INTO category(name,id_parent) VALUES('Сдача в аренду',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ="Доходы" ) WHERE  id_parent = 0 and isgroup =0 ;;;


--Расходы
INSERT INTO category(name,id_parent,isgroup) VALUES('Финансы',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Налоги',0);;;
INSERT INTO category(name,id_parent) VALUES('Взносы',0);;;
INSERT INTO category(name,id_parent) VALUES('Страховка',0);;;
INSERT INTO category(name,id_parent) VALUES('Кредит',0);;;
INSERT INTO category(name,id_parent) VALUES('Аренда',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ="Финансы" ) WHERE  id_parent = 0 and isgroup =0;;;


INSERT INTO category(name,id_parent,isgroup) VALUES('Транспорт',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Поезд',0);;;
INSERT INTO category(name,id_parent) VALUES('Автобус',0);;;
INSERT INTO category(name,id_parent) VALUES('Трамвай',0);;;
INSERT INTO category(name,id_parent) VALUES('Такси',0);;;
INSERT INTO category(name,id_parent) VALUES('Расходы на проезд',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ="Транспорт" ) WHERE  id_parent = 0 and isgroup =0;;;


INSERT INTO category(name,id_parent,isgroup) VALUES('Автотранспорт',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Парковка',0);;;
INSERT INTO category(name,id_parent) VALUES('Автомобиль',0);;;
INSERT INTO category(name,id_parent) VALUES('Техосмотр',0);;;
INSERT INTO category(name,id_parent) VALUES('Мотоцикл',0);;;
INSERT INTO category(name,id_parent) VALUES('Шины',0);;;
INSERT INTO category(name,id_parent) VALUES('Ремонт',0);;;
INSERT INTO category(name,id_parent) VALUES('Сервис',0);;;
INSERT INTO category(name,id_parent) VALUES('Заправка',0);;;
INSERT INTO category(name,id_parent) VALUES('Мойка',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ="Автотранспорт" ) WHERE  id_parent = 0 and isgroup =0;;;



INSERT INTO category(name,id_parent,isgroup) VALUES('Одежда',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Аксесуары',0);;;
INSERT INTO category(name,id_parent) VALUES('Украшения',0);;;
INSERT INTO category(name,id_parent) VALUES('Рубашки',0);;;
INSERT INTO category(name,id_parent) VALUES('Пуловеры',0);;;
INSERT INTO category(name,id_parent) VALUES('Толстовки',0);;;
INSERT INTO category(name,id_parent) VALUES('Футболки',0);;;
INSERT INTO category(name,id_parent) VALUES('Брюки',0);;;
INSERT INTO category(name,id_parent) VALUES('Юбки',0);;;
INSERT INTO category(name,id_parent) VALUES('Обувь',0);;;
INSERT INTO category(name,id_parent) VALUES('Босоножки',0);;;
INSERT INTO category(name,id_parent) VALUES('Ботинки',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ="Одежда" ) WHERE  id_parent = 0 and isgroup =0;;;



INSERT INTO category(name,id_parent,isgroup) VALUES('Электроника и компьютеры',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Программное обеспечение',0);;;
INSERT INTO category(name,id_parent) VALUES('Компьютеры',0);;;
INSERT INTO category(name,id_parent) VALUES('Принтеры',0);;;
INSERT INTO category(name,id_parent) VALUES('DVD',0);;;
INSERT INTO category(name,id_parent) VALUES('Фототовары',0);;;
INSERT INTO category(name,id_parent) VALUES('Камеры',0);;;
INSERT INTO category(name,id_parent) VALUES('Мониторы',0);;;
INSERT INTO category(name,id_parent) VALUES('Ноутбуки',0);;;
INSERT INTO category(name,id_parent) VALUES('Компьютерные игры',0);;;
INSERT INTO category(name,id_parent) VALUES('Комплектующие для ПК',0);;;
INSERT INTO category(name,id_parent) VALUES('Телефоны',0);;;
INSERT INTO category(name,id_parent) VALUES('Телевизоры',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ="Электроника и компьютеры" ) WHERE  id_parent = 0 and isgroup =0;;;




INSERT INTO category(name,id_parent,isgroup) VALUES('Мебель и бытовые товары ',0,1);;;


INSERT INTO category(name,id_parent,isgroup) VALUES('Образование',0,1);;;
INSERT INTO category(name,id_parent) VALUES('ВУЗ',0);;;
INSERT INTO category(name,id_parent) VALUES('Школа',0);;;
INSERT INTO category(name,id_parent) VALUES('Обучение',0);;;
INSERT INTO category(name,id_parent) VALUES('Канцелярия',0);;;
INSERT INTO category(name,id_parent) VALUES('Средства обучения',0);;;
INSERT INTO category(name,id_parent) VALUES('Учебники',0);;;
INSERT INTO category(name,id_parent) VALUES('Книги',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ="Образование" ) WHERE  id_parent = 0 and isgroup =0;;;



INSERT INTO category(name,id_parent,isgroup) VALUES('Отдых и Спорт',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Дискотека',0);;;
INSERT INTO category(name,id_parent) VALUES('Свободное время',0);;;
INSERT INTO category(name,id_parent) VALUES('Парк развлечений',0);;;
INSERT INTO category(name,id_parent) VALUES('Хобби',0);;;
INSERT INTO category(name,id_parent) VALUES('Кинотеатр',0);;;
INSERT INTO category(name,id_parent) VALUES('Пивная',0);;;
INSERT INTO category(name,id_parent) VALUES('Концерт',0);;;
INSERT INTO category(name,id_parent) VALUES('Культура',0);;;
INSERT INTO category(name,id_parent) VALUES('Музей',0);;;
INSERT INTO category(name,id_parent) VALUES('Ресторан',0);;;
INSERT INTO category(name,id_parent) VALUES('Игры',0);;;
INSERT INTO category(name,id_parent) VALUES('Спортивные клубы',0);;;
INSERT INTO category(name,id_parent) VALUES('Театр',0);;;
INSERT INTO category(name,id_parent) VALUES('Отпуск',0);;;
INSERT INTO category(name,id_parent) VALUES('Мероприятия',0);;;
INSERT INTO category(name,id_parent) VALUES('Членские взносы',0);;;
INSERT INTO category(name,id_parent) VALUES('Клуб',0);;;
INSERT INTO category(name,id_parent) VALUES('Журналы',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ="Отдых и Спорт" ) WHERE  id_parent = 0 and isgroup =0;;;



INSERT INTO category(name,id_parent,isgroup) VALUES('Дом и домашнее хозяйство',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Электроэнергия',0);;;
INSERT INTO category(name,id_parent) VALUES('Вода',0);;;
INSERT INTO category(name,id_parent) VALUES('Газ',0);;;
INSERT INTO category(name,id_parent) VALUES('Отопление',0);;;
INSERT INTO category(name,id_parent) VALUES('Кабельное телевидение',0);;;
INSERT INTO category(name,id_parent) VALUES('Интернет',0);;;
INSERT INTO category(name,id_parent) VALUES('Телефон',0);;;
INSERT INTO category(name,id_parent) VALUES('Продукты',0);;;
INSERT INTO category(name,id_parent) VALUES('Сад и огород',0);;;
INSERT INTO category(name,id_parent) VALUES('Домашние животные',0);;;
INSERT INTO category(name,id_parent) VALUES('Дополнительные затраты',0);;;
INSERT INTO category(name,id_parent) VALUES('Ремонт',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ="Дом и домашнее хозяйство" ) WHERE  id_parent = 0 and isgroup =0;;;
