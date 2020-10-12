package com.sunny.bookstore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunny.bookstore.dao.BookDAO;
import com.sunny.bookstore.model.Book;

/**
 * ControllerServlet.java.
 * 
 * This servlet acts as a page controller for the application, handling all
 * requests from the user.
 * 
 * @author Sanelisiwe Ntini
 *
 */
public class ControllerServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private BookDAO bookDAO;

	/**
	 * Initialize JDBC connection.
	 * 
	 */
	public void init()
	{
		String jdbcURL = getServletContext().getInitParameter("jdbcURL");
		String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
		String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

		bookDAO = new BookDAO(jdbcURL, jdbcUsername, jdbcPassword);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String action = request.getServletPath();

		try
		{
			switch (action)
			{
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertBook(request, response);
				break;
			case "/delete":
				deleteBook(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/update":
				updateBook(request, response);
				break;
			default:
				listBook(request, response);
				break;
			}
		} catch (SQLException ex)
		{
			throw new ServletException(ex);
		}
	}

	/**
	 * Read.
	 * 
	 * @param request  The HttpServletRequest request.
	 * @param response The HttpServletRequest response.
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void listBook(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException
	{
		List<Book> listBook = bookDAO.listAllBooks();
		request.setAttribute("listBook", listBook);
		RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher("BookForm.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Show form.
	 * 
	 * @param request  The HttpServletRequest request.
	 * @param response The HttpServletRequest response.
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException
	{
		int id = Integer.parseInt(request.getParameter("id"));
		Book existingBook = bookDAO.getBook(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("BookForm.jsp");
		request.setAttribute("book", existingBook);
		dispatcher.forward(request, response);

	}

	/**
	 * Create.
	 * 
	 * @param request  The HttpServletRequest request.
	 * @param response The HttpServletRequest response.
	 * @throws SQLException
	 * @throws IOException
	 */
	private void insertBook(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException
	{
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		float price = Float.parseFloat(request.getParameter("price"));

		Book newBook = new Book(title, author, price);
		bookDAO.insertBook(newBook);
		response.sendRedirect("list");
	}

	/**
	 * Update.
	 * 
	 * @param request  The HttpServletRequest request.
	 * @param response The HttpServletRequest response.
	 * @throws SQLException
	 * @throws IOException
	 */
	private void updateBook(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException
	{
		int id = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		float price = Float.parseFloat(request.getParameter("price"));

		Book book = new Book(id, title, author, price);
		bookDAO.updateBook(book);
		response.sendRedirect("list");
	}

	/**
	 * Delete.
	 * 
	 * @param request  The HttpServletRequest request.
	 * @param response The HttpServletRequest response.
	 * @throws SQLException
	 * @throws IOException
	 */
	private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException
	{
		int id = Integer.parseInt(request.getParameter("id"));

		Book book = new Book(id);
		bookDAO.deleteBook(book);
		response.sendRedirect("list");

	}

}
