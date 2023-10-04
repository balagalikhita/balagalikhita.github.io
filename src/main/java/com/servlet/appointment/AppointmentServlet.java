package com.servlet.appointment;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/appointment")
public class AppointmentServlet extends HttpServlet{
	//create query
			private static final String INSERT_QUERY="INSERT INTO slots(Clinic,Location,Specialist,Timings) VALUES (?,?,?,?)";
		protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
			//get PrintWriter
					PrintWriter pw=res.getWriter();
					//set Content type
					res.setContentType("text/html");
					String Clinic=req.getParameter("Clinic");
					String Location=req.getParameter("Location");
					String Specialist=req.getParameter("Specialist");
					String datetimeStr = req.getParameter("DATE_AND_TIME");
					System.out.println("Name:" +Clinic);
					System.out.println("LastName:" +Location);
					System.out.println("BloodGroup:" +Specialist);
					System.out.println("Phonenumber:" +datetimeStr);
					try {
						Class.forName("com.mysql.jdbc.Driver");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//create the connection
					try(Connection con=DriverManager.getConnection("jdbc:mysql:///medicom","root","system");
							PreparedStatement ps=con.prepareStatement(INSERT_QUERY);){
						ps.setString(1,Clinic);
						ps.setString(2, Location);
						ps.setString(3, Specialist);
						ps.setString(4, datetimeStr);
						
						
						//execute query
						int count=ps.executeUpdate();
						if(count ==0) {
							pw.println("record not submitted");
						}
						else {
							pw.println("Slot confirmed");
							res.sendRedirect("popup2.html");
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
		
		protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
			doGet(req,res);
		}
}
