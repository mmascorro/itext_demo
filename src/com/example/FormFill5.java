package com.example;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.XfaForm;

public class FormFill5 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FormFill5() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String src = getServletContext().getRealPath("forms/dynamic.pdf");
		
		String xmlData = getServletContext().getRealPath("data/data.xml");
		
		InputStream is = new FileInputStream(xmlData);
		//just saves file
		/*
		String dest = getServletContext().getRealPath("forms/filled.pdf");
		try {
			PdfReader reader = new PdfReader(src);
	        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));

	        AcroFields form = stamper.getAcroFields();
	        XfaForm xfa = form.getXfa();
	        xfa.fillXfaForm( is );
	
	        stamper.close();
		} catch (Exception e) {
			
		}
		*/
		
		//serve generated pdf to browser
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			PdfReader reader = new PdfReader(src);
	        PdfStamper stamper = new PdfStamper(reader,baos);

	        AcroFields form = stamper.getAcroFields();
	        XfaForm xfa = form.getXfa();
	        xfa.fillXfaForm( is );
	
	        stamper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
	}

}
