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

- [x] Lo primero es crear una clase que implemente el UserDetails y UserDetailsService
Dentro del paquete config->userConfig se van a crear las clases UserConfig y UserManagerConfig
#### UserConfig Class
Sirve para implementar la interfaz UserDetails, la cual nos provee del core de usuarios.
Donde despues podemos encapsular la informacion dentro de Authentication objects.
```java
@RequiredArgsConstructor
public class UserConfig implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays
                .stream(user
                        .getRoles()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
```

#### UserManagerConfig Class

Esta clase implementando la interfaz UserDetailsService, nos permite cargar un usuario por su nombre,
usando loadUserByUsername, y retornando un objeto UserDetails.

```java
@Service
@RequiredArgsConstructor
public class UserManagerConfig implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(UserConfig::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Username: " + username + " does not exist"
                        ));
    }
}
```

#### SecurityConfig Class

Vamos creando la clase de SecurityConfig, donde hacermos las configuraciones de seguridad.
Para los endpoints de la api.

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final UserManagerConfig userInfoManagerConfig;
    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenRepository refreshTokenRepo;
    private final LogoutHandlerService logoutHandlerService;

    /**
     * Security configuration for the sign-in endpoint
     * @param httpSecurity the HttpSecurity object to configure
     * @return the SecurityFilterChain
     * @throws Exception if an error occurs
     */

    @Order(1)
    @Bean
    public SecurityFilterChain signInSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/sign-in/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .userDetailsService(userInfoManagerConfig)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint((request, response, authException) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
                })
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /**
     * Security configuration for API endpoints
     *
     * @param httpSecurity the HttpSecurity object to configure
     * @return The SecurityFilterChain
     * @throws Exception if an error occurs
     */
    @Order(2)
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/api/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAccessTokenFilter(rsaKeyRecord, jwtTokenUtils), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> {
                    log.error("[SecurityConfig:apiSecurityFilterChain] Exception due to :{}",ex);
                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                })
                .httpBasic(Customizer.withDefaults())
                .build();
    }


    @Order(3)
    @Bean
    public SecurityFilterChain refreshTokenSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/refresh-token/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtRefreshTokenFilter(rsaKeyRecord,jwtTokenUtils,refreshTokenRepo), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> {
                    log.error("[SecurityConfig:refreshTokenSecurityFilterChain] Exception due to :{}",ex);
                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                })
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Order(4)
    @Bean
    public SecurityFilterChain logoutSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/logout/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAccessTokenFilter(rsaKeyRecord,jwtTokenUtils), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandlerService)
                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                )
                .exceptionHandling(ex -> {
                    log.error("[SecurityConfig:logoutSecurityFilterChain] Exception due to :{}",ex);
                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                })
                .build();
    }


    @Order(5)
    @Bean
    public SecurityFilterChain registerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/sign-up/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    /**
     * @Bean Para el metodo de encriptacion de las contraseñas
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKeyRecord.rsaPublicKey()).privateKey(rsaKeyRecord.rsaPrivateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }
}
```


### Sexta fase: Crear la public key y private key para JWT utilizando OpenSSL

Vamos a generar una Asymmetric Key con OpenSSL.
1. Primero creamos la carpeta certs en el paquete resources y navegamos en ella, dentro de una terminal.
```
cd src/main/resources/certs
```
2. Generamos el KeyPair: Esta linea genera el RSA private key con una longitud de 2048 bits usando OpenSSL.
donde especificamos el archivo de salida (-out keypair.pem) donde se guardara la clave privada. Esta llave privada puede utilziarse para encriptacion, desencriptacion y firma digital con criptografia asimetrica.
```
openssl genrsa -out keypair.pem 2048
```
3. Generamos una Public key a partir de la private key. Este comando extrae la public key de la anteriormente
generada private key. Este comando lee la private key del archivo especificado (-in keypair.pem) y hace una salida correspondiente
   (-pubout) a un archivo llamado publicKey.pem. La importancia de obtener la clave publica de la privada, es que podemos
compartirla libremente para encriptacion y propositos de verificacion mientes mantenemos la llave privada segura.
```
openssl rsa -in keypair.pem -pubout -out publicKey.pem
```
4. Formateamos la Private Key (keypair.pem) en un formato soportado (PKCS8): Esta linea convierte la private key generada
en un formato PKCS#8, es un estandar para la codificacion de una private key. Especificacom que el input es una key en formato
.pem (-inform PEM), y no tiene ninguna encriptacion aplicada (-nocrypt). La private key resultante es guardada en un archivo llamado
private.pem. La importancia de convertir la llave privada en un formato estandar es que es interoperable entre 
diferentes sistemas y aplicaciones de criptografia.
```
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out privateKey.pem
```

5. Agregamos las referencias a las llaves, del archivo application.properties o en este caso el application.yml
usado en RSAKeyRecord.java para cargar las llaves.
```java
@ConfigurationProperties(prefix = "jwt")
public record RSAKeyRecord(
        RSAPublicKey rsaPublicKey,
        RSAPrivateKey rsaPrivateKey
) {

}
```
6. Dentro de nuestra aplicacion principal del proyecto, agregaremos la notacion siguiente

```java
@EnableConfigurationProperties(RSAKeyRecord.class) // Para cargar las llaves

@SpringBootApplication
public class ProjectHackatonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectHackatonApplication.class, args);
	}


}
```
7. Dentro del application.yml agregaremos las rutas de las llaves
```yaml
jwt:
     rsa-private-key: classpath:certs/privateKey.pem
     rsa-public-key: classpath:certs/publicKey.pem
```

#### Septima fase: Crear los controladores de autentificacion

Creamos el AuthController, para recibir la sign-in api
```java
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(
            Authentication authentication,
            HttpServletResponse response){

        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication, response));
    }
   @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
   @PostMapping ("/refresh-token")
   public ResponseEntity<?> getAccessToken(
           @RequestHeader(HttpHeaders.AUTHORIZATION)
           String authorizationHeader){
      return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
   }

   @PostMapping("/sign-up")
   public ResponseEntity<?> registerUser(
           @Valid @RequestBody UserRegistrationDto userRegistrationDto,
           BindingResult bindingResult,
           HttpServletResponse httpServletResponse
   ){
      log.info("[AuthController:registerUser]Signup Process Started for user:{}",userRegistrationDto.username());
      if(bindingResult.hasErrors()){
         List<String> errorMessage = bindingResult.getAllErrors().stream()
                 .map(DefaultMessageSourceResolvable::getDefaultMessage)
                 .toList();
         log.error("[AuthController:registerUser]Errors in user:{}",errorMessage);
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
      }
      return  ResponseEntity.ok(authService.registerUser(userRegistrationDto,httpServletResponse));
   }
}
```

#### Octava fase: Crear los Services de cada entidad

- [x] UserService
La clase UserService tiene como logica de negocio el buscar usuarios dentro de la base de datos.
Con las siguientes operaciones:
  - findById(Long id)   
      Busca y devuelve la informacion del usuario por su id.
  - findByUsername(String username)
  - findByEmail(String email)
  - findAll()
- [x] TeamService
- [x] TeamMemberService
- [x] EventService
- [x] CompetitorService

