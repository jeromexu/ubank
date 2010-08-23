package com.ufinity.marchant.ubank.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.service.FileService;
import com.ufinity.marchant.ubank.service.ServiceFactory;

/**
 * 
 * download file servlet
 * 
 * @author jerome
 * @version 1.0
 * @since 2010-8-19
 */
public class DownLoadServlet extends AbstractServlet {
	
	private static final long serialVersionUID = 1L;
	// Logger for this class
    protected static  final Logger LOGGER = Logger.getLogger(DownLoadServlet.class);
    
    private FileService fileService = null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownLoadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String method = parseActionName(request);
        String rslt = "";
        
        if(Constant.ACTION_DOWNLOAD.equals(method)){
            rslt = download(request, response);
        }
        if(!"".equals(rslt)){
        	forward(request, response, rslt);
        }
	}

	/**
	 * 
	 * download the file
	 * 
	 * @param request download file
	 * @param response 
	 * @author jerome
	 */
	@SuppressWarnings("deprecation")
	private String download(HttpServletRequest request,
			HttpServletResponse response) {
		
		response.setContentType("text/html; charset=UTF-8");
		String id = request.getParameter("id");
		Long fileId = 0l;
		if (!Validity.isEmpty(id)) {
			fileId = Long.valueOf(id);
		} else {
			return Constant.ERROR_PAGE;
		}
		fileService = ServiceFactory.createService(FileService.class);
		FileBean fileBean = fileService.getFileBean(fileId);
		File file =  null;
		if (fileBean != null) {
			// create file object
	        file = new File(request.getRealPath("/")+getFilePath(fileBean.getDirectory(),fileBean.getFileName()));
	        
	        // set response encoded mode 
	        response.setContentType("application/x-msdownload");

	        // set the file size 
	        response.setContentLength((int) file.length());

	        // set the addition file name
	        response.setHeader("Content-Disposition","attachment;filename="+fileBean.getFileName());
		} else {
			return Constant.ERROR_PAGE;
		}
		BufferedInputStream buff = null;
		OutputStream outPut = null;
		try {
			 // read file into i/o
			 buff = new BufferedInputStream(new FileInputStream(file));
			 // cache
		     byte [] b = new byte[5*1024];
		     // count the file size which file has downloaded currently
		     long k = 0;
		     outPut = response.getOutputStream();
		     while(k < file.length()){
		          int j = buff.read(b,0,1024);
		          k += j;
		          //write the data into the client memory
		          outPut.write(b,0,j);
		     }
		     outPut.flush();
		} catch (Exception e) {
			LOGGER.error("download file exception!", e);
		} finally{
			if(buff != null){
				try {
					buff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outPut != null) {
				try {
					outPut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * get the integrited path
	 * 
	 * @param directory the file path folder
	 * @param fileName  the file name
	 * @return integrited path 
	 * @author jerome
	 */
	private String getFilePath(String directory, String fileName){
		String filePath = null;
		if(!Validity.isEmpty(directory) && directory.endsWith(File.separator)){
			filePath = directory + fileName;
		} else {
			filePath = directory + File.separator + fileName;
		}
		return filePath;
	}

}
