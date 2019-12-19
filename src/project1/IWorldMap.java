package project1;

import java.util.List;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 */
public interface IWorldMap {

    boolean place(Animal animal);

    boolean isOccupied(Vector2d position);

    <Object> List<Animal> objectAt(Vector2d position);

    <Object> List<Object> objectsAt(Vector2d currentPosition);

    Vector2d wrapPosition(Vector2d position);

    double getHeight();

    double getWidth();

    double getCellSize();

    int getStartEnergy();
}
