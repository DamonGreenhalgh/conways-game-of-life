# **Conway's Game of Life** 
A fun little application that runs **Conway's Game of Life**.  

![alt text](https://github.com/DamonGreenhalgh/conways-game-of-life-app/blob/main/design/icon.png?raw=true) 

## Rules
How does the game work? There exists a grid where each node on the grid is one of two states, alive or dead. You can manipulate these states
by simply clicking on the node. In the next iteration the node will either live or die depending on the following three rules of the game.  


**1.** Alive nodes **die** due to loneliness if there exists **1 or fewer** neighbours around it.  

| 1 or fewer Neighbours | Next Iteration |
| - | -------------- |
| ![alt text](https://github.com/DamonGreenhalgh/conways-game-of-life-app/blob/main/design/press-2.PNG?raw=true) | ![alt text](https://github.com/DamonGreenhalgh/conways-game-of-life-app/blob/main/design/press-3.PNG?raw=true) |  

**2.** Alive nodes **die** due to overcrowding if there exists **4 or more** neighbours around it.  

| 4 or more Neighbours | Next Iteration |
| -------------------- | -------------- |
| ![alt text](https://github.com/DamonGreenhalgh/conways-game-of-life-app/blob/main/design/press-4.PNG?raw=true) | ![alt text](https://github.com/DamonGreenhalgh/conways-game-of-life-app/blob/main/design/press-5.PNG?raw=true)

**3.** Dead nodes **live** due to repopulation if there exists exactly **3** neighbours around it.  

| 3 Neighbours | Next Iteration |
| ------------ | -------------- |
| ![alt text](https://github.com/DamonGreenhalgh/conways-game-of-life-app/blob/main/design/press-6.PNG?raw=true) | ![alt text](https://github.com/DamonGreenhalgh/conways-game-of-life-app/blob/main/design/press-7.PNG?raw=true) |

## Features
Current features include:

* Basic functionality of the game.
* **Random** generation of cells on the gameboard.
* **Speed** control over each new iteration.

Upcoming features:

* **Themes**
* **Preset Structures**
* **Scaling**

 
## Installation
Open the directory you want to **clone** the repository in and run the following line in **Git Bash**,  

`git clone https://github.com/DamonGreenhalgh/conways-game-of-life`  
 
Then **run** the executable **"Conway's Game of Life.exe"**, and **enjoy!**  

## Requirements
Requires Windows and Java 8 JRE.
