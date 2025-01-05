<template>
  <div class="app-container">
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
    <div>
      <v-chart class="business_chart" :option="option" />
    </div>
    <div>
      <el-table :data="businessData" style="width: 100%">
        <el-table-column prop="x" label="时间" width="180" />
        <el-table-column prop="y" label="商机增量" width="180" />
        <el-table-column prop="y2" label="商机总量" />
      </el-table>
    </div>
  </div>
</template>

<script setup name="BusinessAnalysis">
import { use } from "echarts/core";
import { CanvasRenderer } from "echarts/renderers";
import { PieChart, LineChart } from "echarts/charts";
const { proxy } = getCurrentInstance();
const { sys_user_sex, course_type, course_apply_to } = proxy.useDict("sys_user_sex", "course_type", "course_apply_to");
import "echarts/lib/component/grid"
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent
} from "echarts/components";
import VChart, { THEME_KEY } from "vue-echarts";
import { ref, provide } from "vue";
import {listChannels,} from "@/api/tienchin/clue.js";
import {businessAnalysisData} from "@/api/tienchin/analysis.js";

use([
  CanvasRenderer,
  PieChart,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent
]);
const businessData = ref([]);

provide(THEME_KEY, "light");

const channelList = ref([]);
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

const option = ref({
  title:{
    text: '商机分析',
    left: 'center'
  },
  tooltip:{
    trigger:'axis'
  },
  legend:{
    data:['增量数据', '存量数据'],
    bottom:0
  },
  xAxis:{
    data: []
  },
  yAxis:{},
  series:[{
    type:'line',
    data:[],
    name: '增量数据'
  },{
    type:'line',
    data:[],
    name: '存量数据'
  }]
});

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
function handleBusinessData(data){
  businessData.value = JSON.parse(JSON.stringify(data.increase));
  let total = data.total;
  for(let i = 0; i < total.length; i++){
    businessData.value[i].y2 = total[i].y;
  }
}
/** 查询商机列表 */
function getList() {
  businessAnalysisData(proxy.addDateRange(queryParams.value, queryParams.value.dateRange)).then(response => {
    const {increase, total} = response.data;
    option.value.xAxis.data = [];
    let increaseData = new Object();
    increaseData.name = '增量数据';
    increaseData.type = 'line';
    increaseData.data = [];
    let totalData = new Object();
    totalData.name = '存量数据';
    totalData.type = 'line';
    totalData.data = [];
    for (let i = 0; i < increase.length; i++) {
      option.value.xAxis.data.push(increase[i].x);
      increaseData.data.push(increase[i].y);
      totalData.data.push(total[i].y);
    }
    option.value.series = [];
    option.value.series.push(increaseData);
    option.value.series.push(totalData);
    handleBusinessData(response.data);
  });
}
function getAllChannels(){
  listChannels().then(response=>{
    channelList.value = response.data;
  })
}
function resetQuery(){
  proxy.resetForm("queryRef");
  handleQuery();
}
getAllChannels();
</script>

<style scoped>
.business_chart {
  height: 400px;
  width: 1262px;
}
</style>
