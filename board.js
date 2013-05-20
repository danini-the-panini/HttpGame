function board(row,col)
{
    var xmlhttp = new XMLHttpRequest();
    
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            alert("D");
            document.getElementById("temp").innerHTML=xmlhttp.responseText;
        }
    }
    
    xmlhttp.open("POST","Play.x",true);
    alert("A");
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    alert("B");
    xmlhttp.send("row="+row+"&col="+col);
    alert("C");
    
}