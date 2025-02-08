<script lang="ts" setup>
import { examAnswer, examAnswerV2, examUserConfig } from "@/api/examTemplate";
import { ElMessage, ElMessageBox } from "element-plus";
import { nextTick, onMounted, onUnmounted, reactive, ref } from "vue";
import { useRoute } from "vue-router";
import PaperPreview from "../paper/PaperPreview.vue";

const props = defineProps({
  examId: {
    type: String,
    default: null,
  },
  userId: {
    type: String,
    default: null,
  },
});

let loading = ref(false);
let loadingText = ref("");
// preview 标准模式，answer 培训模式,correct 审批模式 history 历史模式
let mode = ref("history");
let config = reactive<any>({});
let userInfo = reactive<any>({});
let paperPreviewVisible = ref<boolean | null>(null);

let active = ref(true);
let showAlert = ref(false);
let start = ref(0); // 记录失焦时间
let setTime = ref(0.25); // 切屏时间限制（分钟）
let isCheating = ref(false); // 是否正在切屏超时
let screenSwitchCount = ref(0); // 切屏次数
let isSmallScreen = ref(false); // 检测是否开启小窗
let accumulatedTime = ref(0); // 累计时间
let warningTime = ref(15); // 设置小窗15秒为阈值
let accumulatedTimeInterval = ref<any>(null);
let remainingTime = ref(15); // 剩余时间初始化为15秒
let countdownInterval = ref<any>(null); // 用于保存定时器引用
let isDonePaper = ref(false); // 是否交卷
let dialogVisible = ref(false); // 提示框类型
let message = ref("");
let title = ref("");
let icon = ref("");

onMounted(() => {
  nextTick(() => {
    init();
  });
  // 监听窗口关闭
  window.addEventListener("beforeunload", handelBeforeunload);
});
onUnmounted(() => {
  disableAntiCheat();
  // 销毁监听
  window.removeEventListener("beforeunload", handelBeforeunload);
});

// 窗口关闭监听处理器
const handelBeforeunload = () => {
  localStorage.setItem("reloadUserShow", new Date().getTime() + "");
};
// 添加防作弊事件监听
const enableAntiCheat = () => {
  window.addEventListener("blur", blurDom);
  window.addEventListener("focus", focusDom);
  window.addEventListener("resize", checkWindowSize);
};
// 移除防作弊事件监听
const disableAntiCheat = () => {
  window.removeEventListener("blur", blurDom);
  window.removeEventListener("focus", focusDom);
  window.removeEventListener("resize", checkWindowSize);
};
// 检查窗口尺寸，判断是否是小窗口
const checkWindowSize = () => {
  // 如果已经交卷，停止检测
  if (accumulatedTime.value >= warningTime.value || isDonePaper.value) return;

  const elementWidth = document.documentElement.clientWidth;
  isSmallScreen.value = elementWidth <= 1700; // 小窗口的阈值为1700px

  // 如果是小窗口并且没有计时器，且当前没有显示提示框
  if (isSmallScreen.value) {
    if (!accumulatedTimeInterval.value) {
      // 启动倒计时并显示警告
      startCountdown();

      // 开启计时器
      accumulatedTimeInterval.value = setInterval(() => {
        accumulatedTime.value += 1; // 每秒增加累计时间
        if (accumulatedTime.value >= warningTime.value) {
          closeAlertMessage();
          // 超过阈值后显示交卷提示框
          showAlertMessage(
            `窗口缩小已超过${warningTime.value}秒，已自动交卷！`,
            "警告",
            "el-icon-error"
          );
          handelPaperAnswer(config); // 超过阈值自动交卷
        }
      }, 1000);
    }
  } else {
    // 如果窗口恢复正常，停止计时器并关闭防作弊提示框
    if (accumulatedTimeInterval.value) {
      clearInterval(accumulatedTimeInterval.value);
      accumulatedTimeInterval.value = null;
      clearInterval(countdownInterval.value); // 倒计时结束，清除定时器
      countdownInterval.value = null; // 清除定时器引用
    }
    accumulatedTime.value = 0; // 重置累计时间
    closeAlertMessage();
  }
};

const startCountdown = () => {
  remainingTime.value = 15; // 初始化倒计时为15秒
  updateAlertMessage(); // 显示初始的倒计时（带样式）

  // 启动定时器，每秒更新一次
  countdownInterval.value = setInterval(() => {
    if (remainingTime.value > 0) {
      remainingTime.value -= 1; // 每秒减少1秒
      updateAlertMessage(); // 更新倒计时提示
    } else {
      clearInterval(countdownInterval.value); // 倒计时结束，清除定时器
      countdownInterval.value = null; // 清除定时器引用
      showAlertMessage(
        `窗口已缩小超过15秒，已自动交卷！`,
        "警告",
        "el-icon-error"
      );
      handelPaperAnswer(config); // 自动交卷
    }
  }, 1000);
};

// 标签页切换检测
const blurDom = () => {
  // 试卷已经完成，直接返回
  if (isDonePaper.value) return;
  // 如果切屏次数已经超过3次，直接调用原来的交卷方法
  if (screenSwitchCount.value >= 2) {
    showAlertMessage(
      "切屏次数已经超过2次，已自动交卷",
      "警告",
      "el-icon-error"
    );
    handelPaperAnswer(config);
    return;
  }

  // 增加切屏次数
  screenSwitchCount.value++;

  // 记录失去焦点的时间
  start.value = Date.now(); // 记录失焦时间
  isCheating.value = true; // 标记为切屏中
};
// 标签页返回检测
const focusDom = () => {
  if (isDonePaper.value) return; // 试卷已经完成，直接返回
  if (!isCheating.value) return; // 如果不是从切屏中返回，直接退出
  isCheating.value = false; // 解除切屏标记

  const now = Date.now();
  const diffTime = now - start.value; // 计算切屏时长（秒）

  if (diffTime > setTime.value * 60 * 1000) {
    showAlertMessage("切屏时间过长，已自动交卷", "警告", "el-icon-error");
    handelPaperAnswer(config); // 自动交卷
  } else {
    const message = `第
    <span style="color: red; font-weight: bold; font-size: 18px;">${screenSwitchCount.value}</span>
    次切屏或单次切屏时间超过
    <span style="color: red; font-weight: bold; font-size: 18px;">15</span>
    s将
    <span style="color: red; font-weight: bold; font-size: 18px;">自动交卷</span>`;
    showAlertMessage(message, "提示", "el-icon-warning");
  }
};
// 清理所有防作弊相关逻辑
const clearAntiCheat = () => {
  // 清除小窗检测定时器
  if (accumulatedTimeInterval.value) {
    clearInterval(accumulatedTimeInterval.value);
    accumulatedTimeInterval.value = null;
  }

  // 移除切屏和窗口尺寸的事件监听
  disableAntiCheat();

  // 重置防作弊相关数据
  accumulatedTime.value = 0;
  screenSwitchCount.value = 0; // 重置切屏次数
  isCheating.value = false; // 重置切屏标记
};
// 手动控制提示框的显示
const showAlertMessage = (message_: string, title_: string, icon_: string) => {
  message.value = message_;
  title.value = title_;
  icon.value = icon_;
  dialogVisible.value = true;
};
// 更新提示框中的倒计时内容
const updateAlertMessage = () => {
  const message = `请全屏窗口，超过
  <span style="color: red; font-weight: bold; font-size: 18px;">${remainingTime.value}</span>
  s将
  <span style="color: red; font-weight: bold; font-size: 18px;">自动交卷</span>！`;
  showAlertMessage(message, "提示", "el-icon-warning");
  showAlertMessage(message, "提示", "el-icon-warning");
};
const closeAlertMessage = () => {
  message.value = "";
  title.value = "";
  icon.value = "";
  dialogVisible.value = false;
};

const route = useRoute();
// 初始化
const init = () => {
  if (props.examId && props.userId) {
    loadConfig(props.examId, props.userId);
  } else {
    if (!route.params.params) {
      ElMessage({
        message: "缺少必要参数，加载失败",
        type: "warning",
        plain: true,
      });
    } else {
      const params_:string = String(route.params.params)
      const param = JSON.parse(atob(decodeURIComponent(params_)));
      const { exmaId, userId } = param;
      loadConfig(exmaId, userId);
    }
  }
};
// 加载配置
const loadConfig = (examId: string, userId: string) => {
  loading.value = true;
  examUserConfig(examId, userId)
    .then((res: any) => {
      Object.assign(config, res.data.data);
      config.title = config.name;
      userInfo = config.user[0];
      if (userInfo.corrected === 0 && userInfo.paperDone === 1) {
        // 用户已完成答题，但是未批改
        mode.value = "correct";
        paperPreviewVisible.value = true;
      } else if (userInfo.corrected === 1 && userInfo.paperDone === 1) {
        // 用户已完成答题，且已经批改
        mode.value = "history";
        paperPreviewVisible.value = true;
      } else if (userInfo.corrected === 0 && userInfo.paperDone === 0) {
        mode.value = "answer";
        // 根据 `cheat` 属性判断是否启用防作弊
        if (config.cheat === 1) {
          showAlert.value = true;
          enableAntiCheat();
          checkWindowSize();
        } else {
          showAlert.value = false;
        }
        paperPreviewVisible.value = true;
      } else {
        ElMessage({
          message: "模式匹配失败",
          type: "error",
          plain: true,
        });
      }
    })
    .catch((err) => {
      if (window.opener) {
        ElMessageBox.alert(err.message, "关闭提示", {
          confirmButtonText: "确定",
          callback: () => {
            window.close();
          },
        });
      }
    })
    .finally(() => {
      loading.value = false;
    });
};
// 交卷
const handelPaperAnswer = (config_: any) => {
  isDonePaper.value = true;
  // 真实交卷
  if (active.value) {
    loadingText.value = "试卷提交中";
    loading.value = true;

    // 清理小窗检测定时器
    if (accumulatedTimeInterval.value) {
      clearInterval(accumulatedTimeInterval.value);
      accumulatedTimeInterval.value = null;
    }
    accumulatedTime.value = 0; // 重置累计时间

    // 调用防作弊清理方法
    clearAntiCheat();

    if (config_.checkType === 0) {
      // 交卷+自动审批
      examAnswer(config_)
        .then(() => {
          init();
        })
        .finally(() => {
          nextTick(() => {
            loading.value = false;
            loadingText.value = "";
          });
        });
    } else {
      // 交卷+人工审批
      examAnswerV2(config_)
        .then(() => {
          paperPreviewVisible.value = false;
        })
        .finally(() => {
          nextTick(() => {
            loading.value = false;
            loadingText.value = "";
          });
        });
    }
  }
  // 模拟交卷
  else {
    mode.value = "history";
    paperPreviewVisible.value = false;
    ElMessage({
      message: "模拟交卷成功",
      type: "success",
      plain: true,
    });
  }
};

const emit = defineEmits(["close"]);
// 关闭试卷
const handelPaperClose = () => {
  if (props.examId && props.userId) {
    emit("close");
  } else {
    if (window.opener) {
      ElMessageBox.confirm("确认关闭当前页面？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        window.close();
      });
    } else {
      ElMessage({
        message: "此页面不能通过脚本关闭,请手动关闭浏览器标签",
        type: "warning",
        plain: true,
      });
    }
  }
};
</script>

<template>
  <div
    style="width: 100vw; height: 100vh;position: relative"
    v-loading="loading"
    :element-loading-text="loadingText"
  >
    <PaperPreview
      v-if="paperPreviewVisible === true"
      :data="config"
      :mode="mode"
      :key="mode"
      :showAlert="showAlert"
      alertContent="郑重说明：答题期间系统会持续监督您的行为。禁止切换浏览器标签、切换应用、最小化浏览器窗口等其他违规行为"
      @answer="handelPaperAnswer"
      @close="handelPaperClose"
    ></PaperPreview>
    <el-row v-if="paperPreviewVisible === false" style="margin-top: 20vh">
      <el-col :span="24">
        <el-result
          icon="success"
          title="您已完成答题"
          subTitle="请您耐心等待试卷批改结果"
        >
          <template slot="extra">
            <el-button type="primary" size="mini" @click="handelPaperClose"
              >关闭</el-button
            >
          </template>
        </el-result>
      </el-col>
    </el-row>
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
      width="30%"
    >
      <div>
        <i :class="icon" :style="{ marginRight: '8px', fontSize: '18px' }"></i>
        <span style="font-size: 16px" v-html="message"></span>
      </div>
      <span slot="footer">
        <el-button type="primary" @click="closeAlertMessage">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped></style>
