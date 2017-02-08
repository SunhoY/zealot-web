const express = require('express');
const port = 8080;
const app = express();
const indexPath = __dirname + '/dist/index.html';

app.use('/dist', express.static(__dirname + "/dist"));
app.use('/assets', express.static(__dirname + "/dist/assets"));
app.get('/', function (_, res) {
    res.sendFile(indexPath)
});

app.listen(port);
