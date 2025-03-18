package org.abgehoben.QuartettVaadin;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("TopSpeed", TopSpeed);
        attributes.put("zeroTo100", zeroTo100);
        attributes.put("PS", PS);
        attributes.put("Consumption", Consumption);
        return attributes;
    }

    public Float get(String attribute) {
        return switch (attribute) {
            case "TopSpeed" -> TopSpeed;
            case "zeroTo100" -> zeroTo100;
            case "PS" -> PS;
            case "Consumption" -> Consumption;
            default -> null;
        };
    }
}
