package project1;

import javafx.scene.layout.Pane;
import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;


public class MapVisualizer {

    /**
     * ------------------------------------------------------ATTRIBUTES--------------------------------------------------------------
     */
    private BorderPane borderPane = new BorderPane();
    private GrassField map;
    private boolean pause = false;
    private int speedValue = 300;
    private Rectangle currentAnimalBackground = new Rectangle();
    private Text animalsCounter;
    private Text grassesCounter;
    private Text currentMapDate;
    private Text currentAnimalInfo = new Text("");
    private Pane grassPane = new Pane();
    private Pane animalPane = new Pane();
    private double visualHeight;
    private double visualWidth;
    private double visualCellSize;

    /**
     * ------------------------------------------------------CONSTRUCTORS--------------------------------------------------------------
     */
    public MapVisualizer(GrassField map) {
        this.map = map;
        setVisualCellSize();
        visualHeight = ((map.getMapParameters().getUpperRight().y + 1) * visualCellSize);
        visualWidth = ((map.getMapParameters().getUpperRight().x + 1) * visualCellSize);
        createRootPane();
        borderPane.setCenter(createRootPane());


    }
    /**
     * ------------------------------------------------------METHODS---------------------------------------------------------------------
     */
    /**
     * ------------------------GETTERS-SETTERS-----------------------
     **/
    public double getVisualCellSize() {
        return visualCellSize;
    }

    private void setVisualCellSize() {
        int visualMaxSize = 1000;
        if (map.getMapParameters().getUpperRight().x > map.getMapParameters().getUpperRight().y) {
            visualCellSize = visualMaxSize / map.getMapParameters().getUpperRight().x;
        } else {
            visualCellSize = visualMaxSize / map.getMapParameters().getUpperRight().y;
        }
    }

    public void setNewAnimalVisualView(Animal animal) {
        animal.setTranslateY(animal.getPosition().y * map.getVisualizer().getVisualCellSize());
        animal.setTranslateX(animal.getPosition().x * map.getVisualizer().getVisualCellSize());
        if (animal.getEnergy() <= (map.getMapParameters().getStartEnergy() / 5)) {
            animal.getVisualRectangle().setFill(Color.web("#C7CFD2", 1.0));
        } else if (animal.getEnergy() <= (map.getMapParameters().getStartEnergy() * 2 / 5)) {
            animal.getVisualRectangle().setFill(Color.web("#FFFECB", 1.0));
        } else if (animal.getEnergy() <= (map.getMapParameters().getStartEnergy() * 3 / 5)) {
            animal.getVisualRectangle().setFill(Color.web("#FBFF27", 1.0));
        } else if (animal.getEnergy() <= (map.getMapParameters().getStartEnergy() * 4 / 5)) {
            animal.getVisualRectangle().setFill(Color.web("#7d694c", 1.0));
        } else if (animal.getEnergy() >= (map.getMapParameters().getStartEnergy() * 4 / 5)) {
            animal.getVisualRectangle().setFill(Color.web("#48240E", 1.0));
        }
    }

    public void addToGrassPane(Grass grass) {
        this.grassPane.getChildren().add(grass);
    }

    public void removeFromGrassPane(Grass grass) {
        this.grassPane.getChildren().remove(grass);
    }

    public void addToAnimalPane(Animal animal) {
        this.animalPane.getChildren().add(animal);
    }

    public void removeFromAnimalPane(Animal animal) {
        this.animalPane.getChildren().remove(animal);
    }

    /**
     * ------------------------FOR PANES-----------------------
     **/
    private Pane createRootPane() {
        Pane root = new Pane();
        root.setPrefSize(visualWidth + 400, visualHeight);
        root.setMaxSize(visualWidth + 400, visualHeight);
        root.getChildren().addAll(createMapBackGround(), createControlPane(), createInfoPane(), grassPane, animalPane);
        return root;
    }

    private Pane createMapBackGround() {
        Pane mapBackground = new Pane();

        for (int i = 0; i <= map.getMapParameters().getUpperRight().y; i++) {
            for (int j = 0; j <= map.getMapParameters().getUpperRight().x; j++) {
                Vector2d v = new Vector2d(j, i);
                EmptyCell cell;
                if (v.precedes(map.getMapParameters().getUpperRightJungle()) && v.follows(map.getMapParameters().getLowerLeftJungle())) {
                    cell = new EmptyCell(visualCellSize, visualCellSize, true);
                } else {
                    cell = new EmptyCell(visualCellSize, visualCellSize, false);
                }
                cell.setTranslateY(i * visualCellSize);
                cell.setTranslateX(j * visualCellSize);
                mapBackground.getChildren().add(cell);
            }
        }
        return mapBackground;
    }

    private Pane createControlPane() {
        Pane settingsPanel = new Pane();
        settingsPanel.setTranslateY(20);
        settingsPanel.setTranslateX(visualWidth + 130);

        Button button = new Button("Pause");
        button.setTranslateY(0);
        button.setTranslateX(40);
        button.setOnAction(event1 -> {
            if (event1.getSource() == button) {
                if (pause) {
                    button.setText("Pause");
                    pause = false;
                    currentAnimalInfo.setText("");
                    currentAnimalBackground.setFill(Color.web("#999", 0));

                } else {
                    button.setText("Play");
                    pause = true;
                    for (Animal animal : map.getAnimalsList()) {
                        animal.setOnMouseClicked(event2 -> {
                            currentAnimalInfo.setText("\t\t\t\tCURRENT ANIMAL: \n\nGenes: \n" + animal.getGenes().toString() + "\nEnergy: "
                                    + animal.getEnergy() + "\n\nAge:" + animal.getAge()
                                    + "\n\nNumber of Children: " + animal.getNumOfChildren());
                            currentAnimalBackground.setFill(Color.web("#2c5f2d", 1.0));
                        });
                    }

                }
            }
        });

        Text textSpeedCount = new Text("one day = " + speedValue + " ms");
        textSpeedCount.setTranslateY(70);
        textSpeedCount.setTranslateX(10);

        Slider speedSlider = new Slider();
        speedSlider.setMin(20);
        speedSlider.setMax(1000);
        speedSlider.setValue(speedValue);
        speedSlider.setTranslateY(40);
        speedSlider.setTranslateX(0);
        speedSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            speedValue = new_val.intValue();
            textSpeedCount.setText("one day = " + new_val.intValue() + " ms");

        });

        settingsPanel.getChildren().addAll(button, speedSlider, textSpeedCount);

        return settingsPanel;
    }

    private Pane createInfoPane() {
        Pane infoPanel = new Pane();
        infoPanel.setTranslateX(visualWidth + 30);
        infoPanel.setTranslateY(120);

        animalsCounter = new Text("Number of Animals: " + map.getAnimalsList().size());
        animalsCounter.setTranslateY(0);
        animalsCounter.setTranslateX(90);

        grassesCounter = new Text("Number Of Grasses: " + map.getGrasses().size());
        grassesCounter.setTranslateY(30);
        grassesCounter.setTranslateX(90);

        currentMapDate = new Text("Date: " + map.getMapParameters().getAge().toString());
        currentMapDate.setTranslateY(60);
        currentMapDate.setTranslateX(70);

        currentAnimalBackground.setHeight(240);
        currentAnimalBackground.setWidth(340);
        currentAnimalBackground.setTranslateX(0);
        currentAnimalBackground.setTranslateY(90);
        currentAnimalBackground.setFill(Color.web("#999", 0));

        currentAnimalInfo.setTranslateY(130);
        currentAnimalInfo.setTranslateX(30);
        currentAnimalInfo.setStyle("-fx-font: 10 arial;");
        currentAnimalInfo.setFill(Color.web("#ffe77a", 1.0));

        infoPanel.getChildren().addAll(animalsCounter, grassesCounter, currentMapDate, currentAnimalBackground, currentAnimalInfo);

        return infoPanel;
    }

    /**
     * ------------------------START-----------------------
     **/

    public void start(Stage primaryStage) {
        Scene scene = new Scene(this.borderPane);

        Thread thread = new Thread(() -> {
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    updateAnimalOnCellCounter();
                    updateInfoPanel();
                    map.life(1);
                }

                private void updateAnimalOnCellCounter() {
                    for (Map.Entry<Vector2d, List<Animal>> animals : map.getAnimalsMap().entrySet()) {
                        Animal animal = animals.getValue().get(0);
                        animal.setTextOnAnimal("");
                        if (animals.getValue().size() > 1) {
                            animal.setTextOnAnimal("" + animals.getValue().size());
                        }
                    }
                }

                private void updateInfoPanel() {
                    animalsCounter.setText("Number of Animals: " + map.getAnimalsList().size());
                    grassesCounter.setText("Number of Grasses: " + map.getGrasses().size());
                    currentMapDate.setText("Date: " + map.getMapParameters().getAge().toString());
                }
            };

            while (true) {
                try {
                    Thread.sleep(this.speedValue);
                } catch (InterruptedException ignored) {
                }
                if (!this.pause) {
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


