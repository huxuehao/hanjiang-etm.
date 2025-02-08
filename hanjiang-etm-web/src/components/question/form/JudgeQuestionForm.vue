<script lang="ts" setup>
import { chineseNumbers, prop, addReadStar } from "./common";
import {
  nextTick,
  onBeforeUnmount,
  onMounted,
  reactive,
  ref,
  watch,
} from "vue";
import Sortable from "sortablejs";

let props = defineProps({
  showClassify: {
    type: Boolean,
    default: true,
  },
  classifyDict: {
    type: Array,
    default: () => {
      return [];
    },
  },
  showShare: {
    type: Boolean,
    default: () => {
      return true;
    },
  },
  mode: {
    type: String,
    default: "add",
  },
  data: {
    type: Object,
    default: () => {
      return {};
    },
  },
  disableEdit: {
    type: Boolean,
    default: false,
  },
});

let form = ref()
let enableDrop = ref(false);
let sortableBody = <any>null;
const config = reactive<any>({
  classifyId: null,
  name: null,
  title: null,
  option: [
    {
      rowKey: "0",
      title: "A",
      value: "对",
      selected: 0,
      sort: 1,
    },
    {
      rowKey: "1",
      title: "B",
      value: "错",
      selected: 0,
      sort: 1,
    },
  ],
  answer: null,
  score: 5,
  analysis: null,
  level: 2,
  type: 3,
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
  answer: [
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

watch(enableDrop, (val) => {
  if (val) {
    initSortable();
  } else {
    destroySortable();
  }
});

onMounted(() => {
  nextTick(() => {
    if (props.mode === "edit") {
      Object.assign(config, props.data);
      config.success = -1;
    }
  });
});

onBeforeUnmount(() => {
  destroySortable();
});

// 生成实体
const genEntity = () => {
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

// 销毁拖拽
const destroySortable = () => {
  enableDrop.value = false;
  if (sortableBody === null) {
    return;
  }
  sortableBody.destroy();
  sortableBody = null;
};

// 初始化拖拽
const subTable = ref();
const initSortable = () => {
  if (!props.disableEdit) {
    enableDrop.value = true;
    const el = subTable.value.$el.querySelector(
      ".el-table__body-wrapper tbody"
    );
    sortableBody = new Sortable.create(el, {
      animation: 150,
      onEnd: handleDragEnd,
    });
  }
};

// 处理拖拽结束
const handleDragEnd = (evt: any) => {
  const { newIndex, oldIndex } = evt;
  if (newIndex === oldIndex) {
    return;
  }
  const currRow = config.option.splice(oldIndex, 1)[0];
  config.option.splice(newIndex, 0, currRow);
  genTitleForOption();
  config.answer = null;
};

// 为选项生成title
const genTitleForOption = () => {
  config.option = config.option.map((item: any, index: number) => {
    // 65 是 'A' 的 ASCII 码
    item.title = String.fromCharCode(65 + index);
    return item;
  });
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
              clearable
            ></el-tree-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="题干" prop="title">
            <el-input
              v-model="config.title"
              type="textarea"
              maxlength="200"
              rows="3"
              :disabled="disableEdit"
              show-word-limit
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <div style="margin-left: 5px; font-size: 12px">
            <span style="color: #f67a7a; margin-right: 5px">*</span>选项
          </div>
        </el-col>
        <el-col v-if="!disableEdit" :span="12" style="text-align: right">
          <el-switch
            v-model="enableDrop"
            active-text="开启选项拖动"
            inactive-text="关闭选项拖动"
          ></el-switch>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-table
            :data="config.option"
            :maxHeight="180"
            border
            ref="subTable"
            class="subTable"
            :header-cell-style="{
              backgroundColor: '#F5F7FA',
              color: '#666666',
            }"
            :style="{ cursor: enableDrop && !disableEdit ? 'move' : 'default' }"
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
              label="选项头"
              width="100px"
              :render-header="addReadStar"
              align="center"
            >
            </el-table-column>
            <el-table-column
              prop="value"
              label="选项值"
              align="center"
              :render-header="addReadStar"
            >
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
              rows="3"
              :disabled="disableEdit"
              show-word-limit
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="答案" prop="answer">
            <el-select
              v-model="config.answer"
              :disabled="disableEdit"
              clearable
              placeholder="请选择"
            >
              <el-option
                v-for="item in config.option"
                :key="item.value"
                :label="item.title"
                :value="item.title"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分数" prop="score" label-width="100px">
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
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="难度" prop="level">
            <el-rate
              v-model="config.level"
              :disabled="disableEdit"
              :texts="['简单', '一般', '中等', '较难', '很难']"
              show-text
            >
            </el-rate>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showShare">
          <el-form-item label="共享" prop="share" label-width="100px">
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
