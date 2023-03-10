# user-system
简单的用户管理小页面



![](https://img.erpweb.eu.org/imgs/2023/03/5568f004bc028c5c.png)



### 功能实现

1. 新增用户；
2. 编辑用户；
3. 删除用户：单选、多选(批量)；
4. 撤销：只支持删除操作；
5. 分页：已实现；
6. 搜索：按姓名、年龄、性别、电话、地址等关键字查询；



### 改造点

1. 编辑、删除等按钮，我觉得统一放在一个地方阅读性更佳，重复的编辑、删除按钮是冗余的；
2. 加了创建时间展示，并按最新时间排序，可方便查询用户建立时间；
3. 删除用户采用逻辑删除；
4. 分页没有选用按钮，用户体验很不佳；
5. 关键字搜索采用按上述字段去查询体验更好；
6. 撤销功能只支持删除操作，正常来说应该也只是删除需要撤销；
7. 增加了查看用户窗口，鼠标移至用户姓名处会变成单击，单击即是查看，不可编辑，有时用户只是想看明细；
8. 单一删除和批量删除功能整合至一起，这样使用会更方便；



### 加分项

1. 界面美观舒适；
2. 代码可读性好，注释、单元测试、日志记录、mybatis框架等；
3. 性能优化：批量更新采用foreach操作，性能更好；



### PS：

1. 想实现年龄、性别、创建时间上做排序三角按钮的，时间有限，嘻嘻。



