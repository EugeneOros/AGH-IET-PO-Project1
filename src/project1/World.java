package project1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

import javafx.scene.text.Text;

import java.util.Map;

public class World extends Application {
    private AllParameters parameters = new AllParameters();
    private GrassField map = new GrassField(parameters);
    private int speedValue = 300;
    private boolean pause = false;

    public World() throws IOException, ParseException {
    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {

        /**------------------------------------rootPanel---------------------------------------------**/
        Pane root = new Pane();
        root.setPrefSize(map.getWidth() + 400, map.getHeight());
        root.setMaxSize(map.getWidth() + 400, map.getHeight());

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(root);
        Pane mapBackground = new Pane();
        for (int i = 0; i <= this.map.upperRight.y; i++) {
            for (int j = 0; j <= map.upperRight.x; j++) {
                Vector2d v = new Vector2d(j, i);
                EmptyCell cell;
                if (v.precedes(map.upperRightJungle) && v.follows(map.lowerLeftJungle)) {
                    cell = new EmptyCell(map.getCellSize(), map.getCellSize(), true);
                } else {
                    cell = new EmptyCell(map.getCellSize(), map.getCellSize(), false);
                }
                cell.setTranslateY(i * this.map.getCellSize());
                cell.setTranslateX(j * this.map.getCellSize());
                mapBackground.getChildren().add(cell);
            }
        }
        /**------------------------------------infoPanel---------------------------------------------**/
        Pane infoPanel = new Pane();
        infoPanel.setTranslateX(this.map.getWidth() + 30);
        infoPanel.setTranslateY(120);

        Text animalsCounter = new Text("Number of Animals: " + map.animalsList.size());
        animalsCounter.setTranslateY(0);
        animalsCounter.setTranslateX(90);

        Text grassesCounter = new Text("Number Of Grasses: " + map.getGrasses().size());
        grassesCounter.setTranslateY(30);
        grassesCounter.setTranslateX(90);

        Text date = new Text("Date: " + map.getAge().toString());
        date.setTranslateY(60);
        date.setTranslateX(70);

        Rectangle rect = new Rectangle();
        rect.setHeight(400);
        rect.setWidth(340);
        rect.setTranslateX(0);
        rect.setTranslateY(90);
        rect.setFill(Color.web("#999", 0));

        Text currentAnimalInfo = new Text("");
        currentAnimalInfo.setTranslateY(130);
        currentAnimalInfo.setTranslateX(40);
        currentAnimalInfo.setStyle("-fx-font: 10 arial;");
        currentAnimalInfo.setFill(Color.web("#ffe77a", 1.0));

        infoPanel.getChildren().addAll(animalsCounter, grassesCounter, date, rect, currentAnimalInfo);

        /**------------------------------------settingsPanel---------------------------------------------**/
        Pane settingsPanel = new Pane();
        settingsPanel.setTranslateY(20);
        settingsPanel.setTranslateX(this.map.getWidth() + 130);

        Button button;
        button = new Button("Pause");
        button.setTranslateY(0);
        button.setTranslateX(40);
        button.setOnAction(event1 -> {
            if (event1.getSource() == button) {
                if (pause) {
                    button.setText("Pause");
                    pause = false;
                    currentAnimalInfo.setText("");
                    rect.setFill(Color.web("#999", 0));

                } else {
                    button.setText("Play");
                    pause = true;
                    for (Animal animal : map.animalsList) {
                        animal.setOnMouseClicked(event2 -> {
                            currentAnimalInfo.setText("\t\t\t\tCURRENT ANIMAL: \n\nGenes: \n" + animal.getGenes().toString() + "\nEnergy: "
                                    + animal.getEnergy() + "\n\nAge:" + animal.getAge()
                                    + "\n\nNumber of Children: " + animal.getNumOfChildren());
                            rect.setFill(Color.web("#2c5f2d", 1.0));
                        });
                    }

                }
            }
        });

        Text textSpeedCount = new Text("one day = 300 ms");
        textSpeedCount.setTranslateY(70);
        textSpeedCount.setTranslateX(10);

        Slider speedSlider = new Slider();
        speedSlider.setMin(20);
        speedSlider.setMax(1000);
        speedSlider.setValue(300);
        speedSlider.setTranslateY(40);
        speedSlider.setTranslateX(0);
        speedSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            speedValue = new_val.intValue();
            textSpeedCount.setText("one day = " + new_val.intValue() + " ms");

        });

        settingsPanel.getChildren().addAll(button, speedSlider, textSpeedCount);

        root.getChildren().addAll(infoPanel, mapBackground, map.grassVisualMap, map.animalVisualMap, settingsPanel);
        Scene scene = new Scene(borderPane);


        Thread thread = new Thread(() -> {
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    visualiseMapElement();
                    visualiseInfoPanel();
                    map.life(1);
                }
                private void visualiseMapElement() {
                    for (Map.Entry<Vector2d, List<Animal>> animals : map.getAnimalsMap().entrySet()) {
                        Animal animal = animals.getValue().get(0);
                        animal.setTextOnAnimal("");
                        if (animals.getValue().size() > 1) {
                            animal.setTextOnAnimal("" + animals.getValue().size());
                        }
                    }
                }
                private void visualiseInfoPanel() {
                    animalsCounter.setText("Number of Animals: " + map.animalsList.size());
                    grassesCounter.setText("Number of Grasses: " + map.getGrasses().size());
                    date.setText("Date: " + map.getAge().toString());
                }
            };

            while (true) {
                try {
                    Thread.sleep(speedValue);
                } catch (InterruptedException ignored) {
                }
                if (!pause) {
                    Platform.runLater(updater);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}





