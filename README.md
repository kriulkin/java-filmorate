# java-filmorate

Database Scheme
![](filmorate_db_scheme.png)

Plotted with [QuickDBD](https://app.quickdatabasediagrams.com/#/) by following code:
```
film
-
film_id PK int
name varchar(64)
description varchar(200)
release_date date
duration int
mpa_id int FK >- mpa.mpa_id

user
-
user_id PK int
name varchar(64)
email varchar(256)
login varchar(64)
birthday date

like
-
user_id int FK >- user.user_id
film_id int FK >- film.film_id

friend
-
user1_id int FK >- user.user_id
user2_id int FK >- user.user_id
approved boolean

genre
-
genre_id int PK
name varchar(64)

film_genre
-
film_id int FK >- film.film_id
genre_id int FK >- genre.genre_id

mpa
-
mpa_id PK int
name varchar(64)i
```