package com.routedetector.Basic;

public class SecurityServerPermission extends java.security.BasicPermission {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	   * Creates a permission with a name.
	   */
	  public SecurityServerPermission(String name) {
	    super(name);
	  }

	  /**
	   * Creates a permission with a name and an action string.
	   * The action string is not used, but this constructor must exist
	   * so that the policy file parser works.
	   */
	  public SecurityServerPermission(String name, String actions) {
	    super(name, actions);
	  }
}
