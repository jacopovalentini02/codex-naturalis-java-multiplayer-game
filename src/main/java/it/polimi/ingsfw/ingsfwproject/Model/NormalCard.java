package it.polimi.ingsfw.ingsfwproject.Model;

abstract public class NormalCard extends PlayableCard{
    private NormalBack backface;

    public NormalBack getBackface() {
        return backface;
    }

    public void setBackface(NormalBack backface) {
        this.backface = backface;
    }
}
