package com.falstad.ripple.client;


public class PhotonicCrystalEllipse extends PhotonicCrystal {
    // 孔洞的折射率对应的速度因子
    private double speedIndexHole;

    // 椭圆的长轴和短轴长度（以像素为单位）
    private int ellipseMajorAxis, ellipseMinorAxis;

    // 默认构造函数
    public PhotonicCrystalEllipse() {
        super();
        this.speedIndexHole = MediumBox.getSpeedIndex(2.0); // 默认孔洞折射率 v2 = 2.0
        this.ellipseMajorAxis = 30; // 默认长轴长度10像素
        this.ellipseMinorAxis = 30;  // 默认短轴长度5像素
    }

    // 通过 StringTokenizer 解析构造函数
    public PhotonicCrystalEllipse(StringTokenizer st) {
        super(st);
        this.speedIndexHole = Double.parseDouble(st.nextToken());    // 孔洞折射率
        this.ellipseMajorAxis = Integer.parseInt(st.nextToken());    // 椭圆长轴
        this.ellipseMinorAxis = Integer.parseInt(st.nextToken());    // 椭圆短轴
    }

    // 通过坐标创建对象的构造函数
    public PhotonicCrystalEllipse(int x, int y, int x2, int y2) {
        super(x, y, x2, y2);
        this.speedIndexHole = MediumBox.getSpeedIndex(2.0); // 默认孔洞折射率 v2 = 2.0
        this.ellipseMajorAxis = 30; // 默认长轴长度10像素
        this.ellipseMinorAxis = 30;  // 默认短轴长度5像素
    }

    @Override
    void prepare() {
        RippleSim.drawMedium(
                topLeft.x,
                topLeft.y,
                topRight.x,
                topRight.y,
                bottomLeft.x,
                bottomLeft.y,
                bottomRight.x,
                bottomRight.y,
                speedIndex, speedIndex,
                1, 1
        );

        // 计算每个孔洞的间距
        double totalWidth = topRight.x - topLeft.x;
        double totalHeight = bottomLeft.y - topLeft.y;

        double cellWidth = totalWidth / cols;
        double cellHeight = totalHeight / rows;

        // 遍历每个孔洞位置并绘制椭圆
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // 计算当前孔洞的中心坐标
                double centerX = topLeft.x + col * cellWidth + cellWidth / 2.0;
                double centerY = topLeft.y + row * cellHeight + cellHeight / 2.0;

                // 绘制椭圆形孔洞
                RippleSim.drawSolidEllipse(
                        (int) Math.round(centerX),
                        (int) Math.round(centerY),
                        ellipseMajorAxis / 2,
                        ellipseMinorAxis / 2,
                        speedIndexHole
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
        } else if (n == 4) {
            // 编辑孔洞折射率
            return new EditInfo("Hole Refractive Index", Math.sqrt(1 / speedIndexHole), 1, 2)
                    .setDimensionless();
        } else if (n == 5) {
            // 编辑椭圆长轴
            return new EditInfo("Ellipse Major Axis", ellipseMajorAxis, 1, 100);
        } else if (n == 6) {
            // 编辑椭圆短轴
            return new EditInfo("Ellipse Minor Axis", ellipseMinorAxis, 1, 100);
        }
        return null;
    }

    @Override
    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            // 设置第一种折射率
            speedIndex = MediumBox.getSpeedIndex(ei.value);
            ei.value = Math.sqrt(1 / speedIndex);
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 1) {
            // 设置第二种折射率
            speedIndex2 = MediumBox.getSpeedIndex(ei.value);
            ei.value = Math.sqrt(1 / speedIndex2);
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 2) {
            // 设置行数
            rows = Math.max(1, (int) ei.value); // 确保行数至少为1
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 3) {
            // 设置列数
            cols = Math.max(1, (int) ei.value); // 确保列数至少为1
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 4) {
            // 设置孔洞折射率
            speedIndexHole = MediumBox.getSpeedIndex(ei.value);
            ei.value = Math.sqrt(1 / speedIndexHole);
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 5) {
            // 设置椭圆长轴
            ellipseMajorAxis = Math.max(1, (int) ei.value); // 确保长轴至少为1
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 6) {
            // 设置椭圆短轴
            ellipseMinorAxis = Math.max(1, (int) ei.value); // 确保短轴至少为1
            EditDialog.theEditDialog.updateValue(ei);
        }
    }

    @Override
    int getDumpType() {
        return 100; // 使用 100 作为类型标识符
    }

    @Override
    String dump() {
        return 100 + super.dump() + " " + speedIndexHole + " " + ellipseMajorAxis + " " + ellipseMinorAxis;
    }
}
