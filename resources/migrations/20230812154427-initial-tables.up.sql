create table hymn
( id integer primary key autoincrement
, english_name varchar(300)
, english_num integer
, spanish_name varchar(300)
, spanish_num integer
, tags varchar(200));

--;;

create table meeting
( id integer primary key autoincrement
, name varchar(200)
, date timestamp
, details text
, date_created timestamp default current_timestamp);

--;;

create table meeting_hymn
( id integer primary key autoincrement
, hymn_id integer
, meeting_id integer
, foreign key (hymn_id) references hymn(id)
, foreign key (meeting_id) references meeting(id));
