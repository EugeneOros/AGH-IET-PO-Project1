package project1;

import java.util.*;

public class Simulation {
    private GrassField map;

    public Simulation(GrassField map) {
        this.map = map;
    }

    private boolean isEmptyPlace(Vector2d position) {
        MapDirection direction = MapDirection.NORTH;
        for (int i = 0; i < 8; i++) {
            if (!(this.map.objectsAt(position.add(direction.toUnitVector())) instanceof Animal)) return true;
            else {
                direction = direction.next();
            }
        }
        return false;
    }

    private void reproduction(Animal parent1, Animal parent2) {
        int childEnergy = (parent1.getEnergy() + parent2.getEnergy()) / 2;
        parent1.subtractEnergy((parent1.getEnergy()) / 4);
        parent2.subtractEnergy((parent2.getEnergy()) / 4);
        parent1.oneChildrenMore();
        parent2.oneChildrenMore();
        Vector2d childPosition;
        if (isEmptyPlace(parent1.getPosition())) {
            do {
                childPosition = parent1.getPosition().add(MapDirection.getRandom().toUnitVector());
            } while (this.map.objectsAt(childPosition) instanceof Animal);
        } else {
            childPosition = parent1.getPosition().add(MapDirection.getRandom().toUnitVector());
        }
        Animal child = new Animal(this.map, childPosition, childEnergy, parent1.getGenes(), parent2.getGenes());
        this.map.place(child);
    }


    public void reproductionAll() {
        LinkedHashMap<Vector2d, List<Animal>> copyMap = new LinkedHashMap<>();
        for (Map.Entry<Vector2d, List<Animal>> animals : map.getAnimalsMap().entrySet()) {
            copyMap.put(animals.getKey(), animals.getValue());
        }
        for (Map.Entry<Vector2d, List<Animal>> animals : copyMap.entrySet()) {
            if (animals.getValue().size() > 1) {
                Iterator iter1 = animals.getValue().iterator();
                Animal maxOne = (Animal) iter1.next();
                Animal maxTwo = maxOne;
                while (iter1.hasNext()) {
                    Animal toCompare = (Animal) iter1.next();
                    if (maxOne.getEnergy() < toCompare.getEnergy()) {
                        maxTwo = maxOne;
                        maxOne = toCompare;
                    } else if (maxTwo.getEnergy() < toCompare.getEnergy()) {
                        maxTwo = toCompare;
                    }
                }
                if (maxOne.getEnergy() >= this.map.getMapParameters().getStartEnergy() / 2 && maxTwo.getEnergy() >= this.map.getMapParameters().getStartEnergy() / 2) {
                    reproduction(maxOne, maxTwo);
                }
            }
        }
    }

    public void eatGrass() {
        for (Map.Entry<Vector2d, List<Animal>> animals : map.getAnimalsMap().entrySet()) {
            if (this.map.isOccupiedByGrass(animals.getKey())) {
                Grass toDelete = this.map.grassAt(animals.getKey());
                if (toDelete.getPosition().follows(this.map.getMapParameters().getLowerLeftJungle()) && toDelete.getPosition().precedes(this.map.getMapParameters().getUpperRightJungle())) {
                    this.map.getMapParameters().subtractTotalGrassOnJungle(1);
                } else {
                    this.map.getMapParameters().subtractTotalGrassOnSteppe(1);
                }
                this.map.getVisualizer().removeFromGrassPane(toDelete);
                this.map.getGrasses().remove(toDelete);
                Iterator iter = animals.getValue().iterator();
                Animal maxEnergyAnimal = (Animal) iter.next();
                while (iter.hasNext()) {
                    Animal toCompare = (Animal) iter.next();
                    if (toCompare.getEnergy() > maxEnergyAnimal.getEnergy()) {
                        maxEnergyAnimal = toCompare;
                    }
                }
                int countWIthSameEnergy = 0;
                iter = animals.getValue().iterator();
                while (iter.hasNext()) {
                    Animal toCompare = (Animal) iter.next();
                    if (toCompare.getEnergy() == maxEnergyAnimal.getEnergy()) {
                        countWIthSameEnergy++;
                    }
                }

                iter = animals.getValue().iterator();
                while (iter.hasNext()) {
                    Animal toCompare = (Animal) iter.next();
                    if (toCompare.getEnergy() == maxEnergyAnimal.getEnergy()) {
                        toCompare.addEnergy(this.map.getMapParameters().getPlantEnergy() / countWIthSameEnergy);
                    }
                }

            }
        }
    }

    public void moveAll() {
        for (Animal animal : map.getAnimalsList()) {
            animal.changeOrientation(animal.generateMove());
            animal.move();
            animal.subtractEnergy(this.map.getMapParameters().getMoveEnergy());
        }
    }

    public void removeDead() {
        List<Animal> toDelFromList = new ArrayList<>();
        for (Animal animal : map.getAnimalsList()) {
            if (animal != null) {
                if (animal.getEnergy() <= 0) {
                    if (map.getAnimalsMap().get(animal.getPosition()).size() > 1) {
                        map.getAnimalsMap().get(animal.getPosition()).remove(animal);
                        toDelFromList.add(animal);
                    } else {
                        map.getAnimalsMap().remove((animal).getPosition());
                        toDelFromList.add(animal);
                    }
                }
            }
        }
        for (Animal animal : toDelFromList) {
            animal.removeObserver(this.map);
            this.map.getVisualizer().removeFromAnimalPane(animal);
            map.getAnimalsList().remove(animal);
        }
    }


}
