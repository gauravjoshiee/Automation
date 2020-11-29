import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class FTPDirectoryTraverse {

	static Session jschSession;
	public static void main(String[] args) {
		try{
		ChannelSftp ch = setupJsch();
	    ch.connect();
	    String workingDir = "//QADataExtract//Requests";
	    ch.cd(workingDir);
	    
	    @SuppressWarnings("unchecked")
		Vector<String> files = ch.ls("*");
	    List<String> ret=new ArrayList<>();
	    List<String> folders=new ArrayList<>();
	    List<String> fileCount=new ArrayList<>();
	    int totalFileCount = 0;
	    for (int i = 0; i < files.size(); i++)
	    {
	        Object obj = files.elementAt(i);
	        if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry)
	        {
	            LsEntry entry = (LsEntry) obj;
//	            if (true && !entry.getAttrs().isDir())
//	            {
//	                ret.add(entry.getFilename()+"file");
//	            }
	            if (true && entry.getAttrs().isDir())
	            {
	                if (!entry.getFilename().equals(".") && !entry.getFilename().equals(".."))
	                {
	                    folders.add(entry.getFilename());
	                }
	            }
	        }
	    }
	    
	    System.out.println("Total folders at location "+workingDir+" - "+folders.size());
	    for (int i=0;i<=folders.size();i++){
	    	String folderName = folders.get(i);
	    	ch.cd(workingDir+"//"+folderName);
	    	Vector<String> files2 = ch.ls("*");
	    	for (int j = 0; j < files2.size(); j++)
		    {
		        Object obj = files2.elementAt(j);
		        if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry)
		        {
		            LsEntry entry = (LsEntry) obj;
		            if (true && !entry.getAttrs().isDir())
		            {
		            	fileCount.add(entry.getFilename());
		            	totalFileCount++;
		            }
		            
		        }
		    }
	    	System.out.println(folderName+","+fileCount.size());
	    	fileCount.clear();
	    }
	    System.out.println("Total file count at location "+workingDir+" - "+totalFileCount);
	    System.out.println("completed processing");
	    ch.disconnect();
		}
		catch (Exception e){
			System.out.println(e.getStackTrace());
		}
	}
	
	private static ChannelSftp setupJsch() throws JSchException {
	    JSch jsch = new JSch();
	    jsch.setKnownHosts("/Users/john/.ssh/known_hosts");
	    String username="sftp_tak_qa";
		String remoteHost="172.38.5.120";
		jschSession = jsch.getSession(username, remoteHost);
	    String password="Polaris@945823!";
//	    jschSession.setPort(8999);
	    java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        jschSession.setConfig(config);
		jschSession.setPassword(password);
	    jschSession.connect();
	    
	    return (ChannelSftp) jschSession.openChannel("sftp");
	}

}
