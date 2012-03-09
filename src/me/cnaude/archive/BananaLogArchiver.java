package me.cnaude.archive;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.bukkit.plugin.java.JavaPlugin;

public class BananaLogArchiver extends JavaPlugin {
	
	private File logFile;
	private double maxSize;
	private byte[] buf = new byte[1024];
	protected String logFileName = "server.log";
        private BananaConfig config;

	public String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy-hh-mm");

		Date date = new Date();

		return dateFormat.format(date);
	}
	@Override
	public void onDisable() {
		getServer().getScheduler().cancelAllTasks();
		System.out.println("BananaLogArchiver disabled.");
	}

	@Override
	public void onEnable() {
                this.loadConfig();
		File arcDir = new File("log-archive/");
		logFile = new File(logFileName);
		if(!arcDir.exists())
		{
		System.out.println(arcDir.getName()+" not found - creating folder.");
		arcDir.mkdir();
		}
		
                maxSize = this.getBConfig().maxSize();

		System.out.println("BananaLogArchive enabled - max log size: "+maxSize+" kb.");
		getServer().getScheduler().scheduleAsyncRepeatingTask(this,
				new Runnable() {
					public void run() {
						runTask();
					}
				}, 0, 1500);
		
	}
        
        public BananaConfig getBConfig() {
            return config;
        }
       
        void loadConfig() {
            getConfig().options().copyDefaults(true);
            saveConfig();
            config = new  BananaConfig(this);
        }      
        
        //the handy little task to archive big logs
	protected void runTask() {
		
		if(logFile.exists())
		{
		
		long fileSize = logFile.length()/1024;
		
			if(fileSize>maxSize)
			{
			System.out.println("---------------------------------");
			System.out.println("[BananaLogArchiver] by codename_B");
			System.out.println("---------------------------------");
			try
			{
			OutputStream thisOut = new FileOutputStream(
					new File("log-archive/"+getDateTime()+".zip"));
		
			BufferedOutputStream bufOut = new BufferedOutputStream(thisOut);
			ZipOutputStream zippy = new ZipOutputStream(bufOut);
			ZipEntry zipInput = new ZipEntry(logFileName);
			zippy.putNextEntry(zipInput);
			FileInputStream in = new FileInputStream(new File(logFileName));
			//System.out.println("outFilename="+outFilename);
			// Transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				zippy.write(buf, 0, len);
			}

			// Complete the entry
			zippy.closeEntry();
			zippy.close();
			in.close();
			File logFile = new File(logFileName);
			FileWriter outFile = new FileWriter(logFile);
			PrintWriter out = new PrintWriter(outFile);
			out.println("");
			} catch
			(Error e)
				{
				System.err.println("[BananaLogArchive] Error archiving file!");
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
			}
		}
	}

