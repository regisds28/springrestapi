package api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import api.rest.service.ImplementacaoUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//ativando a proteção contra usuario que não está validado por token
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		/*ativando a permissão para o acesso à pagina inicial do sistema*/
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("index").permitAll()
		
		/*redirecionamento de logout*/
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/*mapeia a URL de logout e invalida o usuario*/
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		//filtra requisições de login para autenticação
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
		
		//filtra demais requisições paraverificar a presença de token jwt no header http
		.addFilterBefore(new JWTAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {	
		/*serviço que irá consultar o usuario no banco de dados*/
		auth.userDetailsService(implementacaoUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
}
