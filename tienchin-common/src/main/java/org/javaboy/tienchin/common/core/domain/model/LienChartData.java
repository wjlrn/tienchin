package org.javaboy.tienchin.common.core.domain.model;

import java.util.List;

/**
 * 折线图的数据实体类
 */
public class LienChartData {
    private List<EchartPoint> increase;
    private List<EchartPoint> total;

    public List<EchartPoint> getIncrease() {
        return increase;
    }

    public void setIncrease(List<EchartPoint> increase) {
        this.increase = increase;
    }

    public List<EchartPoint> getTotal() {
        return total;
    }

    public void setTotal(List<EchartPoint> total) {
        this.total = total;
    }
}
