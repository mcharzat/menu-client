package com.cicdlectures.menuclient.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(name = "menucli", mixinStandardHelpOptions = true, version = "menucli 1.0",
        description = "Interact with menu server")
public class MenuCommandLine implements Callable<Integer> {

  @Option(names = "--server-url", description = "Server called.")
  private String server = "https://menusserver.herokuapp.com";

  @Parameters(index = "0", description = "Command to run")
  private String command;

  @Option(names = "-id", description = "Id of the menu")
  private int idMenu;

  public Integer call() throws Exception {
    System.out.println("Test");
    if (this.command.equals("list_menus")) {
      // create a client
      var client = HttpClient.newHttpClient();

      // create a request
      var request = HttpRequest.newBuilder(
                      URI.create(this.server + "/menus"))
              .GET()
              .header("accept", "application/json")
              .build();

      // use the client to send the request
      var response = client.send(request, HttpResponse.BodyHandlers.ofString());

      // the response:
      System.out.println(response.body());
      JSONArray array = new JSONArray(response.body());
      JSONObject json = array.getJSONObject(0);
      System.out.println(json.getString("name"));
    } else if (command.equals("delete_menu")) {
      // create a client
      var client = HttpClient.newHttpClient();

      // create a request
      var request = HttpRequest.newBuilder(
                      URI.create(this.server + "/menus/" + this.idMenu))
              .DELETE()
              .build();

      // use the client to send the request
      var response = client.send(request, HttpResponse.BodyHandlers.ofString());

      // the response:
      System.out.println(response.body());
    }
    return null;
  }

  public static void main(String... args) {
    int exitCode = new CommandLine(new MenuCommandLine()).execute(args);
    System.exit(exitCode);
  }
}
