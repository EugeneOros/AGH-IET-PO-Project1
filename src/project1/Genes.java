package project1;

import java.util.ArrayList;
import java.util.Collections;


public class Genes {
    /**
     * ------------------------------------------------------ATTRIBUTES--------------------------------------------------------------
     */

    private ArrayList<MoveDirection> genes;

    /**
     * ----------------------------------------------------CONSTRUCTORS---------------------------------------------------------------
     */
    public Genes() {
        ArrayList<MoveDirection> newGenes = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            newGenes.add(MoveDirection.getRandom());
        }
        this.addMissingGenes(newGenes);
        Collections.sort(newGenes);
        this.genes = newGenes;
    }

    public Genes(Genes genesOfParant1, Genes genesOfParant2) {
        int firstSeparator = (int) (Math.random() * 30) + 1;
        int secondSeparator = (int) (Math.random() * (31 - firstSeparator + 1)) + firstSeparator;
        ArrayList<MoveDirection> margeList = new ArrayList<>();
        margeList.addAll(genesOfParant1.getGenes().subList(0, firstSeparator));
        margeList.addAll(genesOfParant2.getGenes().subList(firstSeparator, secondSeparator));
        margeList.addAll(genesOfParant1.getGenes().subList(secondSeparator, 32));
        this.addMissingGenes(margeList);
        Collections.sort(margeList);
        this.genes = margeList;
    }

    /**
     * ------------------------------------------------------METHODS---------------------------------------------------------------------
     */

    public ArrayList<MoveDirection> getGenes() {
        return genes;
    }

    private void addMissingGenes(ArrayList<MoveDirection> genesList) {
        boolean check;
        do {
            check = true;
            for (MoveDirection direction : MoveDirection.values()) {
                if (!genesList.contains(direction)) {
                    genesList.remove((int) (Math.random() * 32));
                    genesList.add(direction);
                    check = false;
                }
            }
        } while (!check);
    }

    @Override
    public String toString() {
        int counter = 0;
        String genesStr = "[";
        for (MoveDirection gene : this.genes) {
            genesStr += " " + gene.toString();
            counter++;
            if (counter == 32) genesStr += " ]";
            if (counter % 8 == 0) {
                genesStr += "\n";
            }
        }

        return genesStr;
    }
}
