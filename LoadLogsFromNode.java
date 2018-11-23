package com.ccrt.atlanticus.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class LoadLogsFromNode {

	private static final String DIRECTORY_PATH = "/var/log/cassandra/temp";
	private static final String DESTINATION_PATH = "C:\\Users\\rnachireddy\\var\\log\\cassendra\\";
	private static final int HOST_PORT = 22;

	private static void fetchLogFiles(String hostName, String userName,
			String password) {
		
		String command = "zip –r /var/log/cassandra/temp/"+hostName+".zip /var/log/cassendra";
		JSch jsch = new JSch();
		Session session = null;
		Channel channel = null;
		ChannelSftp sftpChannel = null;
		ChannelExec channelExec = null;
		try {
			session = jsch.getSession(userName, hostName, HOST_PORT);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("PreferredAuthentications",
					"publickey,keyboard-interactive,password");
			session.setConfig(config);
			session.connect();
			channelExec = (ChannelExec) session.openChannel("exec");
			channelExec.setCommand(command);
			channelExec.connect();
			System.out.println("ZIP done");
			
			channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			//sftpChannel.cd(DIRECTORY_PATH);
			sftpChannel.get(DIRECTORY_PATH, DESTINATION_PATH);
			
		} catch (JSchException e) {
			System.err.println("GetFilesFromSFTP : JSchException, " + e.toString());
		} catch (SftpException e) {
			System.err.println("GetFilesFromSFTP : SftpException, " + e.toString());
		} finally{
			try {
                if(sftpChannel.isConnected()) {
                    session.disconnect();
					channelExec.disconnect();
                    channel.disconnect();
                    sftpChannel.quit();              
                }
            }
            catch(Exception e) {               
            	System.err.println( "GetFilesFromSFTP : Could not close SFTP Client after Downloading files from SFPT server, " + e.toString());
            }
		}
		System.out.println("Done !!");
	}
	
	public static void main(String args[]){
		fetchLogFiles("HostNameOrIP", "userName", "password");
	}

}
