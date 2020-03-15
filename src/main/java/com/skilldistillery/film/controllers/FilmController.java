package com.skilldistillery.film.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.skilldistillery.film.dao.FilmDAOJDBCImpl;
import com.skilldistillery.film.entities.Film;

@Controller
public class FilmController {

	@Autowired
	private FilmDAOJDBCImpl filmDAO;
	
	@RequestMapping(path = "GetFilmId.do", method = RequestMethod.GET)
	public ModelAndView getFilmById(@RequestParam("id") int filmId) {
		ModelAndView mv = new ModelAndView();
		Film film = filmDAO.findFilmById(filmId);
		mv.addObject("film", film);
		System.out.println(film);
		mv.setViewName("WEB-INF/filmId.jsp");
		return mv;
	}
	
}
