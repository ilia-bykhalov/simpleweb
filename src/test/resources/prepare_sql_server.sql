drop table if exists userinfo;

create table userinfo (
    id uuid not null primary key,
    login text not null,
    pass text not null,
    balance int not null
);


CREATE UNIQUE INDEX u__userinfo__login
  ON userinfo (login);



drop table if exists userinfo;

create table userinfo (
    login text not null primary key,
    pass text not null
);



sudo docker run --rm   --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5400:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres

