package com.example.vincentzhang.Sprite.UI;

import com.example.vincentzhang.Sprite.XMLUtilities.XMLElementType;

import java.util.ArrayList;

/**
 * Created by VincentZhang on 7/23/2017.
 */

public class ButtonArray {
    private String name;

    @XMLElementType(ButtonDefinition.class)
    private ArrayList<ButtonDefinition> buttons;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ButtonDefinition> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<ButtonDefinition> buttons) {
        this.buttons = buttons;
    }
}
