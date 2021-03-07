<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" +
            request.getServerName() + ":" +
            request.getServerPort()+
            request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <title>交易统计图</title>
    <script src="jquery/echarts.min.js"></script>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script>
        $(function (){
            getCharts();
        });
        function getCharts(){
            $.ajax({
                url:"workbench/transaction/getCharts.do",
                type: "get",
                dataType:"json",
                success:function (data){
                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));

                    // option = {
                    //     legend: {
                    //         top: 'bottom'
                    //     },
                    //     toolbox: {
                    //         show: true,
                    //         feature: {
                    //             mark: {show: true},
                    //             dataView: {show: true, readOnly: false},
                    //             restore: {show: true},
                    //             saveAsImage: {show: true}
                    //         }
                    //     },
                    //     series: [
                    //         {
                    //             name: '面积模式',
                    //             type: 'pie',
                    //             radius: [50, 250],
                    //             center: ['50%', '50%'],
                    //             roseType: 'area',
                    //             itemStyle: {
                    //                 borderRadius: 8
                    //             },
                    //             data: [
                    //                 {value: 40, name: 'rose 1'},
                    //                 {value: 38, name: 'rose 2'},
                    //                 {value: 32, name: 'rose 3'},
                    //                 {value: 30, name: 'rose 4'},
                    //                 {value: 28, name: 'rose 5'},
                    //                 {value: 26, name: 'rose 6'},
                    //                 {value: 22, name: 'rose 7'},
                    //                 {value: 18, name: 'rose 8'}
                    //             ]
                    //         }
                    //     ]
                    // };

                    option = {
                        title: {
                            text: '交易漏斗图',
                            subtext: '统计交易阶段数量的漏斗图'
                        },
                        series: [
                            {
                                name:'交易漏斗图',
                                type:'funnel',
                                left: '10%',
                                top: 60,
                                //x2: 80,
                                bottom: 60,
                                width: '80%',
                                // height: {totalHeight} - y - y2,
                                min: 0,
                                max: data.total,
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data: data.dataList
                                    // [
                                    //     {value: 60, name: '访问'},
                                    //     {value: 40, name: '咨询'},
                                    //     {value: 20, name: '订单'},
                                    //     {value: 80, name: '点击'},
                                    //     {value: 100, name: '展现'}
                                    // ]
                            }
                        ]
                    };

                    myChart.setOption(option);
                }
            })
        }
    </script>
</head>
<body>
<body>
            <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
            <div id="main" style="width: 600px;height:400px;"></div>
</body>
</body>
</html>
