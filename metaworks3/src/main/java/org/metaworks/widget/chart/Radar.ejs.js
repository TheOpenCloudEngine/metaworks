mw3.importScript("http://www.chartjs.org/assets/Chart.js");


var org_metaworks_widget_chart_Radar= function(objectId, className){

    var object = mw3.objects[objectId];

    var lineOptions = {
        animation: true,
        pointDot: true,
        scaleOverride: true,
        scaleShowGridLines: false,
        scaleShowLabels: true,
        scaleSteps: 4,
        scaleStepWidth: 25,
        scaleStartValue: 25,
    };


    var dataSets = [];
    for(i=0; i<object.radarData.length; i++){

        dataSets[i] = {
            fillColor: object.radarData[i].color,
            strokeColor: object.radarData[i].color,
            pointColor: object.radarData[i].color,
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

        new Chart(ctx2).Radar(radarData, lineOptions);
    }
}
