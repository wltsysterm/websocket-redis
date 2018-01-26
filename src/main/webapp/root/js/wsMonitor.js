var webSocket = null;
var timestamp;
var logobj;
var appendTimes = 0;
$(document).ready(function () {
    logobj = $("#log");
    resetDivHeight();
    timestamp=Date.parse(new Date());
    // var url = "ws://localhost:80";
    var url = bs.apiRoot();
    url = url.substring(4,url.length);
    webSocket=new WebSocket("ws"+url+"/wsMonitor/"+timestamp);
    webSocket.onerror = function(event) {
        showLog("报错了！！！");
    };
    webSocket.onopen = function(event) {
        showLog("连接建立成功！");
    };
    webSocket.onmessage = function(event) {
        receiveMessage(event.data);
    };
    webSocket.onclose=function(e){
        webSocket.close();
    };

});
function query(){
    var channel = $("#queryForm input[name=channel]").val();
    if (channel=="") {
        bs.toast("warning","参数异常","订阅频道不能为空");
        return;
    }
    showLog("正在订阅频道-"+channel+"...");
    sendMessage(channel);
}
function showLog(msg){
    logobj.html(msg);
}
function sendMessage(channel) {
    webSocket.send(channel);
}
function receiveMessage(message) {
    showLog(logobj.html()+"<br>"+message);
    appendTimes++;
    if (appendTimes == 100) {
        logobj.html("");
        appendTimes = 0;
    }
    logobj.scrollTop( logobj[0].scrollHeight);
}
function clearScreen(){
    logobj.html("");
    appendTimes = 0;
}
function download(){
    var channel = $("#queryForm input[name=channel]").val();
    if (channel=="") {
        bs.toast("warning","参数异常","订阅频道不能为空");
        return;
    }
    window.open(bs.apiRoot() + "/api/wsController/downloadLog?channel=" + channel + "&timestamp=" + timestamp);
}
window.onresize=function () {
    if(logobj!=undefined){
        resetDivHeight();
    }
}
function resetDivHeight(){
    logobj.css("height", (window.innerHeight-70)+"px");
}
