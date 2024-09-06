package com.library;

import com.library.business.Library;
import com.library.presentation.ConsoleUI;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        ConsoleUI consoleUI = new ConsoleUI(library);
        consoleUI.start();
  }
}