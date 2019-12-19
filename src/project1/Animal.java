package project1;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.*;

import javafx.scene.shape.Rectangle;

public class Animal extends StackPane {

    private Vector2d position;
    private MapDirection orientation;
    private IWorldMap map;
    private Genes genes;
    private ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    private int energy;
    public Rectangle rect = new Rectangle();
    private Text textOnAnimal = new Text();
    private Age age = new Age(0, 0, 0);
    ;
    private int numOfChildren = 0;


    /**
     * ----------------------------------------------------CONSTRUCTORS---------------------------------------------------------------------
     */
    public Animal(IWorldMap map) {
        this(map, new Vector2d(0, 0));
    }

    public Animal(IWorldMap map, Vector2d initialPosition) {
        this(map, initialPosition, map.getStartEnergy());
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int energy) {
        this.energy = energy;
        this.orientation = MapDirection.getRandom();
        this.map = map;
        this.position = initialPosition;
        this.genes = new Genes();


        rect.setHeight(this.map.getCellSize());
        rect.setWidth(this.map.getCellSize());
        rect.setStroke(Color.web("#1B1112", 1.0));
        rect.setFill(Color.web("#48240E", 1.0));
        textOnAnimal.setStyle("-fx-font: 10 arial;");
        getChildren().addAll(rect, textOnAnimal);
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int energy, Genes genesOfParant1, Genes genesOfParant2) {
        this.energy = energy;
        this.orientation = MapDirection.getRandom();
        this.map = map;
        this.position = initialPosition;
        this.genes = new Genes(genesOfParant1, genesOfParant2);

        rect.setHeight(this.map.getCellSize());
        rect.setWidth(this.map.getCellSize());
        rect.setStroke(Color.web("#1B1112", 1.0));
        rect.setFill(Color.web("#48240E", 1.0));
        textOnAnimal.setStyle("-fx-font: 10 arial;");
        getChildren().addAll(rect, textOnAnimal);

    }
    /**------------------------------------------------------METHODS---------------------------------------------------------------------*/
    /**
     * ----------------------------GETER-----------------------------
     */
    public Vector2d getPosition() {
        return this.position;
    }

    public Genes getGenes() {
        return this.genes;
    }

    public int getEnergy() {
        return this.energy;
    }

    public Age getAge() {
        return this.age;
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public void setTextOnAnimal(String text) {
        this.textOnAnimal.setText(text);
    }

    /**
     * ----------------------------OTHERS-----------------------------
     */


    public MoveDirection generateMove() {
        return this.genes.getGenes().get((int) (Math.random() * 32));
    }

    public void changeOrientation(MoveDirection direction) {
        switch (direction) {
            case ZERO: {
                break;
            }
            case ONE: {
                this.orientation = this.orientation.next();
                break;
            }
            case TWO: {
                this.orientation = this.orientation.next().next();
                break;
            }
            case THREE: {
                this.orientation = this.orientation.opposite().previous();
                break;
            }
            case FOUR: {
                this.orientation = this.orientation.opposite();
                break;
            }
            case FIVE: {
                this.orientation = this.orientation.opposite().next();
                break;
            }
            case SIX: {
                this.orientation = this.orientation.previous().previous();
                break;
            }
            case SEVEN: {
                this.orientation = this.orientation.previous();
                break;
            }
        }
    }

    public void move() {
        Vector2d positionCanMoveTo = this.map.wrapPosition(this.position.add(this.orientation.toUnitVector()));
        Vector2d oldPosition = this.position;
        this.position = positionCanMoveTo;
        this.positionChanged(oldPosition);
//        this.setTranslateY(this.getPosition().y  * map.getCellSize());
//        this.setTranslateX((this.getPosition().x) * map.getCellSize());
//        if(this.getEnergy() <= (map.getStartEnergy()/5)){
//            this.rect.setFill(Color.web("#C7CFD2",1.0));
//        }else if (this.getEnergy() <= (map.getStartEnergy()*2/5)){
//            this.rect.setFill(Color.web("#FFFECB",1.0));
//        }else if(this.getEnergy()<=(map.getStartEnergy()*3/5)){
//            this.rect.setFill(Color.web("#FBFF27",1.0));
//        }else if(this.getEnergy()<=(map.getStartEnergy()*4/5)){
//            this.rect.setFill(Color.web("#7d694c",1.0));
//        }else if(this.getEnergy()>=(map.getStartEnergy()*4/5)){
//            this.rect.setFill( Color.web("#48240E",1.0));
//        }
//        this.age.addOneDay();
    }

    public void addEnergy(int energy) {
        this.energy += energy;
    }

    public void addOneDay() {
        this.age.addOneDay();
    }

    public void subtractEnergy(int energy) {
        this.energy = this.energy - energy;
    }


    public void oneChildrenMore() {
        this.numOfChildren++;
    }

    void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, this.position, this);
        }
    }

    public String toString() {
        return "A";
    }


}

