package it.polimi.ingsfw.ingsfwproject.Model;

abstract public class NormalCard extends PlayableCard{
    private NormalBack backface;

    public NormalBack getBackface() {
        return backface;
    }

    public NormalCard(int id, NormalBack backface) {
        super(id);
        this.backface = backface;
    }
}
