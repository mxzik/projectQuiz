use Khimach
go
insert into Registration(Email, LoginName, FullName, Password1)
values('mxzikz7@gmail.com', 'duramaan', 'Химач Кирилл d', '24799742kx')


/*
create table Registration
(
Id int PRIMARY KEY IDENTITY,dssd
Email NVARCHAR(40) UNIQUE,
LoginName NVARCHAR(20) UNIQUE,
FullName NVARCHAR(20),
Password1 VARCHAR(20)
)

create table Results
(
Id int PRIMARY KEY IDENTITY,
PersonsId INT REFERENCES Registration (Id),
PersonsName NVARCHAR(20) REFERENCES Registration (LoginName),
Result INT
)*/

