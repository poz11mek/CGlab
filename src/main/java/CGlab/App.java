package CGlab;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    String version = "0.02";

    public static void main(String[] args) {

        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);

        Renderer mainRenderer = new Renderer(args[0], width, height, Renderer.LineAlgo.NAIVE);
        mainRenderer.clear();
        mainRenderer.drawPoint(100, 100);
        try {
            mainRenderer.save();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }


        //Oktany:
       // Renderer oktant1 = new Renderer("oktant1.png", width, height, Renderer.LineAlgo.BRESENHAM_INT);
       // Renderer oktant2 = new Renderer("oktant2.png", width, height, Renderer.LineAlgo.BRESENHAM_INT);
       // Renderer oktant3 = new Renderer("oktant3.png", width, height, Renderer.LineAlgo.BRESENHAM_INT);
       // Renderer oktant4 = new Renderer("oktant4.png", width, height, Renderer.LineAlgo.BRESENHAM_INT);
       // Renderer oktant5 = new Renderer("oktant5.png", width, height, Renderer.LineAlgo.BRESENHAM_INT);
       // Renderer oktant6 = new Renderer("oktant6.png", width, height, Renderer.LineAlgo.BRESENHAM_INT);
       // Renderer oktant7 = new Renderer("oktant7.png", width, height, Renderer.LineAlgo.BRESENHAM_INT);
       // Renderer oktant8 = new Renderer("oktant8.png", width, height, Renderer.LineAlgo.BRESENHAM_INT);

        //oktant1.clear();
        //oktant2.clear();
        //oktant3.clear();
        //oktant4.clear();
        //oktant5.clear();
        //oktant6.clear();
        //oktant7.clear();
        //oktant8.clear();

        //oktant1.drawLine(200,200, 300, 399);
        //oktant2.drawLine(200,200, 100, 399);
        //oktant3.drawLine(200,200, 0, 300);
        //oktant4.drawLine(200,200, 0, 100);
        //oktant5.drawLine(200,200, 100, 0);
        //oktant6.drawLine(200,200, 300, 0);
        //oktant7.drawLine(200,200, 399, 100);
        //oktant8.drawLine(200,200, 399, 300);
    }

    public String getVersion() {
	return this.version;
    }
}
