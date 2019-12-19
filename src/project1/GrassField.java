package project1;

import javafx.scene.layout.Pane;

import java.util.*;


public class GrassField extends AbstractWorldMap {

    /**
     * ------------------------------------------------------ATTRIBUTES--------------------------------------------------------------
     */
    private List<Grass> grasses = new ArrayList<>();
    private int moveEnergy;
    private int plantEnergy;
    public Vector2d upperRightJungle;
    public Vector2d lowerLeftJungle;
    private int minReproductionEnergy;
    private int startEnergy;
    private int startNumOfAnimal;
    private int totalGrassOnJungle = 0;
    private int totalGrassOnSteppe = 0;
    private Age age = new Age(0, 0, 0);
    public Pane grassVisualMap = new Pane();
    public Pane animalVisualMap = new Pane();

    /**
     * ------------------------------------------------------CONSTRUCTORS--------------------------------------------------------------
     */
    public GrassField(AllParameters parameters) {
        readJsonFile(parameters);
        this.minReproductionEnergy = this.startEnergy / 2;
        int num1 = ((upperRight.x * upperRight.y) - ((upperRightJungle.x - lowerLeftJungle.x) * (upperRightJungle.y - lowerLeftJungle.y))) / 4;
        int num2 = ((upperRightJungle.x - lowerLeftJungle.x) * (upperRightJungle.y - lowerLeftJungle.y)) * 3 / 4;
        generateGrass(num1, num2);
        for (int i = 0; i < this.startNumOfAnimal; i++) {
            int randX = (int) ((Math.random() * (upperRightJungle.x - lowerLeftJungle.x)) + lowerLeftJungle.x);
            int randY = (int) ((Math.random() * (upperRightJungle.y - lowerLeftJungle.y)) + lowerLeftJungle.y);
            this.place(new Animal(this, new Vector2d(randX, randY)));
        }

    }


    /**
     * ------------------------------------------------------METHODS---------------------------------------------------------------------
     */

    @Override
    public int getStartEnergy() {
        return this.startEnergy;
    }

    @Override
    public double getHeight() {
        return ((this.upperRight.y + 1) * this.getCellSize());
    }

    public double getWidth() {
        return ((this.upperRight.x + 1) * this.getCellSize());
    }

    public double getCellSize() {
        double cellSize;
        int visualMaxSize = 1000;
        if (this.upperRight.x > this.upperRight.y) {
            cellSize = visualMaxSize / upperRight.x;
        } else {
            cellSize = visualMaxSize / upperRight.y;
        }
        return cellSize;
    }

    public List<Grass> getGrasses() {
        return grasses;
    }

    public Age getAge() {
        return age;
    }

    /**
     * ------------------------FOR GRASS-----------------------
     **/
    private void generateGrass(int numOfGrassSteppe, int numOfGrassJungle) {
        int i = 0;
        Vector2d upper = this.upperRightJungle;
        Vector2d lower = this.lowerLeftJungle;
        while (i < numOfGrassJungle) {
            if (!canPlaceGrassJungle()) break;
            Vector2d position = new Vector2d((int) (Math.random() * (upper.x - lower.x + 1)) + lower.x, (int) (Math.random() * (upper.y - lower.y + 1)) + lower.y);
            if (!isOccupied(position)) {
                i++;
                Grass toAdd = new Grass(position, this);
                grasses.add(toAdd);
                grassVisualMap.getChildren().add(toAdd);
                totalGrassOnJungle++;
            }
        }
        upper = this.upperRight;
        i = 0;
        while (i < numOfGrassSteppe) {
            if (!canPlaceGrassSteppe()) break;
            Vector2d position = new Vector2d((int) (Math.random() * (upper.x + 1)), (int) (Math.random() * (upper.y + 1)));
            if (!isOccupied(position) && !(position.precedes(upperRightJungle) && position.follows(lowerLeftJungle))) {
                i++;
                Grass toAdd = new Grass(position, this);
                grasses.add(toAdd);
                grassVisualMap.getChildren().add(toAdd);
                totalGrassOnSteppe++;
            }
        }
    }


    private boolean isOccupiedByGrass(Vector2d position) {
        return this.grassAt(position) != null;
    }

    private Grass grassAt(Vector2d position) {
        for (Grass grass : grasses) {
            if (grass.getPosition().equals(position)) {
                return grass;
            }
        }
        return null;
    }

    private boolean canPlaceGrassJungle() {
        int animalOnJungleCount = 0;
        for (Map.Entry<Vector2d, List<Animal>> animals : this.animalsMap.entrySet()) {
            if (animals.getKey().follows(this.lowerLeftJungle) && animals.getKey().precedes(this.upperRightJungle)) {
                animalOnJungleCount++;
            }
        }
        return totalGrassOnJungle < ((upperRightJungle.x - lowerLeftJungle.x + 1) * (upperRightJungle.y - lowerLeftJungle.y + 1)) - animalOnJungleCount;
    }

    private boolean canPlaceGrassSteppe() {
        int animalOnSteppeCount = 0;
        for (Map.Entry<Vector2d, List<Animal>> animals : this.animalsMap.entrySet()) {
            if (!(animals.getKey().follows(this.lowerLeftJungle) && animals.getKey().precedes(this.upperRightJungle))) {
                animalOnSteppeCount++;
            }
        }
        return totalGrassOnSteppe < (((this.upperRight.x + 1) * (this.upperRight.y + 1)) - ((upperRightJungle.x - lowerLeftJungle.x + 1) * (upperRightJungle.y - lowerLeftJungle.y + 1))) - animalOnSteppeCount;
    }

    /**
     * ------------------------FOR ANIMAL-----------------------
     **/
    @Override
    public boolean place(Animal animal) throws IllegalArgumentException {
        animalsList.add(animal);
        animal.addObserver(this);
        animalVisualMap.getChildren().add(animal);
        if (super.isOccupied(animal.getPosition())) {
            List<Animal> animalsOnPosition = objectsAt(animal.getPosition());
            animalsOnPosition.add(animal);
            animalsMap.put(animal.getPosition(), animalsOnPosition);
        } else {
            List<Animal> animalsOnPosition = new ArrayList<>();
            animalsOnPosition.add(animal);
            animalsMap.put(animal.getPosition(), animalsOnPosition);
        }

        return true;
    }

    /**
     * ------------------------FOR ANIMAL & GRASS-----------------------
     **/
    @Override
    public <Object> List<Object> objectsAt(Vector2d position) {
        ArrayList elementsOn = new ArrayList();
        if (super.objectAt(position) != null) {
            elementsOn = (ArrayList) super.objectAt(position);
            return elementsOn;
        }
        for (Grass grass : grasses) {
            if (grass.getPosition().equals(position)) {
                elementsOn.add(grass);
                return elementsOn;
            }
        }
        return null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || isOccupiedByGrass(position);
    }

    /**
     * ------------------------FOR LIFE-----------------------
     **/

    private void reproduction(Animal parent1, Animal parent2) {
        int childEnergy = (parent1.getEnergy() + parent2.getEnergy()) / 2;
        parent1.subtractEnergy((parent1.getEnergy()) / 4);
        parent2.subtractEnergy((parent2.getEnergy()) / 4);
        parent1.oneChildrenMore();
        parent2.oneChildrenMore();
        Vector2d childPosition = parent1.getPosition().add(MapDirection.getRandom().toUnitVector());
        Animal child = new Animal(this, childPosition, childEnergy, parent1.getGenes(), parent2.getGenes());
        place(child);
    }

    private void reproductionAll() {
        LinkedHashMap<Vector2d, List<Animal>> copyMap = new LinkedHashMap<>();
        for (Map.Entry<Vector2d, List<Animal>> animals : this.animalsMap.entrySet()) {
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
                if (maxOne.getEnergy() >= minReproductionEnergy && maxTwo.getEnergy() >= minReproductionEnergy) {
                    reproduction(maxOne, maxTwo);
                }
            }
        }
    }

    private void eatGrass() {
        for (Map.Entry<Vector2d, List<Animal>> animals : animalsMap.entrySet()) {
            if (isOccupiedByGrass(animals.getKey())) {
                Grass toDelete = this.grassAt(animals.getKey());
                if (toDelete.getPosition().follows(lowerLeftJungle) && toDelete.getPosition().precedes(upperRightJungle)) {
                    totalGrassOnJungle--;
                } else {
                    totalGrassOnSteppe--;
                }
                grassVisualMap.getChildren().remove(toDelete);
                grasses.remove(toDelete);
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
                        toCompare.addEnergy(this.plantEnergy / countWIthSameEnergy);
                    }
                }

            }
        }
    }

    private void moveAll() {
        for (Animal animal : animalsList) {
            animal.changeOrientation(animal.generateMove());
            animal.move();
            animal.subtractEnergy(moveEnergy);
        }
    }

    private void removeDead() {
        List<Animal> toDelFromList = new ArrayList<>();
        for (Animal animal : animalsList) {
            if (animal != null) {
                if (animal.getEnergy() <= 0) {
                    if (animalsMap.get(animal.getPosition()).size() > 1) {
                        animalsMap.get(animal.getPosition()).remove(animal);
                        toDelFromList.add(animal);
                    } else {
                        animalsMap.remove((animal).getPosition());
                        toDelFromList.add(animal);
                    }
                }
            }
        }
        for (Animal animal : toDelFromList) {
            animal.removeObserver(this);
            animalVisualMap.getChildren().remove(animal);
            animalsList.remove(animal);
        }
    }


    public void life(int numberOfDays) {
        for (int i = 0; i < numberOfDays; i++) {

            this.age.addOneDay();
            removeDead();
            moveAll();
            eatGrass();
            reproductionAll();
            generateGrass(1, 1);
        }
    }

    /**
     * ------------------------OTHERS-----------------------
     **/

    public Vector2d wrapPosition(Vector2d position) {
        if (position.precedes(this.upperRight) && position.follows(this.lowerLeft)) {
            return position;
        } else if (position.x > this.upperRight.x && position.y < this.lowerLeft.y) {
            return new Vector2d(this.lowerLeft.x, this.upperRight.y);
        } else if (position.x < this.lowerLeft.x && position.y > this.upperRight.y) {
            return new Vector2d(this.upperRight.x, this.lowerLeft.y);
        } else if (position.x < this.lowerLeft.x && position.y < this.lowerLeft.y) {
            return this.upperRight;
        } else if (position.x > this.upperRight.x && position.y > this.upperRight.y) {
            return this.lowerLeft;
        } else if (position.x < this.lowerLeft.x && position.y >= this.lowerLeft.y)
            return new Vector2d(this.upperRight.x, position.y);
        else if (position.x >= this.lowerLeft.x && position.y < this.lowerLeft.y) {
            return new Vector2d(position.x, this.upperRight.y);
        } else if (position.x <= this.upperRight.x && position.y > this.upperRight.y)
            return new Vector2d(position.x, this.lowerLeft.y);
        else {
            return new Vector2d(this.lowerLeft.x, position.y);
        }
    }


    private void readJsonFile(AllParameters parameters) {
        this.upperRight = new Vector2d(parameters.getWidth(), parameters.getHeight());
        int jungleLowerX = (parameters.getWidth() / 2) - ((parameters.getWidth() * parameters.getJungleRatio() / 100) / 2);
        int jungleUpperX = (parameters.getWidth() / 2) + ((parameters.getWidth() * parameters.getJungleRatio() / 100) / 2);
        int jungleLowerY = (parameters.getHeight() / 2) - ((parameters.getHeight() * parameters.getJungleRatio() / 100) / 2);
        int jungleUpperY = (parameters.getHeight() / 2) + ((parameters.getHeight() * parameters.getJungleRatio() / 100) / 2);
        this.lowerLeftJungle = new Vector2d(jungleLowerX, jungleLowerY);
        this.upperRightJungle = new Vector2d(jungleUpperX, jungleUpperY);
        this.moveEnergy = parameters.getMoveEnergy();
        this.plantEnergy = parameters.getPlantEnergy();
        this.startEnergy = parameters.getStartEnergy();
        this.startNumOfAnimal = parameters.getStartNumOfAnimal();
    }

    @Override
    public String toString() {
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(this.lowerLeft, this.upperRight);
    }
}











