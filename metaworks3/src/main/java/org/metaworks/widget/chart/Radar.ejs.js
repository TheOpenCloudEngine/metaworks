mw3.importScript("http://www.chartjs.org/assets/Chart.js");


var org_metaworks_widget_chart_Radar= function(objectId, className){

    var object = mw3.objects[objectId];

    var lineOptions = {
        animation: true,
        pointDot: true,
        scaleOverride: true,
        scaleShowGridLines: false,
        scaleShowLabels: true,
        scaleSteps: 7,
        scaleStepWidth: 1,
        scaleStartValue: 1,
    };


    var dataSets = [];
    for(i=0; i<object.radarData.length; i++){

        dataSets[i] = {
            fillColor: "rgba(" + object.radarData[i].color + ", 0.5)",
            strokeColor: "rgba(" + object.radarData[i].color + ", 1)",
            pointColor: "rgba(" + object.radarData[i].color + ", 1)",
            pointStrokeColor: "#fff",
            data: object.radarData[i].data
        };
    }

//radar chart data
    var radarData = {
        labels: object.perspectives,
        datasets: dataSets
    }
//Create Radar chart
    var chartDiv = document.getElementById("radarChart");

    if(chartDiv){
        var ctx2 = chartDiv.getContext("2d");
        var myNewChart = new Chart(ctx2).Radar(radarData);

        //new Chart(ctx2).Radar(radarData, lineOptions);
    }
}
