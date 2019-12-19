package project1;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grass extends StackPane {
    private Vector2d position;

    public Grass(Vector2d grassPosition, IWorldMap map) {
        this.position = grassPosition;
        Rectangle rect = new Rectangle();
        rect.setHeight(map.getCellSize());
        rect.setWidth(map.getCellSize());
        rect.setStroke(Color.web("#1B1112", 1.0));
        rect.setFill(Color.web("#376040", 1.0));
        setTranslateY(this.getPosition().y * map.getCellSize());
        setTranslateX((this.getPosition().x) * map.getCellSize());
        getChildren().addAll(rect);
    }

    Vector2d getPosition() {
        return this.position;
    }

    public String toString() {
        return "*";
    }

}
