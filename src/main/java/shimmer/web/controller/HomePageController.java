package shimmer.web.controller;

import java.io.Serializable;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;

/**
 * Basic controller for home page
 * 
 * @author Filip Daca
 */
@Named
@Scope("view")
public class HomePageController implements Serializable {
	
	// ************************************************************************
	// STATIC FIELDS
	
	private static final long serialVersionUID = 1L;
	
	// ************************************************************************
	// VIEW METHODS
	
	public String getHelloWorld() {
		return "Hello world from Shimmer!";
	}
	
}