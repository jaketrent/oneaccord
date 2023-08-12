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

-- :name insert-meeting-hymn! :! :n
INSERT INTO meeting_hymn
(meeting_id, hymn_id)
VALUES (:meeting-id, :hymn-id);

-- :name delete-meeting-hymn! :! :n
delete from meeting_hymn
where meeting_id = :meeting-id
and hymn_id = :hymn-id;

-- :name select-meeting-hymns :? :*
select h.*
from meeting_hymn mh
join hymn h on mh.hymn_id = h.id
where mh.meeting_id = :id;

-- :name find-next-meeting-hymns :? :*
SELECT m.*
, h.*
FROM meeting m
join meeting_hymn mh on mh.meeting_id = m.id
join hymn h on mh.hymn_id = h.id
order by m.date;
