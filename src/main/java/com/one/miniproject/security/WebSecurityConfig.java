package com.one.miniproject.security;

import com.one.miniproject.filter.RestLoginFailureHandler;
import com.one.miniproject.filter.RestLoginSuccessHandler;
import com.one.miniproject.filter.RestUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 회원 관리 처리 API (POST /user/**) 에 대해 CSRF 무시
        http.csrf().disable();
//                .ignoringAntMatchers("/user/**");
        http
                .cors()
                .configurationSource(corsConfigurationSource());

        http.authorizeRequests()
                // image 폴더를 login 없이 허용
                .antMatchers("/images/**").permitAll()
                // css 폴더를 login 없이 허용
                .antMatchers("/css/**").permitAll()
                // 회원 관리 처리 API 전부를 login 없이 허용
                .antMatchers("/user/**").permitAll()
                // 그 외 어떤 요청이든 '인증'
//                .anyRequest().authenticated();
                .anyRequest().permitAll();
//                .and()

            http.formLogin().disable();
            http.addFilterAt(getAuthenticationFilter(), RestUsernamePasswordAuthenticationFilter.class);

//                // [로그인 기능]
//                .formLogin()
//                // 로그인 View 제공 (GET /user/login)
//                .loginPage("/user/login")
//                // 로그인 처리 (POST /user/login)
//                .loginProcessingUrl("/user/login")
//                // 로그인 처리 후 성공 시 URL
//                .defaultSuccessUrl("/")
//                // 로그인 처리 후 실패 시 URL
//                .failureUrl("/user/login?error")
//                .permitAll()
//                .and()


//                // [로그아웃 기능]
//                .logout()
//                // 로그아웃 처리 URL
//                .logoutUrl("/user/logout")
//                .permitAll();
    }

    protected RestUsernamePasswordAuthenticationFilter getAuthenticationFilter(){
        RestUsernamePasswordAuthenticationFilter authFilter = new RestUsernamePasswordAuthenticationFilter();
        try{
            authFilter.setFilterProcessesUrl("/user/login"); // 로그인에 대한 POST 요청을 받을 url을 정의합니다. 해당 코드가 없으면 정상적으로 작동하지 않습니다.
            authFilter.setUsernameParameter("username");
            authFilter.setPasswordParameter("password");
            authFilter.setAuthenticationManager(this.authenticationManagerBean());
            authFilter.setAuthenticationSuccessHandler(successHandler());
            authFilter.setAuthenticationFailureHandler(failureHandler());

        } catch (Exception e){
            e.printStackTrace();
        }
        return authFilter;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler(){
        return new RestLoginSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return new RestLoginFailureHandler();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("http://localhost:3000"); // 배포 주소
//        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Authorization");
        configuration.addAllowedOriginPattern("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
