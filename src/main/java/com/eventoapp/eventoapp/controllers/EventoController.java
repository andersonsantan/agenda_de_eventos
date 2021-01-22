package com.eventoapp.eventoapp.controllers;

import com.eventoapp.eventoapp.models.Convidado;
import com.eventoapp.eventoapp.models.Evento;
import com.eventoapp.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.eventoapp.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;



@Controller
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private ConvidadoRepository convidadoRepository;

    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)
    public String form (){
        return"evento/formevento";
    }

    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
    public String form (Evento evento){
        eventoRepository.save(evento);
        return"redirect:/cadastrarEvento";
    }

    @RequestMapping(value = "/eventos")
    public ModelAndView listaEventos(){
        ModelAndView modelAndView = new ModelAndView("index");
        Iterable<Evento> eventos = eventoRepository.findAll();
        modelAndView.addObject("eventos", eventos);
        return modelAndView;

    }
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public ModelAndView detalhesEvento(@PathVariable("id") long id){
        Evento evento = eventoRepository.findById(id);
        ModelAndView modelAndView = new ModelAndView("evento/detalhesEvento");
        modelAndView.addObject("evento", evento);

        Iterable<Convidado> convidados = convidadoRepository.findByEvento(evento);

        modelAndView.addObject("convidados", convidados);

        return modelAndView;
    }
    @PostMapping
    @RequestMapping(value="/{id}", method=RequestMethod.POST)
    public String detalhesEventoPost(@PathVariable("id") long id, @Valid Convidado convidado, BindingResult bindingResult, RedirectAttributes attributes){
        System.out.println(bindingResult);
        if (bindingResult.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/{id}";
        }

        Evento evento = eventoRepository.findById(id);
        convidado.setEvento(evento);
        convidadoRepository.save(convidado);
        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
        return "redirect:/{id}";


    }


}
