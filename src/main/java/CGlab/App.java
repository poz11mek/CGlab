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
    }

    public String getVersion() {
	return this.version;
    }
}
