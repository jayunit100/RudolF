
function Display() {
  this.chart = null;
  this.fInfo = null;
}

Display.prototype.setFunction = function(fInfo) {
  this.fInfo = fInfo;
  this.displayParameters();
}

Display.prototype.displayParameters = function() {
  var fInfo = this.fInfo;
  var params = fInfo.parameters;
  var table = $("#params");
  table.empty();
  
  var header = [
    '<tr>',
     '<th>parameter name</th>',
     '<th>description</th>',
     '<th>value</th>',
    '</tr>'
  ].join('');
  table.append(header);
  
  for(var name in params) {
    var param = params[name];
    var row = '<tr><td>' + name + '</td><td>' + param[0] + '</td><td>' + '<input value="' + param[2] + '" name="' + name + '"></input></td></tr>'; 
	table.append(row);
  }
}

function makeRange(low, high, steps) {
  if(low >= high || steps <= 0) {
    throw new Error("low must be less than high, steps must be positive");
  }
  var gap = (high - low) / steps;
  var range = [];
  for(var i = low; i < high; i += gap) {
    range.push(i);
  }
  return range;
}

Display.prototype.displayPlot = function() {
  if(this.chart) {
    this.chart.destroy();
  }
  
  var vals, val, fInfo, vfunc;
  fInfo = this.fInfo;
  // blow the chart away if it exists ... is this necessary?
  // get all the values
  vals = {};
  $("#params input").each(function(ix, el) {
    vals[$(this).attr('name')] = $(this).val();
  });
  // validate them
  for(var name in fInfo.parameters) {
    vfunc = fInfo.parameters[name][1];
	val = vals[name];
	vfunc(val);
  }
  // build the range
  var xs = makeRange(0, vals.tSize, vals.tSize);
  // shove them into the function
  function f(x) {
    return fInfo['function'](vals, x);
  }
  var ys = xs.map(f);
//  alert("y values: " + JSON.stringify(ys));
  // map over the range
  // put em in the chart
  this.chart = makeChart("chartdiv", {'xmax': xs[xs.length - 1], 'ys': ys, 'title': $("#function").val()});
}


function makeChart(elemid, data) {
  var myChart = new Highcharts.Chart({
        chart: {
            renderTo: elemid,
            defaultSeriesType : 'line',
            zoomType : 'xy'
        },
        title : {
            text : data.title
        },
        xAxis : {
            title : {
                enabled : true,
                text : "x"
            },
            startOnTick   : false,
            endOnTick     : false,
            showLastLabel : true,
            min: 0,
            max: data.xmax
        },
		//// whoa bad indentation !!!!
            yAxis : {
                title : {
                    text : 'y'
                }
            },
            tooltip : {
                formatter : function() {
                    return "x: " + this.x + "<br/>y: " + this.y;
                }
            },
            legend : {
                layout : 'vertical',
                align : 'left',
                verticalAlign : 'top',
                x : 5,
                y : 5,
                floating : false,
                backgroundColor : '#FFFFFF',
                borderWidth : 1
            },
            plotOptions : {
                scatter : {
                    marker : {
                        radius : 5,
                        states : {
                            hover : {
                                enabled : true,
                                lineColor : 'rgb(100,100,100)'
                            }
                        }
                    },
                    states : {
                        hover : {
                            marker : {
                                enabled : false
                            }
                        }
                    }
                }
            }, 
        series: [
            {'data': data.ys, name: "f(x)"},
        ]
    });
    return myChart;
}
