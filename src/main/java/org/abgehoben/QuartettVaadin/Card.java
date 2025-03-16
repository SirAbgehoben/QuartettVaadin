package org.abgehoben.QuartettVaadin;

public class Card {
    private Integer id;
    private String Name;
    private Float TopSpeed;
    private Float zeroTo100;
    private Float PS;
    private Float Consumption;

    public Card() {
    }
    public void set(Integer id, String Name, Float TopSpeed, Float zeroTo100, Float PS, Float Consumption) {
        this.id = id;
        this.Name = Name;
        this.TopSpeed = TopSpeed;
        this.zeroTo100 = zeroTo100;
        this.PS = PS;
        this.Consumption = Consumption;
    }

    public String getImagePath() {
        System.out.println(Name);
        return "Cards/" + Name + ".png";
    }
    public Integer getId() {
        return id;
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
