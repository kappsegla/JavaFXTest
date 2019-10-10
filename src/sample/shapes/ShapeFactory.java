package sample.shapes;

import static sample.shapes.ShapeType.*;

public class ShapeFactory {

    public static Shape createShape(ShapeProperties properties){
        if(properties.type == CIRCLE)
        {
            return new Circle(properties.x,properties.y,10,properties.paint);
        }
        if( properties.type == RECT)
        {
            return new Rectangle(properties.x,properties.y,10,10,properties.paint);
        }
        if( properties.type == TRIANGLE)
        {
            return new Triangle(properties.x,properties.y,properties.paint);
        }
        return null;
    }
}
