package com.ccrt.atlanticus.sftp;

import java.io.File;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class LoadLogsFromNode {

	private static final String DIRECTORY_PATH = "/var/log/cassendra";
	private static final String DESTINATION_PATH = "C:\\Users\\rnachireddy\\var\\log\\cassendra\\";
	
	private static final int HOST_PORT = 22;

	private static void fetchLogFiles(String hostName, String userName,
			String password, String nodeName) {

		JSch jsch = new JSch();
		Session session = null;
		Channel channel = null;
		ChannelSftp sftpChannel = null;
		try {
			String destinationPath = DESTINATION_PATH+nodeName+"\\";
			File directory = new File(destinationPath);
		    if (! directory.exists()){
		        directory.mkdir();
		    } 
			
			session = jsch.getSession(userName, hostName, HOST_PORT);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("PreferredAuthentications",
					"publickey,keyboard-interactive,password");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			sftpChannel.cd(DIRECTORY_PATH);
			@SuppressWarnings("unchecked")
			Vector<LsEntry> filesToDownload = sftpChannel.ls("*");
			if (filesToDownload.size() > 0) {
				for (int fileIndex = 0; fileIndex < filesToDownload.size(); fileIndex++) {
					String srcFileName = filesToDownload.get(fileIndex)
							.getFilename();
					sftpChannel.get(srcFileName, destinationPath);
				}
			}
		} catch (JSchException e) {
			System.err.println("GetFilesFromSFTP : JSchException, " + e.toString());
		} catch (SftpException e) {
			System.err.println("GetFilesFromSFTP : SftpException, " + e.toString());
		} finally{
			try {
                if(sftpChannel.isConnected()) {
                    session.disconnect();
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
		fetchLogFiles("nodeName", "userName", "password", "nodeName");
	}

}
