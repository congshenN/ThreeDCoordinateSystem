package cn.sc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ThreeDCoordinateSystem extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final double SCALE = 100; // 缩放比例
    private static final double D = 1000; // 视距

    private double angleX = 0; // 绕X轴旋转角度
    private double angleY = 0; // 绕Y轴旋转角度
    private int mouseX, mouseY; // 鼠标位置

    public ThreeDCoordinateSystem() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);

        // 鼠标拖动旋转
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - mouseX;
                int dy = e.getY() - mouseY;
                angleY += dx * 0.01; // 绕Y轴旋转
                angleX += dy * 0.01; // 绕X轴旋转
                mouseX = e.getX();
                mouseY = e.getY();
                repaint(); // 重绘
            }
        });
    }

    /**
     * 将三维坐标投影到二维平面
     */
    private Point project(double x, double y, double z) {
        // 绕Y轴旋转
        double tempX = x * Math.cos(angleY) - z * Math.sin(angleY);
        double tempZ = x * Math.sin(angleY) + z * Math.cos(angleY);
        x = tempX;
        z = tempZ;

        // 绕X轴旋转
        double tempY = y * Math.cos(angleX) - z * Math.sin(angleX);
        tempZ = y * Math.sin(angleX) + z * Math.cos(angleX);
        y = tempY;
        z = tempZ;

        // 透视投影
        double scale = D / (z + D);
        int x2d = (int) (x * scale) + WIDTH / 2;
        int y2d = (int) (-y * scale) + HEIGHT / 2; // Y轴向上为正

        return new Point(x2d, y2d);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制X轴（红色）
        Point xStart = project(-SCALE, 0, 0);
        Point xEnd = project(SCALE, 0, 0);
        g2d.setColor(Color.RED);
        g2d.drawLine(xStart.x, xStart.y, xEnd.x, xEnd.y);

        // 绘制Y轴（绿色）
        Point yStart = project(0, -SCALE, 0);
        Point yEnd = project(0, SCALE, 0);
        g2d.setColor(Color.GREEN);
        g2d.drawLine(yStart.x, yStart.y, yEnd.x, yEnd.y);

        // 绘制Z轴（蓝色）
        Point zStart = project(0, 0, -SCALE);
        Point zEnd = project(0, 0, SCALE);
        g2d.setColor(Color.BLUE);
        g2d.drawLine(zStart.x, zStart.y, zEnd.x, zEnd.y);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Coordinate System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ThreeDCoordinateSystem());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}