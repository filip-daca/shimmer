package shimmer.web.controller;

import java.io.Serializable;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;

/**
 * Podstawowy kontroler strony głównej
 * 
 * @author filip.daca@javatech.com.pl
 */
@Named
@Scope("view")
public class HomePageController implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public String getHelloWorld() {
		return "Hello world from Shimmer!";
	}
	
}