<template>
  <div class="about  p48" style="padding-top: 0;">
    <h1>用户管理</h1>
    <div class="search">
      <el-row :gutter="24">
        <el-col :span="3"><el-input v-model="searchData.name" placeholder="请输入姓名" clearable /></el-col>
        <el-col :span="3"><el-input v-model="searchData.age" placeholder="请输入年龄" clearable /></el-col>
        <!-- <el-col :span="4"><el-input v-model="searchData.sex" placeholder="请输入性别" clearable /></el-col> -->
        <el-col :span="3">
          <el-select v-model="searchData.sex" class="m-2" placeholder="请选择性别" size="large">
            <el-option v-for="item in sexList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="3"><el-input v-model="searchData.phone" placeholder="请输入电话" clearable /></el-col>
        <el-col :span="3"><el-input v-model="searchData.address" placeholder="请输入地址" clearable /></el-col>
        <el-col :span="1"> <el-button type="primary" @click="home_notice">搜索</el-button></el-col>
      </el-row>
    </div>
    <div class="content">
      <el-row :gutter="16">
        <el-col :span="1"> <el-button @click="addNew" type="primary">新增</el-button></el-col>
        <el-col :span="1"> <el-button type="success" @click="edit">编辑</el-button></el-col>
        <el-col :span="1"> <el-button @click="dele" type="info">删除</el-button></el-col>
        <el-col :span="1"> <el-button @click="cancelEvent" type="primary">撤销</el-button></el-col>
      </el-row>
      <el-table :data="tableData" style="width: 100%" ref="multipleTableRef" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column fixed prop="name" label="姓名" width="150">
          <template #default="scope">
            <span class="name" @click="showDetail(scope.row)"> {{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="120" />
        <el-table-column prop="sex" label="性别" width="120">
          <template #default="scope">
            {{ scope.row.sex == 1 ? '女' : '男' }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="120" />
        <el-table-column prop="address" label="详细地址" width="250" />
        <el-table-column prop="createTime" label="创建时间" />
      </el-table>
      <div class="demo-pagination-block">
        <el-pagination v-model:current-page="currentPage" :page-sizes="[5, 10, 20]" :small="small" background
          layout="sizes, prev, pager, next" :total="total" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>
    <!--  -->
    <el-dialog v-model="dialogVisible" :title="title" width="40%" :before-close="handleClose">
      <el-form :model="form" label-width="120px" ref="ruleFormRef" :rules="rules">
        <el-form-item required label="姓名">
          <el-input v-model="form.name" :disabled="isDisabled" />
        </el-form-item>
        <el-form-item required label="性别">
          <el-select :disabled="isDisabled" v-model="form.sex" class="m-2" placeholder="请选择" size="large">
            <el-option v-for="item in sexList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item required label="联系电话">
          <el-input :disabled="isDisabled" v-model="form.phone" />
        </el-form-item>
        <el-form-item required label="年龄">
          <el-input :disabled="isDisabled" v-model="form.age" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input :disabled="isDisabled" v-model="form.address" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer" v-if="!isDisabled">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirms">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import queryUser from '../api/home'
import { ref, reactive } from 'vue'
// import { h } from 'vue'
import { ElMessage } from 'element-plus'
export default {
  data() {
    return {

    }
  },

  setup() {
    let pageSize = ref(10)
    let currentPage = ref(1)
    const searchData = reactive({
      name: '',
      age: '',
      sex: '',
      phone: '',
      address: '',
      orderBy: '1',
      currentPage: 1,
      pageSize: pageSize.value
    })
    //  页码更改
    const handleCurrentChange = (val) => {
      console.log(val, 'ooo')
      searchData.currentPage = val
      home_notice()
    }
    // 改变页面pageSize
    const handleSizeChange = (val) => {
      console.log(val)
      searchData.pageSize = val
      home_notice()

    }
    const input = ref('')
    let tableData = ref(null)
    let total = ref(0)
    const home_notice = async () => {
      const { data: res } = await queryUser.queryUser(searchData)
      console.log(res, 'lll')
      tableData.value = res.data
      total.value = res.total
    };
    // 列表加载
    home_notice()
    let dialogVisible = ref(false)
    let title = ref('新增')
    // 新增操作
    const addNew = () => {
      title.value = '新增'
      clearData()
      isDisabled.value = false
      dialogVisible.value = true
    }
    const form = reactive({
      name: '', sex: '', phone: '', age: '', address: ''
    })
    const sexList = reactive([
      {
        label: '男',
        value: '0'
      },
      {
        label: '女',
        value: '1'
      },
    ])
    // 确认操作
    const confirms = async () => {
      if (isEdit.value) {
        form.id = editId.value
        const { data: res } = await queryUser.editData(form)
        if (res.code == 200) {
          // 加载表格数据
          home_notice()
          ElMessage('成功')
          dialogVisible.value = false
        } else {
          ElMessage(res.massage)
        }
      } else {
        const { data: res } = await queryUser.addNew(form)
        if (res.code == 200) {
          // 加载表格数据
          home_notice()
          ElMessage('成功')
          dialogVisible.value = false
        } else {
          ElMessage(res.massage)
        }
      }
      console.log(form.name, 'ppp')

      dialogVisible.value = false
    }
    // 删除操作
    const dele = async () => {
      if (!multipleSelection.length) {
        alert('请选择数据')
      } else {
        let temp = []
        multipleSelection.map(item => {
          temp.push(item.id)
        })
        console.log(temp, 'queryUser')
        // deletes
        const { data: res } = await queryUser.deleteUser({idList:temp.toString()})
        if (res.code == 200) {
          ElMessage('删除成功')
          // 加载表格数据
          home_notice()
        }
      }
    }
    // 数据清空
    const clearData = ()=>{
      form.name = ''
      form.phone = ''
      form.sex = ''
      form.address = ''
      form.age = ''
      form.id = ''
    }
    // 数据多选处理
    let multipleSelection = reactive([])
    const handleSelectionChange = (val) => {
      console.log(val, 'lll')
      multipleSelection = val
    }
    // 撤销操作
    const cancelEvent = async () => {
      const { data: res } = await queryUser.undo()
      console.log(res)
      if (res.code == 200) {
        // 加载表格数据
        home_notice()
      } else {
        ElMessage(res.massage)
      }
    }
    // 编辑操作
    let editId = ref(null)
    let isEdit = ref(false)
    const edit = async () => {
      title.value = '编辑'
      isDisabled.value = false
      if (multipleSelection.length !== 1) {
        ElMessage('请选择一条数据')
      }
      if (multipleSelection.length == 1) {
        editId.value = multipleSelection[0].id
        isEdit.value = true
        form.name = multipleSelection[0].name
        form.phone = multipleSelection[0].phone
        form.age = multipleSelection[0].age
        form.sex = multipleSelection[0].sex
        form.address = multipleSelection[0].address
        dialogVisible.value = true
      }
    }
    let isDisabled = ref(false)
    // 点击姓名展示详情
    const showDetail = (row) => {
      console.log(row)
      // 只展示 不修改
      title.value = '详情'
      isDisabled.value = true
      form.name = row.name
      form.phone = row.phone
      form.sex = row.sex
      form.address = row.address
      form.age = row.age
      dialogVisible.value = true
    }
    return { isDisabled, showDetail, editId, isEdit, searchData, pageSize, currentPage, edit, total, handleCurrentChange, home_notice, input, tableData, dialogVisible, addNew, title, form, sexList, confirms, dele, cancelEvent, multipleSelection, handleSelectionChange, handleSizeChange }
  }
}
</script>
<style>
.p48 {
  padding: 48px;
}

.el-row {
  margin-bottom: 20px;
}

.el-row:last-child {
  margin-bottom: 0;
}

.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.content {
  margin-top: 40px;
}

.demo-pagination-block {
  margin-top: 20px;
}

.name {
  cursor: pointer;
}

.name:hover {
  color: blueviolet;
  text-decoration: underline;
}

.el-input__wrapper{
  height: 30px;
}
</style>