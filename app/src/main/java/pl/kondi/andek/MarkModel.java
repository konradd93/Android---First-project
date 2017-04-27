package pl.kondi.andek;

/**
 * Created by ATyKondziu on 09.03.2017.
 */

public class MarkModel {
    private String name;
    private int mark;

    public MarkModel(String name, int mark) {

        this.name = name;
        this.mark = mark;
    }

    public MarkModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
