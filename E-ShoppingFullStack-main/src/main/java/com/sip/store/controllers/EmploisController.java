package com.sip.store.controllers;

import com.sip.store.entities.Article;
import com.sip.store.entities.Classe;
import com.sip.store.entities.Emplois;
import com.sip.store.entities.Provider;
import com.sip.store.repositories.ClasseRepository;
import com.sip.store.repositories.EmploisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/emplois/")
public class EmploisController {
    @Autowired
    private EmploisRepository emploisRepository;
    @Autowired
    private ClasseRepository classeRepository;

    @PostMapping("add")
    public String addEmplois(@RequestParam(name = "classeId",required = false) Long f,
                             @RequestParam MultipartFile img, HttpSession session,Emplois emplois)
    {
        Classe classe=classeRepository.findById(f)
                .orElseThrow(()-> new IllegalArgumentException("Invalid classe Id:" + f));
        emplois.setClasse(classe);
        emplois.setPdf(img.getOriginalFilename());

        Emplois uploadImg = emploisRepository.save(emplois);

        if (uploadImg != null) {
            try {

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img.getOriginalFilename());
                System.out.println(path);
                Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        session.setAttribute("msg", "Image Upload Sucessfully");
        return "redirect:list";
    }
    @GetMapping("add")
    public String showAddEmploisForm(Model model) {
        model.addAttribute("classe",classeRepository.findAll());
        model.addAttribute("emplois", new Emplois());
        return "emplois/addemplois";
    }

    @GetMapping("list")
    //@ResponseBody
    public String listEmplois(Model model) {

        List<Emplois> lp = (List<Emplois>)emploisRepository.findAll();
        if(lp.size()==0)
            lp=null;
        model.addAttribute("emplois", lp);
        return "emplois/Listemplois";
    }
    @GetMapping("delete/{id}")
    public String deleteEmplois(@PathVariable("id") long id,Model model) {
        Emplois emplois = emploisRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Invalid emplois Id:" + id));
        System.out.println("suite du programme...");
        emploisRepository.delete(emplois);
        model.addAttribute("emplois", emploisRepository.findAll());

        return "emplois/Listemplois";
    }

    @GetMapping("edit/{id}")
    public String showEmploisFormToUpdate(@PathVariable("id") long id, Model model) {
        Emplois emplois = emploisRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Invalid emplois Id:" + id));

        model.addAttribute("emplois", emplois);

        return "emplois/updateemplois";
    }



    @PostMapping("edit")
    public String updateEmplois(@Validated Emplois emplois, BindingResult result, Model model) {
        emploisRepository.save(emplois);
        return "redirect:list";

    }
}