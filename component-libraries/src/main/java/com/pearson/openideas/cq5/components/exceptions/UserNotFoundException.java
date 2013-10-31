package com.pearson.openideas.cq5.components.exceptions;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

    public UserNotFoundException(final String message) 
    {
    	super(message);
    }
    
}
