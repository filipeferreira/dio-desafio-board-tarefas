package com.dio.bootcampbradesco2025.board;

import com.dio.bootcampbradesco2025.board.ui.MainMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@SpringBootApplication
public class BoardApplication implements CommandLineRunner {

	@Autowired
	private MainMenu mainMenu;

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mainMenu.execute();
	}
}
