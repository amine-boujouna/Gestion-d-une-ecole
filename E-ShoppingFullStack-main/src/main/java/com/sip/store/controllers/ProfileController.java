package com.sip.store.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sip.store.entities.Profile;
import com.sip.store.entities.Response;
import com.sip.store.exception.ResourceNotFoundException;
import com.sip.store.repositories.ProfileRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api")
public class ProfileController {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    ServletContext context;

    @PostMapping("/profile")
    public ResponseEntity<?> createArticle (@RequestParam("file") MultipartFile file,
                                                  @RequestParam("profile") String profile) throws JsonParseException , JsonMappingException , Exception
    {
        System.out.println("Ok .............");
        Profile profile1 = new ObjectMapper().readValue(profile, Profile.class);
        boolean isExit = new File(context.getRealPath("/Images/")).exists();
        if (!isExit)
        {
            new File (context.getRealPath("/Images/")).mkdir();
            System.out.println("mk dir.............");
        }
        String filename = file.getOriginalFilename();
        String newFileName = FilenameUtils.getBaseName(filename)+"."+FilenameUtils.getExtension(filename);
        File serverFile = new File (context.getRealPath("/Images/"+File.separator+newFileName));
        try
        {
            System.out.println("Image");
            FileUtils.writeByteArrayToFile(serverFile,file.getBytes());

        }catch(Exception e) {
            e.printStackTrace();
        }


        profile1.setFileName(newFileName);
        Profile profile2 = profileRepository.save(profile1);
        if (profile2 != null)
        {
            return new ResponseEntity<Profile>(profile2,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<Response>(new Response ("Profile not saved"),HttpStatus.BAD_REQUEST);
        }
    }


        @PutMapping("/profile/{id}")
    public Profile updateProfile(@PathVariable("id") long id, @RequestBody Profile Profile) {
        Profile profile=null;
        Optional<Profile> profileinfo = profileRepository.findById(id);
        if (profileinfo.isPresent()) {
            profile = profileinfo.get();
            profile.setAddress(Profile.getAddress());
            profile.setDatedenaissance(Profile.getDatedenaissance());
            profile.setEducation(Profile.getEducation());
            profile.setEmail(Profile.getEmail());
            profile.setFirstName(Profile.getFirstName());
            profile.setLastName(Profile.getLastName());
            profile.setExperience(Profile.getExperience());
            profile.setMobile(Profile.getMobile());
            profile.setGenre(Profile.getGenre());
            profileRepository.save(profile);
        }
            if(profile == null) throw new IllegalArgumentException("profile with id = "+ id +"not Found");
            return profile;
    }



    @GetMapping("/profile/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable(value = "id") Long Id)
            throws ResourceNotFoundException {
        Profile profile = profileRepository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Profrile not found for this id :: " + Id));
        return ResponseEntity.ok().body(profile);
    }
    @GetMapping(path="/imgprofile/{id}")
    public byte[] getPhoto(@PathVariable("id") Long id) throws Exception{
        Profile profile   = profileRepository.findById(id).get();
        return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+profile.getFileName()));
    }



}
