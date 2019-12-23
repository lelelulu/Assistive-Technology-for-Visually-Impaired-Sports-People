package com.example.assistivetechfrontend;

import java.net.MalformedURLException;

public interface Communicator {
    public Boolean updateLocation(int sessionId, int personalId, double latitude, double longitude, String url_in) throws MalformedURLException;
    public Boolean leaveSession(int sessionId, int personalId, String url_in)throws MalformedURLException;
    public int createUser(String name, String encode, String url_in)throws MalformedURLException;
    public int[] createSession(String name, double latitude, double longitude, String url_in) throws MalformedURLException;
    public double[] getLocation(int personalId, double latitude, double longitude, String url_in) throws MalformedURLException;
    public int joinSession(String name, double latitude, double longitude, int sessionId, String url_in) throws MalformedURLException;
}
