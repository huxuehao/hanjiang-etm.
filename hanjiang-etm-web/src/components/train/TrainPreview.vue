<script lang="ts" setup>
import { paperAnswer } from '@/api/trainTemplate';
import { ElMessage, ElMessageBox } from 'element-plus';
import { reactive, ref } from 'vue';
import PaperPreview from '../paper/PaperPreview.vue';

const props = defineProps({
  mode: {
    type: String,
    default: 'preview'
  },
  active: {
    type: Boolean,
    default: false
  },
  data: {
    type: Object,
    default: () => {
      return {}
    }
  }
})

const config = reactive<any>({})
let mode_ = ref("preview")

const initConfig = () => {
  mode_.value = props.mode
  Object.assign(config, props.data)
};
initConfig()

// onMounted(() => {
//   nextTick(() => {
//     mode_.value = props.mode
//     Object.assign(config, props.data)
//   })
// })

const emit = defineEmits(["close"])

// 退出
const handelReturn = () => {
  emit('close')
};

let activeStep = ref(1)
// 改变步骤条
const changeStep = (step: number) => {
  activeStep.value = step
};

const attachProp = reactive<any>({
  fileId: null,
  fileType: null,
  visible: false,
  title: '',
  top: '5vh',
  width: '75%',
  modal: true,
  appendToBody: true,
  showClose: true,
  fullscreen: true,
  url: '',
})
// 课件预览
const previewFile = (fileEntity: any) => {
  attachProp.title = `${fileEntity.fileName}（${fileEntity.fileExtension}）`
  attachProp.fileType = fileEntity.fileType
  if (fileEntity.fileType === 1) {
    previewVideo(fileEntity)
  } else if (fileEntity.fileType === 2) {
    previewFiles(fileEntity)
  } else {
    ElMessage({
      message: "暂不支持预览该文件类型",
      type: "warning",
      plain: true
    })
  }
};
// 预览视频
const previewVideo = (fileEntity: any) => {
  ElMessage({
    message: "暂不支持预览视频",
    type: "warning",
    plain: true
  })
};
// 预览File
const previewFiles = (fileEntity: any) => {
  ElMessage({
    message: "暂不支持预览文件",
    type: "warning",
    plain: true
  })
};

let paperPreviewVisible = ref(false)
// 打开试卷
const showPaper = () => {
  if (mode_.value === 'answer') {
    ElMessageBox.confirm('确认开始答题？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      paperPreviewVisible.value = true
    })
  } else {
    paperPreviewVisible.value = true
  }
};

let paperLoading = ref(false)
// 用户交卷
const handelPaperAnswer = (config_: any) => {
  // 真实交卷
  if (props.active) {
    paperLoading.value = true
    paperAnswer(config_).then((res) => {
      mode_.value = 'history'
      Object.assign(config, res.data.data)
      paperPreviewVisible.value = false
    }).finally(() => {
      paperLoading.value = false
    })
  }
  // 模拟交卷
  else {
    mode_.value = 'history'
    paperPreviewVisible.value = false
    ElMessage({
      message: "模拟交卷成功",
      type: "success",
      plain: true
    })
  }
};

// 关闭试卷
const handelPaperClose = () => {
  paperPreviewVisible.value = false
};

</script>

<template>
  <div style="letter-spacing: 1px;">
    <div class="header-bg" style="margin-bottom: 0;height: 165px;border-bottom: 1px solid #eaeaea">
      <el-row>
        <el-col :span="24" class="ellipsis-container header-title">
          <span class="outlined-text" :title="config.title">{{config.title}}</span>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="4"></el-col>
        <el-col :span="20" class="ellipsis-container header-desc outlined-text">
          <span  class="outlined-text" :title="config.des || '暂无描述'">{{config.des || '暂无描述'}}</span>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-steps :active="activeStep" simple style="user-select: none;height: 40px;cursor: pointer;background-color: transparent;padding-top: 0;">
            <el-step title="退出" @click.native="handelReturn"></el-step>
            <el-step title="资料" icon="DocumentCopy" @click.native="changeStep(1)"></el-step>
            <el-step title="试卷" icon="Edit" @click.native="changeStep(2)"></el-step>
          </el-steps>
        </el-col>
        <el-col :span="16" class="ellipsis-container header-time">
          <span>培训日期：{{config.limitStartTime}} ~ {{config.limitEndTime}}</span>
        </el-col>
      </el-row>
    </div>
    <!--底部区域-->
    <el-row>
      <!--学习资料-->
      <div v-show="activeStep === 1" class="context-box">
        <el-row v-for="(item, index) in config.file" :key="index">
          <div class="train-card">
            <div class="train-card-img">
              <img v-if="item.fileExtension === 'mp4'" class="myIcon" src="../../assets/" :width="48" alt="图片">
              <img v-else-if="item.fileExtension === 'pdf'" class="myIcon" src="../../assets/images/bg/pdf.png" :width="48" alt="图片">
              <img v-else-if="item.fileExtension === 'png'" class="myIcon" src="../../assets/images/bg/png.png" :width="48" alt="图片">
              <img v-else-if="item.fileExtension === 'pdf'" class="myIcon" src="../../assets/images/bg/pdf.png" :width="48" alt="图片">
              <img v-else-if="item.fileExtension === 'txt'" class="myIcon" src="../../assets/images/bg/txt.png" :width="48" alt="图片">
              <img v-else-if="['docx','doc'].indexOf(item.fileExtension) > -1" class="myIcon" src="../../assets/images/bg/doc.png" :width="48" alt="图片">
              <img v-else-if="['xlsx','xls'].indexOf(item.fileExtension) > -1" class="myIcon" src="../../assets/images/bg/xls.png" :width="48" alt="图片">
              <img v-else class="myIcon" src="../../assets/images/bg/none.png" :width="48" alt="图片">
            </div>
            <div class="train-card-context">
              <div class="ellipsis-container card-context-title" :title="item.fileName">{{item.fileName}}（{{item.fileExtension}}）</div>
              <div class="ellipsis-container card-context-desc" :title="item.fileDesc || '暂无描述'">{{item.fileDesc || '暂无描述'}}</div>
            </div>
            <div class="train-card-btn">
              <el-button
                class="preview-btn"
                @click="previewFile(item)"
                title="查看"
                size="large"
                icon="DArrowRight"
                circle>
              </el-button>
            </div>
          </div>
        </el-row>
      </div>
      <!--试题检测-->
      <div v-show="activeStep === 2" class="context-box">
        <el-row>
          <div class="train-card">
            <div class="train-card-img">
              <img class="myIcon" src="../../assets/images/bg/paper.png" :width="48" alt="图片">
            </div>
            <div class="train-card-context">
              <div class="ellipsis-container card-context-title" :title="config.title">
                [ {{config.title}} ] 试卷
                <template v-if="active">
                  <el-tag disable-transitions v-if="config.user[0].paperDone === 1" type="success">已完成</el-tag>
                  <el-tag disable-transitions v-else type="danger">未完成</el-tag>
                </template>
              </div>
              <div class="ellipsis-container card-context-desc" :title="config.title">
                这是 [ {{config.title}} ] 培训附带的试卷，您在学习完培训资料后需完成本次试卷的答题
              </div>
            </div>
            <div class="train-card-btn">
              <el-button 
                title="打开" 
                @click="showPaper" 
                icon="DArrowRight" 
                size="large"
                class="preview-btn"
                circle>
              </el-button>
            </div>
          </div>
        </el-row>
      </div>
    </el-row>
    <!--课件预览-->
    <el-dialog v-bind="attachProp" @close="()=>{attachProp.visible=false}">
      。。。。。。
    </el-dialog>
    <!--试卷预览-->
    <el-drawer
      v-loading="paperLoading"
      element-loading-text="试卷提交中"
      title='试卷预览'
      v-model="paperPreviewVisible"
      :wrapper-closable="true"
      :with-header="false"
      class="drawer-none-padding"
      append-to-body
      direction="btt"
      size="100%">
      <PaperPreview
        v-if="paperPreviewVisible"
        :data="config"
        :mode="mode_"
        :showAlert="false"
        @answer="handelPaperAnswer"
        @close="handelPaperClose"
      ></PaperPreview>
    </el-drawer>
  </div>
</template>

<style lang="scss" scoped>
.header-bg{
  height: 160px;
  width: 100%;
  background-image: url('../../assets/images/bg/a.png');
  background-size: cover;
  background-position: center; /* 可选，确保图片居中显示 */
  background-repeat: no-repeat; /* 可选，确保图片不重复 */
}
.header-title {
  height: 80px;
  width: 100%;
  line-height: 80px;
  text-align: right;
  padding: 10px;
  font-size: 40px;
  font-weight: bolder;
  color:#FFFFFF;
}
.header-desc {
  height: 40px;
  font-size: 15px;
  color: white;
  text-align: right;
  padding: 10px;
}
.header-time {
  height: 30px;
  font-size: 13px;
  color: white;
  text-align: right;
  padding: 10px;
}

.return {
  cursor: pointer;
  font-size: 14px;
  margin-top: 10px;
  margin-left: 10px;
  width: 100px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  color: #2485a7;
  transition: 0.3s;
  border-radius: 3px;
  border: 1px solid #e9e9ee;
  user-select: none;
}
.return:hover {
 color: white;
  background-color: #0c6591;
}

/* 超出部分显示省略号 */
.ellipsis-container {
  cursor: default;
  overflow: hidden;      /* 隐藏超出部分 */
  white-space: nowrap;   /* 禁止换行 */
  text-overflow: ellipsis; /* 显示省略号 */
}
/* 描边效果 */
.outlined-text {
  text-shadow: 0 0 3px rgba(30, 30, 30, 0.7);
}
.context-box {
  height: calc(100vh - 210px);
  width: 100%;
  overflow: auto;
  padding: 10px;
}
.train-card {
  width: 100%;
  height: 80px;
  background-color: white;
  margin-bottom: 10px;
  border-radius: 3px;
  transition: 0.3s;

  &:hover .preview-btn {
    background-color: #e6edf1;
  }
}
.train-card-img {
  width: 90px;
  height: 80px;
  float: left
}
.train-card-context {
  float: left;
  width: calc(100% - 180px);
  height: 80px;
}
.card-context-title {
  font-size: 15px;
  font-weight: bolder;
  height: 40px;
  line-height: 60px;
  color: #606060
}
.card-context-desc {
  font-size: 12px;
  height: 40px;
  line-height: 30px;
  font-weight: lighter;
  color: #6d6d6d
}
.train-card-btn {
  width: 80px;
  float: left;
  line-height: 77px;
  text-align: center;
  transition: 0.3s;
}

.train-card:hover {
  transform: translateY(-3px); /* 向上移动 */
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}
.preview-btn {
  width: 50px;
  height: 50px;
  border: 0 solid #ededed;
  color: #7d7d7d
}
.myIframe {
  height: calc(100vh - 95px);
  //background-color: #404040;
  border: none;
  border-radius: 5px
}
.myIcon {
  margin: 18px 25px 25px 30px
}
</style>