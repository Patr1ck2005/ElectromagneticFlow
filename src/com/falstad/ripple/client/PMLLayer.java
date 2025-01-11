package com.falstad.ripple.client;

public class PMLLayer extends MediumBox {
    double frequency;
    int order;


    PMLLayer() {}
    PMLLayer(StringTokenizer st) { super(st); }

    void prepare() {
        RippleSim.drawPML(topLeft.x, topLeft.y, topRight.x, topRight.y,
                bottomLeft.x, bottomLeft.y,
                bottomRight.x, bottomRight.y,
                speedIndex, dampingIndex, frequency, order);
    }


    @Override
    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            return new EditInfo("Refractive Index (1-2)", Math.sqrt(1/speedIndex), 0, 1).
                    setDimensionless();
        } else if (n == 1) {
            return new EditInfo("Damping Index (0.9-1.1)", dampingIndex, 0, 1).
                    setDimensionless();
        } else if (n == 2) {
            return new EditInfo("frequency", frequency, 1, 100);
        } else if (n == 3) {
            return new EditInfo("order", order, 1, 100);
        }
        return null;
    }

    @Override
    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            speedIndex = MediumBox.getSpeedIndex(ei.value);
            ei.value = Math.sqrt(1 / speedIndex);
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 1) {
            dampingIndex = ei.value;
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 2) {
            // 设置行数
            frequency = ei.value;
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 3) {
            // 设置列数
            order = (int) ei.value;
            EditDialog.theEditDialog.updateValue(ei);
        }
    }

    @Override
    int getDumpType() {
        return 999; // 使用 001 作为类型标识符
    }

    @Override
    String dump() {
        return 999 + super.dump() + " " + speedIndex + " " + dampingIndex + " " + frequency + " " + order;
    }

}
