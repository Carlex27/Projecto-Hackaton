## Seguimiento de los avances del proyecto

### Introducción
Este documento es para llevar un seguimiento de los pasos que se han realizado en el proyecto.
Asi como para tener un control de lo que falta por desarrollar, para prevenir y monitorear errores a un futuro.

### Idea del proyecto
La idea del proyecto es desarrollar una plataforma donde poder organizar hackatones digitales, donde los usuarios podran
registrarse, crear equipos, unirse a equipos, crear eventos, unirse a eventos, y competir en los eventos.
El lider del equipo es el unico capaz de poder invitar a los participantes a su equipo, asi como todos los eventos son publicos
para todos los usuarios.
Para poder llevar un control del desarrollo de los proyectos de las hackatones, sera necesario vincular un repositorio 
de github a cada equipo, para que los organizadores de los eventos tengan un acceso rapido y sencillo a los proyectos, 
asi como contacto directo con los participantes.
Entonces el proyecto se divide en dos partes:
La primera es la parte de la creacion de eventos junto con sus operaciones adjuntas, asi como el monitoreo y comunicacion
con cada equipo de manera directa por parte de los organizadores.

Se necesitara lo siguiente:
- Crear usuarios
- Crear eventos
- Crear equipos
- Unirse a equipos
- Unirse a eventos
- Crear proyectos
- Vincular repositorios de github
- Chat con el equipo

### Dependencias a utilizar en el backend:
- Java
- Spring Framework
- Spring Web
- Spring Data JPA
- Spring Security
- Lombok
- OAuth2 Client
- OAuth2 Resource Server
- OAuth2 Jose
- Spring Configuration processor
- Spring Boot DevTools
- Spring Boot Starter Validation


### Primera fase: Preparación del entorno de trabajo
- [x] Crear el repositorio en GitHub.
- [x] Crear el entorno de desarrollo.
- [x] Crear el proyecto con Spring Boot.
- [x] Agregar las dependencias faltantes
- [x] Crear la estructura de paquetes.

### Segunda fase: Creación de la base de datos
- [x] Diseñar el modelo de datos.

El modelo de datos que ha sido diseñado es el siguiente:


### Tercera fase: Crear entidades del proyecto

- [x] Entidad User

```java
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private  List<RefreshTokenEntity> refreshTokens;


}
```

- [x] Entidad Teams
```java
public class Teams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

}
```

- [x] Entidad TeamMembers
```java
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Teams team;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String rol;

    private boolean isLeader;

}
```
- [x] Entidad Event
```java
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Lob
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

}
```
- [x] Entidad Competitor
```java
public class Competitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_ids")
    private Event event;
}
```
### Cuarta fase: Crear repositorios del proyecto
-[x] Repositorio User
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPassword(String password);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteByUsername(String username);
}

```
-[x] Repositorio Teams
```java
@Repository
public interface TeamsRepository extends JpaRepository<Teams, Long> {
    Optional<Teams> findByName(String name);
    List<Teams> findAllByEventId(Long eventId);
    Optional<Teams> findByNameAndEventId(String name, Long eventId);
    void deleteByName(String name);
    void deleteByNameAndEventId(String name, Long eventId);
}
```
- [x] Repositorio TeamMembers
```java
@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>{
    Optional<TeamMember> findByUserIdAndTeamId(Long userId, Long teamId);
}

```
- [x] Repositorio Event
```java
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
    Optional<Event> findById(Long id);
    Optional<Event> findByCreatorId(Long creatorId);
}
```
- [x] Repositorio Competitor
```java
@Repository
public interface CompetitorRepository extends JpaRepository<Competitor, Long> {
    List<Competitor> findAllByEventId(Long eventId);
    List<Competitor> findAllByUserId(Long userId);
    Optional<Competitor> findByUserIdAndEventId(Long userId, Long eventId);
}
```

### Quinta fase: Implementar Spring security y JWT


