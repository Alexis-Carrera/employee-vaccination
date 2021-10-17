# Deploy

A continuación se detalla como realizar el Deploy del proyecto.

Primero Crear base de datos llamada employee-vaccination en PostgreSQL.

Seguido de eso cambiar en application.properties las credenciales de la base de datos.

- Es necesario actualizar proyecto maven para llamar a las librerias necesarias.

Simplemente compilar el proyecto y seguido de ello es necesario ver si se crearon en la tabla employee, role , y employee_roles el primer usuario administrador

Si no existe dicho usuario es necesario crear la data entregada en data.sql

PROCESO:

en primer lugar se tiene que hacer un login 

endpoint :

POST http://localhost:8080/oauth/token
Authorization:
Basic Auth
Username: krugger
Password: password

Content-Type: application/x-www-form-urlencoded

BODY:
username: admin
password: password
grant_type: password


Con el access_token entregado se debe crear el empleado:

POST http://localhost:8080/employee
PARAMS:
access_token: access_token del API oauth/token



A partir de aquí se entrega un usuario (cédula), y contraseña aleatoria.

Con esto se puede ya empezar a realizar las pruebas para mas información tenemos:

http://localhost:8080/swagger-ui.html#/

Recordar que solo Usuario con ROL ADMIN puede crear empleado, borrar empleado, y adquirir la lista de empleados.


ENDPOINTS EJEMPLOS:
1.-LOGIN  (ROL: ADMIN, USER)
POST http://localhost:8080/oauth/token
Authorization:
Basic Auth
Username: krugger
Password: password

Content-Type: application/x-www-form-urlencoded

BODY:
username: admin
password: password
grant_type: password


2.- CREAR EMPLEADO (ROL: ADMIN)
POST http://localhost:8080/employee
PARAMS:
access_token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzQ1Mjc1NzgsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJkM2NiNTc2MS1mMWI4LTQwZjItYThiMi1iMmI3YjNhNjNmNDgiLCJjbGllbnRfaWQiOiJrcnVnZ2VyIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXX0.Ynt89AltA00OeYSRZQsZiTivCGW7pm6zBYE-GNZo1lU

Content-Type: application/json

BODY:
{    
"nationalIdNumber": "1720168291",
"names": "Alexis Gabriel",
"lastNames": "Carrera Cepeda",
"email": "alexiscarrerachino@hotmail.com",
"role": ["USER"]
}

3.- ACTUALIZAR INFORMACIÓN EMPLEADO (ROL: ADMIN, USER)

PATCH  http://localhost:8080/employee/{employeeId}
PARAMS:
access_token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzQ1Mjc1NzgsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJkM2NiNTc2MS1mMWI4LTQwZjItYThiMi1iMmI3YjNhNjNmNDgiLCJjbGllbnRfaWQiOiJrcnVnZ2VyIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXX0.Ynt89AltA00OeYSRZQsZiTivCGW7pm6zBYE-GNZo1lU

Content-Type: application/json

BODY:
{    
"dateOfBirth": "23/06/1994",
"address": "Quito Kennedy Blas Riveros N53-207",
"mobilePhoneNumber": "0997539515",
"vaccineStatus": "Vacunado",
"employeeVaccinationInfo": {
    "vaccineTypeId": 2,
    "dosageNumberApplied": 2,
    "vaccinationDate": "26/09/2021"
}   
}

4.- GET INFORMACIÓN EMPLEADO (ROL: ADMIN, USER)

GET  http://localhost:8080/employee/{employeeId}
PARAMS:
access_token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzQ1Mjc1NzgsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJkM2NiNTc2MS1mMWI4LTQwZjItYThiMi1iMmI3YjNhNjNmNDgiLCJjbGllbnRfaWQiOiJrcnVnZ2VyIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXX0.Ynt89AltA00OeYSRZQsZiTivCGW7pm6zBYE-GNZo1lU

Content-Type: application/json



5.- BORRAR INFORMACIÓN EMPLEADO (ROL: ADMIN)

DELETE  http://localhost:8080/employee/{employeeId}
PARAMS:
access_token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzQ1Mjc1NzgsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJkM2NiNTc2MS1mMWI4LTQwZjItYThiMi1iMmI3YjNhNjNmNDgiLCJjbGllbnRfaWQiOiJrcnVnZ2VyIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXX0.Ynt89AltA00OeYSRZQsZiTivCGW7pm6zBYE-GNZo1lU

Content-Type: application/json


6.- GET LISTA COMPLETA CON PARAMETROS INFORMACIÓN EMPLEADO (ROL: ADMIN)

GET  http://localhost:8080/employeeslist
PARAMS:
access_token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzQ1Mjc1NzgsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJkM2NiNTc2MS1mMWI4LTQwZjItYThiMi1iMmI3YjNhNjNmNDgiLCJjbGllbnRfaWQiOiJrcnVnZ2VyIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXX0.Ynt89AltA00OeYSRZQsZiTivCGW7pm6zBYE-GNZo1lU
(SOLO uno de los siguientes)

1.- vaccinationStatus : Vacunado 

2.- vaccineTypeId: 2

3.- rangeInit: 21/09/2021
    rangeFinal: 30/09/2021

Content-Type: application/json