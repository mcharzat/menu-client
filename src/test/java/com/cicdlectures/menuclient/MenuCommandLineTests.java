package com.cicdlectures.menuclient;

import com.cicdlectures.menuclient.controller.MenuCommandLine;

import org.junit.jupiter.api.*;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MenuCommandLineTests {

    CommandLine cmd;

    final PrintStream originalOut = System.out;
    final PrintStream originalErr = System.err;
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ByteArrayOutputStream err = new ByteArrayOutputStream();

    @BeforeAll
    public void init() {
        MenuCommandLine app = new MenuCommandLine();
        this.cmd = new CommandLine(app);
    }

    @BeforeEach
    public void setUpStreams() {
        out.reset();
        err.reset();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    @Order(1)
    @DisplayName("create and save a menu")
    public void createNewMenu() {
        int exitCode = cmd.execute("create_menu");
        assertEquals(0, exitCode);
        //assertEquals("{\"id\":1,\"name\":\"Menu spécial du " +
        //        "chef\",\"dishes\":[{\"id\":1,\"name\":\"Bananes aux " +
        //        "fraises\"},{\"id\":2,\"name\":\"Bananes flambées\"}]}" +
        //        "\nCreation complete\n", out.toString());
    }

    @Test
    @Order(2)
    @DisplayName("get all menus")
    public void getMenus() {
        int exitCode = cmd.execute("list_menus");
        assertEquals(0, exitCode);
        //assertEquals("[{\"id\":1,\"name\":\"Menu spécial du " +
        //        "chef\",\"dishes\":[{\"id\":1,\"name\":\"Bananes aux " +
        //        "fraises\"},{\"id\":2,\"name\":\"Bananes flambées\"}]}]" +
        //        "\nMenu spécial du chef\n", out.toString());
    }

    @Test
    @Order(3)
    @DisplayName("delete a menu")
    public void deleteMenu() {
        int exitCode = cmd.execute("delete_menu", "-id=1");
        assertEquals(0, exitCode);
        //assertEquals("\nDelete complete\n", out.toString());
    }

    @Test
    @Order(4)
    @DisplayName("get all menus with custom url")
    public void getMenusUrl() {
        int exitCode = cmd.execute("--server-url=https://menusserver.herokuapp.com", "list_menus");
        assertEquals(0, exitCode);
        //assertEquals("[]\nNo menus in server.\n", out.toString());
    }
}
