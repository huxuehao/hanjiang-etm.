<script lang="ts" setup>
import * as echarts from "echarts";
import { onMounted, onUnmounted, ref } from "vue";

let myChart: any = null;
let data: any = [];
let option: any = null;
let chartInterval: number;
let resizeObserver: any = null;

onMounted(() => {
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

  data = [
    { value: 48, name: "txt" },
    { value: 5, name: "png" },
    { value: 80, name: "xlsx" },
    { value: 44, name: "ppt" },
    { value: 30, name: "ptf" },
  ];
  option = {
    tooltip: {
      trigger: 'item',
    },
    series: {
      name: "文件类型分布",
      type: "pie",
      radius: '75%',
      center: ['50%', '50%'],
      tooltip: {
        valueFormatter: function (value: any) {
          return value + " 个";
        },
      },
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: "#fff",
        borderWidth: 2,
      },
      label: {
        show: false,
        position: "center",
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 40,
          fontWeight: "bold",
        },
      },
      labelLine: {
        show: false,
      },
      data: data,
    }
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
};
</script>

<template>
  <div ref="main" style="width: 100%; height: 100%"></div>
</template>

<style lang="scss" scoped></style>
