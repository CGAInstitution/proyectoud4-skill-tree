drop database if exists NotasDB;
create database if not exists NotasDB;
use NotasDB;

create table Usuarios(
	id int primary key auto_increment,
    email varchar(50) not null,
    nombre varchar(20) not null,
    apellidos varchar(40) not null,
    contraseña varchar(255) not null
    
);

create table Escritorios(
	id int primary key auto_increment,
    nombre varchar(20) not null,
    idUsuario int not null,
    
    foreign key Escritorios(idUsuario) references Usuarios(id)
);


create table Categorias(
	id int primary key auto_increment,
    color varchar(6) not null,
    nombre varchar(15) not null,
    idUsuario int not null,
    
    foreign key (idUsuario) references Usuarios(id)
);
create table Notas(
	id int primary key auto_increment,
	titulo varchar(30) not null,
    descripcion mediumtext,
    color varchar(6) not null,
    idCreador int not null,
    idEscritorio int not null,
    idCategoria int not null,
    foreign key (idCreador) references Usuarios(id),
     foreign key (idCategoria) references Categorias(id),
    foreign key (idEscritorio) references Escritorios(id)
);

create table Preferencias(
	
    modoOscuro boolean default false not null,    
    tamañoFuente tinyint not null,
    idioma varchar(2) not null,
    idUsuario int not null primary key,
    
	foreign key (idUsuario) references Usuarios(id)
);

create table Usuarios_Notas(
	idUsuario int not null,
    idNota int not null,
    
	foreign key (idUsuario) references Usuarios(id),
	foreign key (idNota) references Notas(id),
    primary key(idUsuario, idNota)
);
