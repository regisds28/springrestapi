package api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import api.rest.ApplicationContextLoad;
import api.rest.model.Usuario;
import api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {

	/* tempo de validação de token: 2 dias */
	private static final long EXPIRATION_TIME = 172800000;

	/* Senha única para compor a autenticação e ajudar na segurança */
	private static final String SECRET = "senhasecreta";

	/* prefixo padrão de token */
	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	/* gera token de autenticação e adicina o cabeçalho a resposta HTTP */
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {

		String JWT = Jwts.builder()/* chama o gerador de token */
				.setSubject(username)/* adiciona o usuário */
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* tempo de expiração do token */
				.signWith(SignatureAlgorithm.HS512, SECRET)/* compacta o algoritmo de geração de token */
				.compact();

		/* junta o token com o prefixo */
		String token = TOKEN_PREFIX + " " + JWT; /*
													 * imprime o Bearer:
													 * alkjsjdfçlaksjdfçlkajsdf.askjdflaksjdflajsdf.aksjdfçlaksjdflkas
													 */

		/* adiciona no cabeçalho do HTTP */
		response.addHeader(HEADER_STRING, token);

		// escreve o token como resposta no corpo HTTP
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

	}

	// retorna o usuario validado com token ou caso não seja validado retorna null
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

		// pega o token validado no cabeçalho http
		String token = request.getHeader(HEADER_STRING);

		if (token != null) {

			// valida o token na requisição
			String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
					.getSubject();

			if (user != null) {
				Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
						.findByLogin(user);

				if (usuario != null) {
					return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(),
							usuario.getAuthorities());
				}
			}

		}
		liberarCORS(response);
		return null;
	}

	// CORS policy
	private void liberarCORS(HttpServletResponse response) {
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}

		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}

		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
	}

}