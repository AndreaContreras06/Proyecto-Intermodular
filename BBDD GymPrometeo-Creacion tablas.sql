CREATE TABLE Actividades (
	id_actividades INT PRIMARY KEY NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	duracion INT NOT NULL
	
);

CREATE TABLE Empleados (
	id_empleados INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	dni VARCHAR(9) UNIQUE NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	apellido VARCHAR(100) NOT NULL,
	telefono VARCHAR(100) NOT NULL,
	email VARCHAR(150) NOT NULL,
	password VARCHAR(255) NOT NULL,
	fecha_contrato TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

CREATE TABLE Planes (
	id_planes INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	personas_interesadas INT NOT NULL,

	id_empleados INT NOT NULL,
	FOREIGN KEY (id_empleados) REFERENCES Empleados (id_empleados)
	
);


CREATE TABLE usuarios(
	id_usuarios INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	dni VARCHAR(9) UNIQUE NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	apellido VARCHAR(100) NOT NULL,
	telefono VARCHAR(100) NOT NULL,
	email VARCHAR(150) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	rol VARCHAR(20) NOT NULL,

	id_planes INT NOT NULL,
	FOREIGN KEY (id_planes) REFERENCES Planes (id_planes)
	
);

CREATE TABLE Plan_Actividad (
	id_planes INT NOT NULL,
	FOREIGN KEY (id_planes) REFERENCES Planes (id_planes),

	id_actividades INT NOT NULL,
	FOREIGN KEY (id_actividades) REFERENCES Actividades (id_actividades)
	
);