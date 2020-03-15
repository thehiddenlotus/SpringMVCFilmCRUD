package com.skilldistillery.film.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

import com.skilldistillery.film.entities.Film;

@Component
public class FilmDAOJDBCImpl implements FilmDAO {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	String user = "student";
	String pass = "student";
	private static Connection conn;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}public Film createActor(Film film) {
		  Connection conn = null;
		  try {
		    conn = DriverManager.getConnection(URL, user, pass);
		    conn.setAutoCommit(false); // START TRANSACTION
		    String sql = "INSERT INTO actor (first_name, last_name) "
		                     + " VALUE (?)";
		    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		    stmt.setInt(1, film.getId());
		    
		    int updateCount = stmt.executeUpdate();
		    if (updateCount == 1) {
		      ResultSet keys = stmt.getGeneratedKeys();
		      if (keys.next()) {
		        int newFilmId = keys.getInt(1);
				film.setId(newFilmId );
		        if (film.getFilms() != null && film.getFilms().size() > 0) {
		          sql = "INSERT INTO film_actor (film_id, actor_id) VALUES (?,?)";
		          stmt = conn.prepareStatement(sql);
		          for (Film films : film.getFilms()) {
		            stmt.setInt(1, film.getId());
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
		      try { conn.rollback(); }
		      catch (SQLException sqle2) {
		        System.err.println("Error trying to rollback");
		      }
		    }
		    throw new RuntimeException("Error inserting film " + film);
		  }
		  return film;
		}

	@Override
	public Film findFilmById(int filmId) {
		if (filmId <= 0) {
			return null;
		}
		Film film = null;
		String sql = "SELECT film.* FROM film WHERE film.id = ?";

		try {
			conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet filmResult = stmt.executeQuery();

			if (filmResult.next()) {
				film = new Film();

				film.setId(filmResult.getInt("id"));
				film.setTitle(filmResult.getString("title"));
				film.setDescription(filmResult.getString("description"));
				film.setReleaseYear(filmResult.getInt("release_year"));
				film.setLanguageId(filmResult.getString("language_id"));
				film.setRentalDuration(filmResult.getInt("rental_duration"));
				film.setRentalRate(filmResult.getDouble("rental_rate"));
				film.setLength(filmResult.getInt("length"));
				film.setReplacementCost(filmResult.getDouble("replacement_cost"));
				film.setRating(filmResult.getString("rating"));
				film.setSpecialFeatures(filmResult.getString("special_features"));

			}
			filmResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
		
		

	}
}
