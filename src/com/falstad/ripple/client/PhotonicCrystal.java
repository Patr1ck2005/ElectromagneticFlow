package com.falstad.ripple.client;

public class PhotonicCrystal extends MediumBox {
    // 第二种折射率对应的速度因子
    double speedIndex2;

    // 矩形阵列的行数和列数
    int rows, cols;

    // 编辑信息对象，用于更新界面
    EditInfo speedIndex1EditInfo, speedIndex2EditInfo, rowsEditInfo, colsEditInfo;

    // 默认构造函数
    public PhotonicCrystal() {
        super();
        this.speedIndex = MediumBox.getRefractiveIndex(1.5); // 默认折射率 v1 = 1.5
        this.speedIndex2 = MediumBox.getRefractiveIndex(2.0); // 默认折射率 v2 = 2.0
        this.rows = 5; // 默认5行
        this.cols = 5; // 默认5列
    }

    // 通过 StringTokenizer 解析构造函数
    public PhotonicCrystal(StringTokenizer st) {
        super(st);
        this.speedIndex = Double.parseDouble(st.nextToken()); // speedIndex1
        this.speedIndex2 = Double.parseDouble(st.nextToken()); // speedIndex2
        this.rows = Integer.parseInt(st.nextToken()); // 行数
        this.cols = Integer.parseInt(st.nextToken()); // 列数
    }

    // 通过坐标创建对象的构造函数
    public PhotonicCrystal(int x, int y, int x2, int y2) {
        super(x, y, x2, y2);
        this.speedIndex = MediumBox.getRefractiveIndex(1.5); // 默认折射率 v1 = 1.5
        this.speedIndex2 = MediumBox.getRefractiveIndex(2.0); // 默认折射率 v2 = 2.0
        this.rows = 5; // 默认5行
        this.cols = 5; // 默认5列
    }

    @Override
    void prepare() {
        // 计算整体边界的宽度和高度
        double totalWidth = topRight.x - topLeft.x;
        double totalHeight = bottomLeft.y - topLeft.y;

        // 计算每个子矩形的宽度和高度
        double cellWidth = totalWidth / cols;
        double cellHeight = totalHeight / rows;

        // 遍历每个子矩形并绘制
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // 计算当前子矩形的坐标 (double)
                double cellTopLeftX_d = topLeft.x + col * cellWidth;
                double cellTopLeftY_d = topLeft.y + row * cellHeight;
                double cellTopRightX_d = cellTopLeftX_d + cellWidth;
                double cellTopRightY_d = cellTopLeftY_d;
                double cellBottomLeftX_d = cellTopLeftX_d;
                double cellBottomLeftY_d = cellTopLeftY_d + cellHeight;
                double cellBottomRightX_d = cellTopRightX_d;
                double cellBottomRightY_d = cellTopRightY_d + cellHeight;

                // 根据行列位置决定使用哪种折射率（例如，棋盘格模式）
                double currentSpeedIndex = ((row + col) % 2 == 0) ? speedIndex : speedIndex2;

                // 将double坐标转换为int，使用四舍五入
                int cellTopLeftX = (int) Math.round(cellTopLeftX_d);
                int cellTopLeftY = (int) Math.round(cellTopLeftY_d);
                int cellTopRightX = (int) Math.round(cellTopRightX_d);
                int cellTopRightY = (int) Math.round(cellTopRightY_d);
                int cellBottomLeftX = (int) Math.round(cellBottomLeftX_d);
                int cellBottomLeftY = (int) Math.round(cellBottomLeftY_d);
                int cellBottomRightX = (int) Math.round(cellBottomRightX_d);
                int cellBottomRightY = (int) Math.round(cellBottomRightY_d);

                // 绘制当前子矩形
                RippleSim.drawMedium(
                        cellTopLeftX, cellTopLeftY,
                        cellTopRightX, cellTopRightY,
                        cellBottomLeftX, cellBottomLeftY,
                        cellBottomRightX, cellBottomRightY,
                        currentSpeedIndex, currentSpeedIndex
                );
            }
        }
    }

    @Override
    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            // 编辑第一种折射率
            return new EditInfo("Refractive Index 1", Math.sqrt(1 / speedIndex), 1, 2)
                    .setDimensionless();
        } else if (n == 1) {
            // 编辑第二种折射率
            return new EditInfo("Refractive Index 2", Math.sqrt(1 / speedIndex2), 1, 2)
                    .setDimensionless();
        } else if (n == 2) {
            // 编辑行数
            return new EditInfo("Rows", rows, 1, 100);
        } else if (n == 3) {
            // 编辑列数
            return new EditInfo("Columns", cols, 1, 100);
        }
        return null;
    }

    @Override
    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            // 设置第一种折射率
            speedIndex = MediumBox.getRefractiveIndex(ei.value);
            ei.value = Math.sqrt(1 / speedIndex);
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 1) {
            // 设置第二种折射率
            speedIndex2 = MediumBox.getRefractiveIndex(ei.value);
            ei.value = Math.sqrt(1 / speedIndex2);
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 2) {
            // 设置行数
            rows = (int) ei.value;
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 3) {
            // 设置列数
            cols = (int) ei.value;
            EditDialog.theEditDialog.updateValue(ei);
        }
    }

    @Override
    int getDumpType() {
        return 'C'; // 使用 'C' 作为类型标识符
    }

    @Override
    String dump() {
        return 'C' + super.dump() + " " + speedIndex + " " + speedIndex2 + " " + rows + " " + cols;
    }
}
