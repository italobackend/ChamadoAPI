# Spring Security do Zero — Padrão JWT (o que empresas usam de verdade)

**Tema da aula:** vamos construir o sistema de autenticação da **Vila da Folha** 🍃.
Cada usuário é um **Ninja**, e cada ninja tem uma **Patente** (Role): `GENIN` (usuário comum) ou `HOKAGE` (admin).

O padrão que vamos montar é: **Spring Security + JWT (stateless)**. É o padrão que 90% das empresas usam em APIs REST hoje (sem sessão, sem cookie, token no header `Authorization: Bearer ...`).

A ordem abaixo é a ordem real que você digitaria as classes num projeto novo. Siga na sequência, sem pular.

---

## Passo 0 — Dependências (pom.xml)

Antes de criar qualquer classe, adicione isso no `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.6</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.12.6</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.12.6</version>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

Isso te dá: web, segurança, banco em memória (H2, só pra testar) e a lib de JWT.

---

## Passo 1 — O Enum de Patente (`Role`)

Toda role no Spring Security precisa existir em algum lugar. Vamos criar o enum primeiro porque a entidade `Ninja` vai depender dele.

```java
package com.vilafolha.model;

public enum Role {
    GENIN,
    HOKAGE
}
```

> Convenção do Spring: internamente ele trabalha com `ROLE_GENIN`, `ROLE_HOKAGE` (prefixo `ROLE_`), mas isso é adicionado automaticamente depois — aqui no enum fica limpo.

---

## Passo 2 — A entidade `Ninja` implementando `UserDetails`

Essa é a peça central. O Spring Security não sabe o que é um "usuário" — ele só entende a interface `UserDetails`. Então nossa entidade **implementa** essa interface.

```java
package com.vilafolha.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "ninjas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ninja implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // ex: "naruto"

    @Column(nullable = false)
    private String password; // sempre criptografada, nunca em texto puro

    @Enumerated(EnumType.STRING)
    private Role role; // GENIN ou HOKAGE

    // ---- Métodos exigidos pela interface UserDetails ----

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
```

**Por que isso importa:** o Spring Security, em qualquer parte do sistema, vai lidar com objetos `UserDetails`. Ao fazer o `Ninja` implementar essa interface, ele passa a "falar a língua" do Security.

---

## Passo 3 — O Repositório (`NinjaRepository`)

```java
package com.vilafolha.repository;

import com.vilafolha.model.Ninja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NinjaRepository extends JpaRepository<Ninja, Long> {
    Optional<Ninja> findByUsername(String username);
}
```

Simples: precisamos buscar o ninja pelo `username` na hora do login.

---

## Passo 4 — O `UserDetailsService`

Essa é a ponte entre o Spring Security e o seu banco. O Security, quando precisa autenticar alguém, chama esse serviço perguntando "me dá o usuário com esse username".

```java
package com.vilafolha.service;

import com.vilafolha.repository.NinjaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NinjaDetailsService implements UserDetailsService {

    private final NinjaRepository ninjaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return ninjaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Ninja não encontrado: " + username));
    }
}
```

---

## Passo 5 — O gerador/validador de JWT (`JwtService`)

Antes da configuração de segurança, precisamos da classe que cria e valida o "pergaminho" (token) do ninja.

```java
package com.vilafolha.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Em produção: isso vem de variável de ambiente, nunca fixo no código.
    private final SecretKey key = Keys.hmacShaKeyFor(
        "chave-secreta-da-vila-da-folha-com-32-caracteres!".getBytes()
    );

    private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1 hora

    public String gerarToken(UserDetails ninja) {
        return Jwts.builder()
                .subject(ninja.getUsername())
                .claim("authorities", ninja.getAuthorities().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    public String extrairUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean tokenValido(String token, UserDetails ninja) {
        String username = extrairUsername(token);
        return username.equals(ninja.getUsername()) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
    }
}
```

---

## Passo 6 — O Filtro de Autenticação JWT (`JwtAuthFilter`)

Esse filtro roda **em toda requisição**, antes de chegar no controller. Ele olha o header `Authorization`, extrai o token, valida, e diz ao Spring "esse cara está autenticado".

```java
package com.vilafolha.security;

import com.vilafolha.service.NinjaDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final NinjaDetailsService ninjaDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // sem token, segue o baile
            return;
        }

        String token = authHeader.substring(7); // remove "Bearer "
        String username = jwtService.extrairUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails ninja = ninjaDetailsService.loadUserByUsername(username);

            if (jwtService.tokenValido(token, ninja)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(ninja, null, ninja.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

**Ideia central:** se o token é válido, o filtro "carimba" o `SecurityContextHolder` dizendo quem está logado. Tudo depois disso (controllers, `@PreAuthorize`) já enxerga o ninja autenticado.

---

## Passo 7 — A Configuração de Segurança (`SecurityConfig`)

Agora juntamos tudo. Essa classe define: quais rotas são públicas, quais exigem login, qual patente pode acessar o quê, e conecta o filtro JWT.

```java
package com.vilafolha.config;

import com.vilafolha.security.JwtAuthFilter;
import com.vilafolha.service.NinjaDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // habilita @PreAuthorize nos controllers
@RequiredArgsConstructor
public class SecurityConfig {

    private final NinjaDetailsService ninjaDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(ninjaDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // não precisamos de CSRF, somos stateless (JWT)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // sem sessão no servidor
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()       // login e registro são públicos
                .requestMatchers("/hokage/**").hasRole("HOKAGE") // só o Hokage acessa
                .anyRequest().authenticated()                  // o resto exige login
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

**Leia essa classe assim, de cima pra baixo:**
1. `passwordEncoder()` — como criptografar senha (BCrypt, padrão de mercado).
2. `authenticationProvider()` — "quando alguém tentar logar, use esse serviço e esse encoder pra checar".
3. `authenticationManager()` — o objeto que o `AuthController` vai usar pra validar login/senha.
4. `filterChain()` — as regras de trânsito: quem entra onde, e onde nosso filtro JWT entra na fila.

---

## Passo 8 — DTOs de entrada/saída

```java
package com.vilafolha.dto;

public record AuthRequest(String username, String password) {}
```

```java
package com.vilafolha.dto;

public record AuthResponse(String token) {}
```

---

## Passo 9 — O `AuthController` (registro e login)

```java
package com.vilafolha.controller;

import com.vilafolha.dto.AuthRequest;
import com.vilafolha.dto.AuthResponse;
import com.vilafolha.model.Ninja;
import com.vilafolha.model.Role;
import com.vilafolha.repository.NinjaRepository;
import com.vilafolha.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final NinjaRepository ninjaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/registrar")
    public AuthResponse registrar(@RequestBody AuthRequest request) {
        Ninja ninja = Ninja.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password())) // nunca salvar senha crua!
                .role(Role.GENIN) // todo mundo nasce Genin
                .build();

        ninjaRepository.save(ninja);

        String token = jwtService.gerarToken(ninja);
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        ); // se a senha estiver errada, isso já lança exceção sozinho

        UserDetails ninja = ninjaRepository.findByUsername(request.username()).orElseThrow();
        String token = jwtService.gerarToken(ninja);
        return new AuthResponse(token);
    }
}
```

---

## Passo 10 — Endpoints protegidos, testando a patente

```java
package com.vilafolha.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.vilafolha.model.Ninja;

@RestController
public class MissaoController {

    @GetMapping("/missoes")
    public String missoesDisponiveis(@AuthenticationPrincipal Ninja ninja) {
        return "Bem-vindo, " + ninja.getUsername() + "! Suas missões: rank D, C, B.";
    }

    @GetMapping("/hokage/decisoes")
    @PreAuthorize("hasRole('HOKAGE')")
    public String decisoesDoHokage() {
        return "Acesso liberado: sala de decisões do Hokage.";
    }
}
```

- `/missoes` → qualquer ninja **autenticado** acessa (Genin ou Hokage).
- `/hokage/decisoes` → só quem tem `ROLE_HOKAGE` passa. Um Genin recebe **403 Forbidden**.

---

## Passo 11 — Testando o fluxo completo

```bash
# 1. Registrar o Naruto (vira GENIN automaticamente)
curl -X POST http://localhost:8080/auth/registrar \
  -H "Content-Type: application/json" \
  -d '{"username":"naruto","password":"rasengan123"}'

# resposta: {"token": "eyJhbGciOi..."}

# 2. Usar o token pra acessar rota protegida
curl http://localhost:8080/missoes \
  -H "Authorization: Bearer eyJhbGciOi..."

# 3. Tentar acessar rota de Hokage (vai dar 403, Naruto ainda é Genin)
curl http://localhost:8080/hokage/decisoes \
  -H "Authorization: Bearer eyJhbGciOi..."
```

Se você quiser testar o caminho do Hokage, é só mudar `Role.GENIN` pra `Role.HOKAGE` direto no banco (ou criar um segundo endpoint de promoção, exclusivo pra quem já é Hokage).

---

## Resumo mental — a ordem que fica na cabeça

1. **Role** (enum) → define as patentes que existem.
2. **Ninja** (`UserDetails`) → a entidade que representa o usuário pro Spring Security.
3. **NinjaRepository** → busca o ninja no banco.
4. **NinjaDetailsService** (`UserDetailsService`) → a ponte oficial entre Security e banco.
5. **JwtService** → gera e valida o token.
6. **JwtAuthFilter** → intercepta cada requisição e autentica via token.
7. **SecurityConfig** → junta tudo: regras de rota, encoder de senha, authentication manager, e registra o filtro.
8. **DTOs** → formato de entrada/saída da API.
9. **AuthController** → `/auth/registrar` e `/auth/login`, onde o token nasce.
10. **Controllers protegidos** → usam `@PreAuthorize` ou as regras do `SecurityConfig` pra travar por patente.

Esse é exatamente o esqueleto usado em produção: **stateless, JWT, BCrypt, roles por método ou por rota**. A partir daqui, as variações comuns em empresas reais são: refresh token, roles vindas de uma tabela separada (many-to-many) em vez de enum, e um `AuthenticationEntryPoint` customizado pra formatar erros 401/403.
