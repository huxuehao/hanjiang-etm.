<script lang="ts" setup>
import { ElMessage, ElMessageBox } from 'element-plus';
import { computed, nextTick, onMounted, reactive, ref } from 'vue';
import UserChose from '../user/UserChose.vue';

const props = defineProps({
  data: {
    type: Object,
    default: () => {
      return {}
    }
  },
  mode: {
    type: String,
    default: 'add'
  },
  disableEdit: {
    type: Boolean,
    default: false,
  },
})

let correctorForm = reactive<any>({
  corrector: [],
})
onMounted(() => {
  nextTick(() => {
    if (['edit','view'].indexOf(props.mode) > -1) {
      correctorForm.corrector = props.data.corrector
    }
  })
})
let selectedUserIds = computed(() => {
  return correctorForm.corrector.map((item: any) => item.userId)
})
let userReload = ref(0)
let rows = ref<any>([])
// 删除用户
const userDelete = () => {
  ElMessageBox.confirm(
    "确认删除所选择的用户？",
    "提示"
  ).then(() => {
    correctorForm.corrector = correctorForm.corrector.filter((item: any) => {
      return !rows.value.some((item2: any) => item2.userId === item.userId);
    })
    rows.value = []
  })
};
// 删除用户
const handleDelete = (row: any, index: number) => {
  ElMessageBox.confirm(
    `确认移除 [ ${row.userName} ] ?`,
    "提示",
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    correctorForm.corrector.splice(index,1)
    userReload.value++;
  })
};

// show Dialog
let showUserDialog = ref(false)
const showDialog = () => {
  showUserDialog.value = true
};

// 获取考试人员
let userChose = ref()
const handleExamPeople = (selectUsers: any) => {
  if (selectUsers.length === 0) {
    correctorForm.corrector = []
  } else if(selectUsers.length > 20) {
    ElMessage({
      message: "人数已达上限，最多支持20人",
      type: "warning",
      plain: true
    })
  } else {
    // 获取第一个比第二个多的元素，以及第一个比第二个少的元素
    const {moreInOne, lessInOne} = getDiffArrays(selectUsers, correctorForm.corrector)
    // 添加用户
    moreInOne.forEach((item: any) => {
      correctorForm.corrector.push({
        userId: item.id,
        orgId: item.orgId,
        userName: item.name,
        userCode: item.code
      })
    })
    // 删除用户
    if (lessInOne.length > 0) {
      correctorForm.corrector = correctorForm.corrector.filter((item: any) => {
        for (let v of lessInOne) {
          if (item.userId === v.userId) {
            return false
          }
        }
        return true
      })
    }
    showUserDialog.value = false
  }
};
// 获取两个数组的不同
const getDiffArrays = (arrA: any[], arrB: any[]) => {
  const idsInOne = arrA.map(item => item.id);
  const idsInTwo = arrB.map(item => item.userId);
  const moreInOne = arrA.filter(item =>!idsInTwo.includes(item.id));
  const lessInOne = arrB.filter(item =>!idsInOne.includes(item.userId));
  return { moreInOne, lessInOne };
};

defineExpose({correctorForm})
</script>

<template>
  <el-form label-width="80px"
           ref="form"
           :model="correctorForm">
    <el-row v-if="!disableEdit" style="margin-bottom: 5px;">
      <el-col :span="12" style="text-align: left">
        <el-button @click="userDelete" type="danger" plain icon="Delete" :disabled="rows.length === 0">删除</el-button>
      </el-col>
      <el-col :span="12" style="text-align: right">
        <el-button type="primary" @click="showDialog" icon="UserFilled" plain>选择批改人员</el-button>
      </el-col>
    </el-row>
    <el-row :span="24">
      <el-table style="width: 100%;"
                :header-cell-style="{backgroundColor: '#f8f8f8',color:'#666666'}"
                :data="correctorForm.corrector"
                @selection-change="(val: any) =>{rows = val}"
                height="calc(100vh - 350px)" border ref="peopleTable">
        <el-table-column type="selection" v-if="!disableEdit"></el-table-column>
        <el-table-column type="index" label="#" width="50" align="center"></el-table-column>
        <el-table-column prop="userName" label="人员姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userCode" label="人员编号" align="center" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.userCode">{{scope.row.userCode}}</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" v-if="!disableEdit">
          <template #default="scope">
            <el-button type="text" icon="Delete" style="color: #ff2d2d"
                       @click="handleDelete(scope.row, scope.$index)">
              移除人员
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-drawer
      title="选择批改人员"
      size="75%"
      direction="rtl"
      destroy-on-close
      :close-on-click-modal="false"
      v-model="showUserDialog"
      append-to-body>
      <UserChose
        style="height: 500px"
        v-if="showUserDialog"
        @submit="handleExamPeople"
        :user-selected-ids="selectedUserIds"
        ref="userChose">
      </UserChose>
    </el-drawer>
  </el-form>
</template>

<style lang="scss" scoped>
</style>