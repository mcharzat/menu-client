# menu-client
Command line client to interact with menu-server

## Install
Download the artifact in the release section.

In the file `~/bashrc` add the line:
`alias menucli='java -cp "/path/to/menu-client.jar" com.cicdlectures.menuclient.controller.MenuCommandLine'`

## Run
In a console, run `menucli` with the different parameters. 

### Create a menu

`menucli create_menu -menu "menu"`

### Get the menus

`menucli list_menus`

### Delete a menu

`menucli delete_menu -id=1`

### Clear the database

`menucli list_menus -clear`

`menucli delete_menu -all`

### Use a custom url

`menucli --server-url="https://monserver.com" list_menus`

## Developer tools

A JRE version 11 is needed.

### Compile

`mvn clean compile`

### Run tests

`mvn clean verify`

### Package

`mvn clean compile assembly:single`
