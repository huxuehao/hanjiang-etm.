<script lang="ts" setup>
import { getExamTrainStatus } from "@/api/statistics";
import * as echarts from "echarts";
import { onMounted, onUnmounted, ref } from "vue";

let myChart: any = null;
let exam: any = [0, 0, 0, 0, 0];
let train: any = [0, 0, 0, 0, 0];
let option: any = null;
let chartInterval: number;
let resizeObserver: any = null;

let loading = ref(false);
onMounted(async () => {
  loading.value = true;
  await getExamTrainStatus().then((res) => {
    const data_ = res.data.data;
      for (let item of data_["exam"]) {
        exam[item.index - 1] = item.value;
      }
      for (let item of data_["train"]) {
        train[item.index - 1] = item.value;
      }
  }).catch(() => {
    loading.value = false;
  })
  initChart();
  monitorSearchResize();
});

onUnmounted(() => {
  if (resizeObserver && main.value) {
    resizeObserver.unobserve(main.value); // 停止监听指定的 DOM 元素
  }
  // if (chartInterval) {
  //   clearInterval(chartInterval);
  // }
  if (myChart) {
    myChart.dispose();
  }
});

// 监听窗口大小变化
const monitorSearchResize = () => {
  resizeObserver = new ResizeObserver(() => {
    if (myChart) {
      myChart.resize();
    }
  });
  resizeObserver.observe(main.value);
};

let main = ref();
const initChart = () => {
  myChart = echarts.init(main.value, null, {
    renderer: "svg",
  });

  option = {
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "cross",
        crossStyle: {
          color: "#999",
        },
      },
    },
    grid: {
      top: "50px",
      bottom: "20px",
      left: "30px",
      right: "30px",
      containLabel: true,
    },
    xAxis: [
      {
        type: "category",
        data: ["未发布", "已发布", "未开始", "进行中", "已结束"],
        axisPointer: {
          type: "shadow",
        },
      },
    ],
    yAxis: [
      {
        type: "value",
        name: "考试/培训状态统计",
        axisLabel: {
          formatter: "{value} 场",
        },
      },
    ],
    series: [
      {
        name: "考试",
        type: "bar",
        barWidth: 30, //柱条的宽度,默认自适应
        tooltip: {
          valueFormatter: function (value: any) {
            return value + " 场";
          },
        },
        data: exam,
      },
      {
        name: "培训",
        type: "bar",
        barWidth: 30, //柱条的宽度,默认自适应
        tooltip: {
          valueFormatter: function (value: any) {
            return value + " 场";
          },
        },
        data: train,
      },
    ],
  };
  // chartInterval = setInterval(function () {
  //   data[0].value = +(Math.random() * 100).toFixed(2);
  //   data[1].value = +(Math.random() * 100).toFixed(2);
  //   data[2].value = +(Math.random() * 100).toFixed(2);
  //   myChart.setOption({
  //     series: [
  //       {
  //         data: data,
  //         pointer: {
  //           show: false,
  //         },
  //       },
  //     ],
  //   });
  // }, 2000);

  option && myChart.setOption(option);
  loading.value = false;
};
</script>

<template>
  <div v-loading="loading" ref="main" style="width: 100%; height: 100%"></div>
</template>

<style lang="scss" scoped></style>
