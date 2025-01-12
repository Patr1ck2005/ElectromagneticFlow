package com.falstad.ripple.client;


public class TimeCrystalMediumBox extends MediumBox {
    // 新增的字段
//    private double dynamicSpeedIndex;   // 实时的动态折射率
    private double amplitude;           // 折射率变化的振幅
    private double frequency;           // 变化的频率（模拟频率）
    private double freqTimeZero;        // 频率时间零点

    EditInfo frequencyEditInfo, wavelengthEditInfo;

    // 频率缩放因子，与 Source 类一致
    static final double freqScale = 92.0 / 3.0 * 0.5; // 约为15.333

    // 默认构造函数
    TimeCrystalMediumBox() {
        super();
        amplitude = 0.1;
        frequency = 0.5; // 与 Source 类默认频率一致
        freqTimeZero = 0.0;
    }

    // 通过 StringTokenizer 解析构造函数
    TimeCrystalMediumBox(StringTokenizer st) {
        super(st);
        amplitude = Double.parseDouble(st.nextToken());
        frequency = Double.parseDouble(st.nextToken());
        freqTimeZero = 0.0;
    }

    @Override
    void prepare() {
        // 更新相位
        double phase = frequency*sim.t;

        // 计算当前速度指数
        double dynamicEpsilon = Math.pow(1/speedIndex, 0.25) + amplitude * Math.sin(phase);
        double dynamicSpeedIndex = 1/dynamicEpsilon/dynamicEpsilon;

        RippleSim.drawMedium(
                topLeft.x, topLeft.y, topRight.x, topRight.y,
                bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y,
                dynamicSpeedIndex, dynamicSpeedIndex, dampingIndex, dampingIndex
        );
    }

    @Override
    public void run() {
        prepare();
    }

    @Override
    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            return new EditInfo("Dielectric Epsilon (1-2)", Math.pow(1/speedIndex, 0.25), 0, 1)
                    .setDimensionless();
        } else if (n == 1) {
            return new EditInfo("Amplitude", amplitude, 0, 1)
                    .setDimensionless();
        } else if (n == 2) {
            return new EditInfo("DampingIndex", dampingIndex, 0, 1)
                    .setDimensionless();
        } else if (n == 3) {
            return new EditInfo("Frequency (Hz)", getRealFrequency(), 4, 500) // 与 Source 类频率范围一致
                    .setDimensionless();
        }
        return null;
    }

    @Override
    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            speedIndex = getSpeedIndex(ei.value*ei.value);
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 1) {
            amplitude = ei.value;
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 2) {
            dampingIndex = ei.value;
            EditDialog.theEditDialog.updateValue(ei);
        } else if (n == 3) {
            setFrequency(ei.value); // 使用 setFrequency 方法
            enforceMaxFrequency();
            EditDialog.theEditDialog.updateValue(ei);
        }
    }

    @Override
    int getDumpType() {
        return 'T'; // 使用字符 'T' 作为类型标识符
    }

    @Override
    String dump() {
        return super.dump() + " " + amplitude + " " + frequency + " " + freqTimeZero;
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
