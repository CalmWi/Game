package com.example.game;

import java.util.Random;

public class GuessNum {
    static public int rnd_comp_num()
    {
        int min = 10;
        int max =99;
        int diff = max - min;
        Random random = new Random();
        return random.nextInt(diff+1)+min;
    }
}
