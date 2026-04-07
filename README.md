# API REST - Clínica Médica

API REST para la gestión de una clínica médica, desarrollada con Spring Boot y MySQL.

## Tecnologías

- Java 17
- Spring Boot 4.0.2
- Spring Data JPA / Hibernate
- MySQL
- Lombok
- Apache Commons Lang3
- Maven

## Requisitos previos

- Java 17+
- Maven
- MySQL en el puerto `3307`

## Configuración

Crear la base de datos en MySQL:

```sql
CREATE DATABASE proyectofinal_damc;
```

Las credenciales por defecto en `application.properties` son:

```
usuario: root
contraseña: pass
puerto: 3307
```

Ajústalas si es necesario antes de arrancar.

## Arranque

```bash
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

---

## Endpoints

### Especialidades — `/api/especialidades`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/lista` | Listar todas las especialidades |
| GET | `/buscarPorId/{id}` | Buscar por ID |
| POST | `/crear` | Crear especialidad |
| DELETE | `/delete/{id}` | Eliminar especialidad |

### Médicos — `/api/medicos`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/lista` | Listar todos los médicos |
| GET | `/buscarPorId/{id}` | Buscar por ID |
| GET | `/porEspecialidad/{especialidadId}` | Filtrar por especialidad |
| POST | `/registrar` | Registrar médico |
| DELETE | `/delete/{id}` | Eliminar médico |

### Pacientes — `/api/pacientes`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/lista` | Listar todos los pacientes |
| GET | `/buscarPorId/{id}` | Buscar por ID |
| GET | `/buscarPorDni/{dni}` | Buscar por DNI |
| POST | `/registrar` | Registrar paciente |
| POST | `/{pacienteId}/crearHistoriaClinica` | Crear historia clínica |
| DELETE | `/delete/{id}` | Eliminar paciente |

### Citas — `/api/citas`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/lista` | Listar todas las citas |
| GET | `/buscarPorId/{id}` | Buscar por ID |
| GET | `/paciente/{pacienteId}` | Citas de un paciente |
| GET | `/medico/{medicoId}` | Citas de un médico |
| GET | `/medico/{medicoId}/fecha/{fecha}` | Citas de un médico en una fecha |
| GET | `/estado/{estado}` | Filtrar por estado (`PENDIENTE`, `COMPLETADA`, `CANCELADA`) |
| POST | `/agendar` | Agendar cita |
| PUT | `/cancelar/{id}` | Cancelar cita |
| PUT | `/registrarDiagnostico/{id}` | Registrar diagnóstico y marcar como completada |
| DELETE | `/delete/{id}` | Eliminar cita |

### Facturas — `/api/facturas`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/lista` | Listar todas las facturas |
| GET | `/buscarPorId/{id}` | Buscar por ID |
| GET | `/buscarPorNumero/{numeroFactura}` | Buscar por número de factura |
| GET | `/porCita/{citaId}` | Factura de una cita |
| GET | `/estado/{estado}` | Filtrar por estado (`PENDIENTE`, `PAGADA`) |
| POST | `/generar` | Generar factura (solo citas completadas) |
| PUT | `/marcarPagada/{id}` | Marcar factura como pagada |
| DELETE | `/delete/{id}` | Eliminar factura |

---

## Modelo de datos

```
Especialidad 1 ──── N Médico
Paciente     1 ──── 1 HistoriaClinica
Paciente     1 ──── N Cita
Médico       1 ──── N Cita
Cita         1 ──── 1 Factura
```

## Flujo de uso típico

1. Crear una **especialidad**
2. Registrar un **médico** asignándole la especialidad
3. Registrar un **paciente**
4. Crear la **historia clínica** del paciente
5. **Agendar una cita** (médico + paciente + fecha/hora)
6. **Registrar el diagnóstico** de la cita → pasa a `COMPLETADA`
7. **Generar la factura** de la cita completada
8. **Marcar la factura como pagada**
