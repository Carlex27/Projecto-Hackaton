## Seguimiento de los avances del proyecto

### Primera fase: Preparación del entorno de trabajo
- [x] Crear el repositorio en GitHub.
- [x] Crear el entorno de desarrollo.
- [x] Crear el proyexto con Spring Boot.

### Segunda fase: Creación de la base de datos
- [x] Diseñar el modelo de datos.

El modelo de datos que ha sido diseñado es el siguiente:


### Tercera fase: Crear entidades del proyecto

- [x] Entidad User

```java
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
```

- [x] Entidad Teams
```java
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
```

- [x] Entidad TeamMembers
```java
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
```
- [x] Entidad Event
```java
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
```
- [x] Entidad Competitor
```java
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "event_ids")
    private Event event;
```
### Cuarta fase: Crear repositorios del proyecto

