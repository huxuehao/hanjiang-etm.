<script lang="ts" setup>
import { getQuestionLevel } from "@/api/statistics";
import * as echarts from "echarts";
import { onMounted, onUnmounted, ref } from "vue";

let myChart: any = null;
let series: any[] = [];
let option: any = null;
let chartInterval: number;
let resizeObserver: any = null;
let one: any[] = [0, 0, 0, 0, 0];
let two: any[] = [0, 0, 0, 0, 0];
let three: any[] = [0, 0, 0, 0, 0];
let four: any[] = [0, 0, 0, 0, 0];
let five: any[] = [0, 0, 0, 0, 0];

let loading = ref(false);
onMounted(async () => {
  loading.value = true;
  await getQuestionLevel()
    .then((res) => {
      const data_ = res.data.data;
      for (let item of data_["one"]) {
        one[item.type - 1] = item.value;
      }
      for (let item of data_["two"]) {
        two[item.type - 1] = item.value;
      }
      for (let item of data_["three"]) {
        three[item.type - 1] = item.value;
      }
      for (let item of data_["four"]) {
        four[item.type - 1] = item.value;
      }
      for (let item of data_["five"]) {
        five[item.type - 1] = item.value;
      }
    })
    .catch(() => {
      loading.value = false;
    });
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

  series = [
    {
      data: one,
      type: "bar",
      barWidth: 30,
      tooltip: {
        valueFormatter: function (value: any) {
          return value + " 道";
        },
      },
      stack: "total",
      name: "简单",
    },
    {
      data: two,
      type: "bar",
      barWidth: 30,
      tooltip: {
        valueFormatter: function (value: any) {
          return value + " 道";
        },
      },
      stack: "total",
      name: "一般",
    },
    {
      data: three,
      type: "bar",
      barWidth: 30,
      tooltip: {
        valueFormatter: function (value: any) {
          return value + " 道";
        },
      },
      stack: "total",
      name: "中等",
    },
    {
      data: four,
      type: "bar",
      barWidth: 30,
      tooltip: {
        valueFormatter: function (value: any) {
          return value + " 道";
        },
      },
      stack: "total",
      name: "较难",
    },
    {
      data: five,
      type: "bar",
      barWidth: 30,
      tooltip: {
        valueFormatter: function (value: any) {
          return value + " 道";
        },
      },
      stack: "total",
      name: "很难",
    },
  ];
  const stackInfo: any = {};
  for (let i = 0; i < series[0].data.length; ++i) {
    for (let j = 0; j < series.length; ++j) {
      const stackName = series[j].stack;
      if (!stackName) {
        continue;
      }
      if (!stackInfo[stackName]) {
        stackInfo[stackName] = {
          stackStart: [],
          stackEnd: [],
        };
      }
      const info = stackInfo[stackName];
      const data = series[j].data[i];
      if (data && data !== "-") {
        if (info.stackStart[i] == null) {
          info.stackStart[i] = j;
        }
        info.stackEnd[i] = j;
      }
    }
  }
  for (let i = 0; i < series.length; ++i) {
    const data = series[i].data;
    const info = stackInfo[series[i].stack];
    for (let j = 0; j < series[i].data.length; ++j) {
      const isEnd = info.stackEnd[j] === i;
      const topBorder = isEnd ? 20 : 0;
      const bottomBorder = 0;
      data[j] = {
        value: data[j],
        itemStyle: {
          borderRadius: [topBorder, topBorder, bottomBorder, bottomBorder],
        },
      };
    }
  }
  option = {
    // 工具提示
    tooltip: {
      trigger: "axis",
    },
    // 折线图网格配置
    grid: {
      top: "50px",
      bottom: "20px",
      left: "40px",
      right: "40px",
      containLabel: true,
    },
    xAxis: {
      type: "category",
      data: ["单选题", "多选题", "判断题", "填空题", "解答题"],
    },
    yAxis: {
      type: "value",
      name: "题目难度分布统计",
      axisLabel: {
        formatter: "{value} 道",
      },
    },
    series: series,
  };
  // chartInterval = setInterval(function () {
  //   gaugeData[0].value = +(Math.random() * 100).toFixed(2);
  //   gaugeData[1].value = +(Math.random() * 100).toFixed(2);
  //   gaugeData[2].value = +(Math.random() * 100).toFixed(2);
  //   myChart.setOption({
  //     series: [
  //       {
  //         data: gaugeData,
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
