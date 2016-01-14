package com.routedetector.Basic;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;
/**
 * 
 * @author Danijel Sudimac
 * 
 * This interface is in charge for passing client credentials to login mechanism on server side.
 *
 */
public interface SecurityLoginInterface extends Remote{
	/**
	 * Passing client credentials to server.
	 * 
	 * @param username clients user-name defined on server by superuser. Users can be defined
	 * by creating an account on www.routedetector.com
	 * @param password is also defined on server
	 * @param company is company's e-mail obtained on account creation
	 * @return object which is client's end point of RMI communication.
	 * @throws RemoteException
	 * @throws LoginException
	 */
	public SecurityServerInterface login(String username, String password, String company) throws RemoteException,LoginException;
}
