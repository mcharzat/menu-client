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
import java.util.concurrent.Callable;

@Command(name = "menucli", mixinStandardHelpOptions = true, version = "menucli 1.0",
        description = "Interact with menu server")
public class MenuCommandLine implements Callable<Integer> {

    @Option(names = "--server-url", description = "Server called.")
    private String server = "https://menusserver.herokuapp.com";

    @Parameters(index = "0", description = "Command to run")
    private String command;

    @Option(names = "-id", description = "Id of the menu to be deleted. Mandatory for command delete_menu")
    private int idMenu;

    @Option(names = "-clear", description = "Delete all menus")
    private boolean clear;

    public void listMenus () throws Exception {
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
        if (this.clear) {
            for (int k = 0; k < array.length(); k++) {
                this.idMenu = array.getJSONObject(k).getInt("id");
                this.deleteMenu();
            }
        } else this.displayList(array);
    }

    public void displayList (JSONArray array) throws Exception {
        if (array.length() != 0) {
            for (int i = 0; i <  array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                System.out.println("~~~~~~~~  " + json.getString("name") + " (id:" + json.getString("id") + ")  ~~~~~~~~");
                JSONArray dishes = new JSONArray(json.getString("dishes"));

                for (int j = 0; j < dishes.length(); j++) {
                    //System.out.println(" - Plat n°" + j);
                    System.out.println(" - " + dishes.getJSONObject(j).getString("name"));
                }

            }
        } else System.out.println("No menus in server.");
    }

    public void createMenu () throws Exception {
        // create a client
        var client = HttpClient.newHttpClient();

        String requestBody = "{\"name\": \"Menu spécial du chef\", \"dishes\": [{\"name\": \"Bananes aux fraises\"},{\"name\": \"Bananes flambées\"}]}";

        // create a request
        var request = HttpRequest.newBuilder(
                        URI.create(this.server + "/menus"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();

        // use the client to send the request
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // the response:
        System.out.println(response.body());
        System.out.println("Creation complete");
    }

    public void deleteMenu () throws Exception {
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
        System.out.println("Delete complete");
    }

    public Integer call() throws Exception {
        if (this.command.equals("list_menus")) {
            this.listMenus();
        } else if (command.equals("delete_menu")) {
            this.deleteMenu();
        } else if (command.equals("create_menu")) {
          this.createMenu();
        }
        return null;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new MenuCommandLine()).execute(args);
        System.exit(exitCode);
    }
}
