![Codex Naturalis Logo](src/main/resources/it/polimi/ingsfw/ingsfwproject/Images/logoTagliato.png)

# Codex Naturalis Java Multiplayer

Java multiplayer implementation of the board game **Codex Naturalis**, developed as a distributed client-server application using the **MVC architectural pattern**.

The system allows **2–4 players** to participate in online matches simultaneously and supports both **CLI and GUI clients**, as well as multiple networking technologies (**RMI and TCP sockets**).

This project was developed as part of the **Software Engineering course** at Politecnico di Milano.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Documentation](#documentation)
- [Execution](#execution)
- [System Requirements](#system-requirements)
- [Authors](#authors)

---

## Overview

The project is a software implementation of the board game **Codex Naturalis**.

Players build their own manuscript by strategically placing cards and earning points based on specific patterns and objectives.

To support multiplayer gameplay, the system was designed as a **distributed architecture**, where a central server coordinates multiple clients connected over the network.

The server can manage **multiple matches concurrently**, allowing players to create new games or join existing ones.

---

## Features

The implementation includes all core features required by the project specifications:

- Complete implementation of the **game rules**
- **TUI (Text User Interface)**
- **GUI (Graphical User Interface)**
- **RMI communication**
- **TCP Socket communication**

Additional features implemented:

### Multiple Games
The server can manage multiple games simultaneously.  
Players can choose an available game lobby or create a new match.

### Chat System
Players can communicate during the match through a built-in chat system supporting:

- global chat
- private messages

---

## Architecture

The system follows a **distributed client-server architecture** structured according to the **MVC (Model-View-Controller) pattern**.

### Model
Handles:
- game state
- game rules
- score computation
- card placement logic

### View
Two different user interfaces are supported:

- **TUI** (command line interface)
- **GUI** (graphical interface)

### Controller
Manages:

- player input
- client-server communication
- synchronization between view and model

---

## Networking

Communication between client and server can occur through two different technologies:

- **Java RMI**
- **TCP Sockets**

This dual implementation allows the system to support different networking paradigms while maintaining the same game logic.

---

## Technologies

Main technologies used in the project:

- **Java 21**
- **Client-Server Architecture**
- **MVC Pattern**
- **Java RMI**
- **TCP Sockets**
- **Concurrent Server Handling Multiple Matches**

---

## Documentation

### UML

- [High Level UML](deliverables/UML/UML_Project.pdf)
- [Detailed UML](deliverables/UML/detailed)

### JavaDoc

The JavaDoc documentation includes descriptions of the main classes and methods of the system.

Available here:

deliverables/javadoc

---

## Execution

### Starting the Server

Run the following command:

```bash
java -jar IngSfwProject-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Default configuration:

- **Socket Port:** 1337
- **RMI Port:** 1099

---

### Starting the Client

Run the same jar file:

```bash
java -jar IngSfwProject-1.0-SNAPSHOT-jar-with-dependencies.jar
```

The user can then select the preferred interface:

- CLI
- GUI

---

## System Requirements

Java **21 or higher** is required.

---

### CLI Interface

#### Emoji visualization in Windows PowerShell

1. Press `Win + R`
2. Type `intl.cpl`
3. Open the **Administrative** tab
4. Click **Change system locale**
5. Enable **Beta: Use Unicode UTF-8 for worldwide language support**

More information:

https://stackoverflow.com/questions/57131654/using-utf-8-encoding-chcp-65001-in-command-prompt-windows-powershell-window

---

#### Grid Rendering

If the grid appears too large:

**macOS**

Command + -

**Windows**

Ctrl + -

Repeat until the grid renders correctly.

---

### GUI Interface

A display resolution higher than **1440x900** is recommended.

---

## Authors

Project developed for the **Software Engineering course** at Politecnico di Milano.

- Michelangelo Spandri
- Beatrice Spazzadeschi
- Jacopo Valentini
- Alessandro Zanoni