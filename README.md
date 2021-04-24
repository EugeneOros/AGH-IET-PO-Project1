# Game Of Life

### Description
It is a simulator designed to present the simple behavior of the environment. Animals moving around the map try to survive by eating grass that "grows" cyclically, and statistics on the entire simulation, such as the number of animal populations, are also collected.

Each day simulation consists of several successive actions
* removing dead animals from the map,
* rotation and moving of each animal,
* food (the plant is eaten by the animal with the most energy, or by several of the strongest animals if more than one has the same, highest energy; in this case, the energy of the plant is shared),
* animal breeding (the two animals with the highest energy in a given field always breed; if there are more animals with the same energy, the choice is random),
* adding new plants to the map.

Of course, at the beginning of the simulation, we place one or more animals in the middle of the world (Adam / Eve)

### How to run?
The class that starts the program is called "World"

### Technologies
Java 13 + Swing
