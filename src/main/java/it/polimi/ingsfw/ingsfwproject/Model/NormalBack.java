package it.polimi.ingsfw.ingsfwproject.Model;

public class NormalBack extends Face{
    private Content center;

    public NormalBack(int id, Content[] corners, Content center, String imagePath) {
        super(id, corners, imagePath);
        this.center=center;
    }

    public Content getCenter() {
        return center;
    }

    public void setCenter(Content center) {
        this.center = center;
    }
}
