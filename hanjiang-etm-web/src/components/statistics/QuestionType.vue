<script lang="ts" setup>
import { getQuestionType } from "@/api/statistics";
import * as echarts from "echarts";
import { onMounted, onUnmounted, ref } from "vue";

let myChart: any = null;
let data: any = [
  {
    value: 20,
    name: "单选题",
    title: {
      offsetCenter: ["0%", "-55%"],
      fontSize: 12,
    },
    tooltip: {
      valueFormatter: function (value: any) {
        return value + " 道";
      },
    },
    detail: {
      valueAnimation: true,
      offsetCenter: ["0%", "-45%"],
    },
  },
  {
    value: 40,
    name: "多选题",
    title: {
      offsetCenter: ["0%", "-30%"],
      fontSize: 12,
    },
    tooltip: {
      valueFormatter: function (value: any) {
        return value + " 道";
      },
    },
    detail: {
      valueAnimation: true,
      offsetCenter: ["0%", "-20%"],
    },
  },
  {
    value: 60,
    name: "判断题",
    title: {
      offsetCenter: ["0%", "-5%"],
      fontSize: 12,
    },
    tooltip: {
      valueFormatter: function (value: any) {
        return value + " 道";
      },
    },
    detail: {
      valueAnimation: true,
      offsetCenter: ["0%", "5%"],
    },
  },
  {
    value: 10,
    name: "填空题",
    title: {
      offsetCenter: ["0%", "19%"],
      fontSize: 12,
    },
    tooltip: {
      valueFormatter: function (value: any) {
        return value + " 道";
      },
    },
    detail: {
      valueAnimation: true,
      offsetCenter: ["0%", "29%"],
    },
  },
  {
    value: 40,
    name: "解答题",
    title: {
      offsetCenter: ["0%", "45%"],
      fontSize: 12,
    },
    tooltip: {
      valueFormatter: function (value: any) {
        return value + " 道";
      },
    },
    detail: {
      valueAnimation: true,
      offsetCenter: ["0%", "55%"],
    },
  },
];
let option: any = null;
let chartInterval: number;
let resizeObserver: any = null;

let loading = ref(false);
onMounted(async () => {
  loading.value = true;
  await getQuestionType()
    .then((res) => {
      const data_ = res.data.data;
      for (let i = 0; i < data_.length; i++) {
        data[i].name = data_[i].name;
        data[i].value = data_[i].value;
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

  option = {
    tooltip: {
      trigger: "item", // 触发类型，'item' 表示数据项触发，'axis' 表示坐标轴触发
    },
    series: [
      {
        name: "题目类型分布",
        type: "gauge",
        startAngle: 90,
        endAngle: -270,
        pointer: {
          show: false,
        },
        radius: "85%",
        center: ["50%", "50%"],
        progress: {
          show: true,
          overlap: false,
          roundCap: true,
          clip: false,
          itemStyle: {
            borderWidth: 1,
            borderColor: "#E6EBF8",
          },
        },
        axisLine: {
          lineStyle: {
            width: 35,
          },
        },
        splitLine: {
          show: false,
          distance: 0,
          length: 10,
        },
        axisTick: {
          show: false,
        },
        axisLabel: {
          show: false,
          distance: 50,
        },
        // color: ["#3B65B5", "#D29567","#286034", "#679A9B","#537C7E"],
        data: data,
        title: {
          fontSize: 14,
        },
        detail: {
          width: 50,
          height: 14,
          fontSize: 14,
          color: "inherit",
          borderColor: "inherit",
          formatter: "{value}道",
        },
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
