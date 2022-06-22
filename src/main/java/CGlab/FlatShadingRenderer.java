package CGlab;
import org.w3c.dom.css.RGBColor;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class FlatShadingRenderer extends Renderer {
    private final Vec3f lightVertex = new Vec3f(0, 0, 1);
    public FlatShadingRenderer() {
        super();
    }
    public FlatShadingRenderer(String filename) {
        super(filename);
    }
    public FlatShadingRenderer(String filename, int w, int h) {
        super(filename, w, h);
    }
    void render(Model model) {
        for (Vec3i face : model.getFaceList()) {
            Vec3f normalVertex = model.getNormalVertex(model.getFaceList().indexOf(face));
            double cos = angleBetweenVectors(normalVertex, lightVertex);
            Vec2i[] screen_coords = new Vec2i[3];
            float[] world_coord_z = new float[3];
            for (int j=0; j<3; j++) {
                Vec3f world_coord = model.getVertex(face.get(j));
                world_coord_z[j] = world_coord.z;
                screen_coords[j] = new Vec2i((int) ((world_coord.x() + 1.0) * this.render.getWidth() / 2.0),
                        (int) ((world_coord.y() + 1.0) * this.render.getHeight() / 2.0) - this.render.getHeight() / 2);

            }

            double length1 = Math.sqrt(Math.pow(face.x, 2) + Math.pow(face.y, 2) + Math.pow(face.z, 2));
            double length2 = Math.sqrt(Math.pow(lightVertex.x, 2) + Math.pow(lightVertex.y, 2) + Math.pow(lightVertex.z, 2));

            double skal = Math.abs(lightVertex.x*face.x + lightVertex.y*face.y + lightVertex.z*face.z);



            Color color = new Color((int)(254*(skal/(length1*length2))), (int)(255*(skal/(length1*length2))), (int)(255*(skal/(length1*length2))));
            System.out.println(color.getRed() + " " + color.getGreen() + " " + color.getBlue());
            drawTriangle(screen_coords[0], screen_coords[1], screen_coords[2], world_coord_z[1], world_coord_z[1], world_coord_z[2], color);
        }
    }

    static double magnitude(Vec3f vector) {
        double magnitude = 0;
        for (int i = 0; i < 3; i++) magnitude += vector.get(i) * vector.get(i);
        return Math.sqrt(magnitude);
    }

    static double dotProduct(Vec3f arr, Vec3f brr) {
        double product = 0;
        for (int i = 0; i < 3; i++) product = product + arr.get(i) * brr.get(i);
        return product;
    }

    static double angleBetweenVectors(Vec3f arr, Vec3f brr) {
        double dotProductOfVectors = dotProduct(arr, brr);
        double magnitudeOfA = magnitude(arr);
        double magnitudeOfB = magnitude(brr);
        return dotProductOfVectors  (magnitudeOfA  magnitudeOfB);
    }
}
