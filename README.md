export const statusOptions = [
  {value: '00', label: '暂存'},
  {value: '10', label: '设计待确认'},
  {value: '20', label: '待调度'},
  {value: '30', label: '待实绩'},
  {value: '40', label: '作业待确定'},
  {value: '50', label: '作业已完成'}
]

export const yesNoOptions = [
  {value: 'Y', label: '是'},
  {value: 'N', label: '否'}
]

export const workClassOptions = [
  {value: 'ADD', label: '附加'},
  {value: 'MAIN', label: '本工程'}
]

export const workTypeOptions = [
  {value: 'INSTALL', label: '安装'},
  {value: 'REMOVE', label: '拆除'}
]

export function statusText(value) {
  const item = statusOptions.find(option => option.value === value)
  return item ? item.label : value
}
