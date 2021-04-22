use works;

show tables;

-- '남자/여자'군에 자료 1건씩 출력
select distinct gender as '성별' from jusorok;

-- 각 직업군(중복을 배제)에 대한 자료출력?
select distinct job as '직업' from jusorok;

-- 남자들의 직업군에 대한 자료출력?
select distinct job as '직업' from jusorok where gender = '남자';

-- 주소록에 사용된 직업의 개수(count)는?
select count(*) from jusorok;  -- 전체 레코드 개수
select count(name) from jusorok where gender = '남자'; -- 성별이 남자인 자료의 개수
select count(distinct job) as '사용된 직업개수' from jusorok;

-- 개수를 구하는 함수 : count()
-- 서울에 거주하는 여자의 인원수를 구하시오?
select count(*) from jusorok where address='서울' and gender='여자';

-- 합계를 구하는 함수 : sum()
-- 전체 'pay의 합계'를 구하시오?
select sum(pay) as 급여 from jusorok;

-- 서울에 거주하는 여자회원의 총 급여 합계를 구하시오?
select sum(pay) from jusorok where address='서울' and gender='여자';

-- 평균을 구하는 함수 : avg()
-- 'pay'의 전체 평균을 구하시오?
select avg(pay) from jusorok;

-- 남자회원의 급여 평균을 구하시오?
select gender,avg(pay) from jusorok where gender = '남자';

-- 최대값을 구하는 함수 : max();
-- 최대 급여액을 구하시오?
select max(pay) from jusorok;

-- 최소값을 구하는 함수 : min();
-- 최소 급여액을 구하시오?
select min(pay) from jusorok;

-- 그룹(군)을 지어서 표시하고자 할때? group by절 ~ having 조건
-- 성별에 대한 자료?
select gender from jusorok group by gender;

-- 성별에 따른 급여액의 평균?
select gender, avg(pay) from jusorok group by gender;

-- 각 성별의 따른, 인원수와 급여의 합계를 출력하시오.
select gender, count(*), sum(pay) from jusorok group by gender;

-- 각 성별의 따른, 인원수와 급여의 합계를 출력하시오.(단, 나이가 30세 이상인 자료만)
select gender, count(*), sum(pay) from jusorok where age>=30 group by gender;

-- 각 직업군을 출력하세요.(group by 절 사용)
select job from jusorok group by job;

-- 각 직업군에 개수를 출력하시오.
select job,count(*) from jusorok group by job;

-- 각 직업군에 개수를 출력하시오.(단, 직업군의 개수가 2개이상인 경우만 출력하시오)
select job,count(*) as cnt from jusorok group by job having cnt>=2;

-- 각 직업군의 남자의 개수를 출력하시오.
select job,count(*) as cnt from jusorok where gender='남자' group by job;

-- 각 직업군의 남자의 개수를 출력하시오.(단, 직업군의 개수가 2개이상인 경우만 출력하시오)
select job,count(*) as cnt from jusorok where gender='남자' group by job having cnt>=2;

-- 반올림 : round(), round(소수이하자리수)
select round(123.456);
select round(123.456, 1);
select round(123.456, -1);

-- 주소록의 나이 평균을 구하시오.
select avg(age) from jusorok;

-- 주소록의 나이 평균을 구하시오.(정수부 반올림 처리 하시오.)
select round(avg(age)) from jusorok;

-- 절삭함수 : truncate(표현자리수)
select truncate(125.678, 0);
select truncate(125.678, 1);
select truncate(125.678, -1);

-- 주소록의 나이 평균을 구하시오.(정수로 출력 : 소수이하 절삭처리 하시오.)
select truncate(avg(age), 0) from jusorok;

-- 무조건 올림 : ceil(),  무조건 내림 : floor()
select ceil(123.456);
select ceil(-123.456);

select floor(123.956);
select floor(-123.956);

-- 나이이의 평균을 구하되, 소수이하는 버리시오.
select floor(avg(age)) from jusorok;

-- 성별에 따른 나이의 평균을 구하되 소수이하는 버리시오.
select gender, floor(avg(age)) from jusorok group by gender;

-- 절대값(양수값 표현) : abs()
select abs(1234);
select abs(-1234);
select (123 - 456);
select abs(123 - 456);

-- 나머지 함수 : mod(정수1, 정수2)
select mod(10, 3);

-- 10을 3으로 나누었을때의 몫과, 나머지를 출력?
select floor(10/3) as '몫', mod(10,3) as '나머지';

-- 지수(승수)함수 : power(n1, n2)  밑수 n1, 지수가 n2
select power(2, 5) as '2^5';

-- 천단위마다 쉼표를 표시 : format(필드명, 소수이하자리수)
select format(1234567, 0);
select format(1234.567, 2);
select name, format(pay, 0) from jusorok;


/* 문자 함수 */
-- 영문자의 대/소문자 변환 ?  upper(), lower()
select upper('SeOuL');
select lower('SeOuL');
select upper('Welcome to Korea!');
select lower('Welcome to Korea!');

-- 문자열의 길이를 구하시오
select length('seoul');
select job,length(job) from jusorok;

-- 결합연산자 : concat(문자열1, 문자열2,....)
select concat('a','b','c');
select name, age from jusorok;
select concat('이름 = ', name), concat('나이 = ', age) from jusorok;

-- 지정된 문자열을 반환 : substr(문자열, 시작위치(1), 발췌할개수)
select substr('Welcome to Korea', 4, 4);  -- 'come' 출력
select substr('Welcome to Korea', 9, 2);  -- 'to' 출력
select substr('Welcome to Korea', 12);  -- 'Korea' 출력

-- 'seoul서울'의 길이는?
select length('seoul서울');
select substr('Welcome to 한국 Korea', 12, 2);  -- '한국' 출력

-- 직업의 '2번째글자'만 출력하시오
select name, substr(job, 2, 1) from jusorok;

-- 생일을 'xxxx년 xx월 xx일'로 성명과 함께 출력하시오.
select name,concat(substr(birthday,1,4),'년 ',substr(birthday,6,2),'월 ',substr(birthday,9,2),'일') as 생일 from jusorok;

-- 중간위치값을 출력? mid(문자열, 시작위치, 추출크기)
select mid('Welcome to Korea',4,4);
select mid('Welcome to Korea',4);

-- 문자열중에서 특정 문자의 존재유무? instr()
select instr('Welcome to Korea', 'o');
select instr('Welcome to Korea', 'O');
select instr('Welcome to Korea', ' ');
select instr('Welcome to Korea', '  ');

-- 왼쪽,오른쪽,중간 문자열 추출 : left(), right(), mid()
select left('Welcome to Korea', 3);
select right('Welcome to Korea', 3);
select mid('Welcome to Korea', 4, 4);

-- 문자열 치환 : replace(str, char1, char2) : 문자열(str)중에서 특정문자(char1)를
--    지정문자(char2)로 치환한다.
select ('Welcome to Korea');
select replace('Welcome to Korea',' ','');
select replace('Welcome to Korea',' ','_');

-- 날짜 함수
-- 오늘 시스템의 날짜/시간 출력 : now()
select now();

-- 년도를 출력 : year()
select year(now());

-- 월를 출력 : month()
select month(now());

-- 년도를 출력 : day()
select day(now());

select year(now()), month(now()), day(now());

select concat(year(now()),'년 ',month(now()),'월 ',day(now()),'일');

select name, birthday from jusorok;
select name, substr(birthday,1,10) from jusorok;

--  시간(hour()) / 분(minute()) / 초(second())
select hour(now());
select minute(now());
select second(now());

select concat(hour(now()),'시 ',minute(now()),'분 ',second(now()),'초') as '현재시간';

-- 요일(dayname() / week())
select dayname(now());    -- 현재 요일을 영어로 출력한다.
select week(now()); 			-- 현재가 올해의 몇번째 주인지를 출력(시작 0부터)

select monthname(now());	-- 현재 월을 영어로 출력한다.

-- 지정된 형식으로 날짜 출력 : date_format() , 날짜기호앞에 '%'붙여서 서식지정
select name, birthday from jusorok;
select date_format(now(),'%y-%m-%d');   -- %y : 년도 2자리 , %m %d:'월'과 '일'이 두자리
select date_format(now(),'%Y-%m-%d');		-- %Y : 년도 4자리
select date_format(now(),'%Y-%M-%d');		-- %M : 월을 영어로 표시
select date_format(now(),'%Y-%m-%d %W'); -- %W : 요일을 영어로
select date_format(now(),'%Y-%m-%d %w'); -- %w : 요일을 숫자로(월요일 : 1)
select date_format(birthday, '%Y-%m-%d %W') from jusorok;
select date_format(birthday, '%Y-%m-%d %w') from jusorok;

select date_format(now(), '%h:%i:%s'); 	-- AM/PM 12시간제 (시간 : 03)
select date_format(now(), '%H:%i:%s');	-- 24시간제
select date_format(now(), '%l:%i:%s');	-- 시간이 : 3(한자리)
select date_format(now(), '%r');				-- AM/PM표시한다. 12시간제 (시간 : 03 PM)
select date_format(now(), '%Y-%c-%e');	-- '월'과 '일'의 표현을 한(두)자리로 표시.
select date_format(birthday, '%Y-%c-%e') from jusorok;

-- 현재날짜? now() / sysdate() / current_timestamp()
select now();
select sysdate();
select current_timestamp();

-- 날짜 연산(date_add(기존날짜, 계산수치값)) - interval 명령어와 함께사용...
select now();														-- 오늘날짜
select date_add(now(), interval 1 day);	-- 오늘날짜 + 1
select date_add(now(), interval -1 day);	-- 오늘날짜 - 1
select date_add(now(), interval 1 hour);	-- 현재시간 + 1
select date_add(now(), interval -1 hour);	-- 현재시간 - 1
select date_add(now(), interval 24 hour);	-- 현재시간 + 24
select date_add(birthday, interval 1 day) from jusorok;
select date_add(birthday, interval 24 hour) from jusorok;

-- 달의 마지막 일자? last_day();
select last_day(now());
select last_day('2021-2-1');
select last_day('2020-2-1');

-- 날짜 차이 계산하기(to_days())
select to_days(now()) - to_days('2021-4-1');  -- (오늘날짜 - 2021년 4월 1일)

-- 오늘부터 5일이 지났다를 표시해 보자?
select to_days(now()) - to_days(date_add(now(), interval -5 day));

-- 날짜 차이 비교 계산 : datediff()
select datediff(now(), '2021-4-1');  -- (오늘날짜 - 2021년 4월 1일)


