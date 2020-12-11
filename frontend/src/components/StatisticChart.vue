<template>
  <div
    v-loading="chartLoading"
    class="chart-container"
  >
    <span
      id="lineChart"
      style="width: 900px;height:400px"
    ></span>
  </div>
</template>

<script>
import echarts from 'echarts'
import 'echarts/lib/chart/line'
import 'echarts/lib/component/tooltip'
import 'echarts/lib/component/title'
export default {
  data() {
    return {
      chartLoading: false,
      option: {
        color: ['#4caf50', '#bb002f', '#03a9f4'],
        title: {
          text: '订单统计'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          data: ['新订单', '取消订单', '完成订单']
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: []
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '新订单',
            type: 'line',
            stack: '总量',
            areaStyle: {},
            data: []
          },
          {
            name: '取消订单',
            type: 'line',
            stack: '总量',
            areaStyle: {},
            data: []
          },
          {
            name: '完成订单',
            type: 'line',
            stack: '总量',
            areaStyle: {},
            data: []
          }
        ]
      }
    }
  },
  methods: {
    getStatistic() {
      this.chartLoading = true
      this.$axios
        .get('/admin/statistic')
        .then(res => {
          if (res.status == 200) {
            res.data.data.forEach(day => {
              this.option.xAxis[0].data.push(day.timestamp)
              this.option.series[0].data.push(day.newOrder)
              this.option.series[1].data.push(day.cancelOrder)
              this.option.series[2].data.push(day.completeOrder)
            })
            this.lineChart()
            this.chartLoading = false
          }
        })
        .catch(err => {
          this.chartLoading = false
          console.log(err)
        })
    },
    lineChart() {
      let myChart = echarts.init(document.getElementById('lineChart'))
      myChart.setOption(this.option)
      return myChart
    }
  },
  created() {
    this.getStatistic()
  }
}
</script>

<style>
  .chart-container {
    padding: 20px;
    display: flex;
    justify-content: center;
  }
</style>
