package com.cicdlectures.menuclient.controller;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "menucli", mixinStandardHelpOptions = true, version = "menucli 1.0",
        description = "Interact with menu server")
public class MenuCommandLine implements Callable<Integer> {

  public Integer call() throws Exception {
    return null;
  }

  public static void main(String... args) {
    int exitCode = new CommandLine(new MenuCommandLine()).execute(args);
    System.exit(exitCode);
  }
}
