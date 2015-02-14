create table Items_Coord
(
Item_ID int(11) primary key,
Lat decimal(10,6) not null,
Lon decimal(10,6) not null
) engine = MyISAM;

insert into Items_Coord(Item_ID,Lat,Lon) select Item_ID,Latitude,Longitude from Items where Latitude is not null and Longitude is not null; 
alter table Items_Coord add Coordinates point not null;

update Items_Coord set Coordinates = POINT (Lat,Lon);
alter table Items_Coord drop Lat;
alter table Items_Coord drop Lon; 
CREATE SPATIAL INDEX sp_index ON Items_Coord(Coordinates);
