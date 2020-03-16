package com.skilldistillery.film.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.skilldistillery.film.entities.Actor;
import com.skilldistillery.film.entities.Film;

@Component
public class FilmDAOJDBCImpl implements FilmDAO {

	private static final String url = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";
	
	public FilmDAOJDBCImpl() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmID) {
		Film film = null;
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "SELECT id, title, description, release_year, language_id, rental_duration, "
					+ " rental_rate, length, replacement_cost, rating, special_features " + " FROM film WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmID);
			ResultSet filmResult = stmt.executeQuery();
			if (filmResult.next()) {
				int filmId = filmResult.getInt(1);
				String title = filmResult.getString(2);
				String desc = filmResult.getString(3);
				int releaseYear = filmResult.getShort(4);
				int langId = filmResult.getInt(5);
				int rentDur = filmResult.getInt(6);
				double rate = filmResult.getDouble(7);
				int length = filmResult.getInt(8);
				double repCost = filmResult.getDouble(9);
				String rating = filmResult.getString(10);
				String features = filmResult.getString(11);
				film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
						features);

			}
			filmResult.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	public Actor findActorById(int actorID) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorID);
			ResultSet actorResult = stmt.executeQuery();
			if (actorResult.next()) {
				actor = new Actor(actorResult.getInt(1), actorResult.getString(2), actorResult.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;
	}

	public List<Actor> findActorsByFilmId(int filmID) {
		List<Actor> actors = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "SELECT id, first_name, last_name FROM actor JOIN film_actor "
					+ "ON actor.id = film_actor.actor_id WHERE film_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmID);
			ResultSet filmActors = stmt.executeQuery();
			while (filmActors.next()) {
				int id = filmActors.getInt(1);
				String firstName = filmActors.getString(2);
				String lastName = filmActors.getString(3);
				Actor actor = new Actor(id, firstName, lastName);
				actors.add(actor);
				actor = null;
			}
			filmActors.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}

	public List<Film> findFilmsByKeyword(String keyword) {
		List<Film> films = new ArrayList<>();
		Film film = null;
		keyword = "%" + keyword + "%";
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "SELECT id, title, description, release_year, language_id, rental_duration, "
					+ " rental_rate, length, replacement_cost, rating, special_features "
					+ " FROM film WHERE title LIKE ? OR description LIKE ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, keyword);
			stmt.setString(2, keyword);
			ResultSet filmResult = stmt.executeQuery();
			while (filmResult.next()) {
				int filmId = filmResult.getInt(1);
				String title = filmResult.getString(2);
				String desc = filmResult.getString(3);
				int releaseYear = filmResult.getShort(4);
				int langId = filmResult.getInt(5);
				int rentDur = filmResult.getInt(6);
				double rate = filmResult.getDouble(7);
				int length = filmResult.getInt(8);
				double repCost = filmResult.getDouble(9);
				String rating = filmResult.getString(10);
				String features = filmResult.getString(11);
				film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
						features);
				films.add(film);
			}
			filmResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	public String findLanguage(int filmId) {
		String lang = null;
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			String sql = "SELECT language.name FROM language JOIN film ON language.id = film.language_id WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet langRes = stmt.executeQuery();
			if (langRes.next()) {
				lang = langRes.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lang;
	}

	public void createFilm(Film film) {
		String sql = "INSERT INTO film (title, description, release_year, language_id, rental_duration, rental_rate, "
				+ "length, replacement_cost, rating, special_features) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int result = 0;
		try {
			Connection conn = DriverManager.getConnection(url, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, film.getTitle());
			stmt.setString(2, film.getDescription());
			stmt.setInt(3, film.getReleaseYear());
			stmt.setInt(4, film.getLanguageID());
			stmt.setDouble(5, film.getRentalDuration());
			stmt.setDouble(6, film.getRentalRate());
			stmt.setDouble(7, film.getLength());
			stmt.setDouble(8, film.getReplacementCost());
			stmt.setString(9, film.getRating());
			stmt.setString(10, film.getSpecialFeatures());
			conn.setAutoCommit(false);
			result = stmt.executeUpdate();
			if (result == 1) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					film.setId(rs.getInt(1));
					conn.commit();
				}
				rs.close();
				stmt.close();
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean saveFilm(Film film) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, pass);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "UPDATE film SET title=?, rating=? " + " WHERE id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, film.getTitle());
			stmt.setString(2, film.getRating());
			stmt.setInt(3, film.getId());
			int updateCount = stmt.executeUpdate();
			if (updateCount == 1) {
				// Replace actor's film list
//		      sql = "DELETE FROM film_actor WHERE actor_id = ?";
//		      stmt = conn.prepareStatement(sql);
//		      stmt.setInt(1, film.getId());
//		      updateCount = stmt.executeUpdate();
//		      sql = "INSERT INTO film_actor (film_id, actor_id) VALUES (?,?)";
//		      stmt = conn.prepareStatement(sql);
//		      for (Film film : film.getFilms()) {
//		        stmt.setInt(1, film.getId());
//		        stmt.setInt(2, film.getId());
//		        updateCount = stmt.executeUpdate();
//		      }
				conn.commit(); // COMMIT TRANSACTION
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} // ROLLBACK TRANSACTION ON ERROR
				catch (SQLException sqle2) {
					System.err.println("Error trying to rollback");
				}
			}
			return false;
		}
		return true;
	}

	public boolean deleteFilm(int id) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, pass);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "DELETE FROM film WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			int updateCount = stmt.executeUpdate();
//			sql = "DELETE FROM actor WHERE id = ?";
//			stmt = conn.prepareStatement(sql);
//			stmt.setInt(1, actor.getId());
//			updateCount = stmt.executeUpdate();
			conn.commit(); // COMMIT TRANSACTION
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException sqle2) {
					System.err.println("Error trying to rollback");
				}
			}
			return false;
		}
		return true;
	}

}
