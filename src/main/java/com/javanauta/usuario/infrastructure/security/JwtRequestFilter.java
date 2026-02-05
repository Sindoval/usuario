package com.javanauta.usuario.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

// Define a classe JwtRequestFilter, que estende   OncePerRequestFilter
public class JwtRequestFilter extends OncePerRequestFilter {

    // Define propriedades para armazenar instâncias de JwtUtil e UserDetailsService
    private final  JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Construtor que inicializa as propriedades com instâncias fornecidas
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Método chamado uma vez por requisição para processar o filtro
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            final String authorizationHeader = request.getHeader("Authorization");

            // Verifica se o cabeçalho existe e começa com "Bearer "
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // Extrai o token JWT do cabeçalho
                final String token = authorizationHeader.substring(7);
                // Extrai o nome de usuário do token JWT
                final String username = jwtUtil.extractUsername(token);

                // Se o nome de usuário não for nulo e o usuário não estiver autenticado ainda
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Carrega os detalhes do usuário a partir do nome de usuário
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    // Valida o token JWT
                    if (jwtUtil.validateToken(token, username)) {
                        // Cria um objeto de autenticação com as informações do usuário
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                        // Define a autenticação no contexto de segurança
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

            // Continua a cadeia de filtros, permitindo que a requisição prossiga
            chain.doFilter(request, response);

            // Obtém o valor do header "Authorization" da requisição

        } catch (io.jsonwebtoken.JwtException e) {
            // JwtException captura ExpiredJwtException, SignatureException, MalformedJwtException, etc.
            handleJwtError(response, request, e);
        } catch (Exception e) {
            // Captura qualquer outro erro inesperado para não retornar 404 vazio
            handleJwtError(response, request, e);
        }
    }
    private void handleJwtError(HttpServletResponse response, HttpServletRequest request, Exception e) throws IOException {
        if (!response.isCommitted()) {
            response.reset();
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String message = "Token inválido ou expirado";
        if (e instanceof io.jsonwebtoken.ExpiredJwtException) {
            message = "Token expirado";
        }

        String jsonResponse = buildMessageError(
            HttpStatus.UNAUTHORIZED.value(),
            message,
            request.getRequestURI(),
            e.getMessage() != null ? e.getMessage() : "Erro na validação do token"
        );

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
        response.getWriter().close();
    }

    private String buildMessageError(int status, String message, String path, String error) {
        return "{" +
            "\"status\": " + status + "," +
            "\"message\": \"" + message + "\"," +
            "\"path\": \"" + path + "\"," +
            "\"error\": \"" + error + "\"," +
            "\"timestamp\": \"" + java.time.LocalDateTime.now() + "\"" +
            "}";
    }
}
