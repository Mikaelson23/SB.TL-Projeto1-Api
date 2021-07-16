package br.com.mikaelson.regestweb.controllers;
import br.com.mikaelson.regestweb.dto.RequisicaoFormProfessor;
import br.com.mikaelson.regestweb.models.Professor;
import br.com.mikaelson.regestweb.models.StatusProfessor;
import br.com.mikaelson.regestweb.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/professores")
public class ProfessorController {
    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping("")
    public ModelAndView index() {
//      Professor prof = new Professor("batman", new BigDecimal("5000.0"), StatusProfessor.ATIVO);
//      Professor prof2 = new Professor("coringa", new BigDecimal("5000.0"), StatusProfessor.ATIVO);

        List<Professor> profes = this.professorRepository.findAll();
        ModelAndView mv = new ModelAndView("professores/index");
        mv.addObject("professores", profes);
        return mv;
    }

    @GetMapping("/new")
    public ModelAndView nnew(RequisicaoFormProfessor professor) {
        ModelAndView mv = new ModelAndView("/professores/new");
        mv.addObject("listaStatusProfessor", StatusProfessor.values());
        return mv;
    }

    @PostMapping("")
    public ModelAndView create(@Valid RequisicaoFormProfessor professor, BindingResult bindingResult) {
        System.out.println(professor);
        if (bindingResult.hasErrors()) {
            System.out.println("Incorrect Validation*****\n\n\n");
            ModelAndView mv = new ModelAndView("professores/new");
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            return mv;
        } else {
            System.out.println(professor.toString() + "Correct Validation*****\n\n\n" + bindingResult.toString());
            Professor novoProfessor = professor.toProfessor();
            this.professorRepository.save(novoProfessor);
            return new ModelAndView("redirect:/professores/" + novoProfessor.getId());
        }
    }

    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable Long id) {
        Optional<Professor> optionalProfessor = this.professorRepository.findById(id);
        if (optionalProfessor.isPresent()) {
            Professor professor = optionalProfessor.get();
            ModelAndView mv = new ModelAndView("professores/show");
            mv.addObject("professor", professor);
            return mv;
        } else {
            return new ModelAndView("redirect:/professores");
        }

    }

    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, RequisicaoFormProfessor requisicao) {
        Optional<Professor> optionalProfessor = this.professorRepository.findById(id);
        if (optionalProfessor.isPresent()) {
            Professor prof = optionalProfessor.get();
            requisicao.fromProfessor(prof);

            ModelAndView mv = new ModelAndView("professores/edit");
            mv.addObject("professorId", prof.getId());
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            return mv;
        } else {
            return new ModelAndView("redirect:/professores");
        }
    }

    @PostMapping("/{id}")
    public ModelAndView update(@PathVariable Long id, RequisicaoFormProfessor requisicao, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            ModelAndView mv = new ModelAndView("professores/edit");
            mv.addObject("professorId", id);
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            return mv;
        }else{
            Optional<Professor> optionalProfessor = this.professorRepository.findById(id);
            if(optionalProfessor.isPresent()){
                Professor professor = requisicao.toProfessor(optionalProfessor.get());
                this.professorRepository.save(professor);
                return new ModelAndView("redirect:/professores/" + professor.getId());
            }else{
                return new ModelAndView("redirect:/professores");
            }
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id){
        try{
            this.professorRepository.deleteById(id);
            return "redirect:/professores";
        }catch (EmptyResultDataAccessException e){
            return "redirect:/professores";
        }
    }
}
