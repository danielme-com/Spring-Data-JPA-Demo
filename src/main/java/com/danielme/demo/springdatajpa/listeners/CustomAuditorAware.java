package com.danielme.demo.springdatajpa.listeners;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.danielme.demo.springdatajpa.AuthenticationMockup;

@Component
public class CustomAuditorAware implements AuditorAware<String>
{

	@Override
	public String getCurrentAuditor()
	{
		return AuthenticationMockup.UserName;
	}

}
