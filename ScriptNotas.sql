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
    idUsuario BIGINT not null,
    
    foreign key Escritorios(idUsuario) references Usuarios(id)
);


create table Categorias(
	id BIGINT primary key auto_increment,
    color varchar(6) not null,
    nombre varchar(15) not null,
    idUsuario BIGINT not null,
    
    foreign key (idUsuario) references Usuarios(id)
);
create table Notas(
	id BIGINT primary key auto_increment,
	titulo varchar(30) not null,
    descripcion mediumtext,
    color varchar(6) not null,
    id_Creador BIGINT not null,
    idEscritorio BIGINT not null,
    idCategoria BIGINT not null,
    foreign key (id_Creador) references Usuarios(id),
     foreign key (idCategoria) references Categorias(id),
    foreign key (idEscritorio) references Escritorios(id)
);

create table Preferencias(
	
    modoOscuro boolean default false not null,    
    tamañoFuente tinyint not null,
    idioma varchar(2) not null,
    idUsuario BIGINT not null primary key,
    
	foreign key (idUsuario) references Usuarios(id)
);

create table Usuarios_Notas(
	idUsuario BIGINT not null,
    idNota BIGINT not null,
    
	foreign key (idUsuario) references Usuarios(id),
	foreign key (idNota) references Notas(id),
    primary key(idUsuario, idNota)
);

insert into Usuarios(email, nombre, apellidos, contraseña) values ("admin@gmail.com","Administrador","Admin","123");


insert into Escritorios(nombre, idUsuario) values ("Escritorio1",1);
insert into Categorias(color,nombre,idUsuario) values ("43e24f","importante",1);

insert into Notas(titulo,descripcion,color,id_Creador,idEscritorio,idCategoria) values ("Nota 1"," Esta es mi primera nota","43e24f",1,1,1);
insert into Notas(titulo,descripcion,color,id_Creador,idEscritorio,idCategoria) values ("Nota 2"," Esta es mi segunda nota","43e24f",1,1,1);
