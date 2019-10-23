package sample.parsers;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import sample.shapes.Shape;
import sample.shapes.ShapeFactory;
import sample.shapes.ShapeProperties;
import sample.shapes.ShapeType;

import java.util.Arrays;
import java.util.Optional;

public class SVGParser {

    public Optional<Shape> elementToShape(String element) {
        if ((element.contains("circle") || element.contains("rect")) && !element.startsWith("[you]") && !element.startsWith("[Server]")) {
            double x = 50;
            double y = 50;
            Paint paint = Color.BLUE;
            ShapeType type;

            var fields = element.split(" ");
            if (fields[1].equals("<circle"))
                type = ShapeType.CIRCLE;
            else if (fields[1].equals("<rect"))
                type = ShapeType.RECT;
            else
                return Optional.empty();

            var alts = Arrays.copyOfRange(fields, 2, fields.length);

            for (String field : alts) {
                var parts = field.split("=");
                if (parts[0].equals("cx") || parts[0].equals("x"))
                    x = getDouble(parts[1]);
                if (parts[0].equals("cy") || parts[0].equals("y"))
                    y = getDouble(parts[1]);
            }
            return Optional.of(ShapeFactory.createShape(new ShapeProperties(type, x, y, paint)));
        }
        return Optional.empty();
    }

    private double getDouble(String part) {
        return Double.parseDouble(part.replaceAll("[^-?.?0-9]",""));
    }
}
