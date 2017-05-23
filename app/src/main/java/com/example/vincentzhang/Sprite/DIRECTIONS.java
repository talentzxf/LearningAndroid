package com.example.vincentzhang.Sprite;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VincentZhang on 4/20/2017.
 */

public enum DIRECTIONS {
    UNKNOWN(-1),
    RIGHT(0),
    DOWN(1),
    UP(2),
    LEFT(3),
    DOWNLEFT(4),
    DOWNRIGHT(5),
    UPLEFT(6),
    UPRIGHT(7);

    private static Map<Integer,DIRECTIONS> dirMap = new HashMap<>();
    static{
        for( DIRECTIONS dir : DIRECTIONS.values() ){
            dirMap.put(dir.getDirNum(), dir);
        }
    }

    private int dirNum;
    DIRECTIONS(int dirNum){
        this.dirNum = dirNum;
    }

    static DIRECTIONS fromDirNum(int dirNum){
        return dirMap.get(dirNum);
    }

    int getDirNum(){
        return dirNum;
    }

}
