<script lang="ts" setup>
import { nextTick, onMounted, reactive, ref, shallowReactive } from "vue";
// 预览组价
import SingleQuestionPreview from "@/components/question/view/SingleQuestionPreview.vue";
import MultipleQuestionPreview from "@/components/question/view/MultipleQuestionPreview.vue";
import JudgeQuestionPreview from "@/components/question/view/JudgeQuestionPreview.vue";
import CompletionQuestionPreview from "@/components/question/view/CompletionQuestionPreview.vue";
import QaQuestionPreview from "@/components/question/view/QaQuestionPreview.vue";
import { ElMessageBox } from "element-plus";
import PaperComponent from "./PaperComponent.vue";

const tagType = {
  "-1": "info",
  "0": "danger",
  "1": "success",
  "2": "warning",
};

const previewComponents = [
  "",
  "SingleQuestionPreview",
  "MultipleQuestionPreview",
  "JudgeQuestionPreview",
  "CompletionQuestionPreview",
  "QaQuestionPreview",
];

const componentNames = shallowReactive<any>({
  SingleQuestionPreview,
  MultipleQuestionPreview,
  JudgeQuestionPreview,
  CompletionQuestionPreview,
  QaQuestionPreview,
});

let props = defineProps({
  data: {
    type: Object,
    default: {},
  },
  mode: {
    type: String,
    default: () => {
      // preview（预览）|answer（答题）|correct（批改）|history（历史）
      return "preview";
    },
  },
  alertContent: {
    type: String,
    default: () => {
      return "郑重说明：答题期间系统会持续监督您的行为。禁止切换浏览器标签、刷新浏览器、关闭浏览器、切换应用等其他违规行为，如有上述操作成绩作废！";
    },
  },
  closeAlert: {
    tye: Boolean,
    default: true,
  },
  showAlert: {
    tye: Boolean,
    default: true,
  },
});

let singleNum = ref(0);
let multipleNum = ref(0);
let judgeNum = ref(0);
let completionNum = ref(0);
let qaNum = ref(0);
let score = ref(0);
let percentage = ref(0);
let allScore = ref(0);
let showAlert_ = ref(false);
let numbers = ref<any[]>([]);
const config = reactive<any>({});

onMounted(() => {
  nextTick(() => {
    showAlert_.value = props.showAlert;
    // 初始化对象
    Object.assign(config, props.data);
    // 初始化序号
    initNum();
    // 初始化tag样式
    initTagStyle();
    // 计算得分/分数
    config.question.forEach((item: any) => {
      allScore.value = allScore.value + item.score;
      if (item.success === 1) {
        score.value += item.score;
      }
    });
    // 设置答题平台
    if (config.user) {
      config.user.forEach((item: any) => {
        if (!item.plat) {
          item.plat = "WEB";
        }
      });
    }
  });
});

// 初始化序号
const initNum = () => {
  for (let item of config.question) {
    switch (item.type) {
      case 1:
        singleNum.value += 1;
        break;
      case 2:
        multipleNum.value += 1;
        break;
      case 3:
        judgeNum.value += 1;
        break;
      case 4:
        completionNum.value += 1;
        break;
      case 5:
        qaNum.value += 1;
        break;
    }
  }
};

// 初始化tag样式
const initTagStyle = () => {
  for (let i = 0; i < config.question.length; i++) {
    if (["preview", "answer"].indexOf(props.mode) > -1) {
      numbers.value.push({
        value: String(i + 1 + "").padStart(2, "0"),
        done: false,
        current: false,
        correct: false,
        type: "warning",
        index: i,
      });
    } else if (props.mode === "correct") {
      numbers.value.push({
        value: String(i + 1 + "").padStart(2, "0"),
        done: false,
        current: false,
        correct: false,
        type: "info",
        index: i,
      });
      handleCorrectChange(config.question[i].success, i);
    } else {
      numbers.value.push({
        value: String(i + 1 + "").padStart(2, "0"),
        done: false,
        current: false,
        correct: false,
        type: tagType[
          (config.question[i].success + "") as "-1" | "0" | "1" | "2"
        ],
        index: i,
      });
    }
  }
};

// 关闭提示
const handleAlertClose = () => {
  showAlert_.value = false;
};

// 点击下一题
const HandelNextTag = () => {
  const currentTagArr = numbers.value.filter((item) => item.current);
  if (currentTagArr.length === 0) {
    handleClickIndex(numbers.value[0]);
  } else {
    const index = currentTagArr[0].index;
    if (index < numbers.value.length - 1) {
      handleClickIndex(numbers.value[index + 1]);
    } else {
      handleClickIndex(numbers.value[0]);
    }
  }
};

let paperTitle = ref();
let previewBox = ref();
const questionRefs = ref<any>({});
const setQuestionRef = (index: number) => {
  return (el: any) => {
    return (questionRefs.value[index] = el);
  };
};
// 点击答题卡
const handleClickIndex = (tag: any) => {
  // 高亮
  numbers.value.forEach((item) => (item.current = false));
  numbers.value[tag.index].current = true;
  // 定位
  const questionElement = questionRefs.value[tag.index]; // 注意这里[0]是因为v-for会生成一个数组
  if (questionElement) {
    let top = questionElement.$el.offsetTop + paperTitle.value.offsetHeight;
    if (showAlert_.value && ["preview", "answer"].indexOf(props.mode) > -1) {
      top = top + 65;
    } else {
      top = top + 15;
    }
    previewBox.value.scrollTo({ left: 0, top: top, behavior: "smooth" });
  }
};

const emits = defineEmits(["answer", "correct", "close"]);
// 考试超时
const handleTimeout = () => {
  ElMessageBox.confirm("答题时间已经用完，系统已自动完成交卷！", "提示", {
    confirmButtonText: "确定",
    showClose: true,
    showCancelButton: false,
    type: "warning",
  }).then(() => {
    emits("answer", config);
  });
};

const handleCorrectChange = (val: any, index: number) => {
  // 更新当前试题的配置
  config.question[index].success = val;
  // 修改tag样式
  numbers.value[index].type = tagType[(val + "") as "-1" | "0" | "1" | "2"];
  // 计算分数
  if (val === 1) {
    score.value += config.question[index].score;
  } else if (val === 0 && score.value > 0) {
    score.value -= config.question[index].score;
  }
};

// 做题
const doHandel = (value: any, type: number, index: number) => {
  if (["correct", "history"].indexOf(props.mode) > -1) {
    return;
  }
  if (index >= 0 && index < config.question.length) {
    numbers.value.forEach((item) => (item.current = false));
    numbers.value[index].current = true;
    switch (type) {
      case 3:
      case 1:
        // 设置选中项
        config.question[index].option.forEach((item: any) => {
          item.selected = 0;
          if (item.title === value) {
            item.selected = 1;
          }
        });
        // 设置tag
        numbers.value[index].done = true;
        numbers.value[index].type = "";
        break;
      case 2:
        if (value && value.length > 0) {
          const values = (value + "").split(",");
          // 设置选中项
          config.question[index].option.forEach((item: any) => {
            item.selected = 0;
            if (values.indexOf(item.title) > -1) {
              item.selected = 1;
            }
          });
          // 设置tag
          numbers.value[index].done = true;
          numbers.value[index].type = "";
        } else {
          // 取消选中项
          config.question[index].option.forEach((item: any) => {
            item.selected = 0;
          });
          // 设置tag
          numbers.value[index].done = false;
          numbers.value[index].type = "warning";
        }
        break;
      case 4:
        if (
          value.filter((item: any) => item.value && item.value.length > 0)
            .length === value.length
        ) {
          // 设置tag
          numbers.value[index].done = true;
          numbers.value[index].type = "";
        } else {
          // 设置tag
          numbers.value[index].done = false;
          numbers.value[index].type = "warning";
        }
        config.question[index].option = value;
        break;
      case 5:
        if (value && value.length > 0) {
          config.question[index].answer = value;
          // 设置tag
          numbers.value[index].done = true;
          numbers.value[index].type = "";
        } else {
          config.question[index].answer = null;
          // 设置tag
          numbers.value[index].done = false;
          numbers.value[index].type = "warning";
        }
        break;
    }
    computedPercentage();
  }
};

// 计算进度
const computedPercentage = () => {
  const counts = numbers.value.length;
  const doneCounts = numbers.value.filter((item) => {
    if (item.done) {
      return item;
    }
  }).length;
  percentage.value = Math.ceil((doneCounts / counts) * 100);
};

// 交卷
const submitAnswer = () => {
  const notDo = numbers.value.filter((item) => {
    if (!item.done) {
      return item;
    }
  });
  ElMessageBox.confirm(
    `${notDo.length > 0 ? "您有未完成作答的试题，确认交卷？" : "确认交卷？"}`,
    `${notDo.length > 0 ? "警告" : "提示"}`,
    {
      confirmButtonText: "确认交卷",
      cancelButtonText: "取消",
      type: `${notDo.length > 0 ? "error" : "warning"}`,
    }
  ).then(() => {
    emits("answer", config);
  });
};

// 提交批改
const submitCorrect = () => {
  const notCorrect = numbers.value
    .filter((item) => item.type === "info")
    .map((item) => {
      return item.value;
    });
  if (notCorrect.length > 0) {
    ElMessageBox.alert(
      `试题 ${notCorrect.join("、")} 未批改，请完成批改后再提交`,
      "提示",
      {
        confirmButtonText: "确定",
        type: "warning",
      }
    );
  } else {
    ElMessageBox.confirm("确认提交审批", "提示", {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      emits("correct", config);
    });
  }
};
</script>

<template>
  <el-scrollbar ref="previewBox" height="height:calc( 100vh - 40px)">
    <el-row v-if="showAlert && ['preview', 'answer'].indexOf(mode) > -1">
      <el-alert
        style="font-weight: bolder; letter-spacing: 1px"
        :title="alertContent"
        :closable="closeAlert"
        @close="handleAlertClose"
        type="warning"
        center
      >
      </el-alert>
    </el-row>
    <el-row>
      <!--左侧区域-->
      <el-col :span="4">
        <div style="position: sticky; top: 0">
          <div style="padding: 8px">
            <el-card>
              <template #header>
                <div style="text-align: center">
                  <span style="font-weight: bolder">答题卡</span>
                </div>
              </template>
              <div
                style="
                  text-align: center;
                  max-height: calc(100vh - 150px);
                  overflow: auto;
                "
              >
                <template v-for="item in numbers">
                  <el-tag
                    disable-transitions
                    :type="item.type"
                    :class="
                      'tag tag-item-' +
                      item.type +
                      ' ' +
                      (item.current ? 'current-tag-' + item.type : '')
                    "
                    size="medium"
                    @click="handleClickIndex(item)"
                  >
                    {{ item.value }}
                  </el-tag>
                </template>
              </div>
            </el-card>
          </div>
          <div style="padding: 8px">
            <el-card>
              <el-button
                size="default"
                @click="HandelNextTag"
                style="width: 100%; font-size: 14px"
                plain
              >
                下一题</el-button
              >
            </el-card>
          </div>
        </div>
      </el-col>
      <!--中间区域-->
      <el-col :span="16">
        <div style="padding: 8px">
          <el-card class="box-card">
            <!--试卷头-->
            <el-row>
              <div class="title" ref="paperTitle">
                <div>{{ config.title }}</div>
                <div
                  style="
                    font-size: 12px;
                    margin-top: 10px;
                    font-weight: lighter;
                    color: #484848;
                  "
                >
                  <span>试卷说明：</span>
                  <span>本试卷共</span>
                  <span v-if="singleNum > 0">{{ singleNum }}道单选题，</span>
                  <span v-if="multipleNum > 0"
                    >{{ multipleNum }}道多选题，</span
                  >
                  <span v-if="judgeNum > 0">{{ judgeNum }}道判断题，</span>
                  <span v-if="completionNum > 0"
                    >{{ completionNum }}道填空题，</span
                  >
                  <span v-if="qaNum > 0">{{ qaNum }}道解答题，</span>
                  <span>满分{{ allScore }}分，</span>
                  <span>考试时间{{ config.duration }}分钟。</span>
                </div>
              </div>
            </el-row>
            <!--试卷内容-->
            <el-row>
              <div style="padding: 10px 20px;width: 100%;">
                <template v-for="(item, index) in config.question">
                  <component
                    :is="componentNames[previewComponents[item.type]]"
                    :data="item"
                    :number="index + 1"
                    :mode="mode"
                    :ref="setQuestionRef(index)"
                    style="margin-bottom: 50px"
                    @correct-change="(val: any) => {handleCorrectChange(val, index)}"
                    @input="(value:any, type:number) => {doHandel(value, type, index)}"
                  ></component>
                </template>
              </div>
            </el-row>
          </el-card>
        </div>
      </el-col>
      <!--右侧区域-->
      <el-col :span="4">
        <div style="position: sticky; top: 0">
          <!--右侧小组件-->
          <PaperComponent
            :mode="mode"
            :duration="config.duration"
            :percentage="percentage"
            :allScore="allScore"
            :score="score"
            @timeout="handleTimeout"
          ></PaperComponent>
          <!--交卷-->
          <div
            style="padding: 8px"
            v-if="['preview', 'answer'].indexOf(mode) > -1"
          >
            <el-card>
              <el-button
                size="default"
                @click="submitAnswer"
                style="width: 100%; font-size: 16px"
                type="primary"
              >
                <i
                  class="el-icon-position"
                  style="margin-right: 5px; font-size: 16px"
                ></i>
                <span>交卷</span>
              </el-button>
            </el-card>
          </div>
          <!--批改-->
          <div style="padding: 8px" v-if="['correct'].indexOf(mode) > -1">
            <el-card>
              <el-button
                size="default"
                @click="submitCorrect"
                style="width: 100%; font-size: 16px"
                type="primary"
              >
                <i
                  class="el-icon-position"
                  style="margin-right: 5px; font-size: 16px"
                ></i>
                <span>提交批改</span>
              </el-button>
            </el-card>
          </div>
          <!--关闭-->
          <div
            style="padding: 8px"
            v-if="['preview', 'correct', 'history'].indexOf(mode) > -1"
          >
            <el-card>
              <el-button
                size="default"
                @click="
                  () => {
                    emits('close');
                  }
                "
                style="width: 100%; font-size: 16px"
              >
                <i
                  class="el-icon-close"
                  style="margin-right: 5px; font-size: 16px"
                ></i>
                <span>关闭</span>
              </el-button>
            </el-card>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-scrollbar>
</template>

<style lang="scss" scoped>
.title {
  width: 100%;
  min-height: 0;
  text-align: center;
  letter-spacing: 1px;
  font-weight: bolder;
  font-size: 25px;
  padding: 10px 20px;
}

.tag {
  cursor: pointer;
  user-select: none;
  margin: 5px !important;
  &-item-:hover {
    // font-weight: bolder;
    border-color: #4BA3FF;
  }
  &-item-success:hover {
    // font-weight: bolder;
    border-color: #67c23a;
  }
  &-item-info:hover {
    // font-weight: bolder;
    border-color: #909399;
  }
  &-item-warning:hover {
    // font-weight: bolder;
    border-color: #e6a23c;
  }
  &-item-danger:hover {
    // font-weight: bolder;
    border-color: #f56c6c;
  }
}
.current-tag {
  &- {
    // font-weight: bolder;
    border-color: #4BA3FF !important;
  }
  &-success {
    // font-weight: bolder;
    border-color: #67c23a !important;
  }
  &-info {
    // font-weight: bolder;
    border-color: #909399 !important;
  }
  &-warning {
    // font-weight: bolder;
    border-color: #e6a23c !important;
  }
  &-danger {
    // font-weight: bolder;
    border-color: #f56c6c !important;
  }
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}

.el-card {
  --el-card-padding: 10px !important;
}
</style>
