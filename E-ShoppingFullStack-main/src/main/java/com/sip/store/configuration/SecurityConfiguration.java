package com.sip.store.configuration;

import com.sip.store.entities.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.sql.DataSource;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    //@Autowired
   // private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private DataSource dataSource;
    @Value("${spring.queries.users-query}")
    private String usersQuery;
    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)   // méthode responsable de l'authentification
            throws Exception {
        auth.
                jdbcAuthentication() // création d'une authentification JDBC
                .usersByUsernameQuery(usersQuery) // recherche du user par son email lors du login
                .authoritiesByUsernameQuery(rolesQuery)  // récuperation des roles du user qui se connecte
                .dataSource(dataSource) // notre datasource
                .passwordEncoder(bCryptPasswordEncoder); // en utilisant l'algorithme de cryptage bCrptPasswordEncoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // méthode responsable de l'autorisation


        http.
                authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/profile").permitAll()
                .antMatchers("/api/getAll").permitAll()
                .antMatchers("/api/profile/{id}").permitAll()
                .antMatchers("/api/imgprofile/{id}").permitAll()
                .antMatchers("/login").permitAll() // accès pour tous users
                .antMatchers("/registration").permitAll() // accès pour tous users
                .antMatchers("/role/**").hasAnyAuthority("SUPERADMIN")
                .antMatchers("/accounts/**").hasAnyAuthority("SUPERADMIN")
                .antMatchers("/providers/**").hasAnyAuthority("ADMIN","SUPERADMIN")  // Authority = role
                .antMatchers("/article/add").hasAnyAuthority("ADMIN","SUPERADMIN")
                .antMatchers("/article/edit/**").hasAnyAuthority("ADMIN","SUPERADMIN")
                .antMatchers("/article/delete/**").hasAnyAuthority("ADMIN","SUPERADMIN")
                .antMatchers("/article/show/**").hasAnyAuthority("ADMIN","SUPERADMIN","AGENT")
                .antMatchers("/article/list").hasAnyAuthority("ADMIN","SUPERADMIN","AGENT").anyRequest()
                .authenticated().and().csrf().disable().formLogin() // l'accès de fait via un formulaire

                .loginPage("/login").failureUrl("/login?error=true") // fixer la page login

                .defaultSuccessUrl("/dashboard") // page d'accueil après login avec succès
                .usernameParameter("email") // paramètres d'authentifications login et password
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // route de deconnexion ici /logut
                .logoutSuccessUrl("/login").and().exceptionHandling() // une fois deconnecté redirection vers login

                .accessDeniedPage("/403");

       // http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }



    // laisser l'accès aux ressources
    @Override
    public void configure(WebSecurity web) throws Exception {   // méthode responsable de l'acces aux ressources
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/icons/**", "/plugins/**");
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws
            Exception {

// configure AuthenticationManager so that it knows from where to load
// user for matching credentials
// Use BCryptPasswordEncoder

        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
