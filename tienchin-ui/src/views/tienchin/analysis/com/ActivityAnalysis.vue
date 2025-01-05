<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" >
      <el-form-item label="创建时间" >
        <el-date-picker
            v-model="queryParams.dateRange"
            value-format="YYYY-MM-DD hh:mm:ss"
            format="YYYY-MM-DD hh:mm:ss"
            type="datetimerange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <v-chart class="chart" :option="option" />
    <v-chart class="chart" :option="option2" />
    <div>
      <el-table :data="activityData" style="width: 100%">
        <el-table-column prop="name" label="活动名称" width="180" />
        <el-table-column prop="count" label="线索数量" width="180" />
        <el-table-column prop="available" label="线索有效率" />
      </el-table>
    </div>
  </div>

</template>

<script setup name="activityAnalysis">
import { use } from "echarts/core";
import { CanvasRenderer } from "echarts/renderers";
import { PieChart } from "echarts/charts";
const { proxy } = getCurrentInstance();
import "echarts/lib/component/grid"
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent
} from "echarts/components";
import VChart, { THEME_KEY } from "vue-echarts";
import { ref, provide } from "vue";
import {activityAnalysisData} from "@/api/tienchin/analysis.js";

use([
  CanvasRenderer,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent
]);

provide(THEME_KEY, "light");

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    phone: undefined,
    channelId: undefined,
    dateRange: undefined,
    status: undefined
  },
});

const { queryParams } = toRefs(data);
const activityData = ref([]);
const option = ref({
  title: {
    text: "活动总数分析",
    left: "center"
  },
  tooltip: {
    trigger: "item",
    formatter: "{a} <br/>{b} : {c} ({d}%)"
  },
  legend: {
    orient: "vertical",
    left: "left",
    data: ["Direct", "Email", "Ad Networks", "Video Ads", "Search Engines"]
  },
  series: [
    {
      name: "活动分析",
      type: "pie",
      radius: "55%",
      center: ["50%", "60%"],
      data: [
        { value: 335, name: "Direct" },
        { value: 310, name: "Email" },
        { value: 234, name: "Ad Networks" },
        { value: 135, name: "Video Ads" },
        { value: 1548, name: "Search Engines" }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: "rgba(0, 0, 0, 0.5)"
        }
      }
    }
  ]
});

const option2 = ref({
  title:{
    text: '活动有效率分析',
    left: 'center'
  },
  tooltip:{
    trigger:'axis'
  },
  legend:{
    data:['活动线索有效率'],
    bottom:0
  },
  xAxis:{
    data: []
  },
  yAxis:{},
  series:{
    type:'line',
    data:[],
    name: '活动线索有效率'
  }
});
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
function handleClueData(data){
  clueData.value = JSON.parse(JSON.stringify(data.increase));
  let total = data.total;
  for(let i = 0; i < total.length; i++){
    clueData.value[i].y2 = total[i].y;
  }
}
/** 查询线索列表 */
function getList() {
  activityAnalysisData(proxy.addDateRange(queryParams.value, queryParams.value.dateRange)).then(response => {
    const {data} = response;
    option.value.series[0].data = [];
    option.value.legend.data = [];
    option2.value.xAxis.data = [];
    option2.value.series.data = [];
    activityData.value = data;
    for (let i = 0; i < data.length; i++) {
      const  {name, count, available} = data[i];
      let newItem = {name, value: count};
      option.value.series[0].data.push(newItem);
      option.value.legend.data.push(name);
      option2.value.xAxis.data.push(name);
      option2.value.series.data.push(available);

    }
  });
}
function resetQuery(){
  proxy.resetForm("queryRef");
  handleQuery();
}
getList();
</script>

<style scoped>
.chart {
  height: 400px;
}
</style>
