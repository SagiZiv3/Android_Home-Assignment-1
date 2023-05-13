package com.example.android_homeassignment2.board;

import com.example.android_homeassignment2.R;
import com.example.android_homeassignment2.utils.RandomUtils;

import java.util.Arrays;

public enum BoardElement {
    None(-1, 0),
    Rock(R.drawable.ic_rock, 85),
    Coin(R.drawable.ic_coin, 15);

    private final int imageResource;
    private final int weight;

    BoardElement(int imageResource, int weight) {
        this.imageResource = imageResource;
        this.weight = weight;
    }

    public int getImage() {
        return imageResource;
    }

    public int getWeight() {
        return weight;
    }

    public static BoardElement selectRandomBoardElement() {
        int totalWeight = Arrays.stream(BoardElement.values())
                .filter(e -> e != BoardElement.None)
                .mapToInt(BoardElement::getWeight)
                .sum();
        int randomWeight = RandomUtils.getInstance().nextInt(totalWeight) + 1;
        int cumulativeWeight = 0;
        for (BoardElement element : BoardElement.values()) {
            if (element == BoardElement.None) {
                continue;
            }
            cumulativeWeight += element.getWeight();
            if (randomWeight <= cumulativeWeight) {
                return element;
            }
        }
        return BoardElement.None;
    }
}
