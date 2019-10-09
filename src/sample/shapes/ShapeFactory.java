package sample.shapes;

import static sample.shapes.ShapeType.CIRCLE;

public class ShapeFactory {

    public static Shape createShape(ShapeProperties properties){
        if(properties.type == CIRCLE)
        {
            return new Circle(properties.x,properties.y,10,properties.paint);
        }
        return null;
    }
}
