package com.falstad.ripple.client;


public class TemporalCrystalMediumBox extends MediumBox {
    // 新增的字段
    private double amplitude;     // 折射率变化的振幅
    private double frequency;     // 变化的频率（模拟频率）
    private double phaseShift;    // 当前相位偏移
    private double freqTimeZero;  // 频率时间零点

    EditInfo frequencyEditInfo, wavelengthEditInfo;

    // 频率缩放因子，与 Source 类一致
    static final double freqScale = 92.0 / 3.0 * 0.5; // 约为15.333

    // 默认构造函数
    TemporalCrystalMediumBox() {
        super();
        this.amplitude = 0.1;
        this.frequency = 0.5; // 与 Source 类默认频率一致
        this.phaseShift = 0.0;
        this.freqTimeZero = 0.0;
    }

    // 通过 StringTokenizer 解析构造函数
    TemporalCrystalMediumBox(StringTokenizer st) {
        super(st);
        this.amplitude = Double.parseDouble(st.nextToken());
        this.frequency = Double.parseDouble(st.nextToken());
        this.phaseShift = Double.parseDouble(st.nextToken());
        this.freqTimeZero = 0.0;
    }

    // 通过坐标创建对象的构造函数
    TemporalCrystalMediumBox(int x, int y, int x2, int y2) {
        super(x, y, x2, y2);
        this.amplitude = 0.1;
        this.frequency = 0.5; // 与 Source 类默认频率一致
        this.phaseShift = 0.0;
        this.freqTimeZero = 0.0;
    }

    @Override
    void prepare() {
        // 更新相位
        double phase = frequency*sim.t;

        // 计算当前折射率
        double dynamicRefractiveIndex = speedIndex + amplitude * Math.sin(phase);

        // 可选：打印当前折射率以进行调试
        // System.out.println("TemporalCrystalMediumBox refractiveIndex: " + dynamicRefractiveIndex);

        RippleSim.drawMedium(
                topLeft.x, topLeft.y, topRight.x, topRight.y,
                bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y,
                dynamicRefractiveIndex, dynamicRefractiveIndex
        );
    }

    @Override
    public void run() {
        prepare();
    }

    @Override
    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            return new EditInfo("Refractive Index (1-2)", Math.sqrt(1 / speedIndex), 0, 1)
                    .setDimensionless();
        } else if (n == 1) {
            return new EditInfo("Amplitude", amplitude, 0, 1)
                    .setDimensionless();
        } else if (n == 2) {
            return new EditInfo("Frequency (Hz)", getRealFrequency(), 4, 500) // 与 Source 类频率范围一致
                    .setDimensionless();
        }
        return null;
    }

    @Override
    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            speedIndex = getRefractiveIndex(ei.value);
            ei.value = Math.sqrt(1 / speedIndex);
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 1) {
            amplitude = ei.value;
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 2) {
            setFrequency(ei.value); // 使用 setFrequency 方法
            enforceMaxFrequency();
            EditDialog.theEditDialog.updateValue(ei);
        }
    }

    static double getRefractiveIndex(double v) {
        if (v < 1)
            v = 1;
        if (v > 2)
            v = 2;
        return 1 / (v * v);
    }

    @Override
    int getDumpType() {
        return 'T'; // 使用字符 'T' 作为类型标识符
    }

    @Override
    String dump() {
        return "T " + super.dump() + " " + amplitude + " " + frequency + " " + phaseShift + " " + freqTimeZero;
    }

    // 设置频率，保持与 Source 类一致
// 设置频率，保持与 Source 类一致
    void setFrequency(double realFrequency) {
        double oldfreq = frequency;
        frequency = (realFrequency * freqScale * sim.lengthScale) / sim.waveSpeed;
        if (sim.useFreqTimeZero())
            freqTimeZero = sim.t - oldfreq * (sim.t - freqTimeZero) / frequency;
        else
            freqTimeZero = 0;
    }

    // 获取波长，保持与 Source 类一致
    double getWavelength() {
        return (freqScale / frequency) * sim.lengthScale;
    }

    // 获取真实频率，保持与 Source 类一致
    double getRealFrequency() {
        return sim.waveSpeed / getWavelength();
    }

    // 强制执行最大频率限制，保持与 Source 类一致
    void enforceMaxFrequency() {
        // enforce minimum wavelength of 6 pixels
        double maxfreq = freqScale / 6;
        if (frequency <= maxfreq)
            return;
        frequency = maxfreq;
        if (frequencyEditInfo != null) {
            frequencyEditInfo.value = getRealFrequency();
            EditDialog.theEditDialog.updateValue(frequencyEditInfo);
        }
        if (wavelengthEditInfo != null) {
            wavelengthEditInfo.value = getWavelength();
            EditDialog.theEditDialog.updateValue(wavelengthEditInfo);
        }
    }
}
