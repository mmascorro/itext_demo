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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.XfaForm;

public class FormFill2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String XFA_DATA_SCHEMA = "http://www.xfa.org/schema/xfa-data/1.0/";
       
    public FormFill2() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String src = getServletContext().getRealPath("forms/dynamic.pdf");

		String xml = getServletContext().getRealPath("data/data.xml");
		
		//to browser
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		//to file
		//String dest =  "..\\result.pdf";
		//FileOutputStream fos = new FileOutputStream(dest); 
		
		try {
			PdfReader reader = new PdfReader(src);
	        PdfStamper stamper = new PdfStamper(reader, baos);
	        XfaForm xfa = new XfaForm(reader);

	        //turn xml source data into stream
	        InputStream is = new FileInputStream(xml);
	        
	        //turn input stream into document
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setNamespaceAware(true);
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document newdoc = db.parse(is);

	        Element fillData = newdoc.getDocumentElement();
	       	        
	        //get XFA structure from pdf
	        Document oldDomDoc = xfa.getDomDocument();


	        //create datasets & dataset node
	        Element ds = oldDomDoc.createElementNS(XFA_DATA_SCHEMA, "xfa:datasets");
	        ds.setAttribute("xmlns:xfa", XFA_DATA_SCHEMA); //1.4 specifc. doesn't seem needed in later version
	        
	        Node data = oldDomDoc.createElementNS(XFA_DATA_SCHEMA, "xfa:data"); 
	        //add data to datasets
	        ds.appendChild(data);
	        
	        //add fillData to new nodes
	        data.appendChild(oldDomDoc.importNode(fillData, true));
	        
	        //add datasets/data/filldata to XFA structure
	        oldDomDoc.getFirstChild().appendChild(ds);

	        //set updated XFA
			xfa.setDomDocument(oldDomDoc);
	        xfa.setChanged(true);
	        XfaForm.setXfa(xfa, stamper.getReader(), stamper.getWriter());
	        stamper.close();
			
	        
	        //for debug
	        /*
	        FileOutputStream os = new FileOutputStream("..\\data.xml");
	        Transformer tf = TransformerFactory.newInstance().newTransformer();
	        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        tf.setOutputProperty(OutputKeys.INDENT, "yes");
	        tf.transform(new DOMSource(oldDomDoc), new StreamResult(os));
			*/
	        
	        
	        //show to browser
	        
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            
	        OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
