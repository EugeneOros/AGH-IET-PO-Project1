package project1;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;


public class MapParameters {
    /**
     * ------------------------------------------------------ATTRIBUTES--------------------------------------------------------------
     */
    private Vector2d upperRight;
    private Vector2d lowerLeft = new Vector2d(0, 0);
    private Vector2d upperRightJungle;
    private Vector2d lowerLeftJungle;
    private int moveEnergy;
    private int plantEnergy;
    private int startEnergy;
    private int startNumOfAnimal;
    private int totalGrassOnJungle = 0;
    private int totalGrassOnSteppe = 0;
    private Age age = new Age(0, 0, 0);

    /**
     * ------------------------------------------------------CONSTRUCTOR--------------------------------------------------------------
     */

    public MapParameters() throws IOException, ParseException {
        readJsonFile();
    }

    /**
     * ------------------------------------------------------METHODS---------------------------------------------------------------------
     **/
    /**
     * ------------------------SETTERS-----------------------
     **/
    private void readJsonFile() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("src\\parameters.json"));

        this.moveEnergy = (int) (long) jsonObject.get("moveEnergy");
        this.plantEnergy = (int) (long) jsonObject.get("plantEnergy");
        this.startEnergy = (int) (long) jsonObject.get("startEnergy");
        this.startNumOfAnimal = (int) (long) jsonObject.get("startNumOfAnimal");

        int width = (int) (long) jsonObject.get("width");
        int height = (int) (long) jsonObject.get("height");
        int jungleRatio = (int) (long) jsonObject.get("jungleRatio");

        int jungleLowerX = (width / 2) - ((width * jungleRatio / 100) / 2);
        int jungleLowerY = (height / 2) - ((height * jungleRatio / 100) / 2);
        lowerLeftJungle = new Vector2d(jungleLowerX, jungleLowerY);

        int jungleUpperX = (width / 2) + ((width * jungleRatio / 100) / 2);
        int jungleUpperY = (height / 2) + ((height * jungleRatio / 100) / 2);
        upperRightJungle = new Vector2d(jungleUpperX, jungleUpperY);

        upperRight = new Vector2d(width, height);
    }

    public void addOneDay() {
        age.addOneDay();
    }

    public void subtractTotalGrassOnSteppe(int subtract) {
        totalGrassOnSteppe -= subtract;
    }

    public void subtractTotalGrassOnJungle(int subtract) {
        totalGrassOnJungle -= subtract;
    }

    public void addTotalGrassOnSteppe(int add) {
        totalGrassOnSteppe += add;
    }

    public void addTotalGrassOnJungle(int add) {
        totalGrassOnJungle += add;
    }

    /**
     * ------------------------GETTERS-----------------------
     **/

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    public Vector2d getUpperRightJungle() {
        return upperRightJungle;
    }

    public Vector2d getLowerLeftJungle() {
        return this.lowerLeftJungle;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public int getStartEnergy() {
        return this.startEnergy;
    }

    public int getStartNumOfAnimal() {
        return startNumOfAnimal;
    }

    public int getStartNumOfGrass(boolean onJungle) {
        int jungleArea = ((upperRightJungle.x - lowerLeftJungle.x) * (upperRightJungle.y - lowerLeftJungle.y));
        if (onJungle)
            return jungleArea * 3 / 4;
        else {
            return ((upperRight.x * upperRight.y) - jungleArea) / 4;
        }
    }

    public int getTotalGrassOnSteppe() {
        return totalGrassOnSteppe;
    }

    public int getTotalGrassOnJungle() {
        return totalGrassOnJungle;
    }

    public Age getAge() {
        return age;
    }

}
