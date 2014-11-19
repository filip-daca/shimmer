package shimmer.web.controller;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;

/**
 * Podstawowy kontroler strony głównej
 * 
 * @author filip.daca@javatech.com.pl
 */
@Named
@Scope("view")
public class HomePageController {

	public String getHelloWorld() {
		return "Hello world from Shimmer!";
	}
	
}