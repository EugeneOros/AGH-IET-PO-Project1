package project1;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.*;

import javafx.scene.shape.Rectangle;

public class Animal extends StackPane {

    /**
     * ------------------------------------------------------ATTRIBUTES--------------------------------------------------------------
     */
    private ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    private MapDirection orientation;
    private Vector2d position;
    private GrassField map;
    private Genes genes;
    private int energy;
    private Age age = new Age(0, 0, 0);
    private int numOfChildren = 0;
    private Rectangle visualRectangle = new Rectangle();
    private Text textOnAnimal = new Text();


    /**
     * ----------------------------------------------------CONSTRUCTORS---------------------------------------------------------------------
     */
    public Animal(GrassField map) {
        this(map, new Vector2d(0, 0));
    }

    public Animal(GrassField map, Vector2d initialPosition) {
        this(map, initialPosition, map.getMapParameters().getStartEnergy());
    }

    public Animal(GrassField map, Vector2d initialPosition, int energy) {
        this(map, initialPosition, energy, null, null);
    }

    public Animal(GrassField map, Vector2d initialPosition, int energy, Genes genesOfParant1, Genes genesOfParant2) {
        this.energy = energy;
        this.orientation = MapDirection.getRandom();
        this.map = map;
        this.position = initialPosition;
        if (genesOfParant1 == null) {
            this.genes = new Genes();
        } else {
            this.genes = new Genes(genesOfParant1, genesOfParant2);
        }
        setVisualView();
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

    public Rectangle getVisualRectangle() {
        return visualRectangle;
    }

    /**
     * ------------------------SETTERS-----------------------
     **/
    private void setVisualView() {
        visualRectangle.setHeight(map.getVisualizer().getVisualCellSize());
        visualRectangle.setWidth(map.getVisualizer().getVisualCellSize());
        visualRectangle.setStroke(Color.web("#1B1112", 1.0));
        visualRectangle.setFill(Color.web("#48240E", 1.0));
        textOnAnimal.setStyle("-fx-font: 10 arial;");
        getChildren().addAll(visualRectangle, textOnAnimal);
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
}

