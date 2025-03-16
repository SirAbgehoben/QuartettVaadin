package org.abgehoben.QuartettVaadin;

public class Card {
    private String Name;
    private Float TopSpeed;
    private Float zeroTo100;
    private Float PS;
    private Float Consumption;

    public Card() {
    }
    public void set(String Name, Float TopSpeed, Float zeroTo100, Float PS, Float Consumption) {
        this.Name = Name;
        this.TopSpeed = TopSpeed;
        this.zeroTo100 = zeroTo100;
        this.PS = PS;
        this.Consumption = Consumption;
    }

    public String getImagePath() {
        return "Cards/" + Name + ".png";
    }
    public String getName() {
        return Name;
    }
    public Float getTopSpeed() {
        return TopSpeed;
    }
    public Float getZeroTo100() {
        return zeroTo100;
    }
    public Float getPS() {
        return PS;
    }
    public Float getConsumption() {
        return Consumption;
    }

}
