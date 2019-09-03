package com.gfso.util;

import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

public class Utils {
	public static String getBaseURL(HttpServletRequest request) {
		try {
			final DatagramSocket socket = new DatagramSocket();
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			String ip = socket.getLocalAddress().getHostAddress();
			socket.close();
			return request.getScheme()+"://"+ip+":"+request.getServerPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
