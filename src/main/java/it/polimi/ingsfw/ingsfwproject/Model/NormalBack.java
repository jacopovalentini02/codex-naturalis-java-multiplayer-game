package it.polimi.ingsfw.ingsfwproject.Model;

public class NormalBack extends Face{
    private Content center;

    public NormalBack(int id, Content[] corners, Content center) {
        super(id, corners);
        this.center=center;
    }

    public Content getCenter() {
        return center;
    }

    public void setCenter(Content center) {
        this.center = center;
    }
}
