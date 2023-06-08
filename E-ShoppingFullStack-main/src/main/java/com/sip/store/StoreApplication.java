package com.sip.store;

import com.sip.store.controllers.ProviderController;
import com.sip.store.entities.Provider;
import com.sip.store.repositories.ProviderRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.sip.store.services.FileStorageService;

import javax.annotation.Resource;


@SpringBootApplication
public class StoreApplication {
	@Resource
	FileStorageService fileStorageService;
	public static void main(String[] args) {

		SpringApplication.run(StoreApplication.class, args);
	}
	/*@Override
	public void run(String... arg) throws Exception {
		fileStorageService.init();
	}

	 */

}
