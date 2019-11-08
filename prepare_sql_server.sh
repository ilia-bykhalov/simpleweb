psql -h localhost -Upostgres -d postgres -p 5400 psql -c "drop database if exists simpleweb"
psql -h localhost -Upostgres -d postgres -p 5400 psql -c "CREATE DATABASE simpleweb;"
psql -h localhost -Upostgres -d simpleweb -p 5400 psql -c "create table userinfo (id uuid not null primary key,login text not null,pass text not null,balance int not null);"
psql -h localhost -Upostgres -d simpleweb -p 5400 psql -c "CREATE UNIQUE INDEX u__userinfo__login ON userinfo (login);"

psql -h localhost -Upostgres -d postgres -p 5400 psql -c "drop database if exists simpleweb_test"
psql -h localhost -Upostgres -d postgres -p 5400 psql -c "CREATE DATABASE simpleweb_test;"
psql -h localhost -Upostgres -d simpleweb_test -p 5400 psql -c "create table userinfo (id uuid not null primary key,login text not null,pass text not null,balance int not null);"
psql -h localhost -Upostgres -d simpleweb_test -p 5400 psql -c "CREATE UNIQUE INDEX u__userinfo__login ON userinfo (login);"

