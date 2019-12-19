package project1;

import javafx.scene.paint.Color;

import java.util.*;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {

    protected LinkedHashMap<Vector2d, List<Animal>> animalsMap = new LinkedHashMap<>();
    protected List<Animal> animalsList = new ArrayList<>();
    protected Vector2d lowerLeft = new Vector2d(0, 0);
    protected Vector2d upperRight;


    @Override
    public boolean isOccupied(Vector2d position) {
        return animalsMap.containsKey(position);
    }

    @Override
    public List<Animal> objectAt(Vector2d position) {
        return animalsMap.get(position);
    }


    @Override
    public String toString() {
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(lowerLeft, upperRight);
    }

    public LinkedHashMap<Vector2d, List<Animal>> getAnimalsMap() {
        return animalsMap;
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
        animal.setTranslateY(animal.getPosition().y * this.getCellSize());
        animal.setTranslateX(animal.getPosition().x * this.getCellSize());
        if (animal.getEnergy() <= (this.getStartEnergy() / 5)) {
            animal.rect.setFill(Color.web("#C7CFD2", 1.0));
        } else if (animal.getEnergy() <= (this.getStartEnergy() * 2 / 5)) {
            animal.rect.setFill(Color.web("#FFFECB", 1.0));
        } else if (animal.getEnergy() <= (this.getStartEnergy() * 3 / 5)) {
            animal.rect.setFill(Color.web("#FBFF27", 1.0));
        } else if (animal.getEnergy() <= (this.getStartEnergy() * 4 / 5)) {
            animal.rect.setFill(Color.web("#7d694c", 1.0));
        } else if (animal.getEnergy() >= (this.getStartEnergy() * 4 / 5)) {
            animal.rect.setFill(Color.web("#48240E", 1.0));
        }
        animal.addOneDay();
    }
}









//    @Override
//    public boolean place(Animal animal) throws IllegalArgumentException{
//        if (this.isOccupied(animal.getPosition())){
//            throw new IllegalArgumentException(animal.getPosition() + " is already Occupied");
//        }
//        if(!this.canMoveTo(animal.getPosition())) return false;
//
//        animals.put(animal.getPosition(), animal);
//
//        return true;
//    }
//
//    public void run(MoveDirection[] directions) {
//
//        for (int k = 0; k < directions.length; k++) {
//            Animal animal;
//            if(animals.size() >= k) animal = this.animalsList.get(k );
//            else{ animal = this.animalsList.get(k % animals.size());}
//            animal.move(directions[k]);
//            System.out.println(animalsList);
//        }
//    }
