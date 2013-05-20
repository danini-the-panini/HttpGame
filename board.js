function board(row,col)
{
    var xmlhttp = new XMLHttpRequest();
    
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            document.getElementById("board").innerHTML=xmlhttp.responseText;
        }
    }
    
    xmlhttp.open("POST","Play.x",true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlhttp.send("row="+row+"&col="+col);
    
}