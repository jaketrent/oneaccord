-- Place your queries here. Docs available https://www.hugsql.org/

-- :name select-hymns :? :*
select * 
from hymn
order by :sql:order_by_column;

-- :name select-meetings :? :*
select * 
from meeting
order by date

-- :name insert-meeting! :! :n
INSERT INTO meeting
(date, name, details)
VALUES (:date, :name, :details);

-- :name update-meeting! :! :n
update meeting
set date = :date
, name = :name
, details = :details
where id = :id;

-- :name delete-meeting! :! :n
delete from meeting
where id = :id;

-- :name find-meeting :? :1
SELECT *
FROM meeting
where id = :id;
