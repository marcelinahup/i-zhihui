package com.hk.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import com.hk.common.security.EncryptMD5;

import de.idyl.winzipaes.AesZipFileEncrypter;
import de.idyl.winzipaes.impl.AESEncrypterBC;

public class ZipUtils {
	public static final String EXT = ".zip";  
    private static final String BASE_DIR = "";  
    private static final String SAVE_DIR = "d:/";  //待确定

    // 符号"/"用来作为目录标识判断符  
    private static final int BUFFER = 1024;  
    
    // ---------------------------------------------------------------------
    
    /** 
     * 文件压缩 
     *  
     * @param srcPath 源文件路径 
     * @param delFlag 是否删除原文件
     * @return 保存路径
     */  
    public static String compress(String srcPath, boolean delFlag) throws Exception {  
        return compress(new File(srcPath), delFlag);  
    }  
  
    /** 
     * 压缩文件，保存到指定文件下
     *  
     * @param srcFile 压缩源文件
     * @param delFlag 是否删除原文件
     * @return 保存路径
     * @throws Exception 
     */  
    public static String compress(File srcFile, boolean delFlag) throws Exception {  
    	String destFile = "";
    	String tempDestFile = getSavePath();
    	// 对输出文件做CRC32校验  
        CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(tempDestFile), new CRC32());  
        ZipOutputStream zos = new ZipOutputStream(cos);  
        zos.setEncoding("GBK");
        compress(srcFile, zos, BASE_DIR);  
        zos.flush();  
        zos.close();  
        if(delFlag){
        	delete(srcFile.getAbsolutePath());
        }
        
    	destFile = tempDestFile;
    	return destFile;
    }  
    /** 
     * 压缩文件，保存到指定文件下
     *  
     * @param files 源文件路径列
     * @param zipfile_name 压缩文件名
     * @param save_path 压缩文件保存路径
     * @param delFlag 是否删除原文件
     * @return 保存文件名
     * @throws Exception 
     */  
    public static String compress_hw(File srcFile,String zipfile_name,String save_path, boolean delFlag) throws Exception {  
    	String destFileName = "";
	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 拼上日期，防止重名
		String saveName =zipfile_name+ "_" + sdf.format(new Date());
		// 拼上扩展名
		//saveName 转成md5随机字符串
		saveName=EncryptMD5.encrypt(saveName);
       
		saveName+=EXT;
    	String tempDestFile = save_path+"/"+saveName;
    	// 对输出文件做CRC32校验  
    	CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(tempDestFile), new CRC32());  
    	ZipOutputStream zos = new ZipOutputStream(cos);  
    	zos.setEncoding("GBK");
        
        compress(srcFile, zos, BASE_DIR);  
        
    	zos.flush();  
    	zos.close(); 
    	
    	if(delFlag){
    		delete(srcFile.getAbsolutePath());
        }
    	
    	destFileName = saveName;
    	return destFileName;
    }  

    /** 
     * 压缩文件，保存到指定文件下
     *  
     * @param files 源文件路径列
     * @param zipfile_name 压缩文件名
     * @param save_path 压缩文件保存路径
     * @param delFlag 是否删除原文件
     * @return 保存文件名
     * @throws Exception 
     */  
    public static String compress_hw(List<String> files,String zipfile_name,String save_path, boolean delFlag) throws Exception {  
    	String destFileName = "";
	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 拼上日期，防止重名
		String saveName =zipfile_name+ "_" + sdf.format(new Date());
		// 拼上扩展名
		//saveName 转成md5随机字符串
		saveName=EncryptMD5.encrypt(saveName);
        
		saveName+=EXT;
    	String tempDestFile = save_path+"/"+saveName;
    	// 对输出文件做CRC32校验  
    	CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(tempDestFile), new CRC32());  
    	ZipOutputStream zos = new ZipOutputStream(cos);  
    	zos.setEncoding("GBK");
        for ( String filename : files ) {
        	compress(new File(filename), zos, BASE_DIR);  
        }
    	zos.flush();  
    	zos.close(); 
    	
    	if(delFlag){
			for (String filename : files) {
				delete(filename);
			}
        }
    	
    	destFileName = saveName;
    	return destFileName;
    }  

    /** 
     * 压缩文件，保存到指定文件下
     *  
     * @param files 源文件路径列
     * @param delFlag 是否删除原文件
     * @return 保存路径
     * @throws Exception 
     */  
    public static String compress(List<String> files, boolean delFlag) throws Exception {  
    	String destFile = "";
    	String tempDestFile = getSavePath();
    	// 对输出文件做CRC32校验  
    	CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(tempDestFile), new CRC32());  
    	ZipOutputStream zos = new ZipOutputStream(cos);  
    	zos.setEncoding("GBK");
        for ( String filename : files ) {
        	compress(new File(filename), zos, BASE_DIR);  
        }
    	zos.flush();  
    	zos.close(); 
    	
    	if(delFlag){
			for (String filename : files) {
				delete(filename);
			}
        }
    	
    	destFile = tempDestFile;
    	return destFile;
    }  

    // ---------------------------------------------------------------------
    
    /** 
     * 压缩文件
     *  
     * @param srcFile 压缩资源路径
     * @param zos ZipOutputStream 
     * @param basePath 压缩包内相对路径 
     * @throws Exception 
     */  
    private static void compress(File srcFile, ZipOutputStream zos, String basePath) throws Exception {  
    	if (srcFile.isDirectory()) {  
    		compressDir(srcFile, zos, basePath);  
    	} else {  
    		compressFile(srcFile, zos, basePath);  
    	}  
    }  
  
    /** 
     * 目录 压缩
     *  
     * @param dir 
     * @param zos 
     * @param basePath 
     * @throws Exception 
     */  
    private static void compressDir(File dir, ZipOutputStream zos, String basePath) throws Exception {  
        File[] files = dir.listFiles();  
        // 构建空目录  
        if (files.length < 1) {  
            ZipEntry entry = new ZipEntry(basePath + dir.getName() + File.separator);
            zos.putNextEntry(entry);  
            zos.closeEntry();  
        }  
        for (File file : files) {  
            // 递归压缩  
            compress(file, zos, basePath + dir.getName() + File.separator);  
        }  
    }  
  
    /** 
     * 文件压缩 
     *  
     * @param file 待压缩文件 
     * @param zos ZipOutputStream 
     * @param dir 压缩文件中的当前路径 
     * @throws Exception 
     */  
    private static void compressFile(File file, ZipOutputStream zos, String dir) throws Exception {  
        //ZipEntry entry = new ZipEntry(dir + file.getName());
    	ZipEntry entry = new ZipEntry(file.getName());
       
        zos.putNextEntry(entry);  
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));  
  
        int count;  
        byte data[] = new byte[BUFFER];  
        while ((count = bis.read(data, 0, BUFFER)) != -1) {  
            zos.write(data, 0, count);  
        }  
        bis.close();  
        zos.closeEntry();  
    }  
    
    // ---------------------------------------------------------------------

    /**
     * 加密压缩的临时文件
     * 
     * @param srcFile 源文件路径 
     * @param destFile 目标路径
     * @throws Exception
     */
    public static void compressTemp(String srcFile, String destFile, boolean delFlag) throws Exception {  
    	System.out.println(destFile);
    	// 对输出文件做CRC32校验  
    	CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(destFile), new CRC32());  
    	ZipOutputStream zos = new ZipOutputStream(cos);  
    	compress(new File(srcFile), zos, BASE_DIR);  
    	zos.flush();  
    	zos.close();
    	
    	if(delFlag){
    		// 删除原文件
    		delete(srcFile);
    	}
    }  
    
    /**
     * 加密压缩的临时文件
     * 
     * @param files 源文件路径列
     * @param destFile 目标路径
     * @param delFlag 是否删除原文件
     * @throws Exception
     */
    private static void compressTemp(List<String> files, String destFile, boolean delFlag) throws Exception {  
    	// 对输出文件做CRC32校验  
    	CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(destFile), new CRC32());  
    	ZipOutputStream zos = new ZipOutputStream(cos);  
        for ( String filename : files ) {
            File file = new File( filename );
            compress(file, zos, BASE_DIR);
        }
        zos.close();
    	if(delFlag){
    		// 删除原文件
    		for ( String filename : files ) {
    			delete(filename);
            }
    	}
    }  
    
    // ---------------------------------------------------------------------
    
	/**
	 * 压缩单个文件 + 加密
	 *  
	 * @param srcFilePath 源文件路径，可以是文件，也可以是目录
	 * @param password 密码
	 * @param delFlag 是否删除原文件
	 * @return
	 * @throws Exception
	 */
	public static String compressAndEncrypter(String srcFilePath, String password, boolean delFlag) throws Exception {
		String savePath = "";
		File file = new File(srcFilePath);
		if(file.exists()){
			if(file.isDirectory()){
				savePath = compressDirAndEncrypter(srcFilePath, getSavePath(), password, delFlag);
			}else if(file.isFile()){
				ArrayList<String> fileList = new ArrayList<String>();
				fileList.add(srcFilePath);
				savePath = compressAndEncrypter(fileList, password, delFlag);
			}
		}
		return savePath;
	}

	/**
	 * 多个文件合并压缩 + 加密
	 * 
	 * @param inFiles 源文件路径列
	 * @param password 密码
	 * @param delFlag 是否删除原文件
	 * @return
	 * @throws Exception
	 */
	public static String compressAndEncrypter(List<String> inFiles, String password, boolean delFlag) throws Exception {
		String savePath = "";
		String tempFile = getSavePath();
		if (null != password && !"".equals(password)) {
			tempFile = tempFile + "temp";
		}
		if (inFiles != null && inFiles.size() > 0) {
			
			compressTemp(inFiles,tempFile ,delFlag);
			
			if (null != password && !"".equals(password)) {
				File temp = new File(tempFile);
				String outFilePath = getSavePath();
				AesZipFileEncrypter enc = new AesZipFileEncrypter(outFilePath, new AESEncrypterBC());
				//enc.setEncoding("UTF-8");
				enc.addAll(temp, password);
				savePath = outFilePath;
				enc.close();
				temp.delete();
			}
		}
		return savePath;
	}

	/**
	 * 压缩目录 + 加密
	 * 
	 * @param inDir 目录
	 * @param key 密码
	 * @return
	 * @throws Exception
	 */
	private static String compressDirAndEncrypter(String inDir, String outFile, String key, boolean delFlag) throws Exception {
		System.out.println("--inDir--" + inDir);
		System.out.println("--outFile--" + outFile);
		String savePath = "";
		
		String tempFile = outFile;
		if (null != key && !"".equals(key)) {
			tempFile = tempFile + "temp";
		}

		compressTemp(inDir, tempFile, delFlag);
		
		if (null != key && !"".equals(key)) {
			File temp = new File(tempFile);
			AesZipFileEncrypter enc = new AesZipFileEncrypter(outFile, new AESEncrypterBC());
			//enc.setEncoding("UTF-8");
			enc.addAll(temp, key);
			savePath = outFile;
			enc.close();
			temp.delete();
		}
		return savePath;
	}
 
	/**
	 * 
	 * 解压缩
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public static String extract(String filePath, String fileName, boolean delFlag) throws Exception {	
		ZipFile zipFile = new ZipFile(fileName);
		Enumeration<? extends ZipEntry> emu = zipFile.getEntries();
		String extraFolder = "";
		while (emu.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) emu.nextElement();
			
			// create directory
			if (entry.isDirectory()) {  
                new File(filePath + entry.getName()).mkdirs();  
                extraFolder = entry.getName();
                continue;  
            }
			
			BufferedInputStream bis = new BufferedInputStream(zipFile
                    .getInputStream(entry));
            File file = new File(filePath + entry.getName());

            // 加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件  
            // 而这个文件所在的目录还没有出现过，所以要建出目录来。  
            File parent = file.getParentFile();
            if (parent != null && (!parent.exists())) {
                parent.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);  
            
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                bos.write(data, 0, count);
            }

            bos.flush();
            bos.close();
            bis.close();
		}
		zipFile.close();

		// delete source zipfile
		if(delFlag){
        	delete(fileName);
        }
		return extraFolder;
	}
	
	/**
	 * 
	 * zipファイルに一つ以上のフォルダが存在かどうかをチェックする
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public static int checkExtract(String fileName) throws Exception {	
		ZipFile zipFile = new ZipFile(fileName);
		Enumeration<? extends ZipEntry> emu = zipFile.getEntries();
		int i = 0;
		while (emu.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) emu.nextElement();
			if (entry.isDirectory()) {  
				i ++;
                continue;  
            }
		}
		zipFile.close();
		return i;
	}

	/**
	 * 生成zip文件名
	 * 
	 * @return
	 */
	private static String getSavePath(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return SAVE_DIR + sdf.format(new Date()) + EXT;
	}
	
    /**   
     * 删除文件，可以是单个文件或文件夹   
     * 
     * @param   fileName    待删除的文件名   
     * @return 文件删除成功返回true,否则返回false   
     */    
    public static boolean delete(String fileName){     
        File file = new File(fileName);     
        if(!file.exists()){     
            System.out.println("删除文件失败："+fileName+"文件不存在");     
            return false;     
        }else{     
            if(file.isFile()){     
                return deleteFile(fileName);     
            }else{     
                return deleteDirectory(fileName);     
            }     
        }     
    }     
         
    /**   
     * 删除单个文件   
     * 
     * @param fileName 被删除文件的文件名   
     * @return 单个文件删除成功返回true,否则返回false   
     */    
    public static boolean deleteFile(String fileName){     
        File file = new File(fileName);     
        if(file.isFile() && file.exists()){     
            file.delete();     
            System.out.println("删除单个文件"+fileName+"成功！");     
            return true;     
        }else{     
            System.out.println("删除单个文件"+fileName+"失败！");     
            return false;     
        }     
    }     
         
    /**   
     * 删除目录（文件夹）以及目录下的文件   
     * 
     * @param   dir 被删除目录的文件路径   
     * @return  目录删除成功返回true,否则返回false   
     */    
    public static boolean deleteDirectory(String dir){     
        //如果dir不以文件分隔符结尾，自动添加文件分隔符     
        if(!dir.endsWith(File.separator)){     
            dir = dir+File.separator;     
        }     
        File dirFile = new File(dir);     
        //如果dir对应的文件不存在，或者不是一个目录，则退出     
        if(!dirFile.exists() || !dirFile.isDirectory()){     
            System.out.println("删除目录失败"+dir+"目录不存在！");     
            return false;     
        }     
        boolean flag = true;     
        //删除文件夹下的所有文件(包括子目录)     
        File[] files = dirFile.listFiles();     
        for(int i=0;i<files.length;i++){     
            //删除子文件     
            if(files[i].isFile()){     
                flag = deleteFile(files[i].getAbsolutePath());     
                if(!flag){     
                    break;     
                }     
            }     
            //删除子目录     
            else{     
                flag = deleteDirectory(files[i].getAbsolutePath());     
                if(!flag){     
                    break;     
                }     
            }     
        }     
             
        if(!flag){     
            System.out.println("删除目录失败");     
            return false;     
        }     
             
        //删除当前目录     
        if(dirFile.delete()){     
            System.out.println("删除目录"+dir+"成功！");     
            return true;     
        }else{     
            System.out.println("删除目录"+dir+"失败！");     
            return false;     
        }     
    }     

//	
//	@Test
//	public void t() {
//		try {
//			long a = System.currentTimeMillis();
//			//System.out.println(compressAndEncrypter(dir, password, false));
//			List<String> files = new ArrayList<String>();
//			files.add(dir + File.separator + "新建 文本文档.txt");
//			files.add(dir + File.separator + "复件 新建 文本文档.txt");
//			files.add(dir + File.separator + "复件 (2) 新建 文本文档.txt");
//			System.out.println(compressAndEncrypter(files, password, true));
//			//System.out.println(compressAndEncrypter(dir,password, true));
//			//System.out.println(compress(files, false));
//			System.out.println("over,耗时 -> " + (System.currentTimeMillis() - a));
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
}
