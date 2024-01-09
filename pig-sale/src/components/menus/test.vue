<template>
    <div class="container">
        <div class="chart-container">
            <div id="chart1" class="chart"></div>
            <select class="dropdown" v-model="selectedMonth" @change="changeMonth">
                <option value="1">1个月</option>
                <option value="3">3个月</option>
                <option value="6">6个月</option>
                <option value="12">12个月</option>
            </select>
        </div>
        <div class="chart-container">
            <div id="chart2" class="chart"></div>
            <select class="dropdown2" v-model="selectedYear" @change="getBenefit">
                <option value="2023">2023年</option>
                <option value="2024">2024年</option>
            </select>
        </div>
    </div>
</template>

<style>
    .container {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-top: 50px;
        position: relative;
    }

    .chart-container {
        width: 800px;
        height: 400px;
        margin-bottom: 40px;
    }

    .chart {
        width: 100%;
        height: 100%;
    }

    .dropdown {
        position: absolute;
        top: 0px;
        left: 1100px;
        padding: 5px;
        font-size: 14px;
    }

    .dropdown2 {
        position: absolute;
        top: 430px;
        left: 1100px;
        padding: 5px;
        font-size: 14px;
    }
</style>

<script>
    import * as echarts from 'echarts';
    import request from '@/utils/request';

    export default {
        name: 'Echarts',
        data() {
            return {
                selectedMonth: '3',
                selectedYear: '2023',
                salesObj: {
                    productNames: [],
                    sales: []
                },
                benefitData: [],
                orderData: []
            }
        },
        created() {
            this.getSales();
            this.getBenefit();
            this.getOrderData();
        },
        methods: {
            getSales() {
                const month = parseInt(this.selectedMonth);
                request
                    .get('/product/salesRank', {
                        params: {
                            month: month
                        }
                    })
                    .then(res => {
                        if (res.code === 200) {
                            this.salesObj = res.data;
                            this.myEcharts(this.salesObj);
                        } else {
                            console.log('error: ' + res.msg);
                        }
                    });
            },
            getBenefit() {
                const year = parseInt(this.selectedYear);
                request.get('/product/benefit', {
                    params: {
                        year: year
                    }
                }).then(res => {
                    console.log(res);
                    if (res.code === 200) {
                        this.benefitData = res.data;
                        this.renderChart(this.benefitData);
                    } else {
                        console.log('error:' + res.msg);
                    }
                });
            },
            getOrderData() {
                request.get('/product/thisYearOrders').then(res => {
                    console.log(res);
                    if (res.code === 200) {
                        this.orderData = res.data;
                        this.renderOrderChart(this.orderData);
                    } else {
                        console.log('error:' + res.msg);
                    }
                });
            },
            myEcharts(data) {
                var myChart = echarts.init(document.getElementById('chart1'));
                var option = {
                    title: {
                        text: '销量统计'
                    },
                    tooltip: {},
                    legend: {
                        data: ['销量']
                    },
                    xAxis: {
                        data: data.productNames,
                        axisLabel: {
                            rotate: 45,
                            interval: 0
                        },
                        name: 'product'
                    },
                    yAxis: {},
                    series: [
                        {
                            name: '销量',
                            type: 'bar',
                            data: data.sales,
                            barWidth: '40%'
                        },
                    ]
                };
                myChart.setOption(option);
            },
            changeMonth() {
                this.getSales();
            },
            renderChart(data) {
                const chart = echarts.init(document.getElementById('chart2'));

                const option = {
                    title: {
                        text: '收益统计',
                    },
                    tooltip: {
                        trigger: 'axis',
                    },
                    legend: {
                        data: ['收益', '成交订单'],
                        selected: {
                            '收益': true,  // 默认显示折线图
                            '成交订单': false,  // 默认隐藏柱状图
                        },
                        selectedMode: 'single',  // 单选模式
                    },
                    xAxis: {
                        name: '月份',
                        type: 'category',
                        data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
                    },
                    yAxis: {
                        type: 'value',
                    },
                    series: [
                        {
                            name: '收益',
                            type: 'line', // 折线图
                            data: data,
                        },
                        {
                            name: '成交订单',
                            type: 'bar', // 柱状图
                            data: this.orderData,
                        },
                    ],
                };

                chart.setOption(option);

                // 监听legend的点击事件
                chart.on('legendselectchanged', (params) => {
                    const selected = params.selected;
                    const legendName = Object.keys(selected)[0];  // 获取点击的legend名称
                    const seriesIndex = selected[legendName] ? 0 : 1;  // 根据选择状态设置series的显示或隐藏

                    // 更新series的显示或隐藏
                    chart.setOption({
                        series: [
                            {show: seriesIndex === 0},  // 折线图
                            {show: seriesIndex === 1},  // 柱状图
                        ],
                    });
                });
            },
        },
    };
</script>
