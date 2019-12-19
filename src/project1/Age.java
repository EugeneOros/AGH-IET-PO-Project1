package project1;

public class Age {
    private int year;
    private int month;
    private int day;
    public Age(int y, int m, int d){
        this.year = y;
        this.month = m;
        this.day = d;
    }
    public void addOneDay(){
        if(this.day == 30){
            this.day = 0;
            if(this.month == 12){
                this.month = 0;
                this.year++;
            }else{
                this.month++;
            }
        }else {
            this.day++;
        }
    }

    @Override
    public String toString() {
        String ageString = " " + year + " year";
        if(year != 1){
            ageString += "s";
        }
        ageString += " " + month + " month";
        if( month != 1){
            ageString += "s";
        }
        ageString += " " + day + " day";
        if( day != 1){
            ageString += "s";
        }
        return ageString;
    }
}
