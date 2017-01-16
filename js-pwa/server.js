var express = require('express');
var app = express();

//To server static assests in root dir
app.use(express.static(__dirname));

//To server index.html page
app.get('/', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});


app.listen(process.env.PORT || 3000, function() {
  console.log('Local Server : http://localhost:3000');
});
