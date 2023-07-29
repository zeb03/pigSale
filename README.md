# pigSale
猪联网电子商务平台	后端开发    2023.02-2023.05
#### 技术栈：SpringBoot、MySQL、Redis
#### 项目描述：为规模养猪场量身打造的猪产品线上交易平台，是基于互联网时代下提倡的猪销售新模式，提供销售渠道，让养殖场销售工作实现数字化管理。实现了商品管理、权限管理、订单管理、消息推送等功能。
#### 项目内容：
1.	实现用户登录功能，使用token完成身份验证，Redis保存短信验证码和用户信息并设置有效时间。
2.	实现商品的增删改查功能，使用Redis缓存商品信息，使用逻辑过期解决缓存击穿问题，提高了查询效率。
3.	实现评论点赞功能，使用Redis中的zset类型存储数据，同时根据点赞时间保证数据有序。
4.	根据Redis的zset实现收藏商品价格变更消息推送，用户退款提醒和用户反馈信息推送，使用Feed流的推模式将消息直接发送到用户收件箱。
5.	利用ECharts进行数据统计，提供了数据可视化报表。
