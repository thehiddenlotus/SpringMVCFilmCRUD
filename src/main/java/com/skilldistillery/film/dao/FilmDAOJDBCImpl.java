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

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";

	@Override
	public Film findFilmById(int filmID) {
		Film film = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
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
			Connection conn = DriverManager.getConnection(URL, user, pass);
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
			Connection conn = DriverManager.getConnection(URL, user, pass);
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
			Connection conn = DriverManager.getConnection(URL, user, pass);
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
			Connection conn = DriverManager.getConnection(URL, user, pass);
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

	public Film createFilm(Film film) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "INSERT INTO (film id, title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features )"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, film.getId());
			stmt.setString(2, film.getTitle());
			stmt.setString(3, film.getDescription());
			stmt.setInt(4, film.getReleaseYear());
			stmt.setInt(5, film.getLanguageID());
			stmt.setInt(6, film.getRentalDuration());
			stmt.setDouble(7, film.getRentalRate());
			stmt.setInt(8, film.getLength());
			stmt.setDouble(9, film.getReplacementCost());
			stmt.setString(10, film.getRating());
			stmt.setString(11, film.getSpecialFeatures());
			int updateCount = stmt.executeUpdate();
			if (updateCount == 1) {
				ResultSet keys = stmt.getGeneratedKeys();
				if (keys.next()) {
					int newFilmId = keys.getInt(1);
					film.setId(newFilmId);
					if (film.getActors() != null && film.getActors().size() > 0) {
						sql = "INSERT INTO film_actor (film_id, actor_id) VALUES (?,?)";
						stmt = conn.prepareStatement(sql);
						for (Actor actor : film.getActors()) {
							stmt.setInt(1, actor.getId());
							stmt.setInt(2, newFilmId);
							updateCount = stmt.executeUpdate();
						}
					}
				}
			} else {
				film = null;
			}
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
			throw new RuntimeException("Error inserting film " + film);
		}
		return film;
	}

	public boolean saveFilm(Film film) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
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

	public boolean deleteFilm(Film film) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "DELETE FROM film WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, film.getId());
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
		}S
		return true;
	}

}
