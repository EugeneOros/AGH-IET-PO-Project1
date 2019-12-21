package project1;

import javafx.application.Application;
import javafx.stage.Stage;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class World extends Application {
    private MapParameters parameters = new MapParameters();
    private GrassField map = new GrassField(parameters);

    public World() throws IOException, ParseException {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.map.getVisualizer().start(primaryStage);
    }
}





