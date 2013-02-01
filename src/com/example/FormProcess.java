package com.example;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class FormProcess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public FormProcess() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletInputStream in=request.getInputStream();
		StringBuffer xmlStr=new StringBuffer();
	    int d;
	    while((d=in.read()) != -1){
	    	xmlStr.append((char)d);
	    }
		    
		
		String xml = xmlStr.toString();

		String name = "";
		String email = "";
		
		try {
			Element dnode =  DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes())).getDocumentElement();
			
			NodeList nodeList = dnode.getChildNodes();
			for(int i=0; i<nodeList.getLength(); i++){
			  Node childNode = nodeList.item(i);
			  System.out.println(childNode.getNodeName() );
			  System.out.println(childNode.getChildNodes().item(0).getNodeValue());
			
			  if(childNode.getNodeName() == "name") {
				  
				  name = childNode.getChildNodes().item(0).getNodeValue();
			  }
			  if(childNode.getNodeName() == "email") {
				  
				  email = childNode.getChildNodes().item(0).getNodeValue();
			  }
			 
			}
			
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		
		String msg = "Thanks " + name + ". A confirmation message has been sent to " + email;
		
		request.setAttribute("msg", msg);  
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/thanks.jsp");
		dispatcher.forward(request, response);
	}

}
