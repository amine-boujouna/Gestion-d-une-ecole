package com.sip.store.controllers;

import com.sip.store.entities.Article;
import com.sip.store.entities.Classe;
import com.sip.store.entities.Emplois;
import com.sip.store.entities.Provider;
import com.sip.store.repositories.ClasseRepository;
import com.sip.store.repositories.EmploisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/emplois/")
public class EmploisController {
    @Autowired
    private EmploisRepository emploisRepository;
    @Autowired
    private ClasseRepository classeRepository;
    public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";

    @PostMapping("add")
    public String addEmplois( Emplois emplois, BindingResult result, @RequestParam(name = "classeId",required = false) Long f,
                              @RequestParam("files") MultipartFile[] files)
    {
        Classe classe=classeRepository.findById(f)
                .orElseThrow(()-> new IllegalArgumentException("Invalid classe Id:" + f));
        emplois.setClasse(classe);
        StringBuilder fileName = new StringBuilder();
        MultipartFile file = files[0];
        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
        fileName.append(file.getOriginalFilename());
        try {
            Files.write(fileNameAndPath,file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        emplois.setPdf(fileName.toString());
        emploisRepository.save(emplois);
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
        return"redirect:list";

    }
    @GetMapping("show/{id}")
    public String showEmplois(@PathVariable("id") long id, Model model) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classe Id:" + id));
        List<Emplois> emplois = emploisRepository.findEmploisByClasse(id);
        for (Emplois a : emplois)
            System.out.println("Emplois = " + a.getPdf());

        model.addAttribute("emplois", emplois);
        model.addAttribute("classe", classe);
        return "emplois/showEmplois";
    }
}

