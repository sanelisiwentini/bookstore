package com.sunny.bookstore.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sunny.bookstore.model.Book;

/**
 * BookDAO.java
 * 
 * This DAO class provides CRUD database operations for the table book in the
 * database.
 * 
 * @author Sanelisiwe Ntini
 *
 */
public class BookDAO
{
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;

	public BookDAO(String jdbcURL, String jdbcUsername, String jdbcPassword)
	{
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}

	/**
	 * Provide JDBC connection.
	 * 
	 * @throws SQLException
	 */
	protected void connect() throws SQLException
	{
		if (jdbcConnection == null || jdbcConnection.isClosed())
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e)
			{
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}
	}

	/**
	 * Close JDBC connection.
	 * 
	 * @throws SQLException
	 */
	protected void disconnect() throws SQLException
	{
		if (jdbcConnection != null && !jdbcConnection.isClosed())
		{
			jdbcConnection.close();
		}
	}

	/**
	 * Insert Book entity into database.
	 * 
	 * @param book The Book entity to be inserted.
	 * @return The success code.
	 * @throws SQLException
	 */
	public boolean insertBook(Book book) throws SQLException
	{
		String sql = "INSERT INTO book (title, author, price) VALUES (?, ?, ?)";
		connect();

		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setString(1, book.getTitle());
		statement.setString(2, book.getAuthor());
		statement.setFloat(3, book.getPrice());

		boolean rowInserted = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowInserted;
	}

	/**
	 * Find all books.
	 * 
	 * @return The list of Books.
	 * @throws SQLException
	 */
	public List<Book> listAllBooks() throws SQLException
	{
		List<Book> listBook = new ArrayList<>();

		String sql = "SELECT * FROM book";

		connect();

		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next())
		{
			int id = resultSet.getInt("book_id");
			String title = resultSet.getString("title");
			String author = resultSet.getString("author");
			float price = resultSet.getFloat("price");

			Book book = new Book(id, title, author, price);
			listBook.add(book);
		}

		resultSet.close();
		statement.close();

		disconnect();

		return listBook;
	}

	/**
	 * Delete a Book entity from the database.
	 * 
	 * @param book The Book entity to be deleted.
	 * @return Success code.
	 * @throws SQLException
	 */
	public boolean deleteBook(Book book) throws SQLException
	{
		String sql = "DELETE FROM book where book_id = ?";

		connect();

		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, book.getId());

		boolean rowDeleted = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowDeleted;
	}

	/**
	 * Update an existing Book entity.
	 * 
	 * @param book The Book entity to be updated.
	 * @return The success code.
	 * @throws SQLException
	 */
	public boolean updateBook(Book book) throws SQLException
	{
		String sql = "UPDATE book SET title = ?, author = ?, price = ?";
		sql += " WHERE book_id = ?";
		connect();

		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setString(1, book.getTitle());
		statement.setString(2, book.getAuthor());
		statement.setFloat(3, book.getPrice());
		statement.setInt(4, book.getId());

		boolean rowUpdated = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowUpdated;
	}

	/**
	 * Find an individual Book entity.
	 * 
	 * @param id The Book id.
	 * @return The Book entity.
	 * @throws SQLException
	 */
	public Book getBook(int id) throws SQLException
	{
		Book book = null;
		String sql = "SELECT * FROM book WHERE book_id = ?";

		connect();

		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, id);

		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next())
		{
			String title = resultSet.getString("title");
			String author = resultSet.getString("author");
			float price = resultSet.getFloat("price");

			book = new Book(id, title, author, price);
		}

		resultSet.close();
		statement.close();

		return book;
	}
}
