drop database if exists NotasDB;
create database if not exists NotasDB;
use NotasDB;

create table Usuarios(
	id BIGINT primary key auto_increment,
    email varchar(50) not null,
    nombre varchar(20) not null,
    apellidos varchar(40) not null,
    contraseña varchar(255) not null
    
);

create table Escritorios(
	id BIGINT primary key auto_increment,
    nombre varchar(20) not null,
    id_Usuario BIGINT not null,
    
    foreign key Escritorios(id_Usuario) references Usuarios(id) on delete cascade
);


create table Categorias(
	id BIGINT primary key auto_increment,
    color varchar(6) not null,
    nombre varchar(15) not null,
    idUsuario BIGINT not null,
    
    foreign key (idUsuario) references Usuarios(id) on delete cascade
);
create table Notas(
	id BIGINT primary key auto_increment,
	titulo varchar(30) not null,
    descripcion mediumtext,
    color varchar(6) not null,
    posicionX int not null,
     posiciony int not null,
    id_Creador BIGINT not null,
    id_Escritorio BIGINT not null,
    idCategoria BIGINT,
    foreign key (id_Creador) references Usuarios(id) on delete cascade,
     foreign key (idCategoria) references Categorias(id) on delete set null,
    foreign key (id_Escritorio) references Escritorios(id) on delete cascade
);

create table Preferencias(
	
    modoOscuro boolean default false not null,    
    tamañoFuente tinyint not null,
    idioma varchar(2) not null,
    idUsuario BIGINT not null primary key,
    
	foreign key (idUsuario) references Usuarios(id) on delete cascade
);

create table Usuarios_Notas(
	idUsuario BIGINT not null,
    idNota BIGINT not null,
    
	foreign key (idUsuario) references Usuarios(id) on delete cascade,
	foreign key (idNota) references Notas(id) on delete cascade,
    primary key(idUsuario, idNota)
);

insert into Usuarios(email, nombre, apellidos, contraseña) values ("admin@gmail.com","Administrador","Admin","a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");


insert into Escritorios(nombre, id_Usuario) values ("Escritorio1",1);

insert into Escritorios(nombre, id_Usuario) values ("Escritorio2",1);
insert into Categorias(color,nombre,idUsuario) values ("43e24f","importante",1);

insert into Notas(titulo,descripcion,color,id_Creador,id_Escritorio,idCategoria,posicionX,posicionY) values ("Nota 1"," Esta es mi primera nota","43e24f",1,1,1,1000,450);
insert into Notas(titulo,descripcion,color,id_Creador,id_Escritorio,idCategoria,posicionX,posicionY) values ("Nota 2"," Esta es mi segunda nota","43224f",1,1,1,50,200);
insert into Notas(titulo,descripcion,color,id_Creador,id_Escritorio,idCategoria,posicionX,posicionY) values ("Nota 3"," Esta es mi tercera nota","f3e28f",1,2,1,400,200);
