package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import sample.shapes.Drawable;
import sample.shapes.Shape;

import java.util.List;
import java.util.Optional;

public class Model {

    final ObservableList<Drawable> shapes;

    public Model(){
        //https://coderanch.com/t/666722/java/Notify-ObservableList-Listeners-Change-Elements
        //Listeners registered for changes on observable list will also
        //be notified about changes on the xpos and ypos on shapes.
        shapes =  FXCollections.observableArrayList(
                new Callback<Drawable, Observable[]>() {
                    @Override
                    public Observable[] call(Drawable param) {
                        return new Observable[]{
                                param.xposProperty(),
                                param.yposProperty(),
                                param.paintProperty()
                        };
                    }
                }
        );
    }

    public ObservableList<Drawable> getShapes() {
        return shapes;
    }

    public Optional<Drawable> findIntersection(double x, double y) {
        //https://stackoverflow.com/questions/21426843/get-last-element-of-stream-list-in-a-one-liner
        return shapes.stream().filter(s -> s.intersects(x,y)).reduce((first, second) -> second);
        //return shapes.stream().filter(s -> s.intersects(x,y)).findFirst();
    }
}
