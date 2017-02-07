import React from 'react';
import ReactDOM from 'react-dom';
import Landing from 'component/Landing';
import 'app.css';

let query = window.location.search.slice(1);
let queryMap = {};
query.split("&").map((value)=>{
     let keyValue = value.split("=");
     queryMap[keyValue[0]] = keyValue[1];
});

ReactDOM.render(
    <Landing percentage={queryMap.score} nickName={queryMap.nickName} />,
    document.getElementById('app')
);