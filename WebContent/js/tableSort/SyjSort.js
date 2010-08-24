if (syj == null) var syj = {};
/*
  汉字按拼音排序需要qswhGB2312.js拼音库的支持,此拼音库支持的汉字范围为GB2312精简出自qiushuiwuhen
  动态构建的table同样可以排序,只需指定TD的sortObject属性指向待取值的对象(例如input控件)的引用 例cell.sortObject=动态构建出的input对象
  在页面加载时执行如下脚本即可
    var sorter=new syj.Sort('mainTable');
    sorter.bindCell(0,'NUMBER');
    sorter.bindCell(1,'STRING_EN',false);如果列表已经有序指定顺序false正序
    sorter.bindCell(2,'STRING_CH');
    sorter.bindCell(3,'DATE');
*/
var table
//tbName待排序的表名start起始tr下标默认1,end结束tr下标默认0
syj.Sort =function(tbName,start,end){
    this.tbName=tbName;//表名
    this.table=document.getElementById(tbName);//表对象
    this.start=start==null?1:start;//起始tr如果值为1则第一行不参与排序
    this.end=end==null?0:end;//结束tr如果值为2则最后两行不参与排序
    this.heads=this.table.rows[0].cells;
	this.spanSrc=document.createElement("span");
    this.spanSrc.innerHTML="▼";//占位用

}
//指定待排序的列cellIdx列号下标,desc如果列表已经有序true为倒序false为正序,null为无序,type类型支持类型有DATE,NUMBER,STRING_EN,STRING_CH
syj.Sort.prototype.bindCell=function(cellIdx,type,desc){
    var c=this.heads[cellIdx],o=this;
    c.style.cursor='hand';
    c.onclick=function(){
        o.sortCell(this,type); 
        if(o.onclickExt!=null)o.onclickExt(this);//执行扩展的onclickExt事件
    }
    var f=this.spanSrc.cloneNode(true);
    //f.style.visibility='hidden';
    c.updateSymbol=function(){ if(c.desc) f.innerHTML="▼";else f.innerHTML="▲";}
    c.desc=desc;
    if(c.desc!=null)c.updateSymbol();
    //c.onmouseover=function(){if(c.desc!=null)f.style.visibility='visible';}
    //c.onmouseout=function(){f.style.visibility='hidden';}
    c.appendChild(f);
    c.style.textIndent=f.offsetWidth;
}
//核心排序方法,采用冒泡排序,使用dom交换数据,不影响内存中cell对象的状态
syj.Sort.prototype.sortCell=function(cell,type){
    var ips = this.table.getElementsByTagName("INPUT"),cks = [];//排序字段 解决交换tr时checkbox的IE bug
    for(var i=0; i < ips.length; i++){if(ips[i].type == "checkbox") cks.push(ips[i], ips[i].checked);}
    if(cell.desc==null) cell.desc=false;else cell.desc=!cell.desc;
    var funcName='CMP_'+type;//根据类型匹配算法的函数名称
    if(this[funcName]==null){alert("类型错误,合法的类型为DATE,NUMBER,STRING_EN,STRING_CH");return ;}
    for (var i = this.start,cIdx=cell.cellIndex,rows=this.table.rows,size = rows.length - this.end; i < size; i++) {
        for (var k = this.start; k < size - 1 - i + this.start; k++) {
            var row1=rows[k],row2=rows[k + 1];
            var x=this.getValue(row1.cells[cIdx]),y=this.getValue(row2.cells[cIdx]);
            if (this[funcName](x,y,cell)>0)  
                //row1.swapNode(row2);
            	swapNode(row1, row2);
        }
    }
    cell.updateSymbol();
    while(cks.length > 0)cks.shift().checked = cks.shift();
}

function swapNode(node1,node2) {
  var parent = node1.parentNode;//父节点
  var t1 = node1.nextSibling;//两节点的相对位置
  var t2 = node2.nextSibling;
  
  //如果是插入到最后就用appendChild
  if(t1) parent.insertBefore(node2,t1);
  else parent.appendChild(node2);
  if(t2) parent.insertBefore(node1,t2);
  else parent.appendChild(node1);
}    

//数字比较算法
syj.Sort.prototype.CMP_NUMBER=function(x,y,cell){
    var r="/[^d|.|-]/g";
    x=x.replace(r,"");
    y=y.replace(r,"");
    return this.compare(x*1,y*1,cell);
}
//时间比较算法
syj.Sort.prototype.CMP_DATE=function(x,y,cell){
    var d='1900-01-01';
    var x=this.strToDate(x==''?d:x);
    var y=this.strToDate(y==''?d:y);
    var z=x-y;
    return cell.desc?z*(-1):z;
}
//英文字符串算法
syj.Sort.prototype.CMP_STRING_EN=function(x,y,cell){
    x=this.getFirstChar(x);
    y=this.getFirstChar(y);
    return this.compare(x,y,cell);
}

////////////////////普通字符串算法
syj.Sort.prototype.CMP_STRING_GE=function(x,y,cell){
	var sx = x;
	var sy = y;
	//如果度相等就直接比较
	if(sx.length == sy.length){
		if(x.localeCompare(y)>0) return cell.desc?-1:1;
	    else if(x.localeCompare(y)<0) return cell.desc?1:-1;
	    else return 0;
	}
	if(sx.length > sy.length){
		var tmpStr = sx.substring(0,sy.length);
		if(tmpStr.localeCompare(sy) == 0) return cell.desc?-1:1;
		if(tmpStr.localeCompare(sy)>0) return cell.desc?-1:1;
	    else if(tmpStr.localeCompare(sy)<0) return cell.desc?1:-1;
	    else return 0;
	}else{
		var tmpStr = sy.substring(0,sx.length);
		if(tmpStr.localeCompare(sx) == 0) return cell.desc?1:-1;
		if(tmpStr.localeCompare(sx)<0) return cell.desc?-1:1;
	    else if(tmpStr.localeCompare(sx)>0) return cell.desc?1:-1;
	    else return 0;
	}
}


//中文字符串算法
syj.Sort.prototype.CMP_STRING_CH=function(x,y,cell){
	x=x==""?"":getGB2312Spell(this.getFirstChar(x));
    y=y==""?"":getGB2312Spell(this.getFirstChar(y));
	return this.compare(x,y,cell);
}
//简单值比较算法
syj.Sort.prototype.compare=function(x,y,cell){
    if(x>y) return cell.desc?-1:1;
    else if(x<y) return cell.desc?1:-1;
    else return 0;
}
//取字符串的第一个字符
syj.Sort.prototype.getFirstChar=function(s){
    if(s=="")return "";
    return (s+"").substr(0,1);
}
//字符串转成日期类型 格式 MM/dd/YYYY MM-dd-YYYY YYYY/MM/dd YYYY-MM-dd   
syj.Sort.prototype.strToDate=function(ds){    
    var d = new Date(Date.parse(ds));   
    if (isNaN(d)){    
       var temp = ds.split(" ");
        var arys= temp[0].split('-'); 
        if (temp.length == 2) {
        	var arys2 = temp[1].split(':');  
        	d = new Date(arys[0],arys[1]-1,arys[2],arys2[0],arys2[1],arys2[2]);
        } else {
        	d = new Date(arys[0],arys[1]-1,arys[2]);
        }
           
    }
    return d;   
}
//取TD中的值
syj.Sort.prototype.getValue=function(cell){
    var v;
    if(cell.sortObject!=undefined) {
    	v= cell.sortObject.value!=undefined?cell.sortObject.value:cell.sortObject.innerHTML;
    }else{ 
    	v= cell.innerHTML;
    }
    return v;
}