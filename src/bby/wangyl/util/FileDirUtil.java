package bby.wangyl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class FileDirUtil {

	public static String[] srcArr = {"src"};
	public static String classPreffix = "WebRoot\\\\WEB-INF\\\\classes";
	public static String srcPath = "";
	public static String webRootPath = "";
	public static int srcCount = 0;
	public static int webRootCount = 0;
	
	static String remoteHost = "192.168.1.98";
	static String remoteUserName = "root";
	static String remotePasswd = "bbyroot";
	static int remotePort = 22;
	static String remoteProjectRootPath = "";
	public static List<File> getLastModify(String path,final long date) {
		File filePath = new File(path);
		if (filePath.exists()) {
			final String[] arr = {".classpath",".mymetadata",".myumldata",".project",".settings",".svn","classes","lib"};
			File[] fileArr = filePath.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					
					for (int i = 0; i < arr.length; i++) {
						if (name.endsWith(arr[i])) {
							return false;
						}
					}
					
					return true;
				}
			});
			boolean isSrc = false;
			for (int i = 0; i < fileArr.length;i++) {
				if (fileArr[i].isDirectory()) {
					getLastModify(fileArr[i].getAbsolutePath(),date);
				} else {
					isSrc = false;
					for (int j = 0; j < srcArr.length; j++) {
						// src路径
						if (fileArr[i].getAbsolutePath().contains(srcArr[j])) {
							if (fileArr[i].lastModified() >= date) {
								String classesPath = fileArr[i].getAbsolutePath().replaceAll(srcArr[j], 
										classPreffix).replaceAll("\\.java","");
								String pathPreffix = classesPath.substring(0, classesPath.lastIndexOf("\\"));
								String classesPosfix = pathPreffix.substring(pathPreffix.indexOf("classes")+"classes".length());
//								System.out.println(classesPosfix);
								
								File[] temp = new File(pathPreffix).listFiles();
								for (int k = 0; k < temp.length; k++) {
									if (temp[k].getAbsolutePath().startsWith(classesPath)) {
										copy(temp[k],srcPath+classesPosfix+"\\");
										srcCount++;
//										System.out.println(temp[k].getAbsolutePath());
									}
								}
								
							}
							isSrc = true;
							break;
						}
					}
					// 不包含src路径
					if (!isSrc) {
						if (fileArr[i].lastModified() >= date) {
							String pathPreffix = fileArr[i].getAbsolutePath().substring(0, fileArr[i].getAbsolutePath().lastIndexOf("\\"));
							String classesPosfix = pathPreffix.substring(pathPreffix.indexOf("WebRoot")+"WebRoot".length());
//							System.out.println(classesPosfix);
							copy(fileArr[i],webRootPath+classesPosfix+"\\");
							webRootCount++;
//							System.out.println(fileArr[i].getAbsolutePath());
						}
					}
					
				}
				
			}
			return Arrays.asList(fileArr);
		}
		return null;
		
	}
	
	public static void copy(File srcFile,String targetPath) {
		File target = new File(targetPath) ;
		if (!target.exists()) {
			target.mkdirs();
		}
		File newFile = new File(targetPath+srcFile.getName()) ;
		try {
			newFile.createNewFile();
			FileInputStream ins = new FileInputStream(srcFile);
			FileOutputStream out = new FileOutputStream(newFile);
			byte[] b = new byte[1024];
			int n = 0;
			while ((n = ins.read(b)) != -1) {
				out.write(b, 0, n);
			}

			ins.close();
			out.close();
			System.out.println(">>>:"+newFile.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void print(String srcpath,SSH2Util ssh2Util)  {
		String pathPreffix = null,name = null;
		try {
			File file = new File(srcpath);
			File[] list = file.listFiles();
			for (int i = 0; i < list.length; i++) {
				name = list[i].getName();
				if (list[i].isDirectory()) {
					print(list[i].getAbsolutePath(),ssh2Util);
				} else {
					String path = list[i].getAbsolutePath();
					if (srcpath.contains("src")) {
						pathPreffix = path.substring(srcPath.length(), path.lastIndexOf("\\")+1);
						ssh2Util.putFile(srcPath+pathPreffix, name,
								remoteProjectRootPath+"/WEB-INF/classes"+pathPreffix);
						
					} else {
						pathPreffix = path.substring(webRootPath.length(), path.lastIndexOf("\\")+1);
						ssh2Util.putFile(webRootPath+pathPreffix, list[i].getName(),
								remoteProjectRootPath+pathPreffix);
					}
//				ssh2Util.closeChannelSftp();
				}
			}
		} catch (Exception e) {
			try {
				ssh2Util.stop();
				ssh2Util = new SSH2Util(remoteHost, remoteUserName, remotePasswd, remotePort);
				if (srcpath.contains("src")) {
					ssh2Util.putFile(srcPath+pathPreffix, name,
							remoteProjectRootPath+"/WEB-INF/classes"+pathPreffix);
				} else {
					ssh2Util.putFile(webRootPath+pathPreffix, name,
							remoteProjectRootPath+pathPreffix);
				}
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	public static void main(String[] args) {
		srcPath = "D:\\bby\\src";
		webRootPath = "D:\\bby\\WebRoot";
		boolean defaultConfig = false;
		String dateStr = null,localPath = null;
		Properties prop = new Properties();
		
		try {
//			prop.load(FileDirUtil.class.getClassLoader().getResourceAsStream("config.properties"));
			prop.load(new FileInputStream(System.getProperty("user.dir")
					+ File.separator + "config.properties"));
			dateStr = prop.getProperty("updateDate");
			remoteProjectRootPath = prop.getProperty("remoteProjectRootPath");
			localPath = prop.getProperty("localProjectPath");
			remoteHost = prop.getProperty("remoteHost");
			remoteUserName = prop.getProperty("remoteUserName");
			remotePasswd = prop.getProperty("remotePasswd");
			remotePort = Integer.parseInt(prop.getProperty("remotePort"));
			
			if (!DateUtil.isValidDate(dateStr)) {
				System.out.println("updateDate格式不正确");
				return;
			}
			long date = DateUtil.strToDate(dateStr).getTime();
			System.out.println(dateStr+"更新后的文件：");
			getLastModify(localPath,date);
			System.out.println("从svn在"+dateStr+"更新后的文件：src:"+srcCount+",webRoot:"+webRootCount);
			
			// 复制完后，一起打包到ssh上
			SSH2Util ssh2Util = new SSH2Util(remoteHost, remoteUserName, remotePasswd, remotePort);
			System.out.println("上传文件开始！");
			print(srcPath,ssh2Util);
			print(webRootPath,ssh2Util);
			System.out.println("上传文件完成！");
			ssh2Util.stop();
			System.exit(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
