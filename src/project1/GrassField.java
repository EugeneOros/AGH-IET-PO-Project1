package project1;

import java.util.*;


public class GrassField implements IWorldMap, IPositionChangeObserver {

    /**
     * ------------------------------------------------------ATTRIBUTES--------------------------------------------------------------
     */
    private List<Grass> grasses = new ArrayList<>();
    private MapParameters mapParameters;
    private MapVisualizer visualizer;
    private LinkedHashMap<Vector2d, List<Animal>> animalsMap = new LinkedHashMap<>();
    private List<Animal> animalsList = new ArrayList<>();

    /**
     * ------------------------------------------------------CONSTRUCTORS--------------------------------------------------------------
     */
    public GrassField(MapParameters parameters) {
        mapParameters = parameters;
        visualizer = new MapVisualizer(this);
        generateGrass(parameters.getStartNumOfGrass(false), parameters.getStartNumOfGrass(true));
        generateAnimal(parameters.getStartNumOfAnimal());
    }

    /**
     * ------------------------------------------------------METHODS---------------------------------------------------------------------
     */
    /**
     * ------------------------GETTERS-----------------------
     **/

    public List<Grass> getGrasses() {
        return grasses;
    }

    public MapVisualizer getVisualizer() {
        return visualizer;
    }

    public MapParameters getMapParameters() {
        return mapParameters;
    }

    public LinkedHashMap<Vector2d, List<Animal>> getAnimalsMap() {
        return animalsMap;
    }

    public List<Animal> getAnimalsList() {
        return animalsList;
    }

    /**
     * ------------------------FOR GRASS-----------------------
     **/

    private void generateGrass(int numOfGrassSteppe, int numOfGrassJungle) {
        int i = 0;
        Vector2d upper = mapParameters.getUpperRightJungle();
        Vector2d lower = mapParameters.getLowerLeftJungle();
        while (i < numOfGrassJungle) {
            if (!canPlaceGrassJungle()) break;
            Vector2d position = new Vector2d((int) (Math.random() * (upper.x - lower.x + 1)) + lower.x, (int) (Math.random() * (upper.y - lower.y + 1)) + lower.y);
            if (!isOccupied(position)) {
                i++;
                Grass toAdd = new Grass(position, this);
                grasses.add(toAdd);
                this.visualizer.addToGrassPane(toAdd);
                mapParameters.addTotalGrassOnJungle(1);
            }
        }
        upper = mapParameters.getUpperRight();
        i = 0;
        while (i < numOfGrassSteppe) {
            if (!canPlaceGrassSteppe()) break;
            Vector2d position = new Vector2d((int) (Math.random() * (upper.x + 1)), (int) (Math.random() * (upper.y + 1)));
            if (!isOccupied(position) && !(position.precedes(mapParameters.getUpperRightJungle()) && position.follows(mapParameters.getLowerLeftJungle()))) {
                i++;
                Grass toAdd = new Grass(position, this);
                grasses.add(toAdd);
                this.visualizer.addToGrassPane(toAdd);
                mapParameters.addTotalGrassOnSteppe(1);
            }
        }
    }


    public boolean isOccupiedByGrass(Vector2d position) {
        return this.grassAt(position) != null;
    }

    public Grass grassAt(Vector2d position) {
        for (Grass grass : grasses) {
            if (grass.getPosition().equals(position)) {
                return grass;
            }
        }
        return null;
    }

    private boolean canPlaceGrassJungle() {
        int animalOnJungleCount = 0;
        int jungleArea = ((mapParameters.getUpperRightJungle().x - mapParameters.getLowerLeftJungle().x + 1) * (mapParameters.getUpperRightJungle().y - mapParameters.getLowerLeftJungle().y + 1));

        for (Map.Entry<Vector2d, List<Animal>> animals : this.animalsMap.entrySet()) {
            if (animals.getKey().follows(mapParameters.getLowerLeftJungle()) && animals.getKey().precedes(mapParameters.getUpperRightJungle())) {
                animalOnJungleCount++;
            }
        }

        return mapParameters.getTotalGrassOnJungle() < jungleArea - animalOnJungleCount;
    }

    private boolean canPlaceGrassSteppe() {
        int animalOnSteppeCount = 0;
        int jungleArea = ((mapParameters.getUpperRightJungle().x - mapParameters.getLowerLeftJungle().x + 1) * (mapParameters.getUpperRightJungle().y - mapParameters.getLowerLeftJungle().y + 1));
        int steppeArea = ((mapParameters.getUpperRight().x + 1) * (mapParameters.getUpperRight().y + 1)) - jungleArea;

        for (Map.Entry<Vector2d, List<Animal>> animals : this.animalsMap.entrySet()) {
            if (!(animals.getKey().follows(mapParameters.getLowerLeftJungle()) && animals.getKey().precedes(mapParameters.getUpperRightJungle()))) {
                animalOnSteppeCount++;
            }
        }
        return mapParameters.getTotalGrassOnSteppe() < steppeArea - animalOnSteppeCount;
    }

    /**
     * ------------------------FOR ANIMAL-----------------------
     **/
    private void generateAnimal(int numOfAnimal) {
        for (int i = 0; i < numOfAnimal; i++) {
            int randX = (int) ((Math.random() * (mapParameters.getUpperRightJungle().x - mapParameters.getLowerLeftJungle().x)) + mapParameters.getLowerLeftJungle().x);
            int randY = (int) ((Math.random() * (mapParameters.getUpperRightJungle().y - mapParameters.getLowerLeftJungle().y)) + mapParameters.getLowerLeftJungle().y);
            this.place(new Animal(this, new Vector2d(randX, randY)));
        }
    }

    public boolean isOccupiedByAnimal(Vector2d position) {
        return animalsMap.containsKey(position);
    }

    public List<Animal> animalAt(Vector2d position) {
        return animalsMap.get(position);
    }


    public boolean place(Animal animal) throws IllegalArgumentException {
        animalsList.add(animal);
        animal.addObserver(this);
        this.visualizer.addToAnimalPane(animal);
        if (isOccupiedByAnimal(animal.getPosition())) {
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

    public <Object> List<Object> objectsAt(Vector2d position) {
        List elementsOn = new ArrayList();
        if (animalAt(position) != null) {
            elementsOn = animalAt(position);
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


    public boolean isOccupied(Vector2d position) {
        return isOccupiedByAnimal(position) || isOccupiedByGrass(position);
    }

    /**
     * ------------------------LIFE-----------------------
     **/


    public void life(int numberOfDays) {
        Simulation simulation = new Simulation(this);
        for (int i = 0; i < numberOfDays; i++) {
            mapParameters.addOneDay();
            simulation.removeDead();
            simulation.moveAll();
            simulation.eatGrass();
            simulation.reproductionAll();
            generateGrass(1, 1);
        }
    }

    /**
     * ------------------------OTHERS-----------------------
     **/

    public Vector2d wrapPosition(Vector2d position) {
        if (position.precedes(mapParameters.getUpperRight()) && position.follows(mapParameters.getLowerLeft())) {
            return position;
        } else if (position.x > mapParameters.getUpperRight().x && position.y < mapParameters.getLowerLeft().y) {
            return new Vector2d(mapParameters.getLowerLeft().x, mapParameters.getUpperRight().y);
        } else if (position.x < mapParameters.getLowerLeft().x && position.y > mapParameters.getUpperRight().y) {
            return new Vector2d(mapParameters.getUpperRight().x, mapParameters.getLowerLeft().y);
        } else if (position.x < mapParameters.getLowerLeft().x && position.y < mapParameters.getLowerLeft().y) {
            return mapParameters.getUpperRight();
        } else if (position.x > mapParameters.getUpperRight().x && position.y > mapParameters.getUpperRight().y) {
            return mapParameters.getLowerLeft();
        } else if (position.x < mapParameters.getLowerLeft().x && position.y >= mapParameters.getLowerLeft().y)
            return new Vector2d(mapParameters.getUpperRight().x, position.y);
        else if (position.x >= mapParameters.getLowerLeft().x && position.y < mapParameters.getLowerLeft().y) {
            return new Vector2d(position.x, mapParameters.getUpperRight().y);
        } else if (position.x <= mapParameters.getUpperRight().x && position.y > mapParameters.getUpperRight().y)
            return new Vector2d(position.x, mapParameters.getLowerLeft().y);
        else {
            return new Vector2d(mapParameters.getLowerLeft().x, position.y);
        }
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        List<Animal> a = new ArrayList<>();
        if (animalsMap.get(newPosition) != null) {
            a = animalsMap.get(newPosition);
        }
        a.add(animal);
        if (animalsMap.get(oldPosition).size() > 1) {
            animalsMap.get(oldPosition).remove(animal);
        } else {
            animalsMap.remove(oldPosition);
        }
        animalsMap.put(newPosition, a);
        visualizer.setNewAnimalVisualView(animal);
        animal.addOneDay();
    }

}