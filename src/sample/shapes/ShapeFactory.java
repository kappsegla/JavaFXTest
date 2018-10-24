package sample.shapes;

public class ShapeFactory {

    public static Shape createShape(ShapeProperties properties){
        if( properties.type.equals("circle"))
        {
            return new Circle(properties.x,properties.y,10,properties.paint);
        }
        return null;
    }
}
