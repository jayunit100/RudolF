
// reference:  http://spin.niddk.nih.gov/NMRPipe/ref/nmrpipe/gmb.html
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

// reference:  http://spin.niddk.nih.gov/NMRPipe/ref/nmrpipe/sp.html
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
 
// reference:  http://spin.niddk.nih.gov/NMRPipe/ref/nmrpipe/em.html
function exponentialMultiply(data, i) {
  var lb = data.lb;
  var sw = data.sw;
  return Math.exp(-Math.PI * i * lb / sw);
}

// reference:  http://spin.niddk.nih.gov/NMRPipe/ref/nmrpipe/gm.html
function gm(data, i) {
  var g1, g2, g3, tSize, sw, e, g;
  g1 = data.g1;
  g2 = data.g2;
  g3 = data.g3;
  tSize = data.tSize;
  sw = data.sw;
  
  e = Math.PI * i * g1 / sw;
  g = 0.6 * Math.PI * g2 * (g3 * (tSize - 1) - i) / sw;
  
  return Math.exp(e - g * g);
}
 
function intValidator(name) {
  function val(x) {
    var i = parseInt(x, 10);
    if(i != x) {
      throw {'parameter': name};
    }
  }
  return val;
}

function floatValidator(name) {
  function val(x) {
    var i = parseFloat(x);
    if(i != x) {
      throw {'parameter': name};
    }
  }
  return val;
}
 
 // contract:  validation functions must throw an
 //   exception if they find a problem
 // in parameters:
 //   key: nmrpipe parameter name
 //   val[0]: more descriptive name
 //   val[1]: validation function
 //   val[2]: default, typical value
 var functions = {
  gmb: {
    parameters: {
	  "lb":    ["exponential term", floatValidator('lb'),  0.9],
	  "gb":    ["gaussian term",    floatValidator('gb'),  0.9],
	  "tSize": ["number of points", intValidator('tSize'), 1024],
	  "sw":    ["sweep width",      floatValidator('sw'),  6000],
	},
	'function': gmb
  },
  sine: {
    parameters: {
	  "off":   ["offset",    floatValidator('off'), .5],
	  "end":   ["end point", floatValidator('end'), 2],
	  "n":     ["exponent",  intValidator('n'), 1],
	  "tSize": ["number of points", intValidator('tSize'), 1024]
	},
	'function': sin_n
  },
  em: {
    parameters: {
	  'lb':     ['exponential line broadening', floatValidator('lb'), 5],
	  'tSize':  ['number of points', intValidator('tSize'), 1024],
	  'sw':     ['sweep width',      floatValidator('sw'), 6000],
	},
	'function': exponentialMultiply
  },
  gm: {
    parameters: {
      'g1':    ['inverse exponential width', floatValidator('g1'), 1],
      'g2':    ['gaussian broaden width',    floatValidator('g2'), 5],
      'g3':    ['location of gauss maximum', floatValidator('g3'), -1],
      'tSize': ['number of points', intValidator('tSize'), 1024],
      'sw':    ['sweep width',      floatValidator('sw'), 6000]
    },
    'function': gm
  }
}

function getFunction(name) {
  if(name in functions) {
    return functions[name];
  } else {
    throw new Error("bad function name <" + name + ">");
  }
}
