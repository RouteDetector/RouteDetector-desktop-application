package com.routedetector.Client;

import com.routedetector.Client.LoginInfo;

import javafx.collections.ObservableList;

public interface ConnectionStateHolder {
	public void setConnected(boolean isConnected);
	public boolean getConnected();
	public LoginInfo getLoginInfo();
	public void setLoginInfo(LoginInfo loginInfo);
	public int getSocketPort();
	public String getHost();
	public ObservableList<String> getObservedGprsDevicesImeiList();
	public void setObservedGprsDevicesImeiList(ObservableList<String> listOfItems);
}
