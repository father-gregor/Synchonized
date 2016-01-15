package com.benlinus92.synchronize.config;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SynchronizeInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { AppConifg.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}
	
	/*@Override
	protected Filter[] getServletFilters() {
		return new Filter[] {new CharsetFilter()};
	}*/


	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}
	
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		return new Filter[] {encodingFilter};
	}

	@Override
	public void onStartup(ServletContext servletContex) throws ServletException {
		super.onStartup(servletContex);
		FilterRegistration.Dynamic encodingFilter = servletContex.addFilter("encoding-filter", new CharacterEncodingFilter());
		servletContex.addListener(new SessionListener());
	      encodingFilter.setInitParameter("encoding", "UTF-8");
	      encodingFilter.setInitParameter("forceEncoding", "true");
	      encodingFilter.addMappingForUrlPatterns(null, true, "/*");
	}
}
