package com.routedetector.Client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Holds list of gprs identification numbers for which gprs devices information should
 * be received.
 * 
 * @author Danijel Sudimac
 *
 */
public class ClientRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> imeiList=new ArrayList<String>();

	public List<String> getImeiList() {
		return imeiList;
	}

	public void setImeiList(List<String> imeiList) {
		this.imeiList = imeiList;
	}
	
}
