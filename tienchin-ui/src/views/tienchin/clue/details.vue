<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="18">
        <el-row>
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>{{clue.name}}/{{clue.clueId}}</span>
              </div>
            </template>
            <div>
              <el-row>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">线索创建时间：</div>
                  <div style="color: #8392a6">{{parseTime(clue.createTime)}}</div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">客户手机号码：</div>
                  <div style="color: #8392a6">{{clue.phone}}</div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">渠道来源：</div>
                  <div style="color: #8392a6">{{clue.channelName}}</div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">线索分配者：</div>
                  <div style="color: #8392a6">{{clue.allocator}}</div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">线索归属时间：</div>
                  <div style="color: #8392a6">{{parseTime(clue.belongTime)}}</div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">线索归属人：</div>
                  <div style="color: #8392a6">{{clue.owner}}</div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="16">
                  <div style="font-style: italic;font-weight: bold">所属促销活动信息：</div>
                  <div style="color: #8392a6">{{clue.activityName}}/{{clue.activityInfo}}</div>
                </el-col>
                <el-col :span="8">
                  <el-button :disabled="isFiledDisabledEdit" type="danger" @click="showInvalidateClueDialog">无效线索</el-button>
                  <el-button :disabled="isFiledDisabledEdit" type="primary">转为商机</el-button>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-row>
        <el-row>
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>线索跟进</span>
              </div>
            </template>
            <div>
              <el-row :gutter="64">
                <el-col :span="8">
                  <div style="font-style: italic; font-weight: bold">客户姓名</div>
                  <div>
                    <el-input :disabled="isFiledDisabledEdit" v-model="clue.name"></el-input>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic; font-weight: bold">客户性别</div>
                  <div>
                    <el-select :disabled="isFiledDisabledEdit" v-model="clue.gender" placeholder="请选择">
                      <el-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label"
                                 :value="parseInt(dict.value)"></el-option>
                    </el-select>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic; font-weight: bold">客户年龄</div>
                  <div>
                    <el-input-number :disabled="isFiledDisabledEdit" v-model="clue.age" :min="6" :max="100" />
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="64">
                <el-col :span="8">
                  <div style="font-style: italic; font-weight: bold">客户微信号码</div>
                  <div>
                    <el-input :disabled="isFiledDisabledEdit" v-model="clue.weixin"></el-input>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic; font-weight: bold">客户QQ号码</div>
                  <div>
                    <el-input :disabled="isFiledDisabledEdit" v-model="clue.qq"></el-input>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic; font-weight: bold">客户手机号码</div>
                  <div>
                    <el-input :disabled="isFiledDisabledEdit" v-model="clue.phone"></el-input>
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                <div style="font-style: italic; font-weight: bold">客户意向等级</div>
                <div>
                  <el-radio-group v-model="clue.level" :disabled="isFiledDisabledEdit">
                    <el-radio v-for="(dict, index) in clue_level" :label="dict.label" :value="parseInt(dict.value)">
                      {{ dict.label }}
                    </el-radio>
                  </el-radio-group>
                </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <div style="font-style: italic; font-weight: bold">客户意向类型</div>
                  <div>
                    <el-radio-group v-model="clue.subject" :disabled="isFiledDisabledEdit">
                      <el-radio v-for="(dict, index) in course_type" :label="dict.label" :value="parseInt(dict.value)">
                        {{ dict.label }}
                      </el-radio>
                    </el-radio-group>
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <div style="font-style: italic; font-weight: bold">跟进记录</div>
                  <div>
                    <el-input v-model="clue.record"
                              :rows="5"
                              type="textarea"
                              :disabled="isFiledDisabledEdit"
                    ></el-input>
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <div style="font-style: italic; font-weight: bold">下次跟进时间</div>
                  <div>
                    <el-date-picker
                        v-model="clue.nextTime"
                        type="datetime"
                        :disabled="isFiledDisabledEdit"
                        clearable
                        value-format="YYYY-MM-DD hh:mm:ss"
                        format="YYYY-MM-DD hh:mm:ss"
                        placeholder="请选择下次跟进时间"
                    />
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <div style="display: flex;justify-content: flex-end">
                    <el-button :disabled="isFiledDisabledEdit" type="primary" @click="handleClueFollow">提交</el-button>
                    <el-button @click="goBack">返回</el-button>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-row>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>线索跟进记录</span>
            </div>
          </template>
          <div>
            <div v-for="(record, index) in records" :key="index" style="margin-bottom: 20px">
              <div>
                <span style="font-weight: bold;font-style: italic">{{record.createBy}}</span>：
                <span>{{record.info}}</span>
              </div>
              <span style="font-size: 15px;color: #8392a6">{{parseTime(record.createTime)}}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog title="无效线索" v-model="invalidateClueDialog" width="500px" append-to-body>
      <el-form ref="invalidateClueRef" :model="invalidateClueForm" :rules="invalidateClueFormRules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="原因" prop="reason_type">
              <el-select
                  v-model="invalidateClueForm.reason_type"
                  placeholder="请选择原因"
                  clearable
                  style="width: 100%"
              >
                <el-option
                    v-for="dict in clue_invalidate"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.label"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="reason_remark">
              <el-input v-model="invalidateClueForm.reason_remark"
                        :rows="5"
                        type="textarea"
                        placeholder="请输入内容"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleClueInvalidate">确 定</el-button>
          <el-button @click="cancelClueInvalidate">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Details">
import {getClueById, clueFollow, getClueRecordByClueId, invalidClue} from '@/api/tienchin/clue'
const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_user_sex, clue_level, course_type, clue_invalidate } = proxy.useDict("sys_user_sex", "clue_level", "course_type", "clue_invalidate");

const records = ref([]);
const type = ref("");
const clue = ref({});
const invalidateClueDialog = ref(false);
const isFiledDisabledEdit = ref(false);

const data = reactive({
  invalidateClueForm:{},
  invalidateClueFormRules:{
    reason_type: [{ required: true, message: "失效原因不能为空", trigger: "blur" }]
  }
});

const { invalidateClueForm, invalidateClueFormRules } = toRefs(data);
/**
 * 跳转到这个页面的请求格式是 /clue/details/index/10/view
 */
onMounted(()=>{
  const clueId = proxy.$route.params && proxy.$route.params.clueId;
  type.value = proxy.$route.params && proxy.$route.params.type;
  isFiledDisabledEdit.value=type.value == 'view';
  handleClue(clueId);
  getRecords(clueId);
})
function handleClueInvalidate(){
  proxy.$refs["invalidateClueRef"].validate(valid=>{
    if(valid){
      let remark = invalidateClueForm.value.reason_remark;
      clue.value.record = invalidateClueForm.value.reason_type + ":"+ (remark?remark:'');
      invalidClue(clue.value).then(response=>{
        router.go(-1);
      })
    }
  })
}
function cancelClueInvalidate(){
  invalidateClueDialog.value = false;
}
function showInvalidateClueDialog(){
  invalidateClueDialog.value = true;
}
function getRecords(clueId){
  getClueRecordByClueId(clueId).then(response=>{
    records.value = response.data;
  })
}
function handleClueFollow(){
  clueFollow(clue.value).then(response=>{
    router.go(-1);
  })
}

function goBack(){
  router.go(-1);
}
function handleClue(clueId){
  getClueById(clueId).then(response=>{
    clue.value = response.data;
    isFiledDisabledEdit.value=isFiledDisabledEdit.value||clue.value.status == 4;
  })
}
</script>

<style scoped>
.box-card{
  margin-top: 10px;
  width: 100%;
}
.el-row{
  margin-bottom: 30px;
}
.el-row:last-child{
  margin-bottom: 10px;
}
</style>
