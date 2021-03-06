package com.indoorloc.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadImage extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		saveImage(request, response);
		
		double x = Math.random() * 10.0;
		double y = Math.random() * 10.0;
		String res = "(" + x + ", " + y + ")";
		response.getWriter().append(res);
    }
	
	//保存图片
	private void saveImage(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			DiskFileItemFactory dff = new DiskFileItemFactory();
			ServletFileUpload sfu = new ServletFileUpload(dff);
			List<FileItem> items = sfu.parseRequest(request);
			
			// 获取上传字段
			FileItem fileItem = items.get(0);
			
			// 文件名
			String filename = fileItem.getName();

			// 生成存储路径
			String storeDirectory = getServletContext().getRealPath("/image");
			File file = new File(storeDirectory);
			if (!file.exists()) {
				file.mkdir();
			}
			String path = genericPath(filename, storeDirectory);
			
			// 处理文件的上传
			try {
				fileItem.write(new File(storeDirectory + path, filename));
				
				String filePath = "/image" + path + "/" + filename;
//				response.getWriter().append(filePath);
			} catch (Exception e) {
				response.getWriter().append("Image upload failure.");
			}
		} 
		catch (Exception e) {
			response.getWriter().append("Image upload failure.");
		}
	}
	//计算文件的存放目录
	private String genericPath(String filename, String storeDirectory) {
		int hashCode = filename.hashCode();
		int dir1 = hashCode&0xf;
		int dir2 = (hashCode&0xf0)>>4;
		
		String dir = "/"+dir1+"/"+dir2;
		
		File file = new File(storeDirectory,dir);
		if(!file.exists()){
			file.mkdirs();
		}
		return dir;
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
