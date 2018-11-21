package com.ccrt.atlanticus.sftp;

import java.io.InputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class PullCassendraLogs {

	private static String COMMAND = "ls /var/log/cassendra";
	private static final int HOST_PORT = 22;
	private static void executeLinuxCommand(String hostName, String userName,
			String password) {
		
		JSch jsch = new JSch();
		Session session = null;
		
		try {
			session = jsch.getSession(userName, hostName, HOST_PORT);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("PreferredAuthentications",
					"publickey,keyboard-interactive,password");
			session.setConfig(config);
			session.connect();
			ChannelExec channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(COMMAND);
			channel.setErrStream(System.err);
			
			InputStream in = channel.getInputStream();
			channel.connect();
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0) {
						break;
					}
					System.out.print(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					System.out.println("Exit Status: "
							+ channel.getExitStatus());
					break;
				}
				Thread.sleep(1000);
			}
			channel.disconnect();
			session.disconnect();
			System.out.println("DONE!!!");
		} catch (Exception e) {
			System.err.println("ExecuteCommand : error while executing linux commnad ::"+e.toString());
		}
	}

	public static void main(String[] args) {
		executeLinuxCommand("HostName", "userName", "password");
	}
}