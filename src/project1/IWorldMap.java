package project1;

import java.util.List;

public interface IWorldMap {

    boolean place(Animal animal);

    boolean isOccupied(Vector2d position);

    <Object> List<Object> objectsAt(Vector2d currentPosition);

    Vector2d wrapPosition(Vector2d position);

}
