package gak.bean;

import java.util.Objects;

/**
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-11 下午5:38
 */
public class ChartBean {
    private String type;
    private int point;
    private int size;
    private double start;
    private double end;
    private double step;

    public ChartBean() {
    }

    public ChartBean(String type, int point, int size, double start, double end) {
        setType(type);
        setPoint(point);
        setSize(size);
        if (start > end) {
            start = end - 1;
        }
        setStart(start);
        setEnd(end);
        step = (end - start + 0.0) / point;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (Objects.isNull(type) || type.isEmpty()) {
            type = "-2x";
        }
        this.type = type;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        if (point <= 10) {
            point = 10;
        }
        this.point = point;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (point == 0) {
            point = 1;
        }
        this.size = size;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "ChartBean{" +
                "type='" + type + '\'' +
                ", point=" + point +
                ", size=" + size +
                ", start=" + start +
                ", end=" + end +
                ", step=" + step +
                '}';
    }
}
