package com.pearson.openideas.cq5.components.exceptions;

public class DuplicateUserException extends Exception {
	
	private static final long serialVersionUID = 1L;   
    
    public DuplicateUserException(final String message) 
    {
    	super(message);
    }
    
}
