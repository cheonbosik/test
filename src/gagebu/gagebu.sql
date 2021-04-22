use works;

show tables;

create table gagebu (
  idx 	int  not  null  auto_increment primary key,	/* 고유번호 */
  wdate datetime default now(),				/* '수입/지출'이 일어난 날짜 */
  gCode char(1)	 not null,						/* 수입(+)/지출(-) */
  price int not null,									/* 수입/지출 금액 */
  content varchar(100) not null,			/* 수입/지출 내역 */
  balance int   default 0							/* 잔고 */
);

desc gagebu;
/* drop table gagebu; */
/* delete from gagebu; */

insert into gagebu values (default,'2021-04-19','+',5000,'2021년 4월용돈',5000);
insert into gagebu values (default,default,'+',10000,'2021년 4월 추가용돈',15000);

select * from gagebu;