package CGlab;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Renderer {

    public enum LineAlgo { NAIVE, BRESENHAM, BRESENHAM_INT; }

    private BufferedImage render;
    public final int h = 200;
    public final int w = 200;

    private String filename;
    private LineAlgo lineAlgo = LineAlgo.NAIVE;

    public Renderer(String filename) {
        render = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
    }

    public Renderer(String filename, int w, int h, LineAlgo l) {
        render = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
        this.lineAlgo = l;
    }

    public void drawPoint(int x, int y) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);
        render.setRGB(x, y, white);
    }

    public void drawLine(int x0, int y0, int x1, int y1) {
        if(lineAlgo == LineAlgo.NAIVE) drawLineNaive(x0, y0, x1, y1);
        if(lineAlgo == LineAlgo.BRESENHAM) drawLineBresenham(x0, y0, x1, y1);
        if(lineAlgo == LineAlgo.BRESENHAM_INT) drawLineBresenhamInt(x0, y0, x1, y1);
    }



    public void drawLineNaive(int x0, int y0, int x1, int y1) {
        int color = 255 | (255<<8) | (255<<16) | (0);

        int wsp_a = (y1 - y0) / (x1 - x0);

        if(Math.abs(wsp_a) <= 1) {
            int y = y0;
            for (int x = x0; x <= x1; x++) {
                render.setRGB(x, Math.round(y), color);
                y += wsp_a;
            }
        } else {
            int x = x0;
            for (int y = y0; y <= y1; y++) {
                render.setRGB(Math.round(x), y, color);
                x += (1/wsp_a);
            }
        }
    }

    public void drawLineBresenham(int x0, int y0, int x1, int y1) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int dx = x1-x0;
        int dy = y1-y0;
        float derr = Math.abs(dy/(float)(dx));
        float err = 0;

        int y = y0;

        for (int x=x0; x<=x1; x++) {
            render.setRGB(x, y, white);
            err += derr;
            if (err > 0.5) {
                y += (y1 > y0 ? 1 : -1);
                err -= 1.;
            }
        }
    }

    public void drawLineBresenhamInt(int x0, int y0, int x1, int y1) {
        if(Math.abs(y1-y0) < Math.abs(x1-x0))
            if(x0>x1)
                plotLineLow(x1, y1, x0, y0);
            else
                plotLineLow(x0, y0, x1, y1);
        else
        if(y0>y1)
            plotLineHigh(x1, y1, x0, y0);
        else
            plotLineHigh(x0, y0, x1, y1);
    }

    private void plotLineLow(int x0, int y0, int x1, int y1) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int yi, err, x, y;
        int dx = x1-x0;
        int dy = y1-y0;

        yi = 1;
        if(dy<0) {
            yi = -1;
            dy = -dy;
        }

        err = (2*dy) - dx;
        y = y0;

        for (x = x0; x < x1; x++) {
            render.setRGB(x, y, white);
            if(err>0) {
                y += yi;
                err = err + (2*(dy-dx));
            } else {
                err = err + 2*dy;
            }
        }
    }

    private void plotLineHigh(int x0, int y0, int x1, int y1) {
        int whiteC = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int xi, err, x, y;
        int dx = x1-x0;
        int dy = y1-y0;

        xi = 1;
        if(dx<0) {
            xi = -1;
            dx = -dx;
        }

        err = (2*dx) - dy;
        x = x0;

        for (y = y0; y < y1; y++) {
            render.setRGB(x, y, whiteC);
            if(err>0) {
                x += xi;
                err = err + (2*(dx-dy));
            } else {
                err = err + 2*dx;
            }
        }
    }

    public void save() throws IOException {
        File outputfile = new File(filename);
        render = Renderer.verticalFlip(render);
        ImageIO.write(render, "png", outputfile);
    }

    public void clear() {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int black = 0 | (0 << 8) | (0 << 16) | (255 << 24);
                render.setRGB(x, y, black);
            }
        }
    }



    public static BufferedImage verticalFlip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(w, h, img.getColorModel().getTransparency());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return flippedImage;
    }
}