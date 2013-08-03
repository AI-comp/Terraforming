var CP = require('child_process');

var ix = process.argv.indexOf('--exec');
if (ix >= 0) {
  var cmd = process.argv.slice(ix + 1).join(' ');
  console.log(cmd);
  console.log('---- node start ----');
  var child = CP.exec(cmd, { timeout: 1000 }, function(err) {
    if (err) {
      console.log('Fail to run command');
    }
    else {
      console.log('---- node end ----');
    }
  });

  child.stdout.on('data', function(chunk) {
    console.log(chunk);
  });

  child.stdin.write("HOGE\n");
  child.stdin.write("EOS\n");
  child.stdin.write("HOGE\n");
  child.stdin.write("EOS\n");
  child.stdin.write("HOGE\n");
  child.stdin.end();
} else {
  console.log('No --exec option');
}


