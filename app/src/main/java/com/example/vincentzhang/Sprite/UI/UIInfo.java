package com.example.vincentzhang.Sprite.UI;

import com.example.vincentzhang.Sprite.XMLUtilities.XMLElementType;

import java.util.ArrayList;

/**
 * Created by VincentZhang on 7/23/2017.
 */

public class UIInfo{
    private String name;

    @XMLElementType(ButtonArray.class)
    private ArrayList<ButtonArray> buttonArrays;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ButtonArray> getButtonArrays() {
        return buttonArrays;
    }

    public void setButtonArrays(ArrayList<ButtonArray> buttonArrays) {
        this.buttonArrays = buttonArrays;
    }
}
