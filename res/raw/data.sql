PRAGMA foreign_keys=ON;;;

--Transfer
INSERT INTO category(active, name,id_parent,isgroup) VALUES(0,'Transfer between accounts',0,1);;;

--Without category
INSERT INTO category(name,id_parent,isgroup) VALUES('Without category',0,1);;;

--Incomes
INSERT INTO category(name,id_parent,isgroup) VALUES('Income',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Child support,alimony',0);;;
INSERT INTO category(name,id_parent) VALUES('Investment',0);;;
INSERT INTO category(name,id_parent) VALUES('Payments or transfers',0);;;
INSERT INTO category(name,id_parent) VALUES('Other(trust distributions,etc.)',0);;;
INSERT INTO category(name,id_parent) VALUES('Reversal of charges',0);;;
INSERT INTO category(name,id_parent) VALUES('From rental property',0);;;
INSERT INTO category(name,id_parent) VALUES('Social security benefits',0);;;
INSERT INTO category(name,id_parent) VALUES('From small business',0);;;
INSERT INTO category(name,id_parent) VALUES('Salary & wages',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ='Income' ) WHERE  id_parent = 0 and isgroup = 0 ;;;

--Excpenses
INSERT INTO category(name,id_parent,isgroup) VALUES('Groceries ',0,1);;;
INSERT INTO category(name,id_parent,isgroup) VALUES('Gifts',0,1);;;
INSERT INTO category(name,id_parent,isgroup) VALUES('Household',0,1);;;
INSERT INTO category(name,id_parent,isgroup) VALUES('Home care',0,1);;;
INSERT INTO category(name,id_parent,isgroup) VALUES('Personal care',0,1);;;
INSERT INTO category(name,id_parent,isgroup) VALUES('Rent',0,1);;;
INSERT INTO category(name,id_parent,isgroup) VALUES('Subscriptions',0,1);;;
INSERT INTO category(name,id_parent,isgroup) VALUES('Clothing',0,1);;;
INSERT INTO category(name,id_parent,isgroup) VALUES('Mortgage payments',0,1);;;
INSERT INTO category(name,id_parent,isgroup) VALUES('Medical,dental costs ',0,1);;;
INSERT INTO category(name,id_parent,isgroup) VALUES('Outgoing payments',0,1);;;

INSERT INTO category(name,id_parent,isgroup) VALUES('Automobile',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Fuel',0);;;
INSERT INTO category(name,id_parent) VALUES('Lease',0);;;
INSERT INTO category(name,id_parent) VALUES('Maintenance',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ='Automobile' ) WHERE  id_parent = 0 and isgroup = 0 ;;;

INSERT INTO category(name,id_parent,isgroup) VALUES('Entertainment',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Dining,eating out',0);;;
INSERT INTO category(name,id_parent) VALUES('Movies',0);;;
INSERT INTO category(name,id_parent) VALUES('Sports,recreation',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ='Entertainment' ) WHERE  id_parent = 0 and isgroup = 0 ;;;

INSERT INTO category(name,id_parent,isgroup) VALUES('Insurance',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Auto',0);;;
INSERT INTO category(name,id_parent) VALUES('Homeowner,renter',0);;;
INSERT INTO category(name,id_parent) VALUES('Health',0);;;
INSERT INTO category(name,id_parent) VALUES('Life',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ='Insurance' ) WHERE  id_parent = 0 and isgroup = 0 ;;;

INSERT INTO category(name,id_parent,isgroup) VALUES('Loans',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Auto',0);;;
INSERT INTO category(name,id_parent) VALUES('Home improvement',0);;;
INSERT INTO category(name,id_parent) VALUES('Home equity',0);;;
INSERT INTO category(name,id_parent) VALUES('Student',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ='Loans') WHERE  id_parent = 0 and isgroup = 0 ;;;

INSERT INTO category(name,id_parent,isgroup) VALUES('Phone',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Long distance',0);;;
INSERT INTO category(name,id_parent) VALUES('Local',0);;;
INSERT INTO category(name,id_parent) VALUES('Wireless',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ='Phone' ) WHERE  id_parent = 0 and isgroup = 0 ;;;

INSERT INTO category(name,id_parent,isgroup) VALUES('Retirement savings',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Roth IRA',0);;;
INSERT INTO category(name,id_parent) VALUES('Traditional IRA',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ='Retirement savings' ) WHERE  id_parent = 0 and isgroup = 0 ;;;

INSERT INTO category(name,id_parent,isgroup) VALUES('Taxes',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Property',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ='Taxes') WHERE  id_parent = 0 and isgroup = 0 ;;;

INSERT INTO category(name,id_parent,isgroup) VALUES('Utilities',0,1);;;
INSERT INTO category(name,id_parent) VALUES('Cable',0);;;
INSERT INTO category(name,id_parent) VALUES('Gas',0);;;
INSERT INTO category(name,id_parent) VALUES('Electric',0);;;
INSERT INTO category(name,id_parent) VALUES('Internet',0);;;
INSERT INTO category(name,id_parent) VALUES('Water',0);;;
UPDATE category SET id_parent =(SELECT c1._id  FROM category c1 WHERE c1.name ='Utilities' ) WHERE  id_parent = 0 and isgroup = 0 ;;;
