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
    private int idMenu = -1;

    @Option(names = {"-clear", "-all"}, description = "Delete all menus")
    private boolean clear;

    @Option(names = "-menu", description = "Menu to save")
    private String menu = "{\"name\": \"Menu spécial du chef\", \"dishes\": [{\"name\": \"Bananes aux fraises\"},{\"name\": \"Bananes flambées\"}]}";

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
                this.displayMenu(json);
            }
        } else System.out.println("No menus in server.");
    }

    public void displayMenu (JSONObject menu) throws Exception {
        int len = menu.getString("name").length() + menu.getString("id").length() + 26;

        System.out.println("~".repeat(len));
        System.out.println("~~~~~~~~  " + menu.getString("name") + " (id:" + menu.getString("id") + ")  ~~~~~~~~");
        System.out.println("~".repeat(len));
        JSONArray dishes = new JSONArray(menu.getString("dishes"));

        for (int j = 0; j < dishes.length(); j++) {
            int len2 = dishes.getJSONObject(j).getString("name").length() + 11;
            len2 = len - len2;
            System.out.println("# # # "+dishes.getJSONObject(j).getString("name") + " ".repeat(len2) + "# # #");
        }
        System.out.println("~".repeat(len));
        System.out.println("~".repeat(len));
        System.out.println();       // saut de ligne entre deux menus
    }

    public void createMenu () throws Exception {
        // create a client
        var client = HttpClient.newHttpClient();

        // create a request
        var request = HttpRequest.newBuilder(
                        URI.create(this.server + "/menus"))
                .POST(HttpRequest.BodyPublishers.ofString(this.menu))
                .header("Content-type", "application/json")
                .build();

        // use the client to send the request
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // the response:
        JSONObject json = new JSONObject(response.body());
        this.displayMenu(json);
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
        if (response.body().isEmpty())  System.out.println("Delete complete");
        else System.out.println("Delete unsuccessful");
    }

    public Integer call() throws Exception {
        if (this.command.equals("list_menus")) {
            this.listMenus();
        } else if (command.equals("delete_menu") && this.clear) {
            this.listMenus();
        } else if (command.equals("delete_menu") && this.idMenu != -1) {
            this.deleteMenu();
        } else if (command.equals("delete_menu")) {
            System.out.println("Delete command need the -id option to be set up.");
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
