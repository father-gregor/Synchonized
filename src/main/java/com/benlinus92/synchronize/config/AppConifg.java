package com.benlinus92.synchronize.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.benlinus92.synchronize.scheduler.ActiveUserScheduler;

@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan(basePackages = "com.benlinus92.synchronize")
@Import({SecurityConfiguration.class, AppWebSocketConfig.class})
public class AppConifg extends WebMvcConfigurerAdapter {
	@Bean(name="multipartResolver")
	public StandardServletMultipartResolver resolver() {
		return new StandardServletMultipartResolver();
	}
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setViewClass(JstlView.class);
		vr.setPrefix("/WEB-INF/jsp/");
		vr.setSuffix(".jsp");
		return vr;
	}
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	@Bean
	public MessageSource messageSource() {
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasename("messages");
	    return messageSource;
	}
	@Bean
	public ScheduledExecutorService taskScheduler() {
		//ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
		//scheduler.setPoolSize(10);
		//scheduler.
		//scheduler.setThreadNamePrefix("VideoTimerScheduler");
		return scheduler;
	}
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webstyle/**").addResourceLocations("/webstyle/");
        registry.addResourceHandler("/videos/**").addResourceLocations("file:" + AppConstants.VIDEOSTORE_LOCATION);
    }
}
