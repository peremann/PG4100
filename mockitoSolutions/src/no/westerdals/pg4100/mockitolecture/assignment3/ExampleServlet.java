package no.westerdals.pg4100.mockitolecture.assignment3;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@SuppressWarnings("serial")
@WebServlet("someURL")
public class ExampleServlet extends HttpServlet {

	private ServletHelper helper = new ServletHelper();
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		String input = request.getParameter("input");
		request.setAttribute("input", input);
		String answer = helper.provideAnswer(input);
		request.setAttribute("answer", answer);
		RequestDispatcher view =
				request.getRequestDispatcher("answer.jsp");
		view.forward(request, response);
	}

	public void setHelper(ServletHelper helper) {
		this.helper = helper;
	}
	
}