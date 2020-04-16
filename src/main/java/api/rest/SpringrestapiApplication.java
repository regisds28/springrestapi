package api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication   //dá o start
@EntityScan(basePackages = {"api.rest.model"})   //varre as pastas do model para criar as tabelas
@ComponentScan(basePackages = {"api.*"})   //configura o projeto
@EnableJpaRepositories (basePackages = {"api.rest.repository"})   //mostra onde estarão as interfaces de persistência
@EnableTransactionManagement    //ativa o controller de transação
@EnableWebMvc
@RestController    //ativa o REST e retorna o json
@EnableAutoConfiguration
public class SpringrestapiApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(SpringrestapiApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("123"));
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/usuario/**")
		.allowedMethods("*")//todos os métodos HTTP
		.allowedOrigins("*");
	}

}
