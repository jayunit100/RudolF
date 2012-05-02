

function gmb(data, i) {
   // unpack parameters
   var sw, tSize, lb, gb;
   sw = data.sw;
   tSize = data.tSize;
   lb = data.lb;
   gb = data.gb;
   
   var a, b, t, aq;
   a = Math.PI * lb;
   aq = tSize / sw;
   t = i / sw;
   b = -a / (2 * gb * aq);
   return Math.exp(-a * t - b * t * t);
}

 function sin_n(data, i) { 
   //unpack
   var n, off, end, tSize;
   n = data.n;
   off = data.off;
   end = data.end;
   tSize = data.tSize;
   
   var product = 1;
   for(var j = 0; j < n; j++) {
     product *= Math.sin(Math.PI * off + Math.PI * (end - off) * i / (tSize - 1));
   }
   return product;
 }
 
 
 // contract:  validation functions have to throw an
 //   exception if they find a problem
 var functions = {
  gmb: {
    parameters: {
	  "sw":    ["sweep width", function(x) {
	                             var p = parseFloat(x);
								 if(p != x) {
								   throw {'parameter': 'sw'};
								 }
							   },
				 100],
	  "tSize": ["number of points", function(x) {
	                                  var i = parseInt(x, 10);
									  if(i != x) {
									    throw {'parameter': 'tSize'};
									  }
									},
				 128],
	  "lb":    ["something?", function(x) {
	                                  var i = parseFloat(x);
									  if(i != x) {
									    throw {'parameter': 'lb'};
									  }
									},
				 0.9],
	  "gb":    ["something2?", function(x) {
	                                  var i = parseFloat(x);
									  if(i != x) {
									    throw {'parameter': 'gb'};
									  }
									},
				 0.9]
	},
	'function': gmb
  },
  sine: {
    parameters: {
	  "off":   ["offset", function(x) {
	                                  var i = parseInt(x, 10);
									  if(i != x) {
									    throw {'parameter': 'off'};
									  }
									},
				 50],
	  "end":   ["end point", function(x) {
	                                  var i = parseInt(x, 10);
									  if(i != x) {
									    throw {'parameter': 'end'};
									  }
									},
				 51],
	  "n":   ["exponent", function(x) {
	                                  var i = parseInt(x, 10);
									  if(i != x) {
									    throw {'parameter': 'n'};
									  }
									},
				 2],
	  "tSize": ["number of points", function(x) {
	                                  var i = parseInt(x, 10);
									  if(i != x) {
									    throw {'parameter': 'tSize'};
									  }
									},
				 100]
	},
	'function': sin_n
  }
}

function getFunction(name) {
  if(name in functions) {
    return functions[name];
  } else {
    throw new Error("bad function name <" + name + ">");
  }
}
