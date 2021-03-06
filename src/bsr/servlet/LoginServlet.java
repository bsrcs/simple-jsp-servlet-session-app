package bsr.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bsr.model.User;
import bsr.service.LoginService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		LoginService loginService = new LoginService();
		boolean isAuthenticated = loginService.authenticate(username, password);

		if (isAuthenticated) {
			//Returns the current HttpSession associated with this request or,
			// if there is no current session and create is true, returns a new session. 
			// create a new session (create parameter is true)
			session = request.getSession(true);

			User enteredUser = loginService.getUserDetails(username);
			session.setAttribute("user", enteredUser);
			session.setAttribute("username", enteredUser.getUsername());
			session.setAttribute("IS_LOGGED_IN", true);

			RequestDispatcher dispatcher = request.getRequestDispatcher("welcome.jsp");
			dispatcher.forward(request, response);
			return;
		} else {
			out.print("Wrong user credentials!");
			request.getRequestDispatcher("login.jsp").include(request, response);
		}
		out.close();
	}

}
