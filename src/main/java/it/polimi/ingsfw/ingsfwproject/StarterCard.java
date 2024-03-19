package it.polimi.ingsfw.ingsfwproject;

public class StarterCard extends PlayableCard{
    private Front front;
    private StarterBack back;

    public Front getFront() {
        return front;
    }

    public void setFront(Front front) {
        this.front = front;
    }

    public StarterBack getBack() {
        return back;
    }

    public void setBack(StarterBack back) {
        this.back = back;
    }
}
