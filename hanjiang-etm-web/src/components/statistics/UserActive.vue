<script lang="ts" setup>
import { onMounted, onUnmounted, ref } from "vue";
import * as echarts from "echarts";
import { getUserActive } from "@/api/statistics";

let myChart: any = null;
let data: any = [];
let option: any = null;
let chartInterval: number = 0;
let resizeObserver: any = null;

let loading = ref(false);
onMounted(async () => {
  loading.value = true;
  await getUserActive()
    .then((res) => {
      const data_ = res.data.data;
      for (let item of data_) {
        data.push([item.time, item.value]);
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
  if (chartInterval) {
    clearInterval(chartInterval);
  }
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
    // 工具提示
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "cross",
        crossStyle: {
          color: "#999",
        },
      },
    },

    // 折线图网格配置
    grid: {
      top: "50px",
      bottom: "20px",
      left: "30px",
      right: "50px",
      containLabel: true,
    },

    // x轴配置
    xAxis: {
      type: "category", // time category
      boundaryGap: false, // 两头是否是否留白
    },

    // y轴配置
    yAxis: {
      type: "value",
      name: "用户活跃次数统计",
      axisLabel: {
        formatter: "{value} 次",
      },
      axisLine: {
        show: true,
      },
    },

    // 折线图数据域
    series: [
      {
        name: "用户活跃状态",
        tooltip: {
          valueFormatter: function (value: any) {
            return value + " 次";
          },
        },
        data: data,
        type: "line", // 折线图 line; 柱状图 bar; 饼状图 pie
        symbol: "circle", // 折点形状 'circle', 'rect', 'roundRect', 'triangle', 'diamond', 'pin', 'arrow', 'none'
        showSymbol: false, // 只有在 tooltip hover 的时候显示
        smooth: true, // 是否平滑
        stack: false, // 是否堆叠
        itemStyle: {
          shadowColor: "#ffffff",
          shadowBlur: 5,
        },
        areaStyle: {
          opacity: 0.8,
          color: {
            type: "linear",
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: "#5470C6",
              },
              {
                offset: 1,
                color: "rgb(255,255,255)",
              },
            ],
          },
        },
        emphasis: {
          focus: "series",
        },
        lineStyle: {
          // 系列级个性化折线样式
          width: 2,
          type: "solid",
          color: "#5470C6",
        },
      },
    ],
  };
  loading.value = false;
  if (option) {
    myChart.off("click");
    myChart.on("click", (params: any) => {
      console.log(params);
    });
    myChart.setOption(option);
  }
};
</script>

<template>
  <div v-loading="loading" ref="main" style="width: 100%; height: 100%"></div>
</template>

<style lang="scss" scoped></style>
