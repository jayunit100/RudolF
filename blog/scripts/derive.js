
function runDerivatives(data) {
  var cleaned = validate(data);
  var xvals = makeRange(cleaned.xmin, cleaned.xmax, cleaned.xsteps);
  var f = cleaned.function;
  var derivs = get3Derivatives(f, cleaned.h);
  var yseries = evalRangeFuncs([f].concat(derivs), xvals);
  var labels = ["f", "f'", "f''", "f'''"];
  var series = {
    x: xvals
  };
  for(var i = 0; i < yseries.length; i++) {
    series[labels[i]] = yseries[i];
  }
  return series;
}


function validate(data) {
  var cleaned = {
    "function": lookupFunction(data.function),
    "h":  parseFloat(data.h),
    "xmin": parseFloat(data.xmin),
    "xmax": parseFloat(data.xmax),
    "xsteps": parseInt(data.xsteps, 10)
  };
  // check that they're all (except for function) numbers
  [cleaned.h, cleaned.xmin, cleaned.xmax, 
    cleaned.xsteps].map(function(v) {
      if(! isFinite(v)) {
        throw new Error("bad number");
      }
    }
  );
  if(cleaned.xsteps > 200 || cleaned.xsteps <= 0) {
    throw new Error("invalid xsteps:  must be between 1 and 200, inclusive");
  }
  if(cleaned.h <= 0) {
    throw new Error("invalid h: must be positive");
  }
  return cleaned;
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


function derive(f, h) {
  function fprime(x) {
    return (f(x + h) - f(x)) / h;
  }
  return fprime;
}


function get3Derivatives(f, h) {
  var f1 = derive(f, h);
  var f2 = derive(f1, h);
  var f3 = derive(f2, h);
  return [f1, f2, f3];
}


function evalRange(f, xvals) {
  return xvals.map(f);
}


function evalRangeFuncs(fs, xvals) {
  function func(f) {
    return evalRange(f, xvals);
  };
  return fs.map(func);
}



var funcs = {
  "square": function(x) {return x * x;},
  "double": function(x) {return x * 2;},
  "cube": function(x) {return x * x * x;},
  "exp": Math.exp,
  "fourth": function(x) {return x * x * x * x;},
  "sine": Math.sin,
  "cosine": Math.cos,
  "tangent": function(x) {
                 if(x < -1.5 || x > 1.5) {
				     throw new Error("implementation's domain of tangent is -1.5 < x < 1.5");
				 } else {
				     return Math.tan(x);
				 } 
			 }, 
  "arcsine": function(x) { 
               if(x >= 1 || x <= -1) {
				    throw new Error("domain of arcsine is -1 < x < 1");
				} else {
				    return Math.asin(x);
				}
			}, 
  "arccosine": function(x) {
                   if(x >= 1 || x <= -1) {
				       throw new Error("domain of arccosine is -1 < x < 1");
				   } else {
				       return Math.acos(x);
				   }
			   },
  "arctangent": Math.atan
};

function lookupFunction(name) {
  if(!funcs[name]) {
    throw new Error("bad function name: <" + name + ">");
  } else {
    return funcs[name];
  }
}

