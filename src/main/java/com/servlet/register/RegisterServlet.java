package com.servlet.register;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	//create query
		private static final String INSERT_QUERY="INSERT INTO users(FirstName,LastName,BloodGroup,Phonenumber,EmailId,Password) VALUES (?,?,?,?,?,?)";
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//get PrintWriter
				PrintWriter pw=res.getWriter();
				//set Content type
				res.setContentType("text/html");
				String FirstName=req.getParameter("FirstName");
				String LastName=req.getParameter("LastName");
				String BloodGroup=req.getParameter("BloodGroup");
				String Phonenumber=req.getParameter("Phonenumber");
				String EmailId=req.getParameter("EmailId");
				String Password=req.getParameter("Password");
//				System.out.println("Name:" +FirstName);
//				System.out.println("LastName:" +LastName);
//				System.out.println("BloodGroup:" +BloodGroup);
//				System.out.println("Phonenumber:" +Phonenumber);
//				System.out.println(FirstName+LastName+BloodGroup+Phonenumber);
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//create the connection
				try(Connection con=DriverManager.getConnection("jdbc:mysql:///medicom","root","system");
						PreparedStatement checkEmailExistence = con.prepareStatement("SELECT EmailId FROM users WHERE EmailId = ?");
						PreparedStatement ps=con.prepareStatement(INSERT_QUERY);){
					 checkEmailExistence.setString(1, EmailId);
			            ResultSet resultSet = checkEmailExistence.executeQuery();
			            if (resultSet.next()) {
			                // Email already exists, redirect to another HTML page
			                res.sendRedirect("popup1.html");
			            } else {
			                // Email does not exist, proceed with registration
			                ps.setString(1, FirstName);
			                ps.setString(2, LastName);
			                ps.setString(3, BloodGroup);
			                ps.setString(4, Phonenumber);
			                ps.setString(5, EmailId);
			                ps.setString(6, Password);
					
					//execute query
					int count=ps.executeUpdate();
					if(count ==0) {
						pw.println("record not submitted");
					}
					else {
						pw.println("Registration succesfull");
						res.sendRedirect("popup.html");
					}
				}
				}catch(SQLException se) {
					pw.println(se.getMessage());
					se.printStackTrace();
				}catch(Exception e) {
					pw.println(e.getMessage());
					e.printStackTrace();
				}
				//close the stream
				pw.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req,res);
	}
}
