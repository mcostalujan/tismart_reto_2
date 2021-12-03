package maxcosta.reto2;

import static maxcosta.reto2.utility.Utility.TIME_ZONE;

import java.util.Arrays;
import java.util.Collections;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
@SpringBootApplication
public class Reto2Application {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(TIME_ZONE));
	}
	public static void main(String[] args) {
		SpringApplication.run(Reto2Application.class, args);
	}

	public static final String HEADER = "Access-Control-Allow-Origin";

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(false);
		corsConfiguration.setAllowedOrigins(Collections.singletonList("https://maxcostareto2.web.app"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", HEADER, "Content-Type", "Accept", "Jwt-Token",
				"Authorization", "Origin, Accept", "X-Requested-With", "Access-Control-Request-Method",
				"Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token",
				"Authorization", HEADER, HEADER, "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
