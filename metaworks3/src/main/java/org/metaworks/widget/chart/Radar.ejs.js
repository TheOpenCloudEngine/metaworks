mw3.importScript("http://www.chartjs.org/assets/Chart.js");


var org_metaworks_widget_chart_Radar= function(objectId, className){

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



//radar chart data
    var radarData = {
        labels: ["Stakeholders", "Opportunity", "Requirements", "System", "Team", "Work", "Way-of-working"],
        datasets: [{
            fillColor: "rgba(102,45,145,.1)",
            strokeColor: "rgba(102,45,145,1)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            data: [65, 59, 90, 81, 56, 55, 40]
        }, {
            fillColor: "rgba(63,169,245,.1)",
            strokeColor: "rgba(63,169,245,1)",
            pointColor: "rgba(151,187,205,1)",
            pointStrokeColor: "#fff",
            data: [28, 48, 40, 19, 96, 27, 100]
        }]
    }
//Create Radar chart
    var chartDiv = document.getElementById("radarChart");

    if(chartDiv){
        var ctx2 = chartDiv.getContext("2d");
        var myNewChart = new Chart(ctx2).Radar(radarData);

        new Chart(ctx2).Radar(radarData, options);
    }
}
