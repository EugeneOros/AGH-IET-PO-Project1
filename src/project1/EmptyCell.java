package project1;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EmptyCell extends StackPane {
    EmptyCell(double width, double height, boolean isJungle) {
        Rectangle rectangle = new Rectangle();
        if (isJungle) {
            rectangle.setFill(Color.web("#3a9b71", 1.0));
        } else {
            rectangle.setFill(Color.web("#b9893e", 1.0));
        }
        rectangle.setHeight(height);
        rectangle.setWidth(width);
        getChildren().addAll(rectangle);
    }
}
