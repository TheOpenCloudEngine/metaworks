var com_test_widget_chart_Area = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
    
    var drawChart = function () {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Year');
        data.addColumn('number', 'Sales');
        data.addColumn('number', 'Expenses');
        data.addRows([
          ['2004', 1000, 400],
          ['2005', 1170, 460],
          ['2006', 660, 1120],
          ['2007', 1030, 540]
        ]);

        var options = {
          width: 400, height: 240,
          title: 'Company Performance',
          vAxis: {title: 'Year',  titleTextStyle: {color: 'red'}}
        };

        var chart = new google.visualization.AreaChart(document.getElementById('chart_'+objectId));
        chart.draw(data, options);
      }
      
      drawChart();
}