package com.cicdlectures.menuclient.controller;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "menucli", mixinStandardHelpOptions = true, version = "menucli 1.0",
        description = "Interact with menu server")
public class MenuCommandLine implements Callable<Integer> {

  @Option(names = "--server-url", description = "Server called.")
  private String server;

  @Parameters(index = "0", description = "Command to run")
  private String command;

  @Option(names = "-id", description = "Id of the menu")
  private int idMenu;

  public Integer call() throws Exception {
    if (command.equals("list-menus")) {

    } else if (command.equals("delete-menu")) {

    }
    return null;
  }

  public static void main(String... args) {
    int exitCode = new CommandLine(new MenuCommandLine()).execute(args);
    System.exit(exitCode);
  }
}
