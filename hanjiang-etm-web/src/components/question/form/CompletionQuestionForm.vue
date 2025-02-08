<script lang="ts" setup>
import { ElMessage } from "element-plus";
import { nextTick, onMounted, reactive, ref } from "vue";
import { chineseNumbers, prop, addReadStar } from "./common";

let props = defineProps({
  showClassify: {
    type: Boolean,
    default: () => true,
  },
  classifyDict: {
    type: Array,
    default: () => [],
  },
  showShare: {
    type: Boolean,
    default: () => true,
  },
  mode: {
    type: String,
    default: () => "add",
  },
  data: {
    type: Object,
    default: () => {
      return {};
    },
  },
  disableEdit: {
    type: Boolean,
    default: () => false,
  },
});

let form = ref()
let cursorPosition = ref(-1);
let config = reactive<any>({
  classifyId: null,
  name: null,
  title: "",
  option: [],
  answer: [],
  score: 5,
  analysis: null,
  level: 2,
  type: 4,
  share: 1,
  success: -1,
});
const rules = {
  classifyId: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  name: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  title: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  score: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  share: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  level: [
    {
      required: true,
      message: "必填项",
      trigger: "change",
    },
  ],
};

onMounted(() => {
  nextTick(() => {
    if (props.mode === "edit") {
      Object.assign(config, props.data);
      config.success = -1;
    }
  });
});

// 生成实体
const genEntity = () => {
  config.option = JSON.parse(JSON.stringify(config.answer));
  config.option.forEach((item: any) => (item.value = null));
  return {
    id: config.id || null,
    classifyId: config.classifyId || null,
    type: config.type,
    name: config.name || null,
    title: config.title || null,
    score: config.score,
    level: config.level,
    config: JSON.stringify(config),
    share: config.share,
    success: config.success || 0,
  };
};
// 获取配置
const getConfig = () => {
  return config;
};

// 添加选项
const clickAddOption = () => {
  if (config.answer.length > 8) {
    ElMessage({
      message: "抱歉，最多支持9个填空",
      type: "warning",
      plain: true,
    });
    return;
  }
  config.title = insertString(config.title, "(__)", cursorPosition.value);
  cursorPosition.value = -1;
  config.answer.push({
    rowKey: config.answer.length + "",
    title: null,
    value: null,
    selected: 0,
    sort: 5,
  });
  config.share = 1;
  genTitleForOption();
  genValueForOption();
};
// 删除选项
const clickRemoveOption = (index: number) => {
  config.title = removeParenthesis(config.title, index);
  config.answer.splice(index, 1);
  genTitleForOption();
};
// 向字符串的指定位置插入字符串
const insertString = (
  originalString: string,
  stringToInsert: string,
  position: number
) => {
  // 确保位置不超过字符串长度
  if (position < 0 || position > originalString.length) {
    throw new Error("抱歉位置超出界限");
  }
  // 使用 slice 插入字符
  return (
    originalString.slice(0, position) +
    stringToInsert +
    originalString.slice(position)
  );
};
// 删除指定的括号
const removeParenthesis = (originalString: string, index: number) => {
  // 使用正则表达式匹配第二个左括号及其后面的内容
  let count = -1;

  return originalString.replace(/\(__\)/g, (match) => {
    count++;
    // 当找到第二个括号时，返回空字符串
    return count === index ? "" : match;
  });
};
// 为选项生成title
const genTitleForOption = () => {
  config.answer = config.answer.map((item: any, index: number) => {
    // 65 是 'A' 的 ASCII 码
    item.title = "第" + chineseNumbers[++index] + "个填空";
    return item;
  });
};
// 为选项生成value
const genValueForOption = () => {
  config.answer = config.answer.map((item: any, index: number) => {
    if (!item.value) {
      item.value = "答案" + chineseNumbers[++index];
    }
    return item;
  });
};
// 获取焦点位置
const inputRef = ref();
const getCursorPosition = () => {
  // 为了兼容不同浏览器，延迟获取光标
  setTimeout(() => {
    const inputElement = inputRef.value.$el.querySelector("textarea");
    if (inputElement) {
      // 获取光标位置
      cursorPosition.value = inputElement.selectionStart;
    }
  }, 0); // 延迟获取光标位置
};

defineExpose({ config, form, genEntity, getConfig });
</script>

<template>
  <div style="margin: 0 20px">
    <el-form
      label-width="50px"
      ref="form"
      :model="config"
      :rules="rules"
    >
      <el-row v-if="showClassify">
        <el-col :span="24">
          <el-form-item label="分类" prop="classifyId">
            <el-tree-select
              v-model="config.classifyId"
              :data="classifyDict"
              :render-after-expand="true"
              :default-expand-all="true"
              :disabled="disableEdit"
              :props="prop"
              filterable
              check-strictly
              clearable></el-tree-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="题干" prop="title">
            <el-input
              v-model="config.title"
              ref="inputRef"
              @focus="getCursorPosition"
              @input="getCursorPosition"
              type="textarea"
              maxlength="250"
              show-word-limit
              rows="3"
              :disabled="disableEdit"
              clearable
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <div style="margin-left: 5px; font-size: 12px">
            <span style="color: #f67a7a; margin-right: 5px">*</span>填空项
          </div>
        </el-col>
        <el-col :span="12" v-if="!disableEdit" style="text-align: right;margin-bottom: 5px;">
          <el-button
            @click="clickAddOption()"
            icon="Flag"
            :title="cursorPosition === -1 ? '请选择题干中的位置' : '插入填空'"
            :disabled="cursorPosition === -1"
            type="primary"
          >
            {{ cursorPosition === -1 ? '请选择题干中的位置' : '插入填空' }}
          </el-button>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-table
            :data="config.answer"
            :maxHeight="180"
            border
            ref="subTable"
            class="subTable"
            :header-cell-style="{
              backgroundColor: '#F5F7FA',
              color: '#666666',
            }"
            row-key="rowKey"
          >
            <el-table-column
              type="index"
              label="#"
              width="50"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="title"
              label="填空位"
              width="100px"
              :render-header="addReadStar"
              align="center"
            >
            </el-table-column>
            <el-table-column
              prop="value"
              label="填空答案"
              :render-header="addReadStar"
            >
              <template #default="scope" v-if="!disableEdit">
                <el-form-item
                  label-width="0"
                  :style="{ marginBottom: 0 }"
                  :prop="'answer[' + scope.$index + '].value'"
                  :rules="[
                    { required: true, message: '必填项', trigger: 'blur' },
                  ]"
                >
                  <el-input v-model="scope.row.value" clearable></el-input>
                </el-form-item>
              </template>
            </el-table-column>

            <el-table-column align="center" width="50px" v-if="!disableEdit">
              <template #header="scope">
                <span>操作</span>
              </template>
              <template #default="scope">
                <el-button
                  title="删除填空"
                  @click="clickRemoveOption(scope.$index)"
                  icon="Delete"
                  circle
                  type="danger"
                ></el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="解析" prop="analysis">
            <el-input
              v-model="config.analysis"
              type="textarea"
              maxlength="500"
              :rows="3"
              :disabled="disableEdit"
              show-word-limit
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="分数" prop="score">
            <el-input-number
              style="width: 100%"
              v-model="config.score"
              :min="1"
              :max="20"
              :precision="0"
              :disabled="disableEdit"
              clearable
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="难度" prop="level" label-width="100px">
            <el-rate
              v-model="config.level"
              :disabled="disableEdit"
              :texts="['简单', '一般', '中等', '较难', '很难']"
              show-text
            >
            </el-rate>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-if="showShare">
        <el-col :span="12">
          <el-form-item label="共享" prop="share">
            <el-switch
              v-model="config.share"
              active-color="#014B70"
              inactive-color="#BFCBD9"
              :active-value="1"
              :inactive-value="0"
              :disabled="disableEdit"
              active-text=""
              inactive-text=""
            >
            </el-switch>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<style lang="scss" scoped>
.subTable {
  width: calc(100% - 50px); 
  margin-left: 50px;
  margin-bottom: 15px
}
</style>
