package com.skilldistillery.film.controllers;

import java.util.List;

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
	
//	@RequestMapping(path="filmByID.do", method=RequestMethod.GET)
//	public ModelAndView getID() {
//		Film film = new Film();
//		ModelAndView mv = new ModelAndView("WEB-INF/filmDisplay.jsp", "film", film);
//		return mv;
//	}
	
	@RequestMapping(path = "filmByID.do", method = RequestMethod.GET)
	public ModelAndView getFilmById(int id) {
		ModelAndView mv = new ModelAndView();
//		Film film = filmDAO.findFilmById(id);
//		System.out.println(film);
		mv.setViewName("WEB-INF/filmDisplay.jsp");
		mv.addObject("film", filmDAO.findFilmById(id));
		return mv;
	}

//	@RequestMapping(path="filmByKeyword.do", method=RequestMethod.GET)
//	public ModelAndView getKeyword() {
//		Film film = new Film();
//		ModelAndView mv = new ModelAndView("WEB-INF/filmKeywordForm.jsp", "film", film);
//		return mv;
//	}
	
	@RequestMapping(path = "filmByKeyword.do", method = RequestMethod.GET)
	public ModelAndView getFilmByKeyword(String keyword) {
		ModelAndView mv = new ModelAndView();
		List<Film> films = filmDAO.findFilmsByKeyword(keyword);
		mv.setViewName("WEB-INF/filmKeyword.jsp");
		mv.addObject("films", films);
		return mv;
	}

	@RequestMapping(path = "addFilm.do", method = RequestMethod.GET)
	public ModelAndView addFilm(Film film) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("WEB-INF/addFilmForm.jsp");
		mv.addObject("film", new Film());
		return mv;
	}
	
	@RequestMapping(path = "addFilm.do", method = RequestMethod.POST)
	public ModelAndView postFilm(Film film) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("WEB-INF/filmDisplay.jsp");
		System.out.println(film);
		filmDAO.createFilm(film);
		return mv;
	}
	
	@RequestMapping(path = "deleteFilm.do", method = RequestMethod.POST, params="id")
	public ModelAndView deleteFilm(int id) {
		boolean delete = filmDAO.deleteFilm(id);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("WEB-INF/filmDeleted.jsp");
		mv.addObject("delete", delete);
		mv.addObject("filmID", id);
		return mv;
	}
	
}
