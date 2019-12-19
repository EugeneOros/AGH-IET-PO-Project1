package project1;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;


public class AllParameters {
    private int moveEnergy;
    private int plantEnergy;
    private int width;
    private int height;
    private int jungleRatio;
    private int startEnergy;
    private int startNumOfAnimal;

    public AllParameters() throws IOException, ParseException {
        readJsonFile();
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getJungleRatio() {
        return jungleRatio;
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

    private void readJsonFile() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("src\\parameters.json"));
        this.moveEnergy = (int) (long) jsonObject.get("moveEnergy");
        this.plantEnergy = (int) (long) jsonObject.get("plantEnergy");
        this.width = (int) (long) jsonObject.get("width");
        this.height = (int) (long) jsonObject.get("height");
        this.jungleRatio = (int) (long) jsonObject.get("jungleRatio");
        this.startEnergy = (int) (long) jsonObject.get("startEnergy");
        this.startNumOfAnimal = (int) (long) jsonObject.get("startNumOfAnimal");

    }
}
