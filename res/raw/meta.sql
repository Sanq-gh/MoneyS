CREATE TABLE "accounts" (
  "_id"  INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL,
  "name" VARCHAR(50),
  "saldo" INTEGER DEFAULT (0),
  "currency" VARCHAR(5),
  "active" INTEGER NOT NULL DEFAULT 1,
  "dt_create" LONG ,
  "dt_update" LONG
  );;;


CREATE TABLE "payords" 
(
"_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 
"id_acc" INTEGER NOT NULL ,
"id_cat" INTEGER NOT NULL ,
"id_link" INTEGER NOT NULL DEFAULT 0,
"name" VARCHAR(50),
"dt" LONG NOT NULL ,
"type" INTEGER NOT NULL  DEFAULT 0 CHECK ("type" IN (0, 1)),
"amount" INTEGER NOT NULL  DEFAULT 0,
"permanent" INTEGER NOT NULL  DEFAULT 0,
"remind" INTEGER NOT NULL  DEFAULT 0,
"time_remind" REAL ,
"dt_end" LONG NOT NULL  DEFAULT 0,
"id_period" INTEGER DEFAULT 0 ,
"active" INTEGER NOT NULL  DEFAULT 1,
"dt_create" LONG,
"dt_update" LONG,
 FOREIGN KEY(id_acc) REFERENCES accounts(_id),
 FOREIGN KEY(id_cat) REFERENCES category(_id)
);;;


CREATE TABLE "transfer"
(
"_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 
"id_payord" INTEGER NOT NULL , 
"dt" LONG NOT NULL ,
"imageName" VARCHAR(10),
"active" INTEGER NOT NULL  DEFAULT 1,
"dt_create" LONG,
"dt_update" LONG,
FOREIGN KEY(id_payord) REFERENCES payords(_id)
);;;



--Dictionary
CREATE TABLE "category"
(
  "_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,
  "name" VARCHAR,
  "id_parent" INTEGER,
  "isgroup" INTEGER NOT NULL  DEFAULT 0,
  "active" INTEGER NOT NULL  DEFAULT 1,
  "dt_create" LONG  ,
  "dt_update" LONG);;;


CREATE TABLE "period"
(
  "_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,
  "name" VARCHAR,
  "type" INTEGER NOT NULL  DEFAULT 0 CHECK ("type" IN (0, 1, 2, 3, 4)) ,
  "count" INTEGER DEFAULT 0,
  "active" INTEGER NOT NULL  DEFAULT 1,
  "dt_create" LONG  ,
  "dt_update" LONG);;;

CREATE TABLE "template"
(
  "_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,
  "name" VARCHAR,
  "id_payord" INTEGER NOT NULL ,
  "active" INTEGER NOT NULL  DEFAULT 1,
  "dt_create" LONG  ,
  "dt_update" LONG,
  FOREIGN KEY(id_payord) REFERENCES payords(_id));;;


--CREATE TABLE "currency"
--(
--  "_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,
--  "name" VARCHAR,
--  "active" INTEGER NOT NULL  DEFAULT 1,
--  "dt_create" LONG  ,
--  "dt_update" LONG);;;


--Indexes
Create Index Payords_idacc_idx ON Payords(id_acc);;;
Create Index Transfer_idacc_idx ON Transfer(id_payord);;;

--Trigger
CREATE TRIGGER "tr_accounts_AI"
AFTER INSERT ON accounts
BEGIN
UPDATE accounts SET dt_create = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
END;;;


CREATE TRIGGER "tr_accounts_AU"
AFTER UPDATE ON accounts BEGIN
UPDATE accounts SET dt_update = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
END;;;

CREATE TRIGGER "tr_accounts_active_AU"
AFTER UPDATE ON accounts
  WHEN new.active != old.active
BEGIN
    UPDATE payords SET active = new.active WHERE id_acc = NEW._id;
END;;;


CREATE TRIGGER "tr_payords_AI"
AFTER INSERT ON payords
BEGIN
UPDATE payords SET dt_create = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
UPDATE payords SET dt_end = CASE dt_end WHEN 0 THEN dt ELSE dt_end END  WHERE _id = NEW._id;
 END;;;


CREATE TRIGGER "tr_payords_AU"
AFTER UPDATE ON payords
BEGIN 
UPDATE payords SET dt_update = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
UPDATE payords SET dt_end = CASE dt_end WHEN 0 THEN dt ELSE dt_end END  WHERE _id = NEW._id;
END;;;


CREATE TRIGGER "tr_payords_active_AU"
AFTER UPDATE ON payords
WHEN new.active != old.active
BEGIN
    UPDATE transfer SET active = new.active WHERE id_payord = NEW._id;
    UPDATE payords  SET active = new.active WHERE _id = OLD.id_link;
    UPDATE transfer SET active = new.active WHERE id_payord = OLD.id_link;
    UPDATE period   SET active = new.active WHERE _id = NEW.id_period;
END;;;

--    444444444444444444444444444444444444444444444

CREATE TRIGGER  "tr_transfer_AI"
AFTER INSERT ON transfer
BEGIN 
UPDATE transfer SET dt_create = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
 END;;;


CREATE TRIGGER  "tr_transfer_AU"
AFTER UPDATE ON transfer
BEGIN 
UPDATE transfer SET dt_update = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
END;;;


CREATE TRIGGER  "tr_category_AI"
AFTER INSERT ON category
BEGIN
  UPDATE category SET dt_create = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
END;;;


CREATE TRIGGER "tr_category_AU"
AFTER UPDATE ON category
BEGIN
  UPDATE category SET dt_update = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
END;;;


CREATE TRIGGER "tr_category_active_AU"
AFTER UPDATE ON category
  WHEN new.active != old.active
BEGIN
  UPDATE category SET active = new.active WHERE id_parent = NEW._id;
END;;;


CREATE TRIGGER "tr_period_AI"
AFTER INSERT ON period
BEGIN
  UPDATE period SET dt_create = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
END;;;


CREATE TRIGGER "tr_period_AU"
AFTER UPDATE ON period
BEGIN
  UPDATE period SET dt_update = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
END;;;

CREATE TRIGGER  "tr_template_AI"
AFTER INSERT ON template
BEGIN
  UPDATE template SET dt_create = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
END;;;


CREATE TRIGGER  "tr_template_AU"
AFTER UPDATE ON template
BEGIN
  UPDATE template SET dt_update = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
END;;;


--CREATE TRIGGER
--  "tr_currency_AI"
--AFTER INSERT ON currency
--BEGIN
--  UPDATE currency SET dt_create = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
--END;;;


--CREATE TRIGGER
--  "tr_currency_AU"
--AFTER UPDATE ON currency
--BEGIN
--  UPDATE currency SET dt_update = strftime('%s','now','localtime')*1000 WHERE _id = NEW._id;
--END;;;
