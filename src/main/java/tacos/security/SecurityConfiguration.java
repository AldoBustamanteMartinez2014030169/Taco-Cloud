package tacos.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.server.UnboundIdContainer;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> authorize.anyRequest().fullyAuthenticated())
				.formLogin(Customizer.withDefaults());

		return http.build();
	}

    @Bean
	public LdapTemplate ldapTemplate() {
		return new LdapTemplate(contextSource());
	}

    @Bean
	public LdapContextSource contextSource() {
		LdapContextSource ldapContextSource = new LdapContextSource();
		// ldapContextSource.setUrl("ldap://localhost:10389");
		ldapContextSource.setUrl("ldap://192.168.100.8:10389");
		ldapContextSource.setUserDn("uid=admin,ou=system");
		ldapContextSource.setPassword("secret");
		return ldapContextSource;
	}

    @Bean
	AuthenticationManager authManager(BaseLdapPathContextSource source) {
		LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(source);
		factory.setUserDnPatterns("cn={0},ou=users,ou=system");
		return factory.createAuthenticationManager();
	}




    // CONFIGURACION PARA JDBC (JdbcUserDetailsManager) ------------------------------------------

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     // Usar un DelegatingPasswordEncoder que no realiza codificación
    //     return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    // }

	// @Bean
    // DataSource dataSource() { // Conexion con la BD
    //     return new EmbeddedDatabaseBuilder()
    //         .setType(EmbeddedDatabaseType.H2)
    //         .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
    //         .build();
    // }

    // @Bean
    // UserDetailsManager users(DataSource dataSource) {

    //     PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    //     UserDetails user = User.builder()
    //         .username("user")
    //         .password(passwordEncoder.encode("hola"))
    //         .roles("USER")
    //         .build();
    //     UserDetails admin = User.builder()
    //         .username("admin")
    //         .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
    //         .roles("USER", "ADMIN")
    //         .build();
    //     JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
    //     users.createUser(user);
    //     users.createUser(admin);
    //     return users;
    // }
    // -----------------------------------------------------------------------------------------------
}
