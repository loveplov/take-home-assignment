create table public.accounts
(
    id       varchar(255) not null,
    balance  bigint not null,
    primary key (id)
);


insert into public.accounts(id, balance) values ('6416b75e643a11ed81ce0242ac120002', 100000);
  insert into public.accounts(id, balance) values ('782c2e72643a11ed81ce0242ac120002', 0);

