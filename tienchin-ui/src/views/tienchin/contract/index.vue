<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="客户姓名" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入客户姓名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户手机" prop="phone">
        <el-input
            v-model="queryParams.phone"
            placeholder="请输入客户手机号码"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="归属人" prop="owner">
        <el-input
            v-model="queryParams.owner"
            placeholder="请输入合同归属人"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="跟进时间" prop="dateRange">
        <el-date-picker
            v-model="queryParams.dateRange"
            value-format="YYYY-MM-DD hh:mm:ss"
            format="YYYY-MM-DD hh:mm:ss"
            type="datetimerange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['tienchin:contract:create']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['tienchin:contract:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['tienchin:contract:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['tienchin:contract:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <div>
      <el-tag>待处理任务列表</el-tag>
      <el-table v-loading="loading" :data="unapproveTaskList" @selection-change="handleSelectionChange"
                :row-class-name="myTaskListRowStyle">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="客户姓名" :show-overflow-tooltip="true" align="center" width="120" prop="name"/>
        <el-table-column label="手机号码" :show-overflow-tooltip="true" align="center" width="120" prop="phone"/>
        <el-table-column label="渠道名称" :show-overflow-tooltip="true" align="center" width="120" prop="channelName"/>
        <el-table-column label="活动名称" :show-overflow-tooltip="true" align="center" width="120" prop="activityName"/>
        <el-table-column label="课程名称" :show-overflow-tooltip="true" align="center" width="120" prop="courseName"/>
        <el-table-column label="合同金额" :show-overflow-tooltip="true" align="center" width="120"
                         prop="contractPrice"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="View" @click="handleContractView(scope.row)"
                       v-hasPermi="['tienchin:contract:details']">查看
            </el-button>
            <el-button
                link type="primary" icon="Document" @click="handlePreviewPDF(scope.row)"
                v-hasPermi="['tienchin:contract:view']">预览
            </el-button>
            <el-button
                v-if="scope.row.approveUserName == userStore.name"
                link type="primary" icon="TopRight" @click="showApproveContractDialog(scope.row)"
                v-hasPermi="['tienchin:contract:approve']">审批
            </el-button>
            <el-button
                v-if="scope.row.reason && scope.row.approveUserName !== userStore.name"
                link type="primary" icon="TopRight" @click="replienishContractInfo(scope.row)"
                v-hasPermi="['tienchin:contract:approve']">补充合同信息
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-divider />

    <div style="margin-top: 30px">
      <el-tag type="success">已提交待审批任务列表</el-tag>
      <el-table v-loading="loading" :data="committedTaskList" @selection-change="handleSelectionChange"
                :row-class-name="myTaskListRowStyle">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="客户姓名" :show-overflow-tooltip="true" align="center" width="120" prop="name" />
        <el-table-column label="手机号码" :show-overflow-tooltip="true" align="center" width="120" prop="phone" />
        <el-table-column label="渠道名称" :show-overflow-tooltip="true" align="center" width="120" prop="channelName" />
        <el-table-column label="活动名称" :show-overflow-tooltip="true" align="center" width="120" prop="activityName" />
        <el-table-column label="课程名称" :show-overflow-tooltip="true" align="center" width="120" prop="courseName" />
        <el-table-column label="合同金额" :show-overflow-tooltip="true" align="center" prop="contractPrice" />
      </el-table>
    </div>

    <el-divider/>

    <div style="margin-top: 30px">
      <el-tag type="danger">审批完成任务列表</el-tag>
      <el-table v-loading="loading" :data="approvedTaskList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="客户姓名" :show-overflow-tooltip="true" align="center" width="120" prop="name" />
        <el-table-column label="手机号码" :show-overflow-tooltip="true" align="center" width="120" prop="phone" />
        <el-table-column label="渠道名称" :show-overflow-tooltip="true" align="center" width="120" prop="channelName" />
        <el-table-column label="活动名称" :show-overflow-tooltip="true" align="center" width="120" prop="activityName" />
        <el-table-column label="课程名称" :show-overflow-tooltip="true" align="center" width="120" prop="courseName" />
        <el-table-column label="合同金额" :show-overflow-tooltip="true" align="center" prop="contractPrice" />
        <el-table-column label="备注信息" :show-overflow-tooltip="true" align="center" prop="reason" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="View" @click="handleContractView(scope.row)"
                       v-hasPermi="['tienchin:contract:details']">查看
            </el-button>
            <el-button
                link type="primary" icon="Document" @click="handlePreviewPDF(scope.row)"
                v-hasPermi="['tienchin:contract:view']">预览
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 添加或修改合同对话框 -->
    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form ref="contractRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="form.phone" @input="phoneChange" placeholder="请输入手机号码"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户姓名" prop="name">
              <el-input disabled v-model="form.name" placeholder="请输入客户姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="渠道来源" prop="channelId">
              <el-select
                  v-model="form.channelName"
                  placeholder="渠道来源"
                  clearable
                  disabled
                  @change="channelChange"
                  style="width: 100%"
              >
                <el-option
                    v-for="channel in channelList"
                    :key="channel.channelId"
                    :label="channel.channelName"
                    :value="{value:channel.channelId, channelId:channel.channelId, channelName:channel.channelName}"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动信息" prop="activityId">
              <el-select
                  v-model="form.activityName"
                  placeholder="活动信息"
                  clearable
                  disabled
                  @change="activityChange"
                  style="width: 100%"
              >
                <el-option
                    v-for="al in activityList"
                    :key="al.activityId"
                    :label="al.name"
                    :value="{value:al.activityId, activityId:al.activityId, activityName:al.name, discountType:al.type}"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="课程分类" prop="type">
                <el-select
                    v-model="form.type"
                    placeholder="课程分类"
                    clearable
                    disabled
                    @change="courseTypeChange"
                    style="width: 100%"
                >
                  <el-option
                      v-for="dict in course_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="parseInt(dict.value)"
                  />
                </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="购买课程" prop="courseId">
              <el-select style="width: 100%" v-model="form.courseName"
                         @change="courseChange"
                         disabled
                         placeholder="请选择">
                <el-option v-for="course in courses" :key="course.courseId" :label="course.name"
                           :value="{value:course.courseId, courseId: course.courseId, courseName:course.name}"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="审批部门" prop="departmentId">
              <el-tree-select
                  v-model="form.departmentId"
                  :data="deptOptions"
                  @change="deptChange"
                  :props="{ value: 'id', label: 'label', children: 'children' }"
                  value-key="id"
                  placeholder="请选择归属部门"
                  check-strictly/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审批人" prop="nickName">
              <el-select
                  v-model="form.nickName"
                  placeholder="选择用户"
                  @change="approveUserChange"
                  clearable
                  style="width: 100%"
              >
                <el-option
                    v-for="user in userList"
                    :key="user.userId"
                    :label="user.nickName"
                    :value="{value:user.userId, userName:user.userName, nickName:user.nickName, deptId: user.deptId}"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同价格" prop="contractPrice">
              <el-input-number style="width: 100%" v-model="form.contractPrice" placeholder="合同价格"
                               :precision="2" :step="100" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同原件" prop="fileList">
              <el-upload
                  v-model:file-list="fileList"
                  class="upload-demo"
                  :headers="headersObj"
                  action="/dev-api/tienchin/contract/upload"
                  :on-remove="handleRemove"
                  :before-remove="handleBeforeRemove"
                  :on-success="handleSuccess"
                  :before-upload="handleBeforeUpload"
                  :limit="1"
              >
                <el-button type="primary">选择合同原件</el-button>
                <template #tip>
                  <div class="el-upload__tip">
                    <div>{{old_file}}</div>
                    合同原件大小不能超过 5 M。
                  </div>
                </template>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!--    合同详情-->
    <el-dialog title="合同详情" v-model="showContractDetailsDialog" width="700px" append-to-body>
        <el-row :gutter="8">
          <el-col :span="8">
            <div>
              <div style="font-weight: bold;font-style: italic">合同编号</div>
              <div style="color: #8392a6">{{contractDetails.contractId}}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div>
              <div style="font-weight: bold;font-style: italic">客户手机号码</div>
              <div style="color: #8392a6">{{contractDetails.phone}}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div>
              <div style="font-weight: bold;font-style: italic">客户姓名</div>
              <div style="color: #8392a6">{{contractDetails.name}}</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8">
        <el-col :span="8">
          <div>
            <div style="font-weight: bold;font-style: italic">课程分类</div>
            <div style="color: #8392a6">
              <template v-for="dict in course_type">
                <template v-if="dict.value == contractDetails.type">{{dict.label}}</template>
              </template>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div>
            <div style="font-weight: bold;font-style: italic">课程名称</div>
            <div style="color: #8392a6">{{contractDetails.courseName}}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div>
            <div style="font-weight: bold;font-style: italic">渠道名称</div>
            <div style="color: #8392a6">{{contractDetails.channelName}}</div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="8">
        <el-col :span="8">
          <div>
            <div style="font-weight: bold;font-style: italic">活动名称</div>
            <div style="color: #8392a6">{{contractDetails.activityName}}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div>
            <div style="font-weight: bold;font-style: italic">合同状态</div>
            <div style="color: #8392a6">
              <template v-for="dict in contract_status">
                <template v-if="dict.value == contractDetails.status">{{dict.label}}</template>
              </template>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div>
            <div style="font-weight: bold;font-style: italic">合同金额</div>
            <div style="color: #8392a6">{{contractDetails.contractPrice}}</div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="8">
        <el-col :span="8">
          <div>
            <div style="font-weight: bold;font-style: italic">课程价格</div>
            <div style="color: #8392a6">{{contractDetails.coursePrice}}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div>
            <div style="font-weight: bold;font-style: italic">折扣类型</div>
            <div style="color: #8392a6">
              <template v-for="dict in activity_type">
                <template v-if="dict.value == contractDetails.discountType">{{dict.label}}</template>
              </template>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div>
            <div style="font-weight: bold;font-style: italic">合同审批人</div>
            <div style="color: #8392a6">{{contractDetails.approveUserName}}</div>
          </div>
        </el-col>
      </el-row>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="showContractDetailsDialog = false">确 定</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
        v-model="showPdfDialogVisiable"
        title="Tips"
        width="500"
        :before-close="handleClose"
    >
      <vue-pdf-embed :source="pdfSource" />
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="showPdfDialogVisiable = false">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

<!--    合同审批对话框-->
    <el-dialog
        v-model="approveContractDialog"
        title="合同审批"
        width="500"
        :before-close="handleClose"
    >
     <div>
       <el-tag>原因：</el-tag>
       <el-input placeholder="快速审批" style="width: 400px" v-model="approveInfo.reason"></el-input>
     </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="danger" @click="handleApproveContract(false)">拒绝</el-button>
          <el-button type="primary" @click="handleApproveContract(true)">
            审批通过
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Contract">
import useUserStore from "@/store/modules/user";
import VuePdfEmbed from 'vue-pdf-embed'
import { getToken } from '@/utils/auth'
import {
  listBusiness, listChannels,
  listActivity,
  addBusiness,
  assignBusiness,
  getBusinessSummaryById, updateBusiness, deleteBusinessByIds, getCourseByType
} from '@/api/tienchin/business'
import {treeselect} from "@/api/system/dept";
import {useRouter} from "vue-router";
import {
  addContract,
  deleteContractFile,
  getCurrentUserUnapproveTask,
  getCustomerNameByPhone,
  listUsers,
  getContractDetailsById,
  getPdfFile,
  getCurrentUserCommittedTask, approveOrReject, getCurrentApprovedTask,
  updateContractInfo
} from "@/api/tienchin/contract";

const userStore = useUserStore();
const router = useRouter();
const { proxy } = getCurrentInstance();
const {
  sys_user_sex,
  business_status,
  course_type,
  course_apply_to,
  contract_status,
    activity_type
} = proxy.useDict("sys_user_sex", "business_status", "course_type", "course_apply_to", "contract_status", "activity_type");

const unapproveTaskList = ref([]);
const committedTaskList = ref([]);
const approvedTaskList = ref([]);
const channelList = ref([]);
const activityList = ref([]);
const userList = ref([]);
const open = ref(false);
const showContractDetailsDialog = ref(false);
const showPdfDialogVisiable = ref(false);
const approveContractDialog = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const names = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const pdfSource = ref("");
const old_file = ref("");
const deptOptions = ref(undefined);
const courses = ref([]);
const headersObj = ref({
  "Authorization":'Bearer ' + getToken()
});
const fileList = ref([])
const contractDetails = ref({});
const approveInfo = ref({});

const data = reactive({
  form: {},
  assignForm: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    phone: undefined,
    owner: undefined,
    dateRange: undefined,
  },
  rules: {
    phone: [{ required: true, message: "手机号码不能为空", trigger: "blur" }],
    name: [{ required: true, message: "客户姓名不能为空", trigger: "blur" }],
    contractPrice: [{ required: true, message: "合同金额不能为空", trigger: "blur" }],
  },
  assignFormRules:{
    departmentId: [{ required: true, message: "部门ID不能为空", trigger: "blur" }],
    nickName: [{ required: true, message: "用户昵称不能为空", trigger: "blur" }],
  }
});

const { queryParams, form, assignForm, rules, assignFormRules } = toRefs(data);

function replienishContractInfo(data){
  initDeptOption();
  getContractDetailsById(data.contractId).then(response=>{
    open.value = true;
    form.value = response.data;
    if(response.data.filePath){
      old_file.value = response.data.filePath.split("/tienchin/contract/views/")[1];
    }
    form.value.departmentId = response.data.approveDeptId;
    initUsers();
    title.value = "修改合同";
  })
}
function handleApproveContract(approve){
  approveInfo.value.approve = approve;
  debugger;
  if(!approve){
    //如果审批不通过
    if(!approveInfo.value.reason){
      proxy.$message.error("请输入拒绝理由");
      return;
    }
  }else{
    //如果审批通过，但是用户却没有设置通过原因，则设置默认的原因为快速审批
    if(!approveInfo.value.reason){
      approveInfo.value.reason = '快速审批';
    }
  }
  approveOrReject(approveInfo.value).then(response=>{
    approveContractDialog.value = false;
    getUnapproveTaskList();
  })
}
function showApproveContractDialog(data){
  approveContractDialog.value = true;
  approveInfo.value = JSON.parse(JSON.stringify(data));
}
function handlePreviewPDF(data){
  let pdfFileName = data.filePath.split('/tienchin/contract/views/')[1];
  getPdfFile(pdfFileName).then(response=>{
    pdfSource.value = 'data:application/pdf;base64,'+response.msg;
  })
}
function phoneChange(value){
  form.value.name = undefined;
  // if(value.lenght  == 11){
    //说明手机号码输入完成，此时根据手机号码去查询客户信息
    getCustomerNameByPhone(value).then(response=>{
      let data = response.data;
      form.value.name = data.name;
      form.value.channelId = data.channelId;
      form.value.channelName = data.channelName;
      form.value.activityId = data.activityId;
      form.value.activityName = data.activityName;
      form.value.courseId = data.courseId;
      form.value.courseName = data.courseName;
      form.value.type = data.type;
      form.value.discountType = data.discountType;
    })
  //}
}
function activityChange(data){
  form.value.activityId = data.activityId;
  form.value.activityName = data.activityName;
  form.value.discountType = data.discountType;
}

function courseChange(data){
  form.value.courseId = data.courseId;
  form.value.courseName = data.courseName;
}
function handleBeforeUpload(file){
  let isLmit = file.size/ 1024/1024 < 5;
  if(!isLmit){
    proxy.$model.msgError("文件大小超过限制，上传失败");
    fileList.value.splice(0, fileList.value.length);
  }
  return isLmit;
}
/**
 * 删除文件
 * 可以在这个方法中去服务端删除文件，删除成功就返回 true，则前端页面的文件也会被删除，删除失败就返回 false，则前端页面的文件就不会被删除
 * @param uploadFile
 * @param uploadFiles
 */
function handleBeforeRemove(uploadFile, uploadFiles){
  var fileName = uploadFile.response.data.url.split("tienchin/contract/views/")[1];
  return deleteContractFile(fileName);
}
function handleRemove(){

}
function handleSuccess(response, uploadFile, uploadFiles){
  form.value.filePath = response.data.url;
}
/**
 * 课程类型发生变化的时候触发，此时根据课程类型重新去加载数据
 * @param type
 */
function courseTypeChange(type){
  form.value.courseId = undefined;
  getCourseByType(type).then(response=>{
    courses.value = response.data;
  })
}
function handleContractView(data){
  showContractDetailsDialog.value = true;
  getContractDetailsById(data.contractId).then(response=>{
    contractDetails.value = response.data;
  })
}
function handleClueFollow(data){
  router.push("/business/details/index/"+data.businessId + "/follow");
}
function cancelAssignClue(){
  //assignClueDialog.value = false;
  resetAssignForm();
}
function resetAssignForm(){
  assignForm.value={
    departmentId:undefined,
    nickName:undefined
  }
  proxy.resetForm("businessAssignRef");
}
/**
 * 分配合同执行方法
 */
function handleAssignBusiness(){
  proxy.$refs["businessAssignRef"].validate(valid=>{
    if(valid){
      assignBusiness(assignForm.value).then(response=>{
        getUnapproveTaskList();
        //assignClueDialog.value = false;
        resetAssignForm();
      })
    }
  });
}
function approveUserChange(data){
  form.value.nickName = data.nickName;
  form.value.approveUserId = data.value;
  form.value.approveUserName = data.userName;
  form.value.approveDeptId = data.deptId;
}

function deptChange(){
  form.value.nickName = undefined;
  initUsers();
}

function initUsers(){
  listUsers(form.value.departmentId).then(response=>{
    userList.value = response.data;
  })
}
function handleAssign(data){
  assignForm.value.assignId = data.businessId;
  assignForm.value.type = 1;
  initDeptOption();
  //assignClueDialog.value = true;
}
function getAllChannels(){
  listChannels().then(response=>{
    channelList.value = response.data;
  })
}

function channelChange(data){
  form.value.channelName = data.channelName;
  form.value.channelId = data.channelId;
  form.value.activityId = undefined;
  listActivity(data.channelId).then(response=>{
    activityList.value = response.data;
  })
}

/** 查询部门下拉树结构 */
function initDeptOption() {
  if(deptOptions.value == undefined){
    treeselect().then(response => {
      deptOptions.value = response.data;
    });
  }
};

/** 查询合同列表 */
function getUnapproveTaskList() {
  loading.value = true;
  getCurrentUserUnapproveTask(proxy.addDateRange(queryParams.value, queryParams.value.dateRange)).then(response => {
    unapproveTaskList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 表单重置 */
function reset() {
  form.value = {
    phone: undefined,
    name: undefined,
    channelId: undefined,
    channelName: undefined,
    activityId: undefined,
    activityName: undefined,
    type: undefined,
    courseName: undefined,
    courseId: undefined,
    approveDeptId: undefined,
    approveUserId: undefined,
    approveUserName: undefined,
    nickName: undefined,
    contractPrice:undefined,
    filePath: undefined

  };
  proxy.resetForm("contractRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getUnapproveTaskList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.businessId);
  names.value = selection.map(item => item.name);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  initDeptOption();
  open.value = true;
  title.value = "添加合同";
}
getAllChannels();

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getAllChannels();
  const businessId = ids.value;
  getBusinessSummaryById(businessId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改合同";
    listActivity(form.value.channelId).then(response=>{
      activityList.value = response.data;
    })
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["contractRef"].validate(valid => {
    if (valid) {
      if (form.value.contractId != undefined) {
        updateContractInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getUnapproveTaskList();
          getCurrentUserCommittedTask();
        });
      } else {
        addContract(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getUnapproveTaskList();
          getCurrentUserCommittedTask();
          getCurrentApprovedTask();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete() {
  const businessIds = ids.value;
  const businessNames = names.value;
  proxy.$modal.confirm('是否确认删除合同编号为"' + businessNames + '"的数据项？').then(function() {
    return deleteBusinessByIds(businessIds);
  }).then(() => {
    getUnapproveTaskList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("tienchin/course/export", {
    ...queryParams.value
  }, `course_${new Date().getTime()}.xlsx`);
}
function handleCommittedTask(){
  getCurrentUserCommittedTask().then(response=>{
    committedTaskList.value = response.rows;

  })
}
function handleApprovedTask(){
  getCurrentApprovedTask().then(response=>{
    approvedTaskList.value = response.rows;

  })
}
function myTaskListRowStyle({row}){
  if(row.reason){
    return 'red-bg';
  }
}
handleApprovedTask();
handleCommittedTask();
getUnapproveTaskList();
</script>

<style>
.el-table .red-bg{
  /*background: #f3756d;*/
  color: red;
}
</style>
