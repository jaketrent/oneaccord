-- Place your queries here. Docs available https://www.hugsql.org/

-- :name select-hymns :? :*
select * 
from hymn
order by :sql:order_by_column;
