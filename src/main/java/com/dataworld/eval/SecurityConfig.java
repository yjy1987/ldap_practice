package com.dataworld.eval;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.userdetails.PersonContextMapper;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//	public SecurityConfig() {
//		super(true);
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/**").authenticated() // 모든 URL에 대해 인증을 요구하는 엑세스 권한 설정
				.and().logout().permitAll() // 로그아웃기능 유효화
				.and().formLogin().permitAll(); // 폼 로그인 기능을 활성화
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// LDAP 인증을 유효화
		auth.ldapAuthentication()
				// 사용자 이름 (DN = 고유이름)
				// {0}에 로그인하여 입력 된 사용자 이름이 채워짐
        		.userDnPatterns("uid={0},ou=people")
				// 그룹을 검색하는 단위를 지정
				.groupSearchBase("ou=groups")
				// LDAP의 데이터 소스 지정
				.contextSource()
				// 연결 URL지정
				.url("ldap://125.141.139.161:10389/dc=junyeong,dc=net")
				// LDAP에 연결하기 위한 사용자 이름 지정
                .managerDn("cn=admin,dc=junyeong,dc=net") 
				// 패스워드지정
				.managerPassword("admin1122").and()
				// UserDetails 객체를 생성하는 것은
				// 엔트리가 성명 (cn = Common Name)이 참조 할 수있는 PersonContextMapper를 사용 → 요구 사항에 맞는 구현을
				// 선택한다. 또는 실어주는 것
		.userDetailsContextMapper(new PersonContextMapper());

}

}
