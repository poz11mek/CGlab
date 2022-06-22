package CGlab;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Renderer {

    public enum LineAlgo { NAIVE, BRESENHAM, BRESENHAM_INT; }

    public BufferedImage render;
    public final int h = 200;
    public final int w = 200;

    float[][] zbuf;
    
    private String filename;
    private LineAlgo lineAlgo = LineAlgo.NAIVE;

    public Renderer() {
    }

    public Renderer(String filename) {
        render = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
    }

    public Renderer(String filename, int w, int h) {
        render = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
        zbuf = new float[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                zbuf[i][j] = Float.NEGATIVE_INFINITY;
            }
        }
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

    public void drawTriangle(Vec2f A, Vec2f B, Vec2f C, Vec3i color) {
        // dla każdego punktu obrazu this.render:
        //      oblicz współrzędne baryc.
        //      jeśli punkt leży wewnątrz, zamaluj (patrz wykład)
        int col;

        if(color.x >=0 && color.x <=255 && color.y>=0 && color.y<=255 && color.z>=0 && color.z<=255)
            col = 255 | (color.x << 8) | (color.y << 16) | (color.z << 24);
        else
            col = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int min_x = (int)Math.min(Math.min(A.x, B.x), C.x);
        int min_y = (int)Math.min(Math.min(A.y, B.y), C.y);
        int max_x = (int)Math.max(Math.max(A.x, B.x), C.x);
        int max_y = (int)Math.max(Math.max(A.y, B.y), C.y);

        for (int x = min_x; x <= max_x; x++) {
            for (int y = min_y; y <= max_y; y++) {
                Vec3f bar = barycentric(A, B, C, new Vec2f(x, y));

                if(bar.x >= 0 && bar.y >= 0 && bar.z >= 0 && bar.x <= 1 && bar.y <= 1 && bar.z <= 1) {
                    this.render.setRGB(x, y, col);
                }
            }
        }
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

    public Vec3f barycentric(Vec2f A, Vec2f B, Vec2f C, Vec2f P) {
        Vec3f v1 = new Vec3f(B.x - A.x, C.x - A.x, A.x - P.x);

        Vec3f v2 = new Vec3f(B.y - A.y, C.y - A.y, A.y - P.y);

        Vec3f cross = cross(v1, v2);

        Vec2f uv = new Vec2f((cross.x/cross.z), (cross.y/cross.z));

        //
        Vec3f barycentric = new Vec3f(uv.x, uv.y, (1 - uv.x - uv.y));
        return barycentric;
    }

    private Vec3f cross(Vec3f v1, Vec3f v2) {
        float a = v1.y*v2.z - v1.z*v2.y;
        float b = v1.z*v2.x - v1.x*v2.z;
        float c = v1.x*v2.y - v1.y*v2.x;

        Vec3f ilo = new Vec3f(a,b,c);
        return ilo;
    }

    public Vec3f barycentric(Vec2i A, Vec2i B, Vec2i C, Vec2i P) {
        Vec3f v1 = new Vec3f(B.x - A.x, C.x - A.x, A.x - P.x);

        Vec3f v2 = new Vec3f(B.y - A.y, C.y - A.y, A.y - P.y);

        Vec3f cross = cross(v1, v2);

        Vec2f uv = new Vec2f((cross.x/cross.z), (cross.y/cross.z));


        Vec3f barycentric = new Vec3f(uv.x, uv.y, (1 - uv.x - uv.y));
        return barycentric;
    }

    private Vec3i cross(Vec3i v1, Vec3i v2) {
        int a = v1.y*v2.z - v1.z*v2.y;
        int b = v1.z*v2.x - v1.x*v2.z;
        int c = v1.x*v2.y - v1.y*v2.x;

        Vec3i ilo = new Vec3i(a,b,c);
        return ilo;
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