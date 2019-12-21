package project1;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grass extends StackPane {
    private Vector2d position;
    private GrassField map;

    public Grass(Vector2d grassPosition, GrassField map) {
        this.position = grassPosition;
        this.map = map;
        setVisualView();
    }

    Vector2d getPosition() {
        return this.position;
    }


    private void setVisualView() {
        Rectangle rect = new Rectangle();
        rect.setHeight(map.getVisualizer().getVisualCellSize());
        rect.setWidth(map.getVisualizer().getVisualCellSize());
        rect.setStroke(Color.web("#1B1112", 1.0));
        rect.setFill(Color.web("#376040", 1.0));
        setTranslateY(this.getPosition().y * map.getVisualizer().getVisualCellSize());
        setTranslateX((this.getPosition().x) * map.getVisualizer().getVisualCellSize());
        getChildren().addAll(rect);
    }

}
